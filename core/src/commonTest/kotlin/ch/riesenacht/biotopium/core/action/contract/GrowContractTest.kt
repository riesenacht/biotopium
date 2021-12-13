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

import ch.riesenacht.biotopium.core.action.model.GrowAction
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [growContract].
 *
 * @author Manuel Riesen
 */
class GrowContractTest : ContractTest() {

    @Test
    fun testGrowContractExec_positive() {
        val timestamp = currentTimestamp

        val owner = defaultOwner

        val world = createMutableTestWorldWithPlayer(owner)

        val plant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.SEED)
        plant.lastGrowth = timestamp
        val plot = Plot(0.coord, 0.coord, plant)
        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val content = GrowAction(plot.copy(plant = plant.copy(growth = PlantGrowth.HALF_GROWN)))
        val action = createActionRecord(timestamp, owner, content)

        execContract(action, world)

        assertEquals(PlantGrowth.HALF_GROWN, (world.tiles[plot.x to plot.y] as Plot).plant!!.growth)
        assertEquals(timestamp, (world.tiles[plot.x to plot.y] as Plot).plant!!.lastGrowth)
    }
}