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

package ch.riesenacht.biotopium.core.blockchain.model.location

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a region on the 2-dimensional blockchain plane.
 * A region description consists of two components,
 * an [x][rx] and [y][rx] component.
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("Region")
data class Region(
    val rx: RegionIndex,
    val ry: RegionIndex
) : Locator

const val regionSize = 8u