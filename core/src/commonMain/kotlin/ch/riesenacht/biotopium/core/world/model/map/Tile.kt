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

package ch.riesenacht.biotopium.core.world.model.map

import ch.riesenacht.biotopium.core.world.model.Coordinate

/**
 * Represents a tile on the map.
 * A tile is identifiable by the coordinates [x] and [y].
 * Every tile has a [type].
 *
 * @author Manuel Riesen
 */
sealed interface Tile {
    val x: Coordinate
    val y: Coordinate
    val type: TileType
}