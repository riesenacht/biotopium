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

package ch.riesenacht.biotopium.core.blockchain

import ch.riesenacht.biotopium.core.blockchain.model.block.AbstractBlock
import ch.riesenacht.biotopium.core.blockchain.model.block.Hashed
import ch.riesenacht.biotopium.core.crypto.Ed25519
import ch.riesenacht.biotopium.core.crypto.Sha3
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.crypto.model.Signature
import ch.riesenacht.biotopium.serialization.HashableStringEncoder

/**
 * Utilities for blocks of a blockchain.
 *
 * @author Manuel Riesen
 */
object BlockUtils {

    /**
     * Creates a hash of a given [block].
     */
    fun hash(block: AbstractBlock): Hash {
        val content = HashableStringEncoder.encode(block.toHashable())
        return Sha3.sha256(content)
    }

    /**
     * Creates a signature of the [block]'s hash with the given [privateKey].
     */
    fun sign(block: Hashed, privateKey: PrivateKey): Signature {
        return Ed25519.sign(block.hash.hex, privateKey)
    }
}