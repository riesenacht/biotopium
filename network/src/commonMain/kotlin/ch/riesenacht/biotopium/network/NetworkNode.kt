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

import ch.riesenacht.biotopium.network.model.message.Message
import ch.riesenacht.biotopium.network.model.message.SerializedMessage
import ch.riesenacht.biotopium.serialization.JsonEncoder
import kotlin.reflect.KClass
import ch.riesenacht.biotopium.logging.Logging
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.message.MessageWrapper


/**
 * Represents a network node.
 *
 * @author Manuel Riesen
 */
abstract class NetworkNode {

    private val handlerMap: MutableMap<KClass<out Message>, MutableList<MessageHandler>> = mutableMapOf()

    val logger = Logging.logger("NetworkNode")

    abstract val peerId: PeerId

    /**
     * Starts the network node.
     */
    abstract suspend fun start()

    /**
     * Stops the network node.
     */
    abstract suspend fun stop()

    /**
     * Sends a serialized [message] to all connected peers.
     */
    abstract fun sendBroadcastSerialized(message: SerializedMessage)

    /**
     * Sends an unboxed [message] to all connected peers.
     */
    inline fun <reified T : Message> sendBroadcast(message: T) {
        logger.debug { "sending message $message to all" }

        val wrapper = wrapMessage(message)

        val serialized = JsonEncoder.encode(wrapper)
        sendBroadcastSerialized(serialized)
    }

    /**
     * Sends a serialized [message] to a peer with the given [peerId].
     */
    abstract fun sendSerialized(peerId: PeerId, message: SerializedMessage)

    /**
     * Wraps a [message] in a message wrapper.
     */
    fun <T : Message> wrapMessage(message: T): MessageWrapper<T> {
        return MessageWrapper(peerId, message)
    }

    /**
     * Sends a [message] to a peer with the given [peer ID][peerId].
     */
    inline fun <reified T : Message> send(peerId: PeerId, message: T) {
        logger.debug { "sending message $message to $peerId" }

        val wrapper: MessageWrapper<T> = wrapMessage(message)

        val serialized = JsonEncoder.encode(wrapper)
        sendSerialized(peerId, serialized)
    }

    /**
     * Registers a [handler] for a message [type].
     */
    fun <T : Message> registerMessageHandler(type: KClass<T>, handler: MessageHandler) {
        if(!handlerMap.containsKey(type)) {
            handlerMap[type] = mutableListOf()
        }
        handlerMap[type]!!.add(handler)
    }

    /**
     * Handles the incoming [serialized] message.
     * The corresponding message handlers are called.
     */
    fun receive(serialized: SerializedMessage) {
        val message: MessageWrapper<Message> = JsonEncoder.decode(serialized)

        logger.debug { "received message: $message" }

        handlerMap.entries.filter { it.key.isInstance(message) }
            .flatMap { it.value }
            .forEach { it.invoke(message) }
    }
}