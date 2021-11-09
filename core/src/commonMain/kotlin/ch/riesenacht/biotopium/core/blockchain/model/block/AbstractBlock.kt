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
import ch.riesenacht.biotopium.core.blockchain.model.OriginInfo
import ch.riesenacht.biotopium.core.crypto.model.Hash


/**
 * Abstract representation of a block on the blockchain.
 * The abstract block contains all common properties.
 *
 * @see Block
 *
 * @author Manuel Riesen
*/
sealed interface AbstractBlock : OriginInfo {

    /**
     * The position on the blockchain.
     */
    val height: ULong

    /**
     * The hash of the previous block.
     */
    val prevHash: Hash

    /**
     * The validator of the block.
     */
    val validator: Address

    /**
     * The data stored in the block.
     */
    val data: BlockData

    /**
     * Creates a hashable block out of the current block.
     * A hashable block consists of (only) the relevant properties
     * for creating a hash.
     * Additional values such as present hashes and signatures are omitted.
     * Internally, a [RawBlock] is used to achieve this.
     */
    fun toHashable(): RawBlock = RawBlock(
        height = height,
        timestamp = timestamp,
        prevHash = prevHash,
        author = author,
        validator = validator,
        data = data
    )
}