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

import ch.riesenacht.biotopium.core.action.model.CreatePlotAction
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.item.Hoe
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.TileType
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Test class for [createPlotContract].
 *
 * @author Manuel Riesen
 */
class CreatePlotContractTest : ContractTest() {

    @Test
    fun testCreatePlotContractExec_positive() {
        val owner = defaultOwner

        val world = createMutableTestWorldWithPlayer(owner)

        val plot = Plot(0.coord, 0.coord)

        val tile = DefaultTile(0.coord, 0.coord)
        world.tiles[tile.x to tile.y] = tile

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        world.realms[realm.ix to realm.iy] = realm

        val hoe = Hoe(owner)
        world.players[owner]!!.addItem(hoe)

        val content = CreatePlotAction(plot, hoe)
        val action = createActionRecord(currentTimestamp, owner, content)

        execContract(action, world)

        assertFalse(world.players[owner]!!.items.contains(hoe))
        assertEquals(TileType.PLOT, world.tiles[plot.x to plot.y]!!.type)
    }
}