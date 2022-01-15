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
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.action.model.record.toActionRecord
import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.crypto.model.KeyPair
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.crypto.model.PublicKey
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.time.DateUtils
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.MutableWorld
import ch.riesenacht.biotopium.core.world.Player
import ch.riesenacht.biotopium.core.world.World
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.Owner
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.reactive.collection.MutableObservableMap
import ch.riesenacht.biotopium.reactive.collection.mutableObservableMapOf
import kotlin.test.BeforeTest

abstract class ContractTest {

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    protected class TestMutableWorld : MutableWorld {

        override val tiles: MutableObservableMap<Pair<Coordinate, Coordinate>, Tile> = mutableObservableMapOf()


        override val realms: MutableObservableMap<Pair<RealmIndex, RealmIndex>, Realm> = mutableObservableMapOf()


        override val players: MutableObservableMap<Address, Player> = mutableObservableMapOf()
    }

    protected class TestWorld: World {

        override val tiles = mutableObservableMapOf<Pair<Coordinate, Coordinate>, Tile>()

        override val realms = mutableObservableMapOf<Pair<RealmIndex, RealmIndex>, Realm>()

        override val players = mutableObservableMapOf<Address, Player>()

    }

    val authorKeyPair = KeyPair(
        privateKey = PrivateKey("iv1qW7KDjJyBkKiLUaH9cr0cgVhhWDS7f5sBd8Lyt9UYIsd9QI7eQH/CcISqjNLeZjgpekdcPVnJlzJkySQ4dw=="),
        publicKey = PublicKey("GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=")
    )

    protected val defaultOwner: Owner
    get() = Owner(authorKeyPair.publicKey)


    protected val currentTimestamp: Timestamp
    get() = DateUtils.currentTimestamp()

    protected inline fun <reified T : Action> createActionRecord(timestamp: Timestamp, author: Address, action: T, privateKey: PrivateKey = authorKeyPair.privateKey): ActionRecord<T> {
        val raw = RawBlockRecord(timestamp, author, action)
        val hashed = raw.toHashedRecord(BlockUtils.hash(raw))
        return hashed.toActionRecord(BlockUtils.sign(hashed, privateKey))
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

    protected fun <T : Action> execContract(action: ActionRecord<T>, world: MutableWorld) {
        ActionContractManager.executeContract(action, world)
    }
}