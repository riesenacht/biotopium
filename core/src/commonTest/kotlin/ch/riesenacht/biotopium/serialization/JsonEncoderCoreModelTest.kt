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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561\",\"sign\":\"tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ==\"}],\"hash\":\"9776bd23ee341a20878525999f8c649fbd242c5c0a0d8652378735c57844013c\",\"sign\":\"5dDMJd5o6bQQZVCmj/KN5BfPkpRMMcV7GnumpjZdyx9avmuuUbDO2DAaRRU7+GTIYK7j/sBwZqrhFXCup/3aDA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561\",\"sign\":\"tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ==\"}],\"hash\":\"9776bd23ee341a20878525999f8c649fbd242c5c0a0d8652378735c57844013c\",\"sign\":\"5dDMJd5o6bQQZVCmj/KN5BfPkpRMMcV7GnumpjZdyx9avmuuUbDO2DAaRRU7+GTIYK7j/sBwZqrhFXCup/3aDA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"44c765639e6acd2de3ed36ece57999a03496334172a973a0aea15cc3cce33b17\",\"sign\":\"M6ZW5eEnRqsZUBae0MxExEjBLZgqlxOEbXZ6PRviDG64r/0k9dF22yuufjkLQ+l9jNoQKEjLOvNvPDiVYU0LBA==\"}],\"hash\":\"40e31d39a54993677990a1ffa858b444a1cf3139423991d40f82df4136ed1644\",\"sign\":\"pc5EdrmUM2igC66dpEYnseh5x4rYKEcjL5CSsXcPsb9+h+P5gKG1pks5g2/B614Egid/N7EYdvzRS/IlZbb6AQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"44c765639e6acd2de3ed36ece57999a03496334172a973a0aea15cc3cce33b17\",\"sign\":\"M6ZW5eEnRqsZUBae0MxExEjBLZgqlxOEbXZ6PRviDG64r/0k9dF22yuufjkLQ+l9jNoQKEjLOvNvPDiVYU0LBA==\"}],\"hash\":\"40e31d39a54993677990a1ffa858b444a1cf3139423991d40f82df4136ed1644\",\"sign\":\"pc5EdrmUM2igC66dpEYnseh5x4rYKEcjL5CSsXcPsb9+h+P5gKG1pks5g2/B614Egid/N7EYdvzRS/IlZbb6AQ==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"f2e458d1cc0c1083687d0c5c8a66bf35810f2704665ed841b2c59f2b336df5fc\",\"sign\":\"HHn/M/2ojaH/jZDM/KmC0/aTy3KYG5KOFFiZCNDXXoA8uWMXgrq45gs0ac2RBjBUf218wPfFSkEjhdPZuMmyDA==\"}],\"hash\":\"a47c93fee6c1fea35fb4f09c7f2c03832a846992c6334c2ed1db677429617d93\",\"sign\":\"MawNIfQglBgkebBl/lnkNnD/cC5HyYwofnstzuSi26noKUcCXJJltJYgKLe9R4lmMypeCFvFdEVGkTZ2n0P3Bw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}},\"hash\":\"f2e458d1cc0c1083687d0c5c8a66bf35810f2704665ed841b2c59f2b336df5fc\",\"sign\":\"HHn/M/2ojaH/jZDM/KmC0/aTy3KYG5KOFFiZCNDXXoA8uWMXgrq45gs0ac2RBjBUf218wPfFSkEjhdPZuMmyDA==\"}],\"hash\":\"a47c93fee6c1fea35fb4f09c7f2c03832a846992c6334c2ed1db677429617d93\",\"sign\":\"MawNIfQglBgkebBl/lnkNnD/cC5HyYwofnstzuSi26noKUcCXJJltJYgKLe9R4lmMypeCFvFdEVGkTZ2n0P3Bw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"65b8b1b7c0ad8f356c628a40e116850dcf53a85f6d5c51c5206544fd7e468282\",\"sign\":\"IPvGTirlXwE9OMGx/t6KQ1rUze2WiXRqrxp/YYnGwDOd3CDisZTsKy1TQqykrmYVEqn4GkOfAqVGKaNAp02kBg==\"}],\"hash\":\"8fd73c9b0e798e5d31e9cfa9af243cdb61a57c4734b060d99fa4ab858a5a24d5\",\"sign\":\"m8NdPLzq9/DX7NmQAHja0Kf50S1tH2abmf8n6GddfdboQ4ET9lwW3NFHiq7KKIocxiR0PwrDLPsPk40AFGxLAg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"65b8b1b7c0ad8f356c628a40e116850dcf53a85f6d5c51c5206544fd7e468282\",\"sign\":\"IPvGTirlXwE9OMGx/t6KQ1rUze2WiXRqrxp/YYnGwDOd3CDisZTsKy1TQqykrmYVEqn4GkOfAqVGKaNAp02kBg==\"}],\"hash\":\"8fd73c9b0e798e5d31e9cfa9af243cdb61a57c4734b060d99fa4ab858a5a24d5\",\"sign\":\"m8NdPLzq9/DX7NmQAHja0Kf50S1tH2abmf8n6GddfdboQ4ET9lwW3NFHiq7KKIocxiR0PwrDLPsPk40AFGxLAg==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"63cd65bf70064a303250dc7a6efda53340c4870d0a68c41335a879c7fe631b6d\",\"sign\":\"SjoyilpRJluURLsI3iRw0egbqzznCZe1Y//u8ltIoY2EBKiNhcxawl1jsEtNNOz6KUALHz+DLY5gaJSW7282Cg==\"}],\"hash\":\"3dfad61ef7b018a116834b314c34930da215fe2428c473bc96c93ec575993f65\",\"sign\":\"owC276fhcwSwrb7vyobgLOkJgMrRrxR2+B4FElj6E87Am/xFMZ4d37Z5uJvpqwkLWwYHbLuPrq6Y6HYgD3ZoAQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"63cd65bf70064a303250dc7a6efda53340c4870d0a68c41335a879c7fe631b6d\",\"sign\":\"SjoyilpRJluURLsI3iRw0egbqzznCZe1Y//u8ltIoY2EBKiNhcxawl1jsEtNNOz6KUALHz+DLY5gaJSW7282Cg==\"}],\"hash\":\"3dfad61ef7b018a116834b314c34930da215fe2428c473bc96c93ec575993f65\",\"sign\":\"owC276fhcwSwrb7vyobgLOkJgMrRrxR2+B4FElj6E87Am/xFMZ4d37Z5uJvpqwkLWwYHbLuPrq6Y6HYgD3ZoAQ==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},\"hoes\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}],\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"d2dc91e743964842e0b9d2d054391915ff99b5fa33318208a5db779db2596d06\",\"sign\":\"6DYZV8mMYAj0icLXC2wlTnxW64RDwesLAuPIbMbEvNneXu6M5D/h5ya4adAa76Le/aG6U++fdSqb8vouyBItAg==\"}],\"hash\":\"d406dc47fd3a75d170aed3418c4542e79506daa0d358219692f20300074d6950\",\"sign\":\"YfJ5aABHEEdzzcm23LYSYbtQbNivt4nSn6TQgwt2K4Kv3PNujUoCwn/Bs24vu2rTbS+YR3f4nQdPFFPKgq35AA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},\"hoes\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\"}],\"seeds\":[{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"},{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"d2dc91e743964842e0b9d2d054391915ff99b5fa33318208a5db779db2596d06\",\"sign\":\"6DYZV8mMYAj0icLXC2wlTnxW64RDwesLAuPIbMbEvNneXu6M5D/h5ya4adAa76Le/aG6U++fdSqb8vouyBItAg==\"}],\"hash\":\"d406dc47fd3a75d170aed3418c4542e79506daa0d358219692f20300074d6950\",\"sign\":\"YfJ5aABHEEdzzcm23LYSYbtQbNivt4nSn6TQgwt2K4Kv3PNujUoCwn/Bs24vu2rTbS+YR3f4nQdPFFPKgq35AA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"}},\"hash\":\"ffef282ef09a571fd0dc6d560dbb0ae6d5774fd19bfda821e733b5441e51af49\",\"sign\":\"80rPGyFJ8CeDNtDN8zWqhXvyuXOTqep3G2ytvaKl+Z5ogdMdKGLUzRs0ZG465r3th7/48AOGLYmAib3gsAAUDA==\"}],\"hash\":\"47e519f90cf8d896cd0981f8dcc4421106285091b4e1157ae6a37a1ca8189c98\",\"sign\":\"GihQcIg4B9Kp65d9M5TDP5GFLLkUoqGpNWkhzGz8SxLv55nk7Iq06+go9PE0XaBPBUUgFkEdkHF6TobqnM4qCg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"plantType\":\"CORN\"}},\"hash\":\"ffef282ef09a571fd0dc6d560dbb0ae6d5774fd19bfda821e733b5441e51af49\",\"sign\":\"80rPGyFJ8CeDNtDN8zWqhXvyuXOTqep3G2ytvaKl+Z5ogdMdKGLUzRs0ZG465r3th7/48AOGLYmAib3gsAAUDA==\"}],\"hash\":\"47e519f90cf8d896cd0981f8dcc4421106285091b4e1157ae6a37a1ca8189c98\",\"sign\":\"GihQcIg4B9Kp65d9M5TDP5GFLLkUoqGpNWkhzGz8SxLv55nk7Iq06+go9PE0XaBPBUUgFkEdkHF6TobqnM4qCg==\"}"

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