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

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;ChunkGenesisAction;DefaultTile;1;1;0;DefaultTile;2;3;0;DefaultTile;4;5;0;DefaultTile;6;7;0;DefaultTile;8;9;0;0;b73a3ed5a5f9fd2c6ee09c074cc01fc625a3c190e1b53d52cd695aac52e46561;tFJ6SNhjy6Z2DRj7wKhIXcyHHMxRZ8V3qJV+Ncgdg26J5wydG5/Lw8eFOi1WLd9KzXp+T2LDGJNTq2G7JMI3DQ=="

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

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;ClaimRealmAction;me;1;0;me;2;2;73325f604d6081a5c1334b498c9b7f2b6a6c638feb5f8e61536fe34b2e5709f9;EDxlB3wSpjKyGaxEf7/MOd+pW67QOlDm0v+zy++EWxvIsfGqa1EhcDgbrbuEFiOEOdCkexwhcWtFC1uYsQEqCw=="

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeCreatePlotAction() {
        val plot = Plot(1.coord, 0.coord)
        val hoe = Hoe(Owner.fromBase64("me"))
        val action = CreatePlotAction(plot, hoe)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;CreatePlotAction;1;0;1;me;0;3;9d6eeaa39b95af5cf23c4e96fc438946136f7ff57f27e7026fbefe803649a269;9xvM/Z/8mPDMMDm75E0+Ns8SzPxQGcoBaBotjyzlRKo5KLQem4zskTYGfHBEDspeAQZZpgCY8D3z+UJNVkRDBw=="

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }

    @Test
    fun testEncodeGrowAction() {
        val plot = Plot(1.coord, 0.coord)
        val action = GrowAction(plot)
        val block = generateDefaultBlock(action)

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;GrowAction;1;0;1;5;d4adeca141e6ec98c7978dec07df8ed05d84a83c764ca38cf38d792c0a28baca;6F/NXx9CuV4sF4bg1lcdNynoxSC9eG/Fw4G1L4Hm3kaFxDomXHYUIApGoRf53tLCJ9GnjgyBQvxslfRVUtL7CQ=="

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

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;HarvestAction;me;0;3;me;0;1;me;0;1;1;0;1;6;8b1b9f7a4c20652bb53500828f63ad3edc44bfd1807de43f74d6aeada74727f4;sLNo4LqiJAJhuZieZu+lOSX20uZ+slK/n2QTwDkU0QQTpQwqHR1NlFtnloDWfRDCxve1S075OFHI6rxYJapSAA=="

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

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;IntroductionAction;me;2;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;0;me;1;1;me;0;1;1;5a6abc042232f2e8490125dc01ea0d3ce134cf3f41a4e3b324acb7afe2e887c1;zn2wayUKyNdNSwTQ3B/YAD5avv0M3XhtyOSm6q5J2YDQd31cwaOod974jpXvn30JMd5Kae2WMQo2sm40lUmcDA=="

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

        val expected = "1;1;prevHash;test;ActionFrame;0;GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=;SeedAction;1;0;1;me;1;1;4;b8b3df977dd0977d6683f67dedf046812698dd1004c5d00a73778daab1677bd5;Edim7MhoKW4ETeRSDCB9coQW2DQkj58VYBRHwvtJRlS2w6AIHgY7lk5PcC3iwvsDf81CXgqvKWMA2Cza4ewaDQ=="

        val encoded = HashableStringEncoder.encode(block.toHashable())

        assertEquals(expected, encoded)
    }
}