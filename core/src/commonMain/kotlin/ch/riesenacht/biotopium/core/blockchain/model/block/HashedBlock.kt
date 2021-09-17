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

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.BlockData
import ch.riesenacht.biotopium.core.blockchain.model.Timestamp
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.Signature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a hashed block.
 *
 * @see AbstractBlock
 * @see Hashed
 * @see Block
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("HashedBlock")
data class HashedBlock(
    override val height: ULong,
    override val timestamp: Timestamp,
    override val prevHash: Hash,
    override val author: Address,
    override val validator: Address,
    override val data: BlockData,
    override val hash: Hash,
) : AbstractBlock, Hashed {

    /**
     * Creates a [signed block][SignedBlock] out of the current block.
     * Extends the block with the [author's signature][sign].
     */
    fun toSignedBlock(sign: Signature) = SignedBlock(
        height = height,
        timestamp = timestamp,
        prevHash = prevHash,
        author = author,
        validator = validator,
        data = data,
        hash = hash,
        sign = sign
    )
}