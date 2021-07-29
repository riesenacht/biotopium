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

import ch.riesenacht.biotopium.network.model.*

/**
 * Represents a network node.
 *
 * @author Manuel Riesen
 */
abstract class NetworkNode {

    private val handlerMap: Map<MessageType, MutableList<MessageHandler>> = mapOf(
        *(MessageType.values()
            .map { Pair(it, mutableListOf<MessageHandler>()) }
            .toTypedArray())
    )

    /**
     * Starts the network node.
     */
    abstract suspend fun start()

    /**
     * Stops the network node.
     */
    abstract suspend fun stop()

    /**
     * Sends a serialized [message].
     */
    protected abstract fun sendSerialized(message: SerializedMessage)

    /**
     * Sends an unboxed [message].
     */
    inline fun <reified T : MessagePayload> send(message: UnboxedMessage<T>) {
        sendBoxed(MessageSerializer.box(message))
    }

    /**
     * Sends a boxed [message].
     */
    fun sendBoxed(message: BoxedMessage) {
        val serialized = MessageSerializer.serialize(message)
        sendSerialized(serialized)
    }

    /**
     * Registers a [handler] for a message [type].
     */
    fun registerMessageHandler(type: MessageType, handler: MessageHandler) {
        handlerMap[type]!!.add(handler)
    }

    /**
     * Handles the receive of a [serialized] message.
     * The corresponding message handlers are called.
     */
    fun receive(serialized: SerializedMessage) {
        val boxedMessage = MessageSerializer.deserialize(serialized)
        val unboxedMessage = MessageSerializer.unbox(boxedMessage)
        handlerMap[unboxedMessage.type]!!.forEach { it.invoke(unboxedMessage.payload) }
    }
}