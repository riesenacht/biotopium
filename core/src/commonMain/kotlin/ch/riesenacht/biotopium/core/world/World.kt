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

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile

/**
 * Represents the world.
 *
 * @author Manuel Riesen
 */
interface World {

    /**
     * All tiles as map.
     * Each tile is mapped by its coordinates.
     */
    val tiles: Map<Pair<Coordinate, Coordinate>, Tile>

    /**
     * All realms as map.
     * Each realm is mapped by its realm indices.
     */
    val realms: Map<Pair<RealmIndex, RealmIndex>, Realm>

    /**
     * All players as map.
     * Each player is mapped by its wallet address.
     */
    val players: Map<Address, Player>

}