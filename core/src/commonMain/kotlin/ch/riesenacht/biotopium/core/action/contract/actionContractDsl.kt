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

package ch.riesenacht.biotopium.core.action.contract

import ch.riesenacht.biotopium.core.action.exec.ActionExec
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.rule.ActionRuleSet

/**
 * Creates an action contract for the given [type] of action.
 * The [rules] and the [execution][exec] are added to the contract.
 */
fun <T : Action> actionContract(type: ActionType, rules: ActionRuleSet<T>, exec: ActionExec<T>): ActionContractManager.() -> Unit = {
    val contract = ActionContract(rules, exec)
    add(type, contract)
}