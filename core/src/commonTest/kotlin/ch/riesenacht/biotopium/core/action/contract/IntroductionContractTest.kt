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

import ch.riesenacht.biotopium.core.action.model.IntroductionAction
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.regionIndex
import ch.riesenacht.biotopium.core.world.model.item.Hoe
import ch.riesenacht.biotopium.core.world.model.item.IntroductionGift
import ch.riesenacht.biotopium.core.world.model.item.RealmClaimPaper
import ch.riesenacht.biotopium.core.world.model.item.Seed
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test class for [introductionContract].
 *
 * @author Manuel Riesen
 */
class IntroductionContractTest : ContractTest() {

    private val region: Region
        get() = Region(0.regionIndex, 0.regionIndex)

    @Test
    fun testIntroductionContractExec_positive() {

        val owner = defaultOwner

        val world = createMutableWorld()

        val gift = IntroductionGift(
            RealmClaimPaper(owner),
            listOf(Hoe(owner), Hoe(owner)),
            listOf(Seed(owner, PlantType.WHEAT), Seed(owner, PlantType.CORN))
        )

        val content = IntroductionAction(gift, region)
        val action = createActionRecord(currentTimestamp, owner, content)

        execContract(action, world)

        assertNotNull(world.players[owner])
        assertEquals(owner, world.players[owner]!!.address)
        assertTrue(world.players[owner]!!.items.contains(gift.realmClaimPaper))
        assertTrue(world.players[owner]!!.items.containsAll(gift.hoes))
        assertTrue(world.players[owner]!!.items.containsAll(gift.seeds))
    }
}