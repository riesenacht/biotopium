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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]}},\"hash\":\"fd3ce165f24eba9fa4e0639fd05f03592f90aed018b2326557d0cddd209dce15\",\"sign\":\"D8JMA1mTLvXdBQ/vgdOOPpIsXngv4jh8hyKLgXYAaZ1qo0UU8C3wIQBH1BUIJlzU0bKHKiKJEqTJmMVf6JoTBg==\",\"validSign\":\"nJ3zM9H4Wt+jTJBr8bFhOEmVFurxtMmBvyDs0T22uPBNbf78O50nqLWI1P6fsOFHGU/CtQH5/9qkRsRUDPfUBw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]}},\"hash\":\"fd3ce165f24eba9fa4e0639fd05f03592f90aed018b2326557d0cddd209dce15\",\"sign\":\"D8JMA1mTLvXdBQ/vgdOOPpIsXngv4jh8hyKLgXYAaZ1qo0UU8C3wIQBH1BUIJlzU0bKHKiKJEqTJmMVf6JoTBg==\",\"validSign\":\"nJ3zM9H4Wt+jTJBr8bFhOEmVFurxtMmBvyDs0T22uPBNbf78O50nqLWI1P6fsOFHGU/CtQH5/9qkRsRUDPfUBw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"c8bbbcf93cef0504bdd99dc4eac06c7d8ef0da90e72ddf2336d1818a572d8096\",\"sign\":\"nxCEjnan6Q5qIpfxHBZTmReiP8/Orz3oxUjOWDXxKAVY3zDiFVwdlZXnzqwY0Q20ol+x29/VwvwT0BBOAGwkCA==\",\"validSign\":\"HmLEo016WenJFIUaVPLijOHivCC2t9M0aSK5D3A94kNEW+gi+7CvJ/P5umF2uPCYqlkEuUSop6jfnkl1Zw0MDQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"c8bbbcf93cef0504bdd99dc4eac06c7d8ef0da90e72ddf2336d1818a572d8096\",\"sign\":\"nxCEjnan6Q5qIpfxHBZTmReiP8/Orz3oxUjOWDXxKAVY3zDiFVwdlZXnzqwY0Q20ol+x29/VwvwT0BBOAGwkCA==\",\"validSign\":\"HmLEo016WenJFIUaVPLijOHivCC2t9M0aSK5D3A94kNEW+gi+7CvJ/P5umF2uPCYqlkEuUSop6jfnkl1Zw0MDQ==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"0952e801990d7d77386f16c6bee60b6fbf3b1a4f70e141996b83c7492ee76cf9\",\"sign\":\"0j0+KqtJnjwfoiPh5wBiuaG3g3bPSCj2+Xpu6HbXxP0TTBENPz/EjAaGyWxlRu+NYeYrTN1bMaQHSTYHNx8PCQ==\",\"validSign\":\"oyBPnBh7vaww6+6x+bZpN0xO1CTziVHuH0/vZYuJJtoeCxcQrjollGnGQTZKIZu7+evgSlNMl48azOD6cITDDA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"0952e801990d7d77386f16c6bee60b6fbf3b1a4f70e141996b83c7492ee76cf9\",\"sign\":\"0j0+KqtJnjwfoiPh5wBiuaG3g3bPSCj2+Xpu6HbXxP0TTBENPz/EjAaGyWxlRu+NYeYrTN1bMaQHSTYHNx8PCQ==\",\"validSign\":\"oyBPnBh7vaww6+6x+bZpN0xO1CTziVHuH0/vZYuJJtoeCxcQrjollGnGQTZKIZu7+evgSlNMl48azOD6cITDDA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"SEED\"}}}},\"hash\":\"5d7a3a0dcde35bd6a9e48305f2c19408fb60586e6941a0fc525b75f0966b16f5\",\"sign\":\"w022gxGfO+VYSbeBjFEdwRVTxya+zuM+JXhyUZ1oNNrumrGvmU96SRFSEPdD0sDLXikvhQOo0y/FKsXANHc9Ag==\",\"validSign\":\"edJJKaLv/iMDBhB9HsCn4FXAwvyq4J014jicloMuzOlPMLtv9q0+Mq7+uQhy/lfuZrQPc22UZZ1VbKZSyCXsBA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"SEED\"}}}},\"hash\":\"5d7a3a0dcde35bd6a9e48305f2c19408fb60586e6941a0fc525b75f0966b16f5\",\"sign\":\"w022gxGfO+VYSbeBjFEdwRVTxya+zuM+JXhyUZ1oNNrumrGvmU96SRFSEPdD0sDLXikvhQOo0y/FKsXANHc9Ag==\",\"validSign\":\"edJJKaLv/iMDBhB9HsCn4FXAwvyq4J014jicloMuzOlPMLtv9q0+Mq7+uQhy/lfuZrQPc22UZZ1VbKZSyCXsBA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}}},\"hash\":\"fb7dac570e20c19999468c735757de1c3b349e5edd89abac9e0ffbc0e6c5e81b\",\"sign\":\"0ARWdda1tfbu66ISS5zyIrL6jw1KlmZsJI8rrNPDiHLz9gCkqddV/sDbFMnSTl8jVLc8in/08YIFEb9mmlyTDQ==\",\"validSign\":\"khFAbA2Q5GjR5I9prG4SsjQK4PSne2NYTtKUvsdF4lAlClm2mmZ4fIYTM0FYrLxHq6el9cKnAz9J/B9avGIZDw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}}},\"hash\":\"fb7dac570e20c19999468c735757de1c3b349e5edd89abac9e0ffbc0e6c5e81b\",\"sign\":\"0ARWdda1tfbu66ISS5zyIrL6jw1KlmZsJI8rrNPDiHLz9gCkqddV/sDbFMnSTl8jVLc8in/08YIFEb9mmlyTDQ==\",\"validSign\":\"khFAbA2Q5GjR5I9prG4SsjQK4PSne2NYTtKUvsdF4lAlClm2mmZ4fIYTM0FYrLxHq6el9cKnAz9J/B9avGIZDw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}}},\"hash\":\"e3d71be45f2431b0f28c979ab381cf36a56f8c9d288631733668524f28c9df83\",\"sign\":\"kN5dFW5ABuf+wsIrc9aCQdYzyhpDQo4OXXbqblVCHzzJgHTJDJl8zfi1XkfIEwJNL6IX33VmWkEAqSOnkPGqDw==\",\"validSign\":\"Yt8g7+S3D8n7qnmmU0rJngpyhoeb3KpxSxTU9etbeHd1Zz9iv7tjYrbxv7GrSoyJLRlCwILaI0LClWeitIHGDg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}}},\"hash\":\"e3d71be45f2431b0f28c979ab381cf36a56f8c9d288631733668524f28c9df83\",\"sign\":\"kN5dFW5ABuf+wsIrc9aCQdYzyhpDQo4OXXbqblVCHzzJgHTJDJl8zfi1XkfIEwJNL6IX33VmWkEAqSOnkPGqDw==\",\"validSign\":\"Yt8g7+S3D8n7qnmmU0rJngpyhoeb3KpxSxTU9etbeHd1Zz9iv7tjYrbxv7GrSoyJLRlCwILaI0LClWeitIHGDg==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}}},\"hash\":\"1b319f8020bc0c9e2622290d44413be7728b99673ba8d2040a1b613d0cf40183\",\"sign\":\"61UhDgQZ37ZOMPaqqBkUzrtx1n09Q+tBAnHd7uHzHyQ0dvqYYoLEK62ha2Yga0ofv6V6FfdAwtVzHYOW3HNaAQ==\",\"validSign\":\"gSNI9DCgLEE6crW62VPALQeu3eFFLhQaldUihICJjg8RB4g6Li4Tpic5Sw4KPTacnq15HCl7urcH00W3aywkDg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}}},\"hash\":\"1b319f8020bc0c9e2622290d44413be7728b99673ba8d2040a1b613d0cf40183\",\"sign\":\"61UhDgQZ37ZOMPaqqBkUzrtx1n09Q+tBAnHd7uHzHyQ0dvqYYoLEK62ha2Yga0ofv6V6FfdAwtVzHYOW3HNaAQ==\",\"validSign\":\"gSNI9DCgLEE6crW62VPALQeu3eFFLhQaldUihICJjg8RB4g6Li4Tpic5Sw4KPTacnq15HCl7urcH00W3aywkDg==\"}"

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