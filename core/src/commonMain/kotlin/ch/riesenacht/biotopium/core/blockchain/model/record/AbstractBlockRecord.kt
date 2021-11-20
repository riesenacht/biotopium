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

import ch.riesenacht.biotopium.core.blockchain.model.HashableProducible
import ch.riesenacht.biotopium.core.blockchain.model.OriginInfo

/**
 * Abstract representation of a block record.
 *
 * @author Manuel Riesen
 */
interface AbstractBlockRecord<T : BlockRecordContent> : OriginInfo, HashableProducible {

    /**
     * The content of the record.
     */
    val content: T

    /**
     * Creates a hashable block record out of the current recordd.
     * Internally, a [RawBlockRecord] is used to achieve this.
     */
    override fun toHashable(): RawBlockRecord<T> = RawBlockRecord(
        timestamp = timestamp,
        author = author,
        content = content
    )
}