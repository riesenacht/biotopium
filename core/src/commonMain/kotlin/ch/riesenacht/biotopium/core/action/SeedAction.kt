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

package ch.riesenacht.biotopium.core.action

import ch.riesenacht.biotopium.core.model.item.Seed
import ch.riesenacht.biotopium.core.model.map.Plot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The action of seeding a plant.
 * This action plants a seed on a tile.
 *
 * @property produce the affected tile
 * @property consume the consumed seed
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("SeedAction")
data class SeedAction(
    override val produce: Plot,
    override val consume: Seed
) : Action, Producible<Plot>, Consumable<Seed> {
    override val type = ActionType.SEED
}