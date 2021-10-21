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

import ch.riesenacht.biotopium.network.go2p.GoP2p
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration
import ch.riesenacht.biotopium.network.model.message.SerializedMessage
import kotlinx.coroutines.*

/**
 * Represents a peer-to-peer node.
 * Implementation for JVM target.
 *
 * @author Manuel Riesen
 */
actual class P2pNode actual constructor(
    p2pConfig: P2pConfiguration
): NetworkNode() {

    actual val peerId: PeerId
        get() = PeerId(gop2p.peerId!!)

    private val gop2p = GoP2p.builder()
        .topic(p2pConfig.topic)
        .protocolName(p2pConfig.protocolName)
        .port(p2pConfig.listenPort)
        .privateKeyBase64(p2pConfig.privateKeyBase64)
        .build()

    private var listenPubSubJob: Job? = null

    private var listenStreamJob: Job? = null

    override suspend fun start() {
        gop2p.start()
        startListeningPubSub()
        startListeningStream()
    }

    override suspend fun stop() {
        gop2p.stop()
        listenPubSubJob?.cancelAndJoin()
    }

    override fun sendBroadcastSerialized(message: SerializedMessage) {
        gop2p.sendPubSub(message)
    }

    override fun sendSerialized(peerId: PeerId, message: SerializedMessage) {
        gop2p.sendStream(peerId.base58, message)
    }

    private suspend fun startListeningStream(): Unit = withContext(Dispatchers.Default) {
        listenStreamJob = launch(Job()) { listenStreamBlocking() }
    }

    private suspend fun startListeningPubSub(): Unit = withContext(Dispatchers.Default) {
        listenPubSubJob = launch(Job()) { listenPubSubBlocking() }
    }

    private fun listenStreamBlocking() {
        val serialized = gop2p.listenStreamBlocking()
        receive(serialized)
        listenStreamBlocking()
    }

    private fun listenPubSubBlocking() {
        val serialized = gop2p.listenPubSubBlocking()
        receive(serialized)
        listenPubSubBlocking()
    }

}