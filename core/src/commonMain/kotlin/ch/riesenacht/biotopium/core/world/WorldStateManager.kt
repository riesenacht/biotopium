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

package ch.riesenacht.biotopium.core.world

import ch.riesenacht.biotopium.bus.IncomingActionBus
import ch.riesenacht.biotopium.core.action.contract.ActionContractManager
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.reactive.collection.MutableObservableMap
import ch.riesenacht.biotopium.reactive.collection.ObservableMap
import ch.riesenacht.biotopium.reactive.collection.mutableObservableMapOf

/**
 * State manager of the world.
 *
 * @author Manuel Riesen
 */
object WorldStateManager: World {

    /**
     * Mutable tile map.
     */
    private val mutableTiles: MutableObservableMap<Pair<Coordinate, Coordinate>, Tile> = mutableObservableMapOf()

    /**
     * Tile map.
     * Each tile is mapped by its coordinates.
     */
    override val tiles: ObservableMap<Pair<Coordinate, Coordinate>, Tile>
    get() = mutableTiles

    /**
     * Mutable realm map.
     */
    private val mutableRealms: MutableObservableMap<Pair<RealmIndex, RealmIndex>, Realm> = mutableObservableMapOf()

    /**
     * Realm map.
     * Each realm is mapped by its realm indices.
     */
    override val realms: ObservableMap<Pair<RealmIndex, RealmIndex>, Realm>
    get() = mutableRealms

    /**
     * Mutable player map.
     */
    private val mutablePlayers: MutableObservableMap<Address, Player> = mutableObservableMapOf()

    /**
     * Player map.
     * Each player is mapped by its wallet address.
     */
    override val players: ObservableMap<Address, Player>
    get() = mutablePlayers

    /**
     * Represents the world in its mutable state.
     */
    private val mutableWorld = object : MutableWorld {
        override val tiles: MutableObservableMap<Pair<Coordinate, Coordinate>, Tile>
            get() = mutableTiles
        override val realms: MutableObservableMap<Pair<RealmIndex, RealmIndex>, Realm>
            get() = mutableRealms
        override val players: MutableObservableMap<Address, Player>
            get() = mutablePlayers
    }

    init {
        IncomingActionBus.subscribe {
            //execute incoming contracts
            ActionContractManager.executeContract(it, mutableWorld)
        }
    }
}