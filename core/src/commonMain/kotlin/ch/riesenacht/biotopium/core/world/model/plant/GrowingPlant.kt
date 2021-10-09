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

package ch.riesenacht.biotopium.core.world.model.plant

import ch.riesenacht.biotopium.core.time.model.Timespan
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.model.Element
import ch.riesenacht.biotopium.core.world.model.Owner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents a plant growing on a plot.
 *
 * @property owner the plant's owner
 * @property type type of plant
 * @property growth the plant's growth
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("GrowingPlant")
data class GrowingPlant(
    override val owner: Owner,
    val type: PlantType,
    val growth: PlantGrowth
) : Element {

    /**
     * The last time of growth.
     */
    @Transient
    var lastGrowth: Timestamp? = null
}

/**
 * The growth rate of a plant.
 */
val growthRate: Timespan = Timespan(seconds = 600)