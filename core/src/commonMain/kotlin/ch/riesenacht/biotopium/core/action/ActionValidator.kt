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

package ch.riesenacht.biotopium.core.action

import ch.riesenacht.biotopium.core.action.contract.ActionContractManager
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.action.rule.ActionRuleSet
import ch.riesenacht.biotopium.core.world.World

/**
 * Validator for actions.
 *
 * @author Manuel Riesen
 */
object ActionValidator {

    /**
     * Validates the execution of an [action] leveraging the [world].
     */
    fun <T : Action> validate(action: ActionRecord<T>, world: World): Boolean {
        //UNCHECKED cast in order to retrieve the ruleset of the specific type T
        @Suppress("UNCHECKED_CAST")
        return (ActionContractManager.contracts[action.content.type]?.rules as ActionRuleSet<T>?)?.invoke(action.content, action, world) ?: false
    }
}