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
import ch.riesenacht.biotopium.core.action.model.CreatePlotAction
import ch.riesenacht.biotopium.core.action.rule.rules
import ch.riesenacht.biotopium.core.world.model.map.TileType

/**
 * Action contract of the [CreatePlotAction].
 */
val createPlotContract = actionContract<CreatePlotAction>(
    ActionType.CREATE_PLOT,

    rules {
        // the plot to create is placed on a realm owned by the author of the block
        rule { action, block, world ->
            val plot = action.produce
            tileOwned(plot.x, plot.y, world, block.author)
        }

        // the author of the block equals the owner of the hoe,
        // the player owns the hoe
        rule { action, block, world ->
            val hoe = action.consume
            hoe.owner == block.author
                    && world.players[hoe.owner]?.items?.contains(hoe) ?: false
        }

        // the plot's tile is currently of type default
        rule { action, _, world ->
            val plot = action.produce
            world.tiles[Pair(plot.x, plot.y)]?.type == TileType.DEFAULT
        }
    },
    exec { action, world ->
        TODO("not yet implemented")
    }
)