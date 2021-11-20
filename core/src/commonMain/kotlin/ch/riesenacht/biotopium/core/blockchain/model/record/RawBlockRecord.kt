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

package ch.riesenacht.biotopium.core.blockchain.model.record

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.Hashable
import ch.riesenacht.biotopium.core.blockchain.model.HashableProducible
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.time.model.Timestamp
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a raw block record.
 * The raw block record is missing information for ensuring integrity
 * and is used in the creation process.
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("RawBlockRecord")
data class RawBlockRecord<T : BlockRecordContent>(
    override val timestamp: Timestamp,
    override val author: Address,
    @Polymorphic
    override val content: T
) : AbstractBlockRecord<T>, Hashable, HashableProducible {

    /**
     * Creates a [hashed record][HashedBlockRecord] out of the current raw record.
     * The record is extended with its [hash].
     */
    fun toHashedRecord(hash: Hash) = HashedBlockRecord(
        timestamp = timestamp,
        author = author,
        content = content,
        hash = hash
    )

    /**
     * Returns a hashable instance.
     * Since the raw block record holds only the required fields for hashing,
     * the instance itself is returned.
     */
    override fun toHashable() = this
}