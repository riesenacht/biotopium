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
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.action.model.record.toActionRecord
import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.crypto.Ed25519
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.time.DateUtils
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.Player
import ch.riesenacht.biotopium.core.world.World
import ch.riesenacht.biotopium.core.world.model.*
import ch.riesenacht.biotopium.core.world.model.item.*
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.plant.growthRate
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

    private val zeroTimestamp: Timestamp
    get() = Timestamp(0)
    
    private val currentTimestamp
    get() = DateUtils.currentTimestamp()
    
    private val defaultKeyPair = Ed25519.generateKeyPair()

    private val defaultOwner: Owner
        get() = Owner(defaultKeyPair.publicKey)

    private fun createOtherOwner(base64: String = "none") = Address.fromBase64(base64)

    private fun createTestWorldWithPlayer(address: Address): TestWorld {
        val world = TestWorld()
        val player = Player("name", address)
        world.players[address] = player
        return world
    }
    
    private inline fun <reified T : Action> createActionRecord(
        timestamp: Timestamp = zeroTimestamp, 
        author: Address = defaultOwner, 
        content: T,
        privateKey: PrivateKey = defaultKeyPair.privateKey
    ): ActionRecord<T> {
        val raw = RawBlockRecord(timestamp, author, content)
        val hashed = raw.toHashedRecord(BlockUtils.hash(raw))
        return hashed.toActionRecord(BlockUtils.sign(hashed, privateKey))
    }

    private class TestWorld: World {

        override val tiles = mutableMapOf<Pair<Coordinate, Coordinate>, Tile>()

        override val realms = mutableMapOf<Pair<RealmIndex, RealmIndex>, Realm>()

        override val players = mutableMapOf<Address, Player>()

    }

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    /*
     *  Tests for [ChunkGenesisAction].
     */

    @Test
    fun testValidateChunkGenesisAction_positive() {

        val owner = defaultOwner

        val world = TestWorld()

        val content = ChunkGenesisAction(listOf(DefaultTile(1.coord, 1.coord)))
        val action = createActionRecord(zeroTimestamp, owner, content)

        assertTrue(ActionValidator.validate(action, world))
    }

    /*
     *  Tests for [ClaimRealmAction].
     */

    @Test
    fun testValidateClaimRealmAction_positive() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        world.players[owner] = Player("name", owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        world.players[owner]?.addItem(realmClaimPaper)

        val content = ClaimRealmAction(realm, realmClaimPaper)
        val action = createActionRecord(zeroTimestamp, owner, content)

        assertTrue(ActionValidator.validate(action, world))
    }

    @Test
    fun testValidateClaimAction_negative_realmExists() {

        val owner = defaultOwner

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        val world = createTestWorldWithPlayer(owner)
        world.players[owner]?.addItem(realmClaimPaper)

        world.realms[realm.ix to realm.iy] = realm

        val content = ClaimRealmAction(realm, realmClaimPaper)
        val action = createActionRecord(zeroTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }


    @Test
    fun testValidateClaimAction_negative_invalidClaimOwner_differentOwner() {

        val owner = defaultOwner
        val differentOwner = createOtherOwner()

        val world = createTestWorldWithPlayer(owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(differentOwner)
        world.players[owner]?.addItem(realmClaimPaper)

        val content = ClaimRealmAction(realm, realmClaimPaper)
        val action = createActionRecord(zeroTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }


    @Test
    fun testValidateClaimAction_negative_invalidClaimOwner_emptyInventory() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        val content = ClaimRealmAction(realm, realmClaimPaper)
        val action = createActionRecord(zeroTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }

    /*
     *  Tests for [CreatePlotAction].
     */

    @Test
    fun testValidateCreatePlotAction_positive() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[tile.x to tile.y] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val hoe = Hoe(owner)
        world.players[owner]!!.addItem(hoe)

        val content = CreatePlotAction(plot, hoe)
        val action = createActionRecord(zeroTimestamp + growthRate, owner, content)

        assertTrue(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_tileNotOwned() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[tile.x to tile.y] = tile

        val hoe = Hoe(owner)
        world.players[owner]!!.addItem(hoe)

        val content = CreatePlotAction(plot, hoe)
        val action = createActionRecord(zeroTimestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_invalidHoeOwner_differentOwner() {

        val owner = defaultOwner
        val differentOwner = createOtherOwner()

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[tile.x to tile.y] = tile

        val hoe = Hoe(differentOwner)
        world.players[owner]!!.addItem(hoe)

        val content = CreatePlotAction(plot, hoe)
        val action = createActionRecord(zeroTimestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_invalidHoeOwner_emptyInventory() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[tile.x to tile.y] = tile

        val hoe = Hoe(owner)

        val content = CreatePlotAction(plot, hoe)
        val action = createActionRecord(zeroTimestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateCreatePlotAction_negative_plotExists() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val hoe = Hoe(owner)

        val content = CreatePlotAction(plot, hoe)
        val action = createActionRecord(zeroTimestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    /*
     *  Tests for [GrowAction].
     */

    @Test
    fun testValidateGrowAction_positive_growSeed() {

        val timestamp = currentTimestamp

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val content = GrowAction(plot.copy(plant = plot.plant?.copy(growth = PlantGrowth.HALF_GROWN)))
        val action = createActionRecord(timestamp + growthRate, owner, content)

        assertTrue(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateGrowAction_negative_tileNotOwned() {

        val timestamp = currentTimestamp

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plot

        val content = GrowAction(plot)
        val action = createActionRecord(timestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateGrowAction_negative_plantGrown() {

        val timestamp = currentTimestamp

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant.copy(growth = PlantGrowth.HALF_GROWN))
        world.tiles[plot.x to plot.y] = plot

        val content = GrowAction(plot)
        val action = createActionRecord(timestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateGrowAction_negative_violatedPlantGrowthSteps() {

        val timestamp = currentTimestamp

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        plant.lastGrowth = timestamp

        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plot

        val content = GrowAction(plot.copy(plant = plot.plant?.copy(growth = PlantGrowth.GROWN)))
        val action = createActionRecord(timestamp + growthRate, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }


    @Test
    fun testValidateGrowAction_negative_violatedGrowthRate() {

        val timestamp = currentTimestamp

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.HALF_GROWN)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plot

        val content = GrowAction(plot.copy(plant = plot.plant?.copy(growth = PlantGrowth.GROWN)))
        val action = createActionRecord(timestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }




    /*
     *  Tests for [HarvestAction].
     */


    @Test
    fun testValidateHarvestAction_positive() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertTrue(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateHarvestAction_negative_tileNotOwned() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }


    @Test
    fun testValidateHarvestAction_negative_plantNotFullyGrown() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.HALF_GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedPlantOwner() {

        val owner = defaultOwner
        val differentOwner = createOtherOwner()

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(differentOwner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedSeedOwner() {

        val owner = defaultOwner
        val differentOwner = createOtherOwner()

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(differentOwner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedPlantType() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.WHEAT)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateHarvestAction_negative_invalidHarvestedSeedType() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.WHEAT)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateHarvestAction_negative_updatedPlotNotEmpty() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    /*
     *  Tests for [IntroductionAction].
     */

    @Test
    fun testValidateIntroductionAction_positive() {

        val owner = defaultOwner

        val world = TestWorld()

        val gift = IntroductionGift(
            RealmClaimPaper(owner),
            listOf(Hoe(owner), Hoe(owner)),
            listOf(Seed(owner, PlantType.WHEAT), Seed(owner, PlantType.CORN))
        )

        val content = IntroductionAction(gift)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertTrue(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateIntroductionAction_negative_playerPresent() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val gift = IntroductionGift(
            RealmClaimPaper(owner),
            listOf(Hoe(owner), Hoe(owner)),
            listOf(Seed(owner, PlantType.WHEAT), Seed(owner, PlantType.CORN))
        )

        val content = IntroductionAction(gift)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }

    @Test
    fun testValidateIntroductionAction_negative_invalidClaimPaperOwner() {

        val owner = defaultOwner

        val differentOwner = createOtherOwner()

        val world = TestWorld()

        val gift = IntroductionGift(
            RealmClaimPaper(differentOwner),
            listOf(Hoe(owner), Hoe(owner)),
            listOf(Seed(owner, PlantType.WHEAT), Seed(owner, PlantType.CORN))
        )

        val content = IntroductionAction(gift)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }

    @Test
    fun testValidateIntroductionAction_negative_invalidHoeOwner() {

        val owner = defaultOwner

        val differentOwner = createOtherOwner()

        val world = TestWorld()

        val gift = IntroductionGift(
            RealmClaimPaper(owner),
            listOf(Hoe(differentOwner), Hoe(owner)),
            listOf(Seed(owner, PlantType.WHEAT), Seed(owner, PlantType.CORN))
        )

        val content = IntroductionAction(gift)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }

    @Test
    fun testValidateIntroductionAction_negative_invalidSeedOwner() {

        val owner = defaultOwner

        val differentOwner = createOtherOwner()

        val world = TestWorld()

        val gift = IntroductionGift(
            RealmClaimPaper(owner),
            listOf(Hoe(owner), Hoe(owner)),
            listOf(Seed(owner, PlantType.WHEAT), Seed(differentOwner, PlantType.CORN))
        )

        val content = IntroductionAction(gift)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))
    }

    /*
     *  Tests for [SeedAction].
     */

    @Test
    fun testValidateSeedAction_positive() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertTrue(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_tileNotOwned() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_plantNotPresent() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val content = SeedAction(plot, seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_plantNotSeed() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.HALF_GROWN)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_wrongPlantType() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, PlantType.WHEAT, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_tileIsOfTypeDefault() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)


        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_seedNotOwned_differentOwner() {

        val owner = defaultOwner
        val differentOwner = createOtherOwner()

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(differentOwner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_plotGivenContainsPlant() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val newPlant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = newPlant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }


    @Test
    fun testValidateSeedAction_negative_plotOnMapContainsPlant() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plotOnMap = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plotOnMap

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val newPlant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = newPlant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

    @Test
    fun testValidateSeedAction_negative_seedNotOwned_emptyInventory() {

        val owner = defaultOwner

        val world = createTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val content = SeedAction(plot.copy(plant = plant), seed)
        val action = createActionRecord(currentTimestamp, owner, content)

        assertFalse(ActionValidator.validate(action, world))

    }

}