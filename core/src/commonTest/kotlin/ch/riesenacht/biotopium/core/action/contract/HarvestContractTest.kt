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

import ch.riesenacht.biotopium.core.action.model.HarvestAction
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.item.Harvest
import ch.riesenacht.biotopium.core.world.model.item.HarvestedPlant
import ch.riesenacht.biotopium.core.world.model.item.Seed
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.plant.GrowingPlant
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test class for [harvestContract].
 *
 * @author Manuel Riesen
 */
class HarvestContractTest : ContractTest() {

    @Test
    fun testHarvestContractExec_positive() {

        val owner = defaultOwner

        val world = createMutableTestWorldWithPlayer(owner)

        val growingPlant = GrowingPlant(owner, PlantType.CORN, PlantGrowth.GROWN)
        val plot = Plot(0.coord, 0.coord, growingPlant)

        world.tiles[plot.x to plot.y] = plot

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val harvestedPlant = HarvestedPlant(owner, PlantType.CORN)
        val harvest = Harvest(harvestedPlant, listOf(Seed(owner, PlantType.CORN)))

        val content = HarvestAction(harvest, plot.copy(plant = null))
        val action = createActionRecord(currentTimestamp, owner, content)

        execContract(action, world)

        assertTrue(world.players[owner]!!.items.contains(harvest.plant))
        assertTrue(harvest.seeds.all { world.players[owner]!!.items.contains(it) })
        assertNull((world.tiles[plot.x to plot.y] as Plot).plant)
    }
}