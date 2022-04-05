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

package ch.riesenacht.biotopium

import ch.riesenacht.biotopium.core.Biotopium
import ch.riesenacht.biotopium.core.action.ActionManager
import ch.riesenacht.biotopium.core.action.model.*
import ch.riesenacht.biotopium.core.blockchain.BlockchainManager
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.world.model.item.*
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.network.MessageHandler
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainFwdMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainReqMessage

/**
 * Represents a biotopium instance in client-mode.
 */
object BiotopiumClient : Biotopium(biotopiumClientConfig) {

    /**
     * The client's regions of interest.
     * //TODO remove hardcoded region
     */
    private val regionsOfInterest: List<Region> = listOf(biotopiumClientRegion)

    /**
     * Updates the blockchain and calls the [callback] function.
     */
    private fun updateChain(location: Locator, callback: () -> Unit) {
        this.networkManager.send(randomBlocklordPeer, ChainReqMessage(BlockchainManager.blockchain[location].maxHeight, location))
        var handler: MessageHandler<ChainFwdMessage>? = null
        handler = MessageHandler { _, _ ->
            this.networkManager.removeMessageHandler(ChainFwdMessage::class, handler!!)
            callback.invoke()
        }
        this.networkManager.registerMessageHandler(ChainFwdMessage::class, handler)
    }

    /**
     * Updates the stem chain.
     * Invokes the [callback] after a successful update.
     */
    fun updateStemChain(callback: () -> Unit) = updateChain(Stem, callback)

    /**
     * Updates the chain of a [region].
     * Invokes the [callback] after a successful update.
     */
    fun updateRegionChain(region: Region, callback: () -> Unit) = updateChain(region, callback)

    /**
     * Updates both the stem chain and all region chains.
     * Invokes the [callback] after a successful update.
     */
    fun updateAllChains(callback: () -> Unit) {
        updateStemChain {
            var numUpdatedRegions = 0
            regionsOfInterest.forEach { region ->
                updateRegionChain(region) {
                    numUpdatedRegions++
                    if(numUpdatedRegions == regionsOfInterest.size) {
                        callback.invoke()
                    }
                }
            }
        }
    }

    /**
     * Creates the introduction action for a new player.
     */
    fun createIntroductionAction(region: Region) {
        val address = KeyManager.address
        val numHoes = 8
        val numWheatSeeds = 4
        val numCornSeeds = 4
        val realmClaimPaper = RealmClaimPaper(address)
        val hoes = (1..numHoes).map { Hoe(address) }.toList()
        val wheatSeeds = (1..numWheatSeeds).map { Seed(address, PlantType.WHEAT) }.toList()
        val cornSeeds = (1..numCornSeeds).map { Seed(address, PlantType.CORN) }.toList()
        val introductionGift = IntroductionGift(realmClaimPaper, hoes, wheatSeeds + cornSeeds)
        ActionManager.createAction(IntroductionAction(introductionGift, region))
    }

    /**
     * Creates a create-plot-action.
     * Turns a [tile] into a plot using a [hoe].
     */
    fun createCreatePlotAction(tile: Tile, hoe: Hoe) {
        val plot = Plot(tile.x, tile.y)
        ActionManager.createAction(CreatePlotAction(plot, hoe))
    }

    /**
     * Creates a seed action.
     * Plants a [seed] on a [plot].
     */
    fun createSeedAction(plot: Plot, seed: Seed) {
        val address = KeyManager.address
        val plantedPlot = plot.copy(plant = GrowingPlant(address, seed.plantType, PlantGrowth.SEED))
        ActionManager.createAction(SeedAction(plantedPlot, seed))
    }

    /**
     * Creates a harvest action.
     * A [plant] is harvested from a [plot].
     */
    fun createHarvestAction(plant: GrowingPlant, plot: Plot) {

        val harvestedPlant = HarvestedPlant(plant.owner, plant.type)
        val seeds = listOf(
            Seed(plant.owner, plant.type),
            Seed(plant.owner, plant.type),
            Seed(plant.owner, plant.type)
        )
        val harvest = Harvest(harvestedPlant, seeds)

        ActionManager.createAction(HarvestAction(harvest, plot))
    }

    fun createGrowAction(plot: Plot) {

        if(plot.plant == null) return

        val growth: PlantGrowth = when(plot.plant?.growth) {
            PlantGrowth.SEED -> PlantGrowth.HALF_GROWN
            PlantGrowth.HALF_GROWN -> PlantGrowth.GROWN
            PlantGrowth.GROWN -> PlantGrowth.GROWN
            else -> PlantGrowth.SEED
        }

        val updatedPlot = plot.copy(plant = plot.plant!!.copy(growth = growth))

        ActionManager.createAction(GrowAction(updatedPlot))
    }
}
