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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]}},\"hash\":\"a62ad39e03f17b84b2b1b50f571af233032a29dd489254c34967f401d2094bc1\",\"sign\":\"dKLgIZuUhvd/DMj0t/cfwnA3j8Iw6TmnXD/RtV5b8bRueWkr7xf202vGAoQGtq9LBd4I9bYKYhDCDdE8S/QRCA==\",\"validSign\":\"Wm/3D56KUGk7Qg6qc1R+Dkobc8chmgEFc8F7N5PhIhYp4cPxoEXuJhQ1OJ/+pnOWdDHyrvZUQMhTHS7l2bfTDw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]}},\"hash\":\"a62ad39e03f17b84b2b1b50f571af233032a29dd489254c34967f401d2094bc1\",\"sign\":\"dKLgIZuUhvd/DMj0t/cfwnA3j8Iw6TmnXD/RtV5b8bRueWkr7xf202vGAoQGtq9LBd4I9bYKYhDCDdE8S/QRCA==\",\"validSign\":\"Wm/3D56KUGk7Qg6qc1R+Dkobc8chmgEFc8F7N5PhIhYp4cPxoEXuJhQ1OJ/+pnOWdDHyrvZUQMhTHS7l2bfTDw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"f3a930ce03569a4f09a39ff0e03ae8869bd88b2a3d59b30039c652475e0d65f3\",\"sign\":\"RHH1mGYBp6CF8GkDTZDsHp7LQ/H+2QFmW0VHxgIwgpH+5hAlILy2OQQnsaJXLvQBEWi8nIN1VuL2IJjgLAV5DQ==\",\"validSign\":\"EG2xbnuyQZz2PSY/pUO5XV2qI9VV8b091V3lggHV7y3DhNVXA3nJuz4ZVuN3J7sk95OC5vEHbyW6fD/3kg3WAw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ClaimRealmAction\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"f3a930ce03569a4f09a39ff0e03ae8869bd88b2a3d59b30039c652475e0d65f3\",\"sign\":\"RHH1mGYBp6CF8GkDTZDsHp7LQ/H+2QFmW0VHxgIwgpH+5hAlILy2OQQnsaJXLvQBEWi8nIN1VuL2IJjgLAV5DQ==\",\"validSign\":\"EG2xbnuyQZz2PSY/pUO5XV2qI9VV8b091V3lggHV7y3DhNVXA3nJuz4ZVuN3J7sk95OC5vEHbyW6fD/3kg3WAw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"507604508d814dba87298f26d311e6e7678982945afd31a238638a35fdd6296d\",\"sign\":\"bgIWXru4LbPduevSaco9/8vBYr5Wj1rCi66JQmmYRh8vjKbnJJQDmXMMcCyNBr18WI4iwTPkzKNsKINa5uL+CQ==\",\"validSign\":\"mpWS4cIJtsFcKn6uoLYuoSMDW68gmmTTf/19i7oHEEKKVcEv1pTcHDHwM6XxdwYkFhSe17RkgHbhkD+oh7O6Dg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"CreatePlotAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}}},\"hash\":\"507604508d814dba87298f26d311e6e7678982945afd31a238638a35fdd6296d\",\"sign\":\"bgIWXru4LbPduevSaco9/8vBYr5Wj1rCi66JQmmYRh8vjKbnJJQDmXMMcCyNBr18WI4iwTPkzKNsKINa5uL+CQ==\",\"validSign\":\"mpWS4cIJtsFcKn6uoLYuoSMDW68gmmTTf/19i7oHEEKKVcEv1pTcHDHwM6XxdwYkFhSe17RkgHbhkD+oh7O6Dg==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"SEED\"}}}},\"hash\":\"0b3a394d50434fcb7c1daf81428b44ffbeca0c24c59ce43b5da906c0cea71fbd\",\"sign\":\"nDjUn2om1eOj7eSYtwIbYx62ZbktfA20pi+9nqMfJLimI06JqfJQGh7klgScN0UP8m16IBh0USakncgKpm+6Bg==\",\"validSign\":\"DVINBFjPeNXRO0H18g8sxeQkLNukFwHX0ORkc6CUFKjOAWLYeX0nuUvtiA/uA5v3tcRmYBQBrXQewke81KPKBw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"GrowAction\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"SEED\"}}}},\"hash\":\"0b3a394d50434fcb7c1daf81428b44ffbeca0c24c59ce43b5da906c0cea71fbd\",\"sign\":\"nDjUn2om1eOj7eSYtwIbYx62ZbktfA20pi+9nqMfJLimI06JqfJQGh7klgScN0UP8m16IBh0USakncgKpm+6Bg==\",\"validSign\":\"DVINBFjPeNXRO0H18g8sxeQkLNukFwHX0ORkc6CUFKjOAWLYeX0nuUvtiA/uA5v3tcRmYBQBrXQewke81KPKBw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}}},\"hash\":\"1ed48471361c2e7864212645b4e04aa15e54c62fe1de354947533589fa284c03\",\"sign\":\"mdh1neOB144XfJNjZfJP8zmx3WBXrVqz1Z1xpWdDmVzXTpi2i/JMwLD16cqnlqyziGe1Nqg1hfM+CfcQNNElBQ==\",\"validSign\":\"UmEns4dwPtL6NKabA4m7HgpQTpRq34oslIigD7zGlIV9UvfHmsXQuU5dSwkS7XqANe4q6qQgZLk84DsUn7a/DA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"HarvestAction\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}}},\"hash\":\"1ed48471361c2e7864212645b4e04aa15e54c62fe1de354947533589fa284c03\",\"sign\":\"mdh1neOB144XfJNjZfJP8zmx3WBXrVqz1Z1xpWdDmVzXTpi2i/JMwLD16cqnlqyziGe1Nqg1hfM+CfcQNNElBQ==\",\"validSign\":\"UmEns4dwPtL6NKabA4m7HgpQTpRq34oslIigD7zGlIV9UvfHmsXQuU5dSwkS7XqANe4q6qQgZLk84DsUn7a/DA==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}}},\"hash\":\"52a3722c952538c3726651e1acb47f388a6f358d12973f2d0620b5f52a1e2002\",\"sign\":\"kzgj3si8Fce4487ye5vgx355klXyNNTSqfpKBhfGA+BNRjSMgzW8GrHlE28OfYhWmzxeAIy6c7wmNRCxlUxXBA==\",\"validSign\":\"tT24674aq7xymk98PGelGwQNqcnUieyxpg67Eb7BoYVnRmCC27kDX28YV90KXxTG0af1o1YhIH97cbGipp3ZDw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"IntroductionAction\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}}},\"hash\":\"52a3722c952538c3726651e1acb47f388a6f358d12973f2d0620b5f52a1e2002\",\"sign\":\"kzgj3si8Fce4487ye5vgx355klXyNNTSqfpKBhfGA+BNRjSMgzW8GrHlE28OfYhWmzxeAIy6c7wmNRCxlUxXBA==\",\"validSign\":\"tT24674aq7xymk98PGelGwQNqcnUieyxpg67Eb7BoYVnRmCC27kDX28YV90KXxTG0af1o1YhIH97cbGipp3ZDw==\"}"

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

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}}},\"hash\":\"2f3791c2b1577d281bca5fcb1588a0d56d916969eceab41bb0ebb86a8df4f834\",\"sign\":\"fxi3XrYrGk4JVlSxzTyDnufeJe3cNh3OUj7ZqypMgc8B/Vc+zBYpkLdvhY9ds1mDrG8JjyPWCtx+jda9BEMEAA==\",\"validSign\":\"RvE0y4yaFTf9m+Lj6tAI99wNlUFxj3tnq+RRi3G/Lnb9fAck0ojQwZMm+SpCnjSDOd4Zw8wUUXGVMyTsV9lJAw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"SeedAction\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}}},\"hash\":\"2f3791c2b1577d281bca5fcb1588a0d56d916969eceab41bb0ebb86a8df4f834\",\"sign\":\"fxi3XrYrGk4JVlSxzTyDnufeJe3cNh3OUj7ZqypMgc8B/Vc+zBYpkLdvhY9ds1mDrG8JjyPWCtx+jda9BEMEAA==\",\"validSign\":\"RvE0y4yaFTf9m+Lj6tAI99wNlUFxj3tnq+RRi3G/Lnb9fAck0ojQwZMm+SpCnjSDOd4Zw8wUUXGVMyTsV9lJAw==\"}"

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