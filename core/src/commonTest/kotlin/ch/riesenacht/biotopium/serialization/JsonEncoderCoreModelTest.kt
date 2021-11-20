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

package ch.riesenacht.biotopium.serialization

import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.action.model.*
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.item.*
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [EncoderTest], testing core model serialization.
 *
 * @author Manuel Riesen
 */
class JsonEncoderCoreModelTest : EncoderTest() {

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    @Test
    fun testEncodeChunkGenesisAction() {
        val tiles = listOf(
            DefaultTile(1.coord, 1.coord),
            DefaultTile(2.coord, 3.coord),
            DefaultTile(4.coord, 5.coord),
            DefaultTile(6.coord, 7.coord),
            DefaultTile(8.coord, 9.coord)
        )
        val action = ChunkGenesisAction(tiles)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561\",\"sign\":\"tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ==\"},\"hash\":\"cb36da38bd9f078fa2f48853fbc01ddf9bc6d51876525c2167e88bfb3a1e1f43\",\"sign\":\"LIkc/1GFsKHzFPnHe4zd+ATbhuWjcwPulVoIVPCbGDbIPlnxs8Jnp7Gm4yDITjN/qSfh+NWoiO0wQ9PC5LWAAw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561\",\"sign\":\"tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ==\"},\"hash\":\"cb36da38bd9f078fa2f48853fbc01ddf9bc6d51876525c2167e88bfb3a1e1f43\",\"sign\":\"LIkc/1GFsKHzFPnHe4zd+ATbhuWjcwPulVoIVPCbGDbIPlnxs8Jnp7Gm4yDITjN/qSfh+NWoiO0wQ9PC5LWAAw==\"}"

        val tiles = listOf(
            DefaultTile(1.coord, 1.coord),
            DefaultTile(2.coord, 3.coord),
            DefaultTile(4.coord, 5.coord),
            DefaultTile(6.coord, 7.coord),
            DefaultTile(8.coord, 9.coord)
        )
        val action = ChunkGenesisAction(tiles)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeClaimRealmAction() {
        val owner = defaultOwner
        val realm = Realm(owner, 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)
        val action = ClaimRealmAction(realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"44c765639e6acd2de3ed36ece57999a03496334172a973a0aea15cc3cce33b17\",\"sign\":\"M6ZW5eEnRqsZUBae0MxExEjBLZgqlxOEbXZ6PRviDG64r/0k9dF22yuufjkLQ+l9jNoQKEjLOvNvPDiVYU0LBA==\"},\"hash\":\"63f0ec6124ae0b64ea5229eb8d2ff380c94db2fdaf779dc84ca29ff96645608f\",\"sign\":\"Tc2PsmXFSw70LxMqZS/h2slBngS5cJRkhGXe+7R6O56LCnKR2I505ej6/4tN3cOCBmBMX6njtiPJdG1+qGDlCw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"44c765639e6acd2de3ed36ece57999a03496334172a973a0aea15cc3cce33b17\",\"sign\":\"M6ZW5eEnRqsZUBae0MxExEjBLZgqlxOEbXZ6PRviDG64r/0k9dF22yuufjkLQ+l9jNoQKEjLOvNvPDiVYU0LBA==\"},\"hash\":\"63f0ec6124ae0b64ea5229eb8d2ff380c94db2fdaf779dc84ca29ff96645608f\",\"sign\":\"Tc2PsmXFSw70LxMqZS/h2slBngS5cJRkhGXe+7R6O56LCnKR2I505ej6/4tN3cOCBmBMX6njtiPJdG1+qGDlCw==\"}"

        val owner = defaultOwner
        val realm = Realm(owner, 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)
        val action = ClaimRealmAction(realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeCreatePlotAction() {
        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(owner)
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"f2e458d1cc0c1083687d0c5c8a66bf35810f2704665ed841b2c59f2b336df5fc\",\"sign\":\"HHn/M/2ojaH/jZDM/KmC0/aTy3KYG5KOFFiZCNDXXoA8uWMXgrq45gs0ac2RBjBUf218wPfFSkEjhdPZuMmyDA==\"},\"hash\":\"23ba1949bc6a0e33845b9304e850a0b99c6fe659d2af46655b967346920cb4ba\",\"sign\":\"0aNMdabqDxDbigICBzZDhsVDTfpDsqb6f3uZkQw139XML3Ew1Rp3c9sXhX1QAE+JbZ9T0VrKIek6Aj6iMjLCBQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"f2e458d1cc0c1083687d0c5c8a66bf35810f2704665ed841b2c59f2b336df5fc\",\"sign\":\"HHn/M/2ojaH/jZDM/KmC0/aTy3KYG5KOFFiZCNDXXoA8uWMXgrq45gs0ac2RBjBUf218wPfFSkEjhdPZuMmyDA==\"},\"hash\":\"23ba1949bc6a0e33845b9304e850a0b99c6fe659d2af46655b967346920cb4ba\",\"sign\":\"0aNMdabqDxDbigICBzZDhsVDTfpDsqb6f3uZkQw139XML3Ew1Rp3c9sXhX1QAE+JbZ9T0VrKIek6Aj6iMjLCBQ==\"}"

        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(owner)
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeGrowAction() {
        val owner = defaultOwner
        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        val plot = Plot(1.coord, 0.coord, plant)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"65b8b1b7c0ad8f356c628a40e116850dcf53a85f6d5c51c5206544fd7e468282\",\"sign\":\"IPvGTirlXwE9OMGx/t6KQ1rUze2WiXRqrxp/YYnGwDOd3CDisZTsKy1TQqykrmYVEqn4GkOfAqVGKaNAp02kBg==\"},\"hash\":\"acc0097ca96d60cc6571ff23833504d8dacf0865532756519b9bc2cb0c744ef1\",\"sign\":\"gGeuLKSpu04v1le3kFMBMtjP3fS9Ozszx3VEpowwbs51eaOwT02cwk8ujDQ5gIx8SE/XKY1rMPqjeP56kegfAA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"65b8b1b7c0ad8f356c628a40e116850dcf53a85f6d5c51c5206544fd7e468282\",\"sign\":\"IPvGTirlXwE9OMGx/t6KQ1rUze2WiXRqrxp/YYnGwDOd3CDisZTsKy1TQqykrmYVEqn4GkOfAqVGKaNAp02kBg==\"},\"hash\":\"acc0097ca96d60cc6571ff23833504d8dacf0865532756519b9bc2cb0c744ef1\",\"sign\":\"gGeuLKSpu04v1le3kFMBMtjP3fS9Ozszx3VEpowwbs51eaOwT02cwk8ujDQ5gIx8SE/XKY1rMPqjeP56kegfAA==\"}"

        val owner = defaultOwner
        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        val plot = Plot(1.coord, 0.coord, plant)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeHarvestAction() {
        val owner = defaultOwner
        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(1.coord, 0.coord, plant)
        val harvest = Harvest(
            HarvestedPlant(
                owner,
                PlantType.WHEAT
            ),
            listOf(
                Seed(
                    owner,
                    PlantType.WHEAT
                ),
                Seed(
                    owner,
                    PlantType.WHEAT
                )
            )
        )
        val action = HarvestAction(harvest, plot)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"63cd65bf70064a303250dc7a6efda53340c4870d0a68c41335a879c7fe631b6d\",\"sign\":\"SjoyilpRJluURLsI3iRw0egbqzznCZe1Y//u8ltIoY2EBKiNhcxawl1jsEtNNOz6KUALHz+DLY5gaJSW7282Cg==\"},\"hash\":\"aee54988cc13940306d67bf9c8cb03e4bce7bc2bc6e9ff52dd974ff01f5f7d1b\",\"sign\":\"uFpg7BAm68dz7A6QKdOCCQCR5eR50Rtn1LW1z32NHc2o/lXdMFQ79VL1IGz8cr1YLeqscOcC2Py8zICwKAVTBA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"63cd65bf70064a303250dc7a6efda53340c4870d0a68c41335a879c7fe631b6d\",\"sign\":\"SjoyilpRJluURLsI3iRw0egbqzznCZe1Y//u8ltIoY2EBKiNhcxawl1jsEtNNOz6KUALHz+DLY5gaJSW7282Cg==\"},\"hash\":\"aee54988cc13940306d67bf9c8cb03e4bce7bc2bc6e9ff52dd974ff01f5f7d1b\",\"sign\":\"uFpg7BAm68dz7A6QKdOCCQCR5eR50Rtn1LW1z32NHc2o/lXdMFQ79VL1IGz8cr1YLeqscOcC2Py8zICwKAVTBA==\"}"

        val owner = defaultOwner
        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(1.coord, 0.coord, plant)
        val harvest = Harvest(
            HarvestedPlant(
                owner,
                PlantType.WHEAT
            ),
            listOf(
                Seed(
                    owner,
                    PlantType.WHEAT
                ),
                Seed(
                    owner,
                    PlantType.WHEAT
                )
            )
        )
        val action = HarvestAction(harvest, plot)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeIntroductionAction() {
        val owner = defaultOwner
        val introductionGift = IntroductionGift(
            RealmClaimPaper(owner),
            (0..8).map { Hoe(owner) }.toList(),
            listOf(
                Seed(
                    owner,
                    PlantType.CORN
                ),
                Seed(
                    owner,
                    PlantType.WHEAT
                )
            )
        )
        val action = IntroductionAction(introductionGift)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},\"hoes\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}],\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"d2dc91e743964842e0b9d2d054391915ff99b5fa33318208a5db779db2596d06\",\"sign\":\"6DYZV8mMYAj0icLXC2wlTnxW64RDwesLAuPIbMbEvNneXu6M5D/h5ya4adAa76Le/aG6U++fdSqb8vouyBItAg==\"},\"hash\":\"6998fbc315afa00f106500470085ea1c897adc4628b4dd1a75d9d9e52431a2a2\",\"sign\":\"nbRN9OsopkFO5f35NC8J2qZXilOcZm4Q44q7GpioEUKT5vujgn94l+Rch1Fk/a+wUCwghJZ4DBtxgM+4cV0iBA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},\"hoes\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}],\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"d2dc91e743964842e0b9d2d054391915ff99b5fa33318208a5db779db2596d06\",\"sign\":\"6DYZV8mMYAj0icLXC2wlTnxW64RDwesLAuPIbMbEvNneXu6M5D/h5ya4adAa76Le/aG6U++fdSqb8vouyBItAg==\"},\"hash\":\"6998fbc315afa00f106500470085ea1c897adc4628b4dd1a75d9d9e52431a2a2\",\"sign\":\"nbRN9OsopkFO5f35NC8J2qZXilOcZm4Q44q7GpioEUKT5vujgn94l+Rch1Fk/a+wUCwghJZ4DBtxgM+4cV0iBA==\"}"

        val owner = defaultOwner
        val introductionGift = IntroductionGift(
            RealmClaimPaper(owner),
            (0..8).map { Hoe(owner) }.toList(),
            listOf(
                Seed(
                    owner,
                    PlantType.CORN
                ),
                Seed(
                    owner,
                    PlantType.WHEAT
                )
            )
        )
        val action = IntroductionAction(introductionGift)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeSeedAction() {
        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val seed = Seed(
            owner,
            PlantType.CORN
        )
        val action = SeedAction(plot, seed)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"}},\"hash\":\"ffef282ef09a571fd0dc6d560dbb0ae6d5774fd19bfda821e733b5441e51af49\",\"sign\":\"80rPGyFJ8CeDNtDN8zWqhXvyuXOTqep3G2ytvaKl+Z5ogdMdKGLUzRs0ZG465r3th7/48AOGLYmAib3gsAAUDA==\"},\"hash\":\"1e8eb41eaa8a43019769ac73cbfdb7d46649585dd876ad08b3d96024dd2bebd2\",\"sign\":\"qB5SOaTfjYUE3bc5bvSLSbwl6PSpQKUrlwmM8Fa1PkhKSVPSAyU8/b3YF1u9l/7PtbWYIvkA1c7Kwbn2/chjBQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"}},\"hash\":\"ffef282ef09a571fd0dc6d560dbb0ae6d5774fd19bfda821e733b5441e51af49\",\"sign\":\"80rPGyFJ8CeDNtDN8zWqhXvyuXOTqep3G2ytvaKl+Z5ogdMdKGLUzRs0ZG465r3th7/48AOGLYmAib3gsAAUDA==\"},\"hash\":\"1e8eb41eaa8a43019769ac73cbfdb7d46649585dd876ad08b3d96024dd2bebd2\",\"sign\":\"qB5SOaTfjYUE3bc5bvSLSbwl6PSpQKUrlwmM8Fa1PkhKSVPSAyU8/b3YF1u9l/7PtbWYIvkA1c7Kwbn2/chjBQ==\"}"

        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val seed = Seed(
            owner,
            PlantType.CORN
        )
        val action = SeedAction(plot, seed)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

}