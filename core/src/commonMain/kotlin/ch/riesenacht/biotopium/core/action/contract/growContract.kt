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
import ch.riesenacht.biotopium.core.action.model.GrowAction
import ch.riesenacht.biotopium.core.action.rule.rules
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.growthRate

/**
 * Action contract of the [GrowAction].
 */
val growContract = actionContract<GrowAction>(
    ActionType.GROW,

    rules {

        // the plot's tile is owned by the block author
        rule { action, block, world ->
            val plot = action.produce
            tileOwned(plot.x, plot.y, world, block.author)
        }

        // the owner of the growing plant equals the block author
        rule { action, block, _ ->
            val plot = action.produce
            plot.plant?.owner == block.author
        }

        // the plot's plant is not yet fully grown
        rule { action, _, world ->
            val plot = action.produce
            val localPlot = world.tiles[plot.x to plot.y] as Plot
            localPlot.plant?.growth != PlantGrowth.GROWN
        }

        // the new plant's growth is equal to the current plant's growth plus 1
        rule { action, _, world ->
            val plot = action.produce
            val localPlot = world.tiles[plot.x to plot.y] as Plot
            plot.plant?.growth?.ordinal == localPlot.plant?.growth?.ordinal?.plus(1)
        }


        // the growth rate is taken into account
        rule { action, block, world ->
            val plot = action.produce
            val localPlot = world.tiles[plot.x to plot.y] as Plot
            localPlot.plant?.lastGrowth?.let {block.timestamp <= it + growthRate } ?: false
        }

    },
    exec { action, block, world ->

        val plot = action.produce

        // update last growth time
        plot.plant?.lastGrowth = block.timestamp

        // set the new plot tile
        world.tiles[plot.x to plot.y] = plot

    }
)