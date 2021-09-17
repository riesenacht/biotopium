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
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
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


        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"a04dcf674892deed86c9d9ecf99ddf8165235e56025609290ab24ba38b62bd60\",\"sign\":\"eN8dR3HmxTDThfVhv+w1i4fbZO/KJif/BIG0hHqGXbGOaauhlAYKckrYf/WcCRQFiW89/UIEhoPw0WF3EnolAg==\",\"validSign\":\"GE5PZH2Gn3KkezDBQaRtezWO5h31kVwjPDtmvV+Ct/F9X7i1Qy62D+4KCrL+rpHvEpS7tkf3D4QSKh5Rul2KAQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"a04dcf674892deed86c9d9ecf99ddf8165235e56025609290ab24ba38b62bd60\",\"sign\":\"eN8dR3HmxTDThfVhv+w1i4fbZO/KJif/BIG0hHqGXbGOaauhlAYKckrYf/WcCRQFiW89/UIEhoPw0WF3EnolAg==\",\"validSign\":\"GE5PZH2Gn3KkezDBQaRtezWO5h31kVwjPDtmvV+Ct/F9X7i1Qy62D+4KCrL+rpHvEpS7tkf3D4QSKh5Rul2KAQ==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"273423b481183679f5e1ed8506c3924560083f3222ac2a82fa0e9de3ae8c2cc5\",\"sign\":\"VDZU0lzoh5MSYkgm9G8AJihf+L3WxB3K7fSQrlcy8UHbPr/igryaG5cxzGnltAegAWEYX/FIF9yDBgOXF6iNDQ==\",\"validSign\":\"yR97jdEpMCsshNtqwPewyxptaftklvpkQ34+iaG7f3ruRIpbNBO9p0YNhR1DCj8tV1iVeF9WhoVyzaAUvLr9Aw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"273423b481183679f5e1ed8506c3924560083f3222ac2a82fa0e9de3ae8c2cc5\",\"sign\":\"VDZU0lzoh5MSYkgm9G8AJihf+L3WxB3K7fSQrlcy8UHbPr/igryaG5cxzGnltAegAWEYX/FIF9yDBgOXF6iNDQ==\",\"validSign\":\"yR97jdEpMCsshNtqwPewyxptaftklvpkQ34+iaG7f3ruRIpbNBO9p0YNhR1DCj8tV1iVeF9WhoVyzaAUvLr9Aw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"4e50ff675e331d912546a34683a8651f1a499818f2ff7edf5843ec6eb7b6fdeb\",\"sign\":\"nalgh9CiRs4o/f8SoGRGsTW9tfbjKTgKlOQWKDcKBpUJMPJDyIeBMNtD/cLIMLwNIPgESZ+f3qw0JNIB8FvtDg==\",\"validSign\":\"pbmDWcSFeDDCoGj8vtAmpUP6ytNZq9Yeh90zCuGx9YZEAXdy10wtqkbsyo32hyYw4k+m3xIQ/PcGrbgmqmNdBA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"4e50ff675e331d912546a34683a8651f1a499818f2ff7edf5843ec6eb7b6fdeb\",\"sign\":\"nalgh9CiRs4o/f8SoGRGsTW9tfbjKTgKlOQWKDcKBpUJMPJDyIeBMNtD/cLIMLwNIPgESZ+f3qw0JNIB8FvtDg==\",\"validSign\":\"pbmDWcSFeDDCoGj8vtAmpUP6ytNZq9Yeh90zCuGx9YZEAXdy10wtqkbsyo32hyYw4k+m3xIQ/PcGrbgmqmNdBA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0}},\"hash\":\"1cdc08bad690be7a1b92fd994b2aa8f8933d235d6dd3e5452a1e4c5c0d4cea28\",\"sign\":\"d2FdV/JQBVGB0qIw3tzF0A9r+nHh0uY0P0VS4HGMEKXt7t2aXaLagzCV8Ta7oacP+ftsjxQ8kVyerCCHggQaAg==\",\"validSign\":\"BAwaVmpQb2Qr/yivS2zKZIJa6bpOXDJfmhEhvlftZAKn4aJR60l59BCm8gfL7s7E+NC2vWuOipdwdJWJi4azDw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0}},\"hash\":\"1cdc08bad690be7a1b92fd994b2aa8f8933d235d6dd3e5452a1e4c5c0d4cea28\",\"sign\":\"d2FdV/JQBVGB0qIw3tzF0A9r+nHh0uY0P0VS4HGMEKXt7t2aXaLagzCV8Ta7oacP+ftsjxQ8kVyerCCHggQaAg==\",\"validSign\":\"BAwaVmpQb2Qr/yivS2zKZIJa6bpOXDJfmhEhvlftZAKn4aJR60l59BCm8gfL7s7E+NC2vWuOipdwdJWJi4azDw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0}},\"hash\":\"78d6588074922fc0a548a622208996011c77a2569123d94c3a3edfc893327cf7\",\"sign\":\"xOSCPKKtuKnyRLYQMyG5qXXJUNDfrzKeNLAyo5Dj760OYy3oxI/yE3Vk+nyxnI1lufEX1q0nfldIpQ2SIgoTBw==\",\"validSign\":\"2AQmi3BR6vuGwsLgQrxDUvZjedhguZMDrQ+kVtbR6MVHuKBA/Y4y5eyrxDT2GUNdbruSqDdWn3+LtHVWFvJwAA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0}},\"hash\":\"78d6588074922fc0a548a622208996011c77a2569123d94c3a3edfc893327cf7\",\"sign\":\"xOSCPKKtuKnyRLYQMyG5qXXJUNDfrzKeNLAyo5Dj760OYy3oxI/yE3Vk+nyxnI1lufEX1q0nfldIpQ2SIgoTBw==\",\"validSign\":\"2AQmi3BR6vuGwsLgQrxDUvZjedhguZMDrQ+kVtbR6MVHuKBA/Y4y5eyrxDT2GUNdbruSqDdWn3+LtHVWFvJwAA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"7be346184d868f4747a4953f12585c5d8864d9e17173f7e838a0f3173828f00d\",\"sign\":\"0euLJcU1sULBi6H34FFseBLOmYf31nTTyPqi6PdOshzM+A4aVGrrhbSLT7/srLMbQPDTsJ3YKgw+/ZG6abPgDQ==\",\"validSign\":\"h91gHdy1TqcOAZXSH1j9dFApbjS1maiOq11Q6DaG68UrAO8xpwLJ9krxt2Q732DyynH9N9wLcjVEa/xcacNaBQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"7be346184d868f4747a4953f12585c5d8864d9e17173f7e838a0f3173828f00d\",\"sign\":\"0euLJcU1sULBi6H34FFseBLOmYf31nTTyPqi6PdOshzM+A4aVGrrhbSLT7/srLMbQPDTsJ3YKgw+/ZG6abPgDQ==\",\"validSign\":\"h91gHdy1TqcOAZXSH1j9dFApbjS1maiOq11Q6DaG68UrAO8xpwLJ9krxt2Q732DyynH9N9wLcjVEa/xcacNaBQ==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}},\"hash\":\"a8173407c3a538844c97fae02157372b756695cc74d5950b7a6c3040bb8acfc3\",\"sign\":\"pGInRxqjDXPBSZLWDFR4CoLl4svv7b4LUj6Wp2H20YQfN1cElCmzSh4ea1B9tvjZ5fw+/hknBDkP+54OQAz1DQ==\",\"validSign\":\"ErcAMMe57+MGXorlD0V5CVS9updokS0NEfPltoLaLrAQfRv0H1KrKiBhwVSQemR6FT6SJaTCDJMZOJsy3GdiCQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}},\"hash\":\"a8173407c3a538844c97fae02157372b756695cc74d5950b7a6c3040bb8acfc3\",\"sign\":\"pGInRxqjDXPBSZLWDFR4CoLl4svv7b4LUj6Wp2H20YQfN1cElCmzSh4ea1B9tvjZ5fw+/hknBDkP+54OQAz1DQ==\",\"validSign\":\"ErcAMMe57+MGXorlD0V5CVS9updokS0NEfPltoLaLrAQfRv0H1KrKiBhwVSQemR6FT6SJaTCDJMZOJsy3GdiCQ==\"}"

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