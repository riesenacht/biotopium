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

import ch.riesenacht.biotopium.core.action.model.*
import ch.riesenacht.biotopium.core.action.rule.actionRuleMap
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.World
import ch.riesenacht.biotopium.core.world.WorldStateManager
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.Owner
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.TileType
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.growthRate

/**
 * Checks if a tile (identified by [x] and [y]) is owned by a given [owner].
 */
private fun tileOwned(x: Coordinate, y: Coordinate, world: World, owner: Owner) = world.realms[Pair(x.realmIndex, y.realmIndex)]?.owner == owner

/**
 * The action rules for action validation.
 */
val actionRules = actionRuleMap {

    action<ChunkGenesisAction>(ActionType.CHUNK_GENESIS) { alwaysValid() }

    action<ClaimRealmAction>(ActionType.CLAIM_REALM) {

        // the realm does not exist
        rule { action, _, world ->
            val realm = action.produce

            !world.realms.containsKey(Pair(realm.ix, realm.iy))
        }

        // the author of the block equals the owner of the claim paper,
        // the player owns the claim paper
        rule { action, block, world ->
            val claimPaper = action.consume

            claimPaper.owner == block.author
                && world.players[claimPaper.owner]?.items?.contains(claimPaper) ?: false
        }

    }

    action<CreatePlotAction>(ActionType.CREATE_PLOT) {

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

    }

    action<GrowAction>(ActionType.GROW) {

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
        rule { action, _, _ ->
            val plot = action.produce
            plot.plant?.growth != PlantGrowth.GROWN
        }

        // the growth rate is taken into account
        rule { action, block, _ ->
            val plot = action.produce
            plot.plant?.lastGrowth?.let {block.timestamp <= it + growthRate } ?: false
        }
    }

    action<HarvestAction>(ActionType.HARVEST) {

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
    }

    action<IntroductionAction>(ActionType.INTRODUCTION) {

        // the player does not yet exist
        rule { _, block, world ->
            world.players[block.author] == null
        }

        // all introduction items are owned by the block author
        rule { action, block, _ ->
            val gift = action.produce
            gift.seeds.all { it.owner == block.author }
                && gift.hoes.all { it.owner == block.author }
                && gift.realmClaimPaper.owner == block.author
        }

    }

    action<SeedAction>(ActionType.SEED) {

        // the plot's tile is owned by the block author
        rule { action, block, world ->
            val plot = action.produce
            tileOwned(plot.x, plot.y, world, block.author)
        }

        // the plot's tile is currently of type plot
        rule { action, _, world ->
            val plot = action.produce
            world.tiles[Pair(plot.x, plot.y)]?.type == TileType.PLOT
        }

        // the plot does not contain a plant
        rule { action, _, world ->
            val plot = action.produce
            val mapTile = world.tiles[Pair(plot.x, plot.y)]
            plot.plant == null
                    && (mapTile is Plot) && (mapTile.plant == null)
        }

        // the owner of the seed equals the author of the block,
        // the player owns the seed
        rule { action, block, world ->
            val seed = action.consume
            seed.owner == block.author
                && world.players[seed.owner]?.items?.contains(seed) ?: false
        }

    }
}