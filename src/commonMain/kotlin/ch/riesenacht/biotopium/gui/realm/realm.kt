/*
 * Copyright (c) 2022 The biotopium Authors.
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

package ch.riesenacht.biotopium.gui.realm

import ch.riesenacht.biotopium.core.world.WorldStateManager
import ch.riesenacht.biotopium.core.world.model.Coordinate
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.coordUnit
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.core.world.model.map.realmSize
import ch.riesenacht.biotopium.gui.darkSecondaryColor
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set


/**
 * Creates a [realm] display at position [x];[y] with the given [size].
 *
 * @param onTileClick callback for tile click events
 * @param ownRealm whether the realm belongs to the current player
 */
fun Container.realmDisplay(
    realm: Realm,
    x: Double,
    y: Double,
    size: Double,
    onTileClick: (Tile) -> Unit,
    ownRealm: Boolean = false
) = RealmDisplay(realm, size, onTileClick, ownRealm)
    .position(x, y)
    .addTo(this)

/**
 * The display of a [Realm].
 *
 * @param realm realm to display
 * @param size realm size
 * @param onTileClick callback for tile click events
 * @param ownRealm whether the realm belongs to the current player
 *
 * @author Manuel Riesen
 * @author Sandro Rüfenacht
 */
class RealmDisplay(realm: Realm, size: Double, onTileClick: (Tile) -> Unit, ownRealm: Boolean = false) : Container() {

    private val tiles: MutableMap<Pair<Coordinate, Coordinate>, TileDisplay> = mutableMapOf()

    companion object {
        val numTiles = realmSize.toInt()
        const val tileGap = 1
    }

    init {
        val world = WorldStateManager

        val tileSize = size / numTiles - tileGap

        solidRect(size, size, darkSecondaryColor)
        for (localX in 0 until numTiles) {
            for (localY in 0 until numTiles) {
                val posX = tileGap + (tileGap + tileSize) * localX
                val posY = tileGap + (tileGap + tileSize) * localY
                val x = realm.ix.rootCoordinate + localX.coordUnit
                val y = realm.iy.rootCoordinate + localY.coordUnit
                val tile = world.tiles[x, y]
                val tileDisplay = tileDisplay(posX, posY, tileSize, tile!!, onTileClick, ownRealm)
                tiles[localX.coord to localY.coord] = tileDisplay
            }
        }

        world.tiles.subscribe {
            val resource = it.resource ?: return@subscribe
            val (x, y) = resource
            tiles[x to y]?.remove()
            val localX = x.coordinate.toInt().mod(numTiles)
            val localY = y.coordinate.toInt().mod(numTiles)
            val posX = tileGap + (tileGap + tileSize) * localX
            val posY = tileGap + (tileGap + tileSize) * localY
            tiles[localX.coord to localY.coord] = tileDisplay(posX, posY, tileSize, world.tiles[x, y]!!, onTileClick, ownRealm)
        }
    }

}