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

package ch.riesenacht.biotopium.core.crypto

import ch.riesenacht.biotopium.core.crypto.model.Hash
import java.security.MessageDigest

/**
 * The SHA-3 hashing algorithm object.
 * Actual implementation for JVM platform.
 *
 * @author Manuel Riesen
 */
actual object Sha3 {

    private val mdInstance = MessageDigest.getInstance("SHA3-256")

    /**
     * Converts a byte array to a hexadecimal string.
     * @param bytes byte array
     * @return hexadecimal string
     */
    private fun bytesToHex(bytes: ByteArray): String = bytes.joinToString("") { "%02x".format(it) }

    /**
     * Applies the SHA3-256 algorithm to a given string.
     *
     * @param input input
     * @return SHA3-256 hash
     */
    actual fun sha256(input: String): Hash = Hash(bytesToHex(mdInstance.digest(input.toByteArray())))
}