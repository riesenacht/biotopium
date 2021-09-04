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
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.blockchain.model.Block
import ch.riesenacht.biotopium.core.world.model.Owner
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.item.*
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [EncoderTest].
 *
 * @author Manuel Riesen
 */
class JsonEncoderCoreTest : EncoderTest() {

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]}}"


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
        val realm = Realm(Owner.fromBase64("me"), 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(Owner.fromBase64("me"))
        val action = ClaimRealmAction(realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}}}"

        val realm = Realm(Owner.fromBase64("me"), 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(Owner.fromBase64("me"))
        val action = ClaimRealmAction(realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeCreatePlotAction() {
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(Owner.fromBase64("me"))
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}}}"

        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(Owner.fromBase64("me"))
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeGrowAction() {
        val plot = Plot(1.coord, 0.coord)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0}}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {
        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0}}}"

        val plot = Plot(1.coord, 0.coord)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeHarvestAction() {
        val plot = Plot(1.coord, 0.coord)
        val owner = Owner.fromBase64("me")
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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0}}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {
        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0}}}"
        val owner = Owner.fromBase64("me")
        val plot = Plot(1.coord, 0.coord)
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
        val owner = Owner.fromBase64("me")
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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {
        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}}}"

        val owner = Owner.fromBase64("me")
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
        val owner = Owner.fromBase64("me")
        val plot = Plot(1.coord, 0.coord)
        val seed = Seed(
            owner,
            PlantType.CORN
        )
        val action = SeedAction(plot, seed)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}}}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}}}"

        val owner = Owner.fromBase64("me")
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