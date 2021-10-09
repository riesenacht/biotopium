package ch.riesenacht.biotopium.serialization

import ch.riesenacht.biotopium.core.action.model.*
import ch.riesenacht.biotopium.core.effect.applyEffect
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
 * Test class for [HashableStringEncoder].
 *
 * @author Manuel Riesen
 */
class HashableStringEncoderTest : EncoderTest() {

    @BeforeTest
    fun init() {
        applyEffect(CoreSerializersModuleEffect)
    }

    @Test
    fun testEncodeChunkGenesisAction() {
        val tiles = listOf(
            DefaultTile(1.coord, 1.coord),
            DefaultTile(2.coord, 3.coord),
            DefaultTile(4.coord, 5.coord),
            DefaultTile(6.coord, 7.coord),
            DefaultTile(8.coord, 9.coord),
        )
        val action = ChunkGenesisAction(tiles)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;ChunkGenesisAction;DefaultTile;1;1;0;DefaultTile;2;3;0;DefaultTile;4;5;0;DefaultTile;6;7;0;DefaultTile;8;9;0;0"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeClaimRealmAction() {
        val owner = Owner.fromBase64("me")
        val realm = Realm(owner, 1.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)
        val action = ClaimRealmAction(realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;ClaimRealmAction;me;1;0;me;2;2"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeCreatePlotAction() {
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(Owner.fromBase64("me"))
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;CreatePlotAction;1;0;1;me;0;3"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeGrowAction() {
        val plot = Plot(1.coord, 0.coord)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;GrowAction;1;0;1;5"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
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

        val expected = "1;1;prevHash;test;blocklord;HarvestAction;me;0;3;me;0;1;me;0;1;1;0;1;6"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
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

        val expected = "1;1;prevHash;test;blocklord;IntroductionAction;me;2;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;1;1;me;0;1;1"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
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

        val expected = "1;1;prevHash;test;blocklord;SeedAction;1;0;1;me;1;1;4"

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }
}