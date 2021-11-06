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

package ch.riesenacht.biotopium.core.action.contract

import ch.riesenacht.biotopium.core.action.model.SeedAction
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.item.Seed
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test class for [seedContract].
 *
 * @author Manuel Riesen
 */
class SeedContractTest : ContractTest() {

    @Test
    fun testSeedContractExec_positive() {

        val owner = defaultOwner

        val world = createMutableTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val seed = Seed(owner, PlantType.CORN)
        world.players[owner]?.addItem(seed)

        val plant = GrowingPlant(owner, seed.plantType, PlantGrowth.SEED)

        val action = SeedAction(currentTimestamp, owner, plot.copy(plant = plant), seed)

        execContract(action, world)

        assertNotNull((world.tiles[plot.x to plot.y] as Plot).plant)
        assertEquals(plant.type, (world.tiles[plot.x to plot.y] as Plot).plant!!.type)
        assertEquals(plant.lastGrowth, (world.tiles[plot.x to plot.y] as Plot).plant!!.lastGrowth)
        assertEquals(PlantGrowth.SEED, (world.tiles[plot.x to plot.y] as Plot).plant!!.growth)

    }
}