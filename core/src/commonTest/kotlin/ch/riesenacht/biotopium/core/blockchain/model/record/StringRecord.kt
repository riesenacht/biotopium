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
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.Signature
import ch.riesenacht.biotopium.core.time.model.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a record containing a string.
 * This is only used for testing purposes.
 *
 * @see BlockRecord
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("StringRecord")
data class StringRecord(
    override val hash: Hash,
    override val timestamp: Timestamp,
    override val author: Address,
    override val content: StringRecordContent,
    override val sign: Signature
) : BlockRecord<StringRecordContent>