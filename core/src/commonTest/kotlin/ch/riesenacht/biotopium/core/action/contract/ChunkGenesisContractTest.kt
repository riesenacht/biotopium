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

import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.model.ChunkGenesisAction
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import kotlin.test.Test
import kotlin.test.assertContains

/**
 * Test class for [chunkGenesisContract].
 *
 * @author Manuel Riesen
 */
class ChunkGenesisContractTest : ContractTest() {

    @Test
    fun testChunkGenesisExec_positive() {

        val world = createMutableWorld()

        val owner = createDefaultOwner()

        val block = createDefaultBlockOrigin(owner)

        val tile11 = DefaultTile(1.coord, 1.coord)

        val action = ChunkGenesisAction(listOf(tile11))

        execContract(action, block, world)

        assertContains(world.tiles, tile11.x to tile11.y)
    }

}