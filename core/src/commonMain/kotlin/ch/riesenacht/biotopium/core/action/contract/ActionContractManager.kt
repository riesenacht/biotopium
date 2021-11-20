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

import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.model.frame.ActionFrame
import ch.riesenacht.biotopium.core.world.MutableWorld

/**
 * Manager of all action contracts.
 *
 * @author Manuel Riesen
 */
object ActionContractManager {

    private val mutableContracts: MutableMap<ActionType, ActionContract<out Action>> = mutableMapOf()

    /**
     * Contract map.
     */
    val contracts: Map<ActionType, ActionContract<out Action>>
    get() = mutableContracts

    init {
        arrayOf(
            chunkGenesisContract,
            claimRealmContract,
            createPlotContract,
            growContract,
            harvestContract,
            introductionContract,
            seedContract
        ).forEach { it.invoke(ActionContractManager) }
    }

    /**
     * Adds an action [contract] mapped by its [type] to the contract map.
     */
    fun <T : Action> add(type: ActionType, contract: ActionContract<T>) {
        mutableContracts[type] = contract
    }

    /**
     * Executes the contract of a given [action] to the [world].
     */
    fun <T : Action> executeContract(action: ActionFrame<T>, world: MutableWorld) {
        //TODO technical debt here
        //unchecked cast in order to retrieve the contract type
        @Suppress("UNCHECKED_CAST")
        val contract: ActionContract<T> = contracts[action.content.type] as ActionContract<T>
        contract.exec(action.content, action, world)
    }
}