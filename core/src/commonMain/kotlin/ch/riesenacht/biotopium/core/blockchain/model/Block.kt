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

package ch.riesenacht.biotopium.core.blockchain.model

import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.Signature
import kotlinx.serialization.Serializable

/**
 * Represents a block in the [Blockchain].
 * The [height] of a block shows the position on the blockchain.
 * A block contains the [timestamp] of its creation time.
 * Over the [previous hash][prevHash] the block is connected to the previous block in the chain.
 * A block holds information about it's [author].
 * The [author] creates a [signature][sign] on the [hash].
 * The block's validity is confirmed by the [validator].
 * A [validator] creates a [validation signature][validSign] on the block's [hash].
 * Each block stores [data].
 * The block's [hash] is used for chaining blocks together, as well as guarantee immutability of a block.
 *
 * @author Manuel Riesen
 */
@Serializable
data class Block(
    val height: Long,
    val timestamp: Long,
    val prevHash: Hash,
    val author: Address,
    val validator: Address,
    val data: BlockData,
    val hash: Hash? = null,
    val sign: Signature? = null,
    val validSign: Signature? = null
)
