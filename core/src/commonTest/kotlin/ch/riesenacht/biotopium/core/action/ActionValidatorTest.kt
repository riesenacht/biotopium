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

import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.action.model.*
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.BlockOrigin
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.time.DateUtils
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.Player
import ch.riesenacht.biotopium.core.world.World
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.item.*
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.plant.growthRate
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test class for [ActionValidator].
 *
 * @author Manuel Riesen
 */
class ActionValidatorTest {

    private fun createDefaultOwner() = Address.fromBase64("me")

    private fun createOtherOwner(base64: String = "none") = Address.fromBase64(base64)

    private fun createDefaultBlockOrigin(address: Address, timestamp: Timestamp = DateUtils.currentTimestamp()) = TestBlockOrigin(timestamp, address)

    private fun createTestWorldWithPlayer(address: Address): TestWorld {
        val world = TestWorld()
        val player = Player("name", address)
        world.players[address] = player
        return world
    }

    private class TestWorld: World {

        override val tiles = mutableMapOf<Pair<Coordinate, Coordinate>, Tile>()

        override val realms = mutableMapOf<Pair<RealmIndex, RealmIndex>, Realm>()

        override val players = mutableMapOf<Address, Player>()

    }

    private data class TestBlockOrigin(override val timestamp: Timestamp, override val author: Address) : BlockOrigin

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    /*
     *  Tests for [ChunkGenesisAction].
     */

    @Test
    fun testValidateChunkGenesisAction_positive() {

        val owner = createDefaultOwner()

        val world = TestWorld()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val action = ChunkGenesisAction(listOf(DefaultTile(1.coord, 1.coord)))

        assertTrue(ActionValidator.validate(action, block, world))
    }

    /*
     *  Tests for [ClaimRealmAction].
     */

    @Test
    fun testValidateClaimRealmAction_positive() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        world.players[owner] = Player("name", owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        world.players[owner]?.addItem(realmClaimPaper)

        val action = ClaimRealmAction(realm, realmClaimPaper)

        assertTrue(ActionValidator.validate(action, block, world))
    }

    @Test
    fun testValidateClaimAction_negative_realmExists() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        val world = createTestWorldWithPlayer(owner)
        world.players[owner]?.addItem(realmClaimPaper)

        world.realms[Pair(realm.ix, realm.iy)] = realm

        val action = ClaimRealmAction(realm, realmClaimPaper)

        assertFalse(ActionValidator.validate(action, block, world))
    }


    @Test
    fun testValidateClaimAction_negative_invalidClaimOwner_differentOwner() {

        val owner = createDefaultOwner()
        val differentOwner = createOtherOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(differentOwner)
        world.players[owner]?.addItem(realmClaimPaper)

        val action = ClaimRealmAction(realm, realmClaimPaper)

        assertFalse(ActionValidator.validate(action, block, world))
    }


    @Test
    fun testValidateClaimAction_negative_invalidClaimOwner_emptyInventory() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        val action = ClaimRealmAction(realm, realmClaimPaper)

        assertFalse(ActionValidator.validate(action, block, world))
    }

    /*
     *  Tests for [CreatePlotAction].
     */

    @Test
    fun testValidateCreatePlotAction_positive() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val hoe = Hoe(owner)
        world.players[owner]!!.addItem(hoe)

        val action = CreatePlotAction(plot, hoe)

        assertTrue(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_tileNotOwned() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val hoe = Hoe(owner)
        world.players[owner]!!.addItem(hoe)

        val action = CreatePlotAction(plot, hoe)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_invalidHoeOwner_differentOwner() {

        val owner = createDefaultOwner()
        val differentOwner = createOtherOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val hoe = Hoe(differentOwner)
        world.players[owner]!!.addItem(hoe)

        val action = CreatePlotAction(plot, hoe)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_invalidHoeOwner_emptyInventory() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val hoe = Hoe(owner)

        val action = CreatePlotAction(plot, hoe)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_plotExists() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, Timestamp(0) + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val hoe = Hoe(owner)

        val action = CreatePlotAction(plot, hoe)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    /*
     *  Tests for [GrowAction].
     */

    @Test
    fun testValidateGrowAction_positive_growSeed() {

        val timestamp = DateUtils.currentTimestamp()

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, timestamp + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val action = GrowAction(plot)

        assertTrue(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateGrowAction_negative_tileNotOwned() {

        val timestamp = DateUtils.currentTimestamp()

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, timestamp + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val action = GrowAction(plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateGrowAction_negative_plantGrown() {

        val timestamp = DateUtils.currentTimestamp()

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, timestamp + growthRate)

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[Pair(plot.x, plot.y)] = plot


        val action = GrowAction(plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateGrowAction_negative_violatedGrowthRate() {

        val timestamp = DateUtils.currentTimestamp()

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner, timestamp)

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val action = GrowAction(plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }


    /*
     *  Tests for [HarvestAction].
     */


    @Test
    fun testValidateHarvestAction_positive() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val action = HarvestAction(harvest, plot)

        assertTrue(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateHarvestAction_negative_tileNotOwned() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val action = HarvestAction(harvest, plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }


    @Test
    fun testValidateHarvestAction_negative_plantNotFullyGrown() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.HALF_GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val action = HarvestAction(harvest, plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedPlantOwner() {

        val owner = createDefaultOwner()
        val differentOwner = createOtherOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val harvestedPlant = HarvestedPlant(differentOwner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val action = HarvestAction(harvest, plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedSeedOwner() {

        val owner = createDefaultOwner()
        val differentOwner = createOtherOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(differentOwner, PlantType.CORN)))

        val action = HarvestAction(harvest, plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedPlantType() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.WHEAT)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val action = HarvestAction(harvest, plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedSeedType() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(tile.x, tile.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.WHEAT)))

        val action = HarvestAction(harvest, plot)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    /*
     *  Tests for [SeedAction].
     */

    @Test
    fun testValidateSeedAction_positive() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val action = SeedAction(plot, seed)

        assertTrue(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateSeedAction_negative_tileNotOwned() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val action = SeedAction(plot, seed)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateSeedAction_negative_tileIsOfTypeDefault() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[Pair(plot.x, plot.y)] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val action = SeedAction(plot, seed)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateSeedAction_negative_seedNotOwned_differentOwner() {

        val owner = createDefaultOwner()
        val differentOwner = createOtherOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val seed = Seed(differentOwner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val action = SeedAction(plot, seed)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateSeedAction_negative_plotGivenContainsPlant() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val action = SeedAction(plot, seed)

        assertFalse(ActionValidator.validate(action, block, world))

    }


    @Test
    fun testValidateSeedAction_negative_plotOnMapContainsPlant() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plotOnMap = Plot(0.coord, 0.coord, plant)
        world.tiles[Pair(plot.x, plot.y)] = plotOnMap

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val action = SeedAction(plot, seed)

        assertFalse(ActionValidator.validate(action, block, world))

    }

    @Test
    fun testValidateSeedAction_negative_seedNotOwned_emptyInventory() {

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[Pair(plot.x, plot.y)] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[Pair(realm.ix, realm.iy)] = realm

        val seed = Seed(owner, PlantType.CORN)

        val action = SeedAction(plot, seed)

        assertFalse(ActionValidator.validate(action, block, world))

    }

}