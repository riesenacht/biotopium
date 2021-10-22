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
import kotlin.js.Promise


internal external interface Libp2pEncryption

internal external interface Libp2pMuxer

internal external interface Libp2pDiscovery

@JsModule("libp2p-noise")
@JsNonModule
internal external object Libp2pNoise {
    val NOISE: Libp2pEncryption
}

internal external interface JSClass

@JsModule("libp2p-websockets")
@JsNonModule
internal external object Websockets : JSClass {
    val prototype: dynamic
}

@JsModule("libp2p-floodsub")
@JsNonModule
internal external object FloodSub : JSClass {
    fun subscribe(topic: String)
    fun publish(topic: String, message: String)
    fun on(event: String, fn: (dynamic) -> Unit)
}

@JsModule("libp2p-websockets/src/filters")
@JsNonModule
internal external object filters {
    val all: dynamic
}

@JsModule("libp2p-mplex")
@JsNonModule
internal external object Mplex : Libp2pMuxer

@JsModule("libp2p-bootstrap")
@JsNonModule
internal external object Bootstrap : Libp2pDiscovery {
    val tag: String
}

@JsModule("libp2p-webrtc-star")
@JsNonModule
internal external object WebRTCStar : JSClass


@JsModule("libp2p")
@JsNonModule
internal external object Libp2p {
    fun create(options: dynamic): Promise<Libp2pInstance>
}

@JsModule("it-pipe")
@JsNonModule
internal external fun pipe(input: Array<String>, stream: Stream, sink: Sink)

@JsModule("it-pipe")
@JsNonModule
internal external fun pipe(stream: Stream, fn: (AsyncGenerator) -> Unit)

internal external class AsyncGenerator {
    fun next(): Promise<BufferListWrapper>?
}

internal external class BufferListWrapper {
    val value: BufferList
}

internal external class BufferList {
    override fun toString(): String
}

internal external class Sink

internal external class Stream {
    val sink: Sink
}

internal external class StreamWrapper {
    val stream: Stream
}


@JsModule("peer-id")
@JsNonModule
internal external class LibP2pPeerId {

    fun toB58String(): String

    companion object {
        fun createFromB58String(str: String): LibP2pPeerId
    }
}

internal external class Libp2pInstance {
    val pubsub: FloodSub
    val peerId: LibP2pPeerId
    fun start(): Promise<Unit>
    fun stop(): Promise<Unit>
    fun on(event: String, fn: (dynamic) -> Unit)
    fun handle(protocolName: String, fn: (dynamic) -> Unit)
    fun dialProtocol(addr: LibP2pPeerId, protocolName: String): Promise<StreamWrapper>
}