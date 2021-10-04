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
import ch.riesenacht.biotopium.core.action.model.SeedAction
import ch.riesenacht.biotopium.core.action.rule.rules
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.TileType
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth

/**
 * Action contract of the [SeedAction].
 */
val seedContract = actionContract<SeedAction>(
    ActionType.SEED,

    rules {

        // the plot's tile is owned by the block author
        rule { action, block, world ->
            val plot = action.produce

            tileOwned(plot.x, plot.y, world, block.author)
        }

        // the plot contains a plant in seed state
        rule { action, _, _ ->
            val plot = action.produce

            plot.plant?.growth == PlantGrowth.SEED
        }

        // the plant type equals the seed type
        rule { action, _, _ ->
            val plot = action.produce
            val seed = action.consume

            plot.plant?.type == seed.plantType
        }

        // the plot's tile is currently of type plot
        rule { action, _, world ->
            val plot = action.produce

            world.tiles[plot.x to plot.y]?.type == TileType.PLOT
        }

        // the plot does not contain a plant
        rule { action, _, world ->
            val plot = action.produce
            val localPlot = world.tiles[plot.x to plot.y]

            (localPlot is Plot) && (localPlot.plant == null)
        }

        // the owner of the seed equals the author of the block,
        // the player owns the seed
        rule { action, block, world ->
            val seed = action.consume

            seed.owner == block.author
                    && world.players[seed.owner]?.items?.contains(seed) ?: false
        }

    },
    exec { action, _, world ->

        val plot = action.produce
        val seed = action.consume

        val owner = seed.owner

        // remove seed item from player
        world.players[owner]!!.removeItem(seed)

        world.tiles[plot.x to plot.y] = plot
    }
)