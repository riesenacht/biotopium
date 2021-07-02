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

/**
 * The SHA-3 hashing algorithm object.
 * Actual implementation for JS platform.
 *
 * @author Manuel Riesen
 */
actual object Sha3 {

    /**
     * Applies the SHA3-256 algorithm to a given string.
     *
     * @param input input
     * @return SHA3-256 hash
     */
    actual fun sha256(input: String): Hash = sha3.SHA3(256)
        .update(input)
        .digest("hex")
}

/**
 * External sha3 class from the sha3 npm package.
 */
@JsModule("sha3")
@JsNonModule
private external class sha3 {

    /**
     * Inner class of sha3 for accessing the real SHA3 hash function (SHA-3 FIPS 202).
     */
    class SHA3(size: Int) {

        /**
         * Updates the current SHA3 instance's content.
         * @param string string to hash
         * @return UpdatedSha3 instance for digestion
         */
        fun update(string: String): UpdatedSha3


        /**
         * External updated SHA-3 interface.
         * Provides access to the digest function.
         */
        interface UpdatedSha3 {
            /**
             * Digests the current content of the hash instance in a given encoding.
             * @param encoding encoding
             * @return hash of content in given encoding
             */
            fun digest(encoding: String): String
        }
    }
}
