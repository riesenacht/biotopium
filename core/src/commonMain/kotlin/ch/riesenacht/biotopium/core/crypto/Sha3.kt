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
 * SHA-3 is referring to the SHA-3 FIPS 202 standard.
 * This SHA-3 algorithm sightly differs from the Keccak-256 algorithm,
 * to which is sometimes misleadingly referred to as SHA-3.
 * For more information see: https://csrc.nist.gov/publications/detail/fips/202/final
 *
 * @author Manuel Riesen
 */
expect object Sha3 {

    /**
     * Applies the SHA3-256 algorithm to a given string.
     *
     * @param input input
     * @return SHA3-256 hash
     */
    fun sha256(input: String): Hash
}