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

import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.BlockOrigin
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.time.DateUtils
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.MutableWorld
import ch.riesenacht.biotopium.core.world.Player
import ch.riesenacht.biotopium.core.world.World
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import kotlin.test.BeforeTest

abstract class ContractTest {

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    protected class TestMutableWorld : MutableWorld {

        override val tiles: MutableMap<Pair<Coordinate, Coordinate>, Tile> = mutableMapOf()


        override val realms: MutableMap<Pair<RealmIndex, RealmIndex>, Realm> = mutableMapOf()


        override val players: MutableMap<Address, Player> = mutableMapOf()
    }

    protected class TestWorld: World {

        override val tiles = mutableMapOf<Pair<Coordinate, Coordinate>, Tile>()

        override val realms = mutableMapOf<Pair<RealmIndex, RealmIndex>, Realm>()

        override val players = mutableMapOf<Address, Player>()

    }

    protected data class TestBlockOrigin(override val timestamp: Timestamp, override val author: Address) : BlockOrigin

    protected fun createDefaultOwner() = Address.fromBase64("me")

    protected fun createOtherOwner(base64: String = "none") = Address.fromBase64(base64)

    protected fun createDefaultBlockOrigin(address: Address, timestamp: Timestamp = DateUtils.currentTimestamp()) = TestBlockOrigin(timestamp, address)

    protected fun createTestWorldWithPlayer(address: Address): TestWorld {
        val world = TestWorld()
        val player = Player("name", address)
        world.players[address] = player
        return world
    }

    protected fun createMutableTestWorldWithPlayer(address: Address): MutableWorld {
        val world = TestMutableWorld()
        val player = Player("name", address)
        world.players[address] = player
        return world
    }

    protected fun createMutableWorld(): MutableWorld {
        return TestMutableWorld()
    }

    protected fun <T : Action> execContract(action: T, block: BlockOrigin, world: MutableWorld) {
        ActionContractManager.executeContract(action, block, world)
    }
}