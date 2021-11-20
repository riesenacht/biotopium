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

import ch.riesenacht.biotopium.core.action.model.ClaimRealmAction
import ch.riesenacht.biotopium.core.world.Player
import ch.riesenacht.biotopium.core.world.model.item.RealmClaimPaper
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.realmIndex
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Test class for [claimRealmContract].
 *
 * @author Manuel Riesen
 */
class ClaimRealmContractTest : ContractTest() {

    @Test
    fun testClaimRealmActionExec_positive() {
        val owner = defaultOwner

        val world = createMutableTestWorldWithPlayer(owner)

        world.players[owner] = Player("name", owner)

        val realm = Realm(owner, 0.realmIndex, 0.realmIndex)
        val realmClaimPaper = RealmClaimPaper(owner)

        world.players[owner]?.addItem(realmClaimPaper)

        val content = ClaimRealmAction(realm, realmClaimPaper)
        val action = createActionFrame(currentTimestamp, owner, content)

        execContract(action, world)

        assertContains(world.realms, realm.ix to realm.iy)
        assertEquals(realm.owner, world.realms[realm.ix to realm.iy]?.owner)
        assertFalse(world.players[owner]!!.items.contains(realmClaimPaper))
    }
}