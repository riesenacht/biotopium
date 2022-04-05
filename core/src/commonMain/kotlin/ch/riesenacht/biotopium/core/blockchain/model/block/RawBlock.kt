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
import ch.riesenacht.biotopium.core.blockchain.model.Hashable
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator
import ch.riesenacht.biotopium.core.blockchain.model.record.RecordBook
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.time.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a raw, unfinished block.
 * Raw blocks are used in the block creation process.
 *
 * @see Block
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("RawBlock")
data class RawBlock(
    override val height: ULong,
    override val location: Locator,
    override val timestamp: Timestamp,
    override val prevHash: Hash,
    override val author: Address,
    override val data: RecordBook
) : AbstractBlock, Hashable {

    /**
     * Creates a [hashed block][HashedBlock] out of the current raw block.
     * The block is extended with its [hash].
     */
    fun toHashedBlock(hash: Hash) = HashedBlock(
        height = height,
        location = location,
        timestamp = timestamp,
        prevHash = prevHash,
        author = author,
        data = data,
        hash = hash
    )
}