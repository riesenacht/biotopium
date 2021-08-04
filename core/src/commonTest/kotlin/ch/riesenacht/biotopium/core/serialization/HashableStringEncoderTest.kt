package ch.riesenacht.biotopium.core.serialization

import ch.riesenacht.biotopium.core.action.*
import ch.riesenacht.biotopium.core.model.item.*
import ch.riesenacht.biotopium.core.model.map.DefaultTile
import ch.riesenacht.biotopium.core.model.map.Plot
import ch.riesenacht.biotopium.core.model.map.Realm
import ch.riesenacht.biotopium.core.model.misc.IntroductionGift
import ch.riesenacht.biotopium.core.model.plant.PlantType
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [HashableStringEncoder].
 *
 * @author Manuel Riesen
 */
class HashableStringEncoderTest : EncoderTest() {

    @Test
    fun testEncodeChunkGenesisAction() {
        val tiles = listOf(
            DefaultTile(1, 1),
            DefaultTile(2, 3),
            DefaultTile(4, 5),
            DefaultTile(6, 7),
            DefaultTile(8, 9),
        )
        val action = ChunkGenesisAction(tiles)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;ChunkGenesisAction;DefaultTile;1;1;0;DefaultTile;2;3;0;DefaultTile;4;5;0;DefaultTile;6;7;0;DefaultTile;8;9;0;0"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeClaimRealmAction() {
        val realm = Realm("me", 1, 0)
        val realmClaimPaper = RealmClaimPaper("me")
        val action = ClaimRealmAction(realm, realmClaimPaper)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;ClaimRealmAction;me;1;0;me;2;2"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeCreatePlotAction() {
        val plot = Plot(1, 0)
        val hoe = Hoe("me")
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;CreatePlotAction;1;0;1;me;0;3"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeGrowAction() {
        val plot = Plot(1, 0)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;GrowAction;1;0;1;5"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeHarvestAction() {
        val plot = Plot(1, 0)
        val harvest = Harvest(
            HarvestedPlant("me", PlantType.WHEAT),
            listOf(
                Seed("me", PlantType.WHEAT),
                Seed("me", PlantType.WHEAT)
            )
        )
        val action = HarvestAction(harvest, plot)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;HarvestAction;me;0;3;me;0;1;me;0;1;1;0;1;6"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeIntroductionAction() {
        val introductionGift = IntroductionGift(
            RealmClaimPaper("me"),
            (0..8).map { Hoe("me") }.toList(),
            listOf(Seed("me", PlantType.CORN), Seed("me", PlantType.WHEAT))
        )
        val action = IntroductionAction(introductionGift)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;IntroductionAction;me;2;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;1;1;me;0;1;1"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeSeedAction() {
        val plot = Plot(1, 0)
        val seed = Seed("me", PlantType.CORN)
        val action = SeedAction(plot, seed)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;blocklord;SeedAction;Plot;1;0;1;me;1;1;4"

        val encoded = HashableStringEncoder.encode(block)

        assertEquals(expected, encoded)
    }
}