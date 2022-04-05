/*
 * Copyright (c) 2022 The biotopium Authors.
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

package ch.riesenacht.biotopium.core.blockchain.model.record.ref

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.ref.RegionBlockReference
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.Signature
import ch.riesenacht.biotopium.core.time.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a record containing a [region block reference][RegionBlockReference].
 *
 * @author Manuel Riesen
 */
@SerialName("ReferenceRecord")
@Serializable
data class ReferenceRecord(
    override val timestamp: Timestamp,
    override val author: Address,
    override val content: RegionBlockReference,
    override val hash: Hash,
    override val sign: Signature,
) : BlockRecord<RegionBlockReference> {

    override fun toHashable() = RawBlockRecord(
        timestamp,
        author,
        content
    )
}
