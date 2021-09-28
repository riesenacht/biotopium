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

import ch.riesenacht.biotopium.core.action.exec.exec
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.model.HarvestAction
import ch.riesenacht.biotopium.core.action.rule.rules
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth

/**
 * Action contract of the [HarvestAction].
 */
val harvestContract = actionContract<HarvestAction>(
    ActionType.HARVEST,

    rules {

        // the plot's tile is owned by the block author
        rule { action, block, world ->
            val plot = action.consume
            tileOwned(plot.x, plot.y, world, block.author)
        }

        // the plant is fully grown
        rule { action, _, _ ->
            val plot = action.consume
            plot.plant?.growth == PlantGrowth.GROWN
        }

        // all items of the harvest are owned by the block author
        rule { action, block, _ ->
            val harvest = action.produce
            harvest.plant.owner == block.author
                    && harvest.seeds.all { it.owner == block.author }
        }

        // harvested plant and seeds must be of same plant type
        rule { action, _, _ ->
            val harvest = action.produce
            val plot = action.consume
            harvest.plant.plantType == plot.plant?.type
                    && harvest.seeds.all { it.plantType == plot.plant.type }
        }

    },
    exec { action, world ->
        TODO("not yet implemented")
    }
)