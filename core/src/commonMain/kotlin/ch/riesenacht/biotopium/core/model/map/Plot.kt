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

package ch.riesenacht.biotopium.core.model.map

import ch.riesenacht.biotopium.core.model.Coordinate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a plot.
 * A plot is a tile on which plants can be grown.
 *
 * @property x x-coordinate
 * @property y y-coordinate
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("Plot")
data class Plot(
    override val x: Coordinate,
    override val y: Coordinate
) : Tile {
    override val type = TileType.PLOT
}