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
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.Signature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a block on the blockchain.
 * The [height] of a block shows the position on the blockchain.
 * A block contains the [timestamp] of its creation time.
 * The [previous hash][prevHash] connects the block to the previous block on the chain.
 * A block holds information about its [author].
 * The [author] creates a [signature][sign] on the block's   [hash].
 * Each block acts as a storage for [data].
 * The block's validity is confirmed by the [validator].
 * A [validator] creates a [validation signature][validSign] on the block's [hash].
 * The block's [hash] is used for chaining blocks together, as well as ensure immutability of the block.
 *
 * @see AbstractBlock
 * @see Hashed
 * @see Signed
 * @see Validated
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("Block")
data class Block(
    override val height: ULong,
    override val timestamp: Timestamp,
    override val prevHash: Hash,
    override val author: Address,
    override val validator: Address,
    override val data: BlockData,
    override val hash: Hash,
    override val sign: Signature,
    override val validSign: Signature
) : AbstractBlock, Hashed, Signed, Validated