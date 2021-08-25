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

package ch.riesenacht.biotopium.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.serializer

/**
 * Encoder for a hashable string.
 * A hashable string represents data in a serialized form
 * and is used for hash creation.
 * The hashable string encoder encodes all non-null properties non-hierarchical.
 * Properties of nested objects are flattened out, a character-separated list of values is created.
 *
 * @author Manuel Riesen
 */
object HashableStringEncoder {

    /**
     * Separator for properties.
     */
    const val separator = ";"

    val encoder = ListEncoder()

    @OptIn(ExperimentalSerializationApi::class)
    class ListEncoder : AbstractEncoder() {

        override val serializersModule = SerializersModuleRegistry.module

        /**
         * The list containing all values to create a hashable string from.
         * This list is cleared after every encoding.
         */
        val list: MutableList<Any> = mutableListOf()

        /**
         * Adds the given [value] to the list.
         */
        override fun encodeValue(value: Any) {
            list.add(value)
        }

        /**
         * Ignores null values.
         */
        override fun encodeNull() {}


    }

    /**
     * Encodes the given [value] to a hashable string.
     */
    inline fun <reified T> encode(value: T): String {
        encoder.encodeSerializableValue(serializer(), value)
        val encoded = encoder.list.joinToString(separator) { it.toString() }
        encoder.list.clear()
        return encoded
    }

}
