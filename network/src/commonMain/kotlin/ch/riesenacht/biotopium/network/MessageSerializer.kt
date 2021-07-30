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
import ch.riesenacht.biotopium.network.model.payload.MessagePayload
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Serializer for messages.
 *
 * @author Manuel Riesen
 */
object MessageSerializer {

    val format = Json {
        classDiscriminator = "class"
    }

    /**
     * Serializes a [message].
     * @return serialized message.
     */
    inline fun <reified T : MessagePayload> serialize(message: Message<T>): SerializedMessage = format.encodeToString(message)

    /**
     * Deserializes a [serialized] message.
     * @return message
     */
    fun deserialize(serialized: SerializedMessage): Message<MessagePayload> = format.decodeFromString(serialized)
}