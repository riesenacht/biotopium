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
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.blockchain.model.block.BlockOrigin
import ch.riesenacht.biotopium.core.world.World

/**
 * The builder class for constructing an action ruleset.
 *
 * @author Manuel Riesen
 */
class ActionRuleMapBuilder {

    /**
     *  The current built action rule map.
     */
    val rules: MutableMap<ActionType, ActionRuleSet<out Action>> = mutableMapOf()

    /**
     * Builder class for the [ActionRuleSet].
     */
    class ActionRuleSetBuilder<T : Action> {
        private val predicates: MutableList<(T, BlockOrigin, World) -> Boolean> = mutableListOf()

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
        fun rule(predicate: (T, BlockOrigin, World) -> Boolean) {
            this.predicates.add(predicate)
        }

        /**
         * Builds the action rule set.
         * If the rule set is empty, an always-invalid rule will be added.
         */
        fun build(): ActionRuleSet<T> {

            if(predicates.isEmpty()) {
                alwaysInvalid()
            }

            return ActionRuleSet(predicates)
        }
    }

    /**
     * Creates an action rule for an action [type] with the given predicates
     * and adds it to the current ruleset on construction.
     */
    fun <T : Action> action(type: ActionType, init: ActionRuleSetBuilder<T>.() -> Unit) {
        val actionRuleSetBuilder = ActionRuleSetBuilder<T>()
        actionRuleSetBuilder.init()
        rules[type] = actionRuleSetBuilder.build()
    }
}

/**
 * Creates an action rule map and applies the [init] function.
 */
fun actionRuleMap(init: ActionRuleMapBuilder.() -> Unit): ActionRuleMap {
    val ruleMapBuilder = ActionRuleMapBuilder()
    ruleMapBuilder.init()
    return ruleMapBuilder.rules
}
