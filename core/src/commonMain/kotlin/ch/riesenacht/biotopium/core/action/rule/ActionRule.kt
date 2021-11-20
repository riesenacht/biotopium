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
import ch.riesenacht.biotopium.core.blockchain.model.OriginInfo
import ch.riesenacht.biotopium.core.world.World

/**
 * Represents a rule for validating the execution of an [Action].
 * To an action rule can also be referred to as predicate.
 *
 * @author Manuel Riesen
 */
fun interface ActionRule<T : Action> {

    /**
     * Tests the [action] against the predicate using information about its [origin] and the [world].
     *
     * @return whether the action can be executed according to this rule
     */
    operator fun invoke(action: T, origin: OriginInfo, world: World): Boolean
}