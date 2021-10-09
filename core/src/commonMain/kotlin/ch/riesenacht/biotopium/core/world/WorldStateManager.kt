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

import ch.riesenacht.biotopium.core.action.ActionValidator
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.BlockOrigin
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import ch.riesenacht.biotopium.core.world.model.item.Item
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile

/**
 * State manager of the world.
 *
 * @author Manuel Riesen
 */
object WorldStateManager: World {

    private val validator = ActionValidator

    /**
     * Mutable tile map.
     */
    private val mutableTiles: MutableMap<Pair<Coordinate, Coordinate>, Tile> = mutableMapOf()

    /**
     * Tile map.
     * Each tile is mapped by its coordinates.
     */
    override val tiles: Map<Pair<Coordinate, Coordinate>, Tile>
    get() = mutableTiles

    /**
     * Mutable realm map.
     */
    private val mutableRealms: MutableMap<Pair<RealmIndex, RealmIndex>, Realm> = mutableMapOf()

    /**
     * Realm map.
     * Each realm is mapped by its realm indices.
     */
    override val realms: Map<Pair<RealmIndex, RealmIndex>, Realm>
    get() = mutableRealms

    /**
     * Mutable player map.
     */
    private val mutablePlayers: MutableMap<Address, Player> = mutableMapOf()

    /**
     * Player map.
     * Each player is mapped by its wallet address.
     */
    override val players: Map<Address, Player>
    get() = mutablePlayers

}