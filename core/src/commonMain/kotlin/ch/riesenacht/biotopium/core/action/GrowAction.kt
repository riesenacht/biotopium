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

import ch.riesenacht.biotopium.core.crypto.model.Signature
import ch.riesenacht.biotopium.core.model.map.Plot
import kotlinx.serialization.Serializable

/**
 * The action of growing a plant.
 * A growth action describes the growth of a plant on a plot.
 *
 * @property produce the affected plot
 * @property sign the signature of the action's initiator
 *
 * @author Manuel Riesen
 */
@Serializable
data class GrowAction(
    override val produce: Plot,
    override val sign: Signature,
) : Action, Producible<Plot> {
    override val type = ActionType.GROW
}