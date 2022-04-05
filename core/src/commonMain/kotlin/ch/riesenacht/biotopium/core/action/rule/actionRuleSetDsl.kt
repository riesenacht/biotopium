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
import ch.riesenacht.biotopium.core.world.World
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.Owner

/**
 * The builder class for constructing an action ruleset.
 *
 * @author Manuel Riesen
 */
class ActionRuleSetBuilder<T : Action> {
    private val predicates: MutableList<ActionRule<T>> = mutableListOf()

    /**
     * Checks if a tile (identified by [x] and [y]) is owned by a given [owner].
     */
    fun tileOwned(x: Coordinate, y: Coordinate, world: World, owner: Owner) = world.realms[x.realmIndex, y.realmIndex]?.owner == owner

    /**
     * A rule which always results in `true`.
     */
    fun alwaysValid() {
        this.predicates.add { _, _, _ -> true }
    }

    /**
     * A rule which always results in `false`.
     */
    private fun alwaysInvalid() {
        this.predicates.add { _, _, _ -> false }
    }

    /**
     * Adds a [predicate] to the current action ruleset.
     */
    fun rule(predicate: ActionRule<T>) {
        this.predicates.add(predicate)
    }

    /**
     * Builds the action ruleset.
     * If the ruleset is empty, an always-invalid rule will be added.
     */
    fun build(): ActionRuleSet<T> {

        if(predicates.isEmpty()) {
            alwaysInvalid()
        }

        return ActionRuleSet(predicates)
    }
}

/**
 * Creates an action ruleset and applies the [init] function.
 */
fun <T : Action> rules(init: ActionRuleSetBuilder<T>.() -> Unit): ActionRuleSet<T> {
    val ruleSetBuilder = ActionRuleSetBuilder<T>()
    ruleSetBuilder.init()
    return ruleSetBuilder.build()
}
