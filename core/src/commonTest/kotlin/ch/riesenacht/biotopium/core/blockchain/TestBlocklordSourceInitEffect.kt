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

package ch.riesenacht.biotopium.core.blockchain

import ch.riesenacht.biotopium.core.blockchain.effect.BlocklordSourceInitEffect
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.effect.EffectProfile

/**
 * The module effect for initializing the blocklord source
 * with the test blocklord addresses.
 * Be aware: applying this effect initializes the blocklord source with *insecure* addresses!
 *
 * @author Manuel Riesen
 */
object TestBlocklordSourceInitEffect : BlocklordSourceInitEffect(
    listOf(
        Address.fromBase64("5bM4woCyhiqks8vZ+ZdyX4B7HvjvdEawljnJ96b7oOw=")
    ),
    trusted = false,
    profile = EffectProfile.TEST
)