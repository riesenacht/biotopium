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

package ch.riesenacht.biotopium.core.blockchain.rule

import ch.riesenacht.biotopium.core.blockchain.model.block.Block

/**
 * Represents a rule for validating a [Block].
 * To a block rule can also be referred to as predicate.
 *
 * @author Manuel Riesen
 */
fun interface BlockRule {

    /**
     * Tests the new [block] against the block rule using its data
     * and the [previous][prev] block's data.
     *
     * @return whether the block is valid according to this rule
     */
    operator fun invoke(block: Block, prev: Block): Boolean
}




