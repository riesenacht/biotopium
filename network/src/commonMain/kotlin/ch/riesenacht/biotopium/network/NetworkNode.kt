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

import ch.riesenacht.biotopium.logging.Logging
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.message.Message
import ch.riesenacht.biotopium.network.model.message.MessageEnvelope
import ch.riesenacht.biotopium.network.model.message.SerializedMessage
import ch.riesenacht.biotopium.serialization.JsonEncoder
import kotlin.reflect.KClass


/**
 * Represents a network node.
 *
 * @author Manuel Riesen
 */
abstract class NetworkNode : NetworkContext {

    private val handlerMap: MutableMap<KClass<out Message>, MutableList<MessageHandler<*>>> = mutableMapOf()

    val logger = Logging.logger("NetworkNode")

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
    protected abstract fun sendBroadcastSerialized(message: SerializedMessage)

    /**
     * Sends an unboxed [message] to all connected peers.
     */
    override fun sendBroadcast(message: Message) {
        logger.debug { "sending message $message to all" }

        val wrapper = wrapMessage(message)

        val serialized = JsonEncoder.encode(wrapper)
        sendBroadcastSerialized(serialized)
    }

    /**
     * Sends a serialized [message] to a peer with the given [peerId].
     */
    protected abstract fun sendSerialized(peerId: PeerId, message: SerializedMessage)

    /**
     * Wraps a [message] in a message wrapper.
     */
    private fun wrapMessage(message: Message): MessageEnvelope<Message> {
        return MessageEnvelope(peerId, message)
    }

    /**
     * Sends a [message] to a peer with the given [peer ID][peerId].
     */
    override fun send(peerId: PeerId, message: Message) {
        logger.debug { "sending message $message to $peerId" }

        val wrapper: MessageEnvelope<Message> = wrapMessage(message)

        val serialized = JsonEncoder.encode(wrapper)
        sendSerialized(peerId, serialized)
    }

    /**
     * Registers a [handler] for a message [type].
     */
    fun <T : Message> registerMessageHandler(type: KClass<T>, handler: MessageHandler<T>) {
        if(!handlerMap.containsKey(type)) {
            handlerMap[type] = mutableListOf()
        }
        handlerMap[type]!!.add(handler)
    }

    /**
     * Removes a [handler] for a message [type].
     */
    fun <T : Message> removeMessageHandler(type: KClass<T>, handler: MessageHandler<T>) {
        handlerMap[type] = handlerMap[type]?.filterNot { it == handler }?.toMutableList() ?: mutableListOf()
    }

    /**
     * Handles the incoming [serialized] message.
     * The corresponding message handlers are called.
     */
    fun receive(serialized: SerializedMessage) {
        val message: MessageEnvelope<out Message> = JsonEncoder.decode(serialized)

        logger.debug { "received message: $message" }

        dispatchToHandler(message)
    }

    /**
     * Dispatches the [message] to the correct handler.
     */
    private fun <T : Message> dispatchToHandler(message: MessageEnvelope<T>) {
        //UNCHECKED cast in order to retrieve the type of the message handler
        @Suppress("UNCHECKED_CAST")
        val handlerList = handlerMap[message.message::class] as List<MessageHandler<T>>
        handlerList.forEach { it.invoke(message, this) }
    }

}