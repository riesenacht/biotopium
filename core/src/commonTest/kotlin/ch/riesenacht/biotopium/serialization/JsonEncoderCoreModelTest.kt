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
        val action = ChunkGenesisAction(zeroTimestamp, defaultOwner, tiles)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"0b23abc8b85115345aeefc28e8de75ab4b1ffeb5266aedf1367440258c66eb97\",\"sign\":\"ar4ElOSbF+CIuF9tKdq2uVWR1ocu7mUkenaANO29NAQPVGLDYT9umhHeV8e4S3lmiFD3xW0yYZan1JxFW8sGDA==\",\"validSign\":\"VkHqQEDwdC54qLqo4zfXDHi8Oy8+1lwzfsija0RhqNtbZfYKKU7ZcWwUNRjr5KnLTGUzd5MycluV2xHwy+PcDg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChunkGenesisAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":1,\"y\":1},{\"class\":\"DefaultTile\",\"x\":2,\"y\":3},{\"class\":\"DefaultTile\",\"x\":4,\"y\":5},{\"class\":\"DefaultTile\",\"x\":6,\"y\":7},{\"class\":\"DefaultTile\",\"x\":8,\"y\":9}]},\"hash\":\"0b23abc8b85115345aeefc28e8de75ab4b1ffeb5266aedf1367440258c66eb97\",\"sign\":\"ar4ElOSbF+CIuF9tKdq2uVWR1ocu7mUkenaANO29NAQPVGLDYT9umhHeV8e4S3lmiFD3xW0yYZan1JxFW8sGDA==\",\"validSign\":\"VkHqQEDwdC54qLqo4zfXDHi8Oy8+1lwzfsija0RhqNtbZfYKKU7ZcWwUNRjr5KnLTGUzd5MycluV2xHwy+PcDg==\"}"

        val tiles = listOf(
            DefaultTile(1.coord, 1.coord),
            DefaultTile(2.coord, 3.coord),
            DefaultTile(4.coord, 5.coord),
            DefaultTile(6.coord, 7.coord),
            DefaultTile(8.coord, 9.coord)
        )
        val action = ChunkGenesisAction(zeroTimestamp, defaultOwner, tiles)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeClaimRealmAction() {
        val owner = defaultOwner
        val realm = Realm(owner, 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)
        val action = ClaimRealmAction(zeroTimestamp, owner, realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ClaimRealmAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"3f43ec75e787cae10ac97db7f9b9b1e399d174b32f53329b0858fe7926ff2540\",\"sign\":\"GRk6Yy0oDa7PsVFf2Tvat21hmIgXpe5SqyrcPMRxBS1Vu4Lu8UbBKXVT8R8OWTKa78tKYHw+yN9TFPWX0Ae4CQ==\",\"validSign\":\"auATVAVJyayTRSbQv+i9zcddSa4qfO+vxN6zhqr+n3h492ilgNTWkuLCWIEc2wMP5Pl77F6ofC52GLN5vynLBQ==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeClaimRealmAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ClaimRealmAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"owner\":\"me\",\"ix\":1,\"iy\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"3f43ec75e787cae10ac97db7f9b9b1e399d174b32f53329b0858fe7926ff2540\",\"sign\":\"GRk6Yy0oDa7PsVFf2Tvat21hmIgXpe5SqyrcPMRxBS1Vu4Lu8UbBKXVT8R8OWTKa78tKYHw+yN9TFPWX0Ae4CQ==\",\"validSign\":\"auATVAVJyayTRSbQv+i9zcddSa4qfO+vxN6zhqr+n3h492ilgNTWkuLCWIEc2wMP5Pl77F6ofC52GLN5vynLBQ==\"}"

        val owner = defaultOwner
        val realm = Realm(owner, 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)
        val action = ClaimRealmAction(zeroTimestamp, owner, realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeCreatePlotAction() {
        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(owner)
        val action = CreatePlotAction(zeroTimestamp, owner, plot, hoe)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"CreatePlotAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"faadaefdf68aa037275eebcf63011fd7040684c09990c083cc5838ad612e8b25\",\"sign\":\"rJZvIiPbOy1lXhbRksCuWy1pAkFYGwOKszGRKNSsiaQu4to8sVneePASgQzSgcVFKAog7P/w5nZ1NPOMND2+Ag==\",\"validSign\":\"wjdfap9RSM5NZinISg7ypPNFp3RfmJ3ezBH84JZU7ofSwed3RLaa/WltjomjzASnptzt55raQaXMtUJufXSKCA==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeCreatePlotAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"CreatePlotAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\"}},\"hash\":\"faadaefdf68aa037275eebcf63011fd7040684c09990c083cc5838ad612e8b25\",\"sign\":\"rJZvIiPbOy1lXhbRksCuWy1pAkFYGwOKszGRKNSsiaQu4to8sVneePASgQzSgcVFKAog7P/w5nZ1NPOMND2+Ag==\",\"validSign\":\"wjdfap9RSM5NZinISg7ypPNFp3RfmJ3ezBH84JZU7ofSwed3RLaa/WltjomjzASnptzt55raQaXMtUJufXSKCA==\"}"

        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(owner)
        val action = CreatePlotAction(zeroTimestamp, owner, plot, hoe)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

    @Test
    fun testEncodeGrowAction() {
        val owner = defaultOwner
        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        val plot = Plot(1.coord, 0.coord, plant)
        val action = GrowAction(zeroTimestamp, owner, plot)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"GrowAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"d4a79f4dd3ab5336c0ab6f028ae7e664602b10f6dc6da10eaec157dd63673810\",\"sign\":\"O/Exg2reHAp1IjYAPFZqrBH4hyVIgGdsq5zXKXxGM3Qi7ptDIBXlxBa6nNBzsP8rwh6B16xMONg+aNrKHv/gDg==\",\"validSign\":\"TJuRmGtrvImSnaHxjanZiyiLhLu1hWDwcD4LHtCeLG8Ez+Nh9L7K+ZV+2P2DOtHjZIIiLMcKRvFxtIEQ/oo9Ag==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeGrowAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"GrowAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"SEED\"}}},\"hash\":\"d4a79f4dd3ab5336c0ab6f028ae7e664602b10f6dc6da10eaec157dd63673810\",\"sign\":\"O/Exg2reHAp1IjYAPFZqrBH4hyVIgGdsq5zXKXxGM3Qi7ptDIBXlxBa6nNBzsP8rwh6B16xMONg+aNrKHv/gDg==\",\"validSign\":\"TJuRmGtrvImSnaHxjanZiyiLhLu1hWDwcD4LHtCeLG8Ez+Nh9L7K+ZV+2P2DOtHjZIIiLMcKRvFxtIEQ/oo9Ag==\"}"

        val owner = defaultOwner
        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        val plot = Plot(1.coord, 0.coord, plant)
        val action = GrowAction(zeroTimestamp, owner, plot)
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
        val action = HarvestAction(zeroTimestamp, owner, harvest, plot)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"HarvestAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"008c3d31006585aab60714ebb6a27a23dfa81fb7c6c0d50a285007d8080ddc62\",\"sign\":\"N+5p7/9uaG9a/Uzq6GwvCHzZ+fv6kyDbxO6EUi4irOsdlV4Ksg6t8hGTIjY/BDNUy/FyNyxlef1wy12QyTzUBQ==\",\"validSign\":\"k+hnb/Sa8ZX5vHBX1VrQGTZ72NZ6lDtcmvP0uEeI8lGh0nN7XeX2MgpxIQIEpchSOGlnU2VGskTLsVk4zWq0Cw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeHarvestAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"HarvestAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"plant\":{\"owner\":\"me\",\"plantType\":\"WHEAT\"},\"seeds\":[{\"owner\":\"me\",\"plantType\":\"WHEAT\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]},\"consume\":{\"x\":1,\"y\":0,\"plant\":{\"owner\":\"me\",\"type\":\"CORN\",\"growth\":\"GROWN\"}}},\"hash\":\"008c3d31006585aab60714ebb6a27a23dfa81fb7c6c0d50a285007d8080ddc62\",\"sign\":\"N+5p7/9uaG9a/Uzq6GwvCHzZ+fv6kyDbxO6EUi4irOsdlV4Ksg6t8hGTIjY/BDNUy/FyNyxlef1wy12QyTzUBQ==\",\"validSign\":\"k+hnb/Sa8ZX5vHBX1VrQGTZ72NZ6lDtcmvP0uEeI8lGh0nN7XeX2MgpxIQIEpchSOGlnU2VGskTLsVk4zWq0Cw==\"}"

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
        val action = HarvestAction(zeroTimestamp, owner, harvest, plot)
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
        val action = IntroductionAction(zeroTimestamp, owner, introductionGift)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"IntroductionAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"2babbc013ba354a19bf17cf9ff63f5e9f0fb34863d03e505c6dc14c47c2109aa\",\"sign\":\"KcGRmZAlcJkHq7MWUkams/S7EeyEqkSJa5XrMGRWSRbuoIX7UrDYoPv61GlCOwQvGw0ADzmM+ZySrN3YMga4CQ==\",\"validSign\":\"h5PL6obUALYrhXWJ05CWjH6LpaEy14ZwPEt4ZvjYwWwUbXh6U6XISIi2VmKE1hxYr+1wN6FP8ghAGTTte7ceDg==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeIntroductionAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"IntroductionAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"realmClaimPaper\":{\"owner\":\"me\"},\"hoes\":[{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"},{\"owner\":\"me\"}],\"seeds\":[{\"owner\":\"me\",\"plantType\":\"CORN\"},{\"owner\":\"me\",\"plantType\":\"WHEAT\"}]}},\"hash\":\"2babbc013ba354a19bf17cf9ff63f5e9f0fb34863d03e505c6dc14c47c2109aa\",\"sign\":\"KcGRmZAlcJkHq7MWUkams/S7EeyEqkSJa5XrMGRWSRbuoIX7UrDYoPv61GlCOwQvGw0ADzmM+ZySrN3YMga4CQ==\",\"validSign\":\"h5PL6obUALYrhXWJ05CWjH6LpaEy14ZwPEt4ZvjYwWwUbXh6U6XISIi2VmKE1hxYr+1wN6FP8ghAGTTte7ceDg==\"}"

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
        val action = IntroductionAction(zeroTimestamp, owner, introductionGift)
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
        val action = SeedAction(zeroTimestamp, owner, plot, seed)
        val block = generateDefaultBlock(action)

        val expected = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"SeedAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}},\"hash\":\"8d2bb2064f1a63168d42f807c9ecbbe0a2f32def06dc38402012092b1be455e6\",\"sign\":\"E2j7V3CbUT4LUxl6qAxPmFUUHWzpx8PdXnQW0VXo2BAQiy+3CvDL/7ez+L2ECI4MzXYkdc2NJya2zUmoeSBIDA==\",\"validSign\":\"LFcfM7M+xuHIcUNSUYoTAKXri/yQRPEO62s48PMJ2AZDqatMQYa9xDHBzEOL3uc9LQrVWZJ7CnSDOi1rOtTlBw==\"}"

        val encoded = JsonEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSeedAction() {

        val encoded = "{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"SeedAction\",\"timestamp\":0,\"author\":\"me\",\"produce\":{\"x\":1,\"y\":0},\"consume\":{\"owner\":\"me\",\"plantType\":\"CORN\"}},\"hash\":\"8d2bb2064f1a63168d42f807c9ecbbe0a2f32def06dc38402012092b1be455e6\",\"sign\":\"E2j7V3CbUT4LUxl6qAxPmFUUHWzpx8PdXnQW0VXo2BAQiy+3CvDL/7ez+L2ECI4MzXYkdc2NJya2zUmoeSBIDA==\",\"validSign\":\"LFcfM7M+xuHIcUNSUYoTAKXri/yQRPEO62s48PMJ2AZDqatMQYa9xDHBzEOL3uc9LQrVWZJ7CnSDOi1rOtTlBw==\"}"

        val owner = defaultOwner
        val plot = Plot(1.coord, 0.coord)
        val seed = Seed(
            owner,
            PlantType.CORN
        )
        val action = SeedAction(zeroTimestamp, owner, plot, seed)
        val block = generateDefaultBlock(action)

        val decoded: Block = JsonEncoder.decode(encoded)

        assertEquals(block, decoded)
    }

}