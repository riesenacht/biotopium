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

package ch.riesenacht.biotopium.core.blockchain.model.block

import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.Hashable
import ch.riesenacht.biotopium.core.blockchain.model.HashableProducible
import ch.riesenacht.biotopium.core.blockchain.model.Hashed
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.crypto.model.Signature
import ch.riesenacht.biotopium.core.time.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents empty block data.
 * This kind of block data should only occur on the genesis block.
 *
 * @author Manuel Riesen
 */
@Serializable
data class EmptyBlockData(
    override val timestamp: Timestamp,
    override val author: Address,
    override val hash: Hash,
    override val sign: Signature
) : BlockData {

    /**
     * Represents raw empty block data.
     */
    @Serializable
    @SerialName("RawEmptyBlockData")
    class RawEmptyBlockData(
        override val timestamp: Timestamp,
        override val author: Address
    ): RawBlockData, Hashable, HashableProducible {
        override fun toHashable() = this
    }

    /**
     * Represents hashed empty block data.
     */
    private data class HashedEmptyBlockData(
        override val timestamp: Timestamp,
        override val author: Address,
        override val hash: Hash
    ): RawBlockData, Hashed, HashableProducible {
        override fun toHashable() = RawEmptyBlockData(timestamp, author)
    }

    companion object {

        /**
         * Creates an empty block data instance out of the given [timestamp] and [author],
         * signs its hash using the given [privateKey].
         */
        fun of(timestamp: Timestamp, author: Address, privateKey: PrivateKey): EmptyBlockData {
            val raw = RawEmptyBlockData(timestamp, author)
            val hashed = HashedEmptyBlockData(
                raw.timestamp,
                raw.author,
                BlockUtils.hash(raw)
            )
            return EmptyBlockData(
                hashed.timestamp,
                hashed.author,
                hashed.hash,
                BlockUtils.sign(hashed, privateKey)
            )
        }
    }

    /**
     * Creates a hashable block data instance out of the current empty block data instance.
     * Internally, [RawEmptyBlockData] is used to achieve this.
     */
    override fun toHashable() = RawEmptyBlockData(timestamp, author)
}