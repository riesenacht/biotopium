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

import ch.riesenacht.biotopium.TestCoreModuleEffect
import ch.riesenacht.biotopium.core.action.model.*
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.regionIndex
import ch.riesenacht.biotopium.core.effect.EffectProfile
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

    private val region: Region
    get() = Region(0.regionIndex, 0.regionIndex)

    @BeforeTest
    fun init() {
        applyEffect(TestCoreModuleEffect, EffectProfile.TEST)
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

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561\",\"sign\":\"tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ==\"}],\"hash\":\"0181a5cc5c1dceff4d2747c15fda998cabe4dff90f2dd07b700a8f2f8a30ac12\",\"sign\":\"N3udtEg1XaEGlEwJoOnz8w7EX5AezLwMUmsiRYdjVuOtcCYw6wFnTBUwxdXwR9x8qTDEjeYk6B3gZMP3fqCaAA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561\",\"sign\":\"tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ==\"}],\"hash\":\"0181a5cc5c1dceff4d2747c15fda998cabe4dff90f2dd07b700a8f2f8a30ac12\",\"sign\":\"N3udtEg1XaEGlEwJoOnz8w7EX5AezLwMUmsiRYdjVuOtcCYw6wFnTBUwxdXwR9x8qTDEjeYk6B3gZMP3fqCaAA==\"}"

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

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"44c765639e6acd2de3ed36ece57999a03496334172a973a0aea15cc3cce33b17\",\"sign\":\"M6ZW5eEnRqsZUBae0MxExEjBLZgqlxOEbXZ6PRviDG64r/0k9dF22yuufjkLQ+l9jNoQKEjLOvNvPDiVYU0LBA==\"}],\"hash\":\"0d46cf612946d93109a0718bd459f261e8c07fa1d0da039603629fcd1235f068\",\"sign\":\"zhiWGR5yzmtdLhyHyIir49H0jexieEhlLtndTKNhEDsTe6PxNl37y89Cfa1sKV6a82hZ4comQNOlbHbLG+IYBw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"44c765639e6acd2de3ed36ece57999a03496334172a973a0aea15cc3cce33b17\",\"sign\":\"M6ZW5eEnRqsZUBae0MxExEjBLZgqlxOEbXZ6PRviDG64r/0k9dF22yuufjkLQ+l9jNoQKEjLOvNvPDiVYU0LBA==\"}],\"hash\":\"0d46cf612946d93109a0718bd459f261e8c07fa1d0da039603629fcd1235f068\",\"sign\":\"zhiWGR5yzmtdLhyHyIir49H0jexieEhlLtndTKNhEDsTe6PxNl37y89Cfa1sKV6a82hZ4comQNOlbHbLG+IYBw==\"}"

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

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"f2e458d1cc0c1083687d0c5c8a66bf35810f2704665ed841b2c59f2b336df5fc\",\"sign\":\"HHn/M/2ojaH/jZDM/KmC0/aTy3KYG5KOFFiZCNDXXoA8uWMXgrq45gs0ac2RBjBUf218wPfFSkEjhdPZuMmyDA==\"}],\"hash\":\"926533a91660fae3ec826df9a6aa66ca1a16f8ce8ca367b244d0088a4f82cbe5\",\"sign\":\"jqC5/5Uis39UcsNNrxuSmTwIBseiJaBSCYzq44coD/rtYiQNO6C5cf3M2Lnw3ltzj5JGvIYHF2lvdRsCLDQuCQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"f2e458d1cc0c1083687d0c5c8a66bf35810f2704665ed841b2c59f2b336df5fc\",\"sign\":\"HHn/M/2ojaH/jZDM/KmC0/aTy3KYG5KOFFiZCNDXXoA8uWMXgrq45gs0ac2RBjBUf218wPfFSkEjhdPZuMmyDA==\"}],\"hash\":\"926533a91660fae3ec826df9a6aa66ca1a16f8ce8ca367b244d0088a4f82cbe5\",\"sign\":\"jqC5/5Uis39UcsNNrxuSmTwIBseiJaBSCYzq44coD/rtYiQNO6C5cf3M2Lnw3ltzj5JGvIYHF2lvdRsCLDQuCQ==\"}"

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

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"65b8b1b7c0ad8f356c628a40e116850dcf53a85f6d5c51c5206544fd7e468282\",\"sign\":\"IPvGTirlXwE9OMGx/t6KQ1rUze2WiXRqrxp/YYnGwDOd3CDisZTsKy1TQqykrmYVEqn4GkOfAqVGKaNAp02kBg==\"}],\"hash\":\"18664d3ac987ecf55c9836cd1ca66b3da3bd257b99f1d082bf70f6abee5d6081\",\"sign\":\"BWg0S8pdn+uUPvcSEbkVtW6v/u51L4omg7Tjim6pt0DhjDFQwfJZiSHgBpxdxyzkiNyO1m8PVi/9AV4GW8fJAA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"65b8b1b7c0ad8f356c628a40e116850dcf53a85f6d5c51c5206544fd7e468282\",\"sign\":\"IPvGTirlXwE9OMGx/t6KQ1rUze2WiXRqrxp/YYnGwDOd3CDisZTsKy1TQqykrmYVEqn4GkOfAqVGKaNAp02kBg==\"}],\"hash\":\"18664d3ac987ecf55c9836cd1ca66b3da3bd257b99f1d082bf70f6abee5d6081\",\"sign\":\"BWg0S8pdn+uUPvcSEbkVtW6v/u51L4omg7Tjim6pt0DhjDFQwfJZiSHgBpxdxyzkiNyO1m8PVi/9AV4GW8fJAA==\"}"

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

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"63cd65bf70064a303250dc7a6efda53340c4870d0a68c41335a879c7fe631b6d\",\"sign\":\"SjoyilpRJluURLsI3iRw0egbqzznCZe1Y//u8ltIoY2EBKiNhcxawl1jsEtNNOz6KUALHz+DLY5gaJSW7282Cg==\"}],\"hash\":\"8650afc0c94ab90ce55b0bb8a6c7cbd0a257d052579525d5d982e20885a82b5a\",\"sign\":\"lvLqD0ppnVfaU+Om1UGYjLrLxgM9a3LGOQTg5VOQrJSWbvs+D1C6CzHRokNKcPuWYKW3ZdqYv17x/ceFlalOAQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"63cd65bf70064a303250dc7a6efda53340c4870d0a68c41335a879c7fe631b6d\",\"sign\":\"SjoyilpRJluURLsI3iRw0egbqzznCZe1Y//u8ltIoY2EBKiNhcxawl1jsEtNNOz6KUALHz+DLY5gaJSW7282Cg==\"}],\"hash\":\"8650afc0c94ab90ce55b0bb8a6c7cbd0a257d052579525d5d982e20885a82b5a\",\"sign\":\"lvLqD0ppnVfaU+Om1UGYjLrLxgM9a3LGOQTg5VOQrJSWbvs+D1C6CzHRokNKcPuWYKW3ZdqYv17x/ceFlalOAQ==\"}"

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
        val action = IntroductionAction(introductionGift, region)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},\"hoes\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}],\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"location\":{\"rx\":0,\"ry\":0}},\"hash\":\"89a0bbf9df17b9cf6065a4b5a78263196bc669bbd0168be259d93f2e3a49814b\",\"sign\":\"Q26R8nRkooxodQzUSV5U+Ztskt/sOVzVGS5luOszxtS1kf7m96IhJT3i7PwVP6Vts0UFP5X6k4JhwMQnLHXhAA==\"}],\"hash\":\"2389212c3f8f95e8519e08f2e6815dc2651f4db352ce19034a3534d9fbcd51a2\",\"sign\":\"+wYMMEEegruCjuVsDohtmmKqkgIzk8GirFHAsMDLoz1ManEOB2kLwkEhsgRnmuLQ14rESE+JjxqXLQLgawyUDw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},\"hoes\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}],\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"location\":{\"rx\":0,\"ry\":0}},\"hash\":\"89a0bbf9df17b9cf6065a4b5a78263196bc669bbd0168be259d93f2e3a49814b\",\"sign\":\"Q26R8nRkooxodQzUSV5U+Ztskt/sOVzVGS5luOszxtS1kf7m96IhJT3i7PwVP6Vts0UFP5X6k4JhwMQnLHXhAA==\"}],\"hash\":\"2389212c3f8f95e8519e08f2e6815dc2651f4db352ce19034a3534d9fbcd51a2\",\"sign\":\"+wYMMEEegruCjuVsDohtmmKqkgIzk8GirFHAsMDLoz1ManEOB2kLwkEhsgRnmuLQ14rESE+JjxqXLQLgawyUDw==\"}"

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
        val action = IntroductionAction(introductionGift, region)
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

        val expected = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"}},\"hash\":\"ffef282ef09a571fd0dc6d560dbb0ae6d5774fd19bfda821e733b5441e51af49\",\"sign\":\"80rPGyFJ8CeDNtDN8zWqhXvyuXOTqep3G2ytvaKl+Z5ogdMdKGLUzRs0ZG465r3th7/48AOGLYmAib3gsAAUDA==\"}],\"hash\":\"360505ba8dbc2cc10d105513d1917e7f8a383c8d4dd07d858e19f3331fd8f524\",\"sign\":\"by3eMh+0FFNJ80T10Rr80puocwFbierN2z4/W3ihcNcMOeFOSKETud2SoO+Z0Mv5FXkwNUhtf2rd2/YWp7uxAw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"}},\"hash\":\"ffef282ef09a571fd0dc6d560dbb0ae6d5774fd19bfda821e733b5441e51af49\",\"sign\":\"80rPGyFJ8CeDNtDN8zWqhXvyuXOTqep3G2ytvaKl+Z5ogdMdKGLUzRs0ZG465r3th7/48AOGLYmAib3gsAAUDA==\"}],\"hash\":\"360505ba8dbc2cc10d105513d1917e7f8a383c8d4dd07d858e19f3331fd8f524\",\"sign\":\"by3eMh+0FFNJ80T10Rr80puocwFbierN2z4/W3ihcNcMOeFOSKETud2SoO+Z0Mv5FXkwNUhtf2rd2/YWp7uxAw==\"}"

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