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

import ch.riesenacht.biotopium.core.blockchain.model.Hashed
import ch.riesenacht.biotopium.core.blockchain.model.Signed

/**
 * Represents a block record contained in a block on the blockchain.
 * Block records can hold content of a given [type][T].
 * The abstract block record contains all common properties.
 *
 * @author Manuel Riesen
 */
interface BlockRecord<T : BlockRecordContent> : AbstractBlockRecord<T>, Hashed, Signed