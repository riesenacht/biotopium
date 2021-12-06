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

package ch.riesenacht.biotopium

import ch.riesenacht.biotopium.blocklord.BlocklordComponentInitEffect
import ch.riesenacht.biotopium.core.effect.ModuleEffect

/**
 * The blocklord module effect.
 * Contains all effects of the blocklord module as nested effects.
 * Applying this effect means applying every effect of the blocklord module.
 *
 * @author Manuel Riesen
 */
object BlocklordModuleEffect : ModuleEffect(nested = arrayOf(BlocklordComponentInitEffect))