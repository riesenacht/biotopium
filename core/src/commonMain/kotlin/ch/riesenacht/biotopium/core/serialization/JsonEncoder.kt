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

package ch.riesenacht.biotopium.core.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * The encoder for encoding and decoding to and from the JSON format.
 *
 * @author Manuel Riesen
 */
object JsonEncoder {

    /**
     * The name of the class discriminator property.
     */
    const val classDiscriminator = "class"

    /**
     * Custom JSON format.
     */
    val format = Json {
        classDiscriminator = JsonEncoder.classDiscriminator
        serializersModule = CoreSerializersModuleCollection.module
    }

    /**
     * Encodes a [value] to JSON.
     * @return serialized value.
     */
    inline fun <reified T> encode(value: T): String = format.encodeToString(value)

    /**
     * Decodes an [encoded] value.
     * @return value
     */
    inline fun <reified T> decode(encoded: String): T = format.decodeFromString(encoded)
}