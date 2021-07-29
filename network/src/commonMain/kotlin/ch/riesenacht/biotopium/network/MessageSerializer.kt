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
import ch.riesenacht.biotopium.network.model.message.DebugMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

/**
 * Serializer for messages.
 * Boxes and unboxes messages.
 * Serializes boxed messages.
 *
 * @author Manuel Riesen
 */
object MessageSerializer {

    /**
     * Boxes an unboxed [message].
     * @return boxed message
     */
    inline fun <reified T : MessagePayload> box(message: UnboxedMessage<T>): BoxedMessage {
        val payload = Json.encodeToString(message.payload)
        return BoxedMessage(message.type, payload)
    }

    /**
     * Unboxes a [boxedMessage].
     * @throws UnsupportedMessageTypeException unsupported message type
     * @return unboxed message
     */
    @Throws(UnsupportedMessageTypeException::class)
    fun unbox(boxedMessage: BoxedMessage): UnboxedMessage<MessagePayload> {
        val implClass = when(boxedMessage.type) {
            MessageType.DEBUG -> DebugMessage::class
            else -> throw UnsupportedMessageTypeException(boxedMessage.type)
        }
        val payload = decode(implClass, boxedMessage.payload)
        return UnboxedMessage(boxedMessage.type, payload)
    }

    /**
     * Decodes an encoded message.
     * @param implClass implementation class
     * @param encoded encoded message
     */
    private inline fun <reified T : MessagePayload> decode(implClass: KClass<T>, encoded: String): T = Json.decodeFromString(encoded)

    /**
     * Serializes a boxed [message].
     * @return serialized message.
     */
    fun serialize(message: BoxedMessage): SerializedMessage = Json.encodeToString(message)

    /**
     * Deserializes a [serialized] message.
     * @return boxed message
     */
    fun deserialize(serialized: SerializedMessage): BoxedMessage = Json.decodeFromString(serialized)
}