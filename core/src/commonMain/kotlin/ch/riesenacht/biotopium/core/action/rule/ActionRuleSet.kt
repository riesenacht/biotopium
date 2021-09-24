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

package ch.riesenacht.biotopium.core.action.rule

import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.blockchain.model.block.AbstractBlock
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.BlockOrigin
import ch.riesenacht.biotopium.core.world.World

/**
 * Represents a rule set for validating an action.
 * An action rule set consists of a list of [predicates].
 *
 * @author Manuel Riesen
 */
class ActionRuleSet<T : Action>(private val predicates: List<(T, BlockOrigin, World) -> Boolean>) {

    /**
     * Invokes the predicate to validate the new [action] using the action itself, its [block] origin information
     * and the [world].
     */
    operator fun invoke(action: T, block: BlockOrigin, world: World) = predicates.all { it.invoke(action, block, world) }
}
