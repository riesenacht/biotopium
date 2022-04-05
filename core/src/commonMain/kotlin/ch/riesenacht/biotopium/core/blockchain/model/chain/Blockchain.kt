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

package ch.riesenacht.biotopium.core.blockchain.model.chain

import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator

/**
 * Represents the blockchain itself.
 * A blockchain consists of a chain of [blocks][Block].
 * Each block is connected to its predecessor over the previous block's hash.
 *
 * @author Manuel Riesen
 */
interface Blockchain : List<Block> {

    /**
     * The blockchain's location.
     */
    val location: Locator

    /**
     * The maximum height of the blockchain.
     */
    val maxHeight: ULong
    get() = if(size == 0) 0u else last().height

}