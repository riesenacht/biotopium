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

package ch.riesenacht.biotopium.core.model.item

import ch.riesenacht.biotopium.core.model.Owner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a hoe tool.
 * A hoe can be leveraged for creating a plot out of another type of tile.
 *
 * @property owner the owner of the hoe tool
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("Hoe")
data class Hoe(
    override val owner: Owner
) : Item {
    override val type = ItemType.HOE
}