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

package ch.riesenacht.biotopium.core.action.model.frame

import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.BlockData
import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.Signature
import ch.riesenacht.biotopium.core.time.model.Timestamp
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an action frame containing an action as [content].
 *
 * @author Manuel Riesen
 */
@SerialName("ActionFrame")
@Serializable
data class ActionFrame<T : Action>(
    override val timestamp: Timestamp,
    override val author: Address,
    @Polymorphic
    override val content: T,
    override val hash: Hash,
    override val sign: Signature,
) : BlockRecord<T>, BlockData {

    override fun toHashable() = RawBlockRecord(
        timestamp,
        author,
        content
    )
}