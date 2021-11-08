/*
 * Copyright (c) 2021 The biotopium Authors.
 * This file is part of biotopium.
 *
 * biotopium is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * biotopium is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with biotopium.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.riesenacht.biotopium.network

import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration
import ch.riesenacht.biotopium.network.model.message.SerializedMessage
import ch.riesenacht.biotopium.network.utils.await
import ch.riesenacht.biotopium.network.utils.jsArray
import ch.riesenacht.biotopium.network.utils.jsObject
import ch.riesenacht.biotopium.network.utils.jsObjectFromPairs
import org.khronos.webgl.Uint8Array

/**
 * Represents a peer-to-peer node.
 * Implementation for JS target.
 *
 * @author Manuel Riesen
 */
actual class P2pNode actual constructor(
    private val p2pConfig: P2pConfiguration
) : NetworkNode() {

    private var libp2p: Libp2pInstance? = null

    private val decoder = TextDecoder("utf-8")

    override val peerId: PeerId
        get() = PeerId(libp2p!!.peerId.toB58String())

    override suspend fun start() {

        val config = jsObject {
            addresses = jsObject {
                listen = jsArray(
                    /*
                    "/dns4/wrtc-star1.par.dwebops.pub/tcp/443/wss/p2p-webrtc-star",
                    "/dns4/wrtc-star2.sjc.dwebops.pub/tcp/443/wss/p2p-webrtc-star"
                    */
                )
            }
            modules = jsObject {
                transport = jsArray(WebRTCStar, Websockets)
                connEncryption = jsArray(Libp2pNoise.NOISE)
                streamMuxer = jsArray(Mplex)
                peerDiscovery = jsArray(Bootstrap)
                pubsub = FloodSub
            }
            config = jsObject {
                transport = jsObject {
                    WebSockets = jsObjectFromPairs(
                        "filter" to filters.all
                    )
                }
                peerDiscovery = jsObjectFromPairs(
                    Bootstrap.tag to jsObject {
                        list = jsArray(*p2pConfig.bootstrapPeers.toTypedArray())
                        interval = 5000
                        enabled = true
                    }
                )
            }

        }
        this.libp2p = Libp2p.create(config).await()
        val libp2p = libp2p!!
        libp2p.on("peer:discovery") { peerId ->
            logger.debug { "discovered peer: ${peerId.toB58String()}" }
        }
        libp2p.pubsub.on(p2pConfig.topic) { msg ->
            val data: Uint8Array = msg.data as Uint8Array
            val serializedMessage: SerializedMessage = decoder.decode(data)
            receive(serializedMessage)
        }
        libp2p.start().then {
            libp2p.pubsub.subscribe(p2pConfig.topic)
        }.await()

        libp2p.handle(p2pConfig.protocolName) {
            val stream = it.stream
            pipe(stream as Stream) { source ->
                source.next()?.then { wrapper ->
                    val bufferList = wrapper.value
                    val message: SerializedMessage = bufferList.toString()
                    receive(message)
                }
            }
        }

        logger.debug { "P2P Node started with peer ID ${libp2p.peerId.toB58String()}" }
    }

    override suspend fun stop() {
        libp2p?.stop()?.await()
    }

    override fun sendBroadcastSerialized(message: SerializedMessage) {
        libp2p!!.pubsub.publish(p2pConfig.topic, message)
    }

    override fun sendSerialized(peerId: PeerId, message: SerializedMessage) {
        libp2p!!.dialProtocol(LibP2pPeerId.createFromB58String(peerId.base58), p2pConfig.protocolName).then {
            val stream = it.stream
            pipe(arrayOf(message), stream, stream.sink)
        }
    }


}