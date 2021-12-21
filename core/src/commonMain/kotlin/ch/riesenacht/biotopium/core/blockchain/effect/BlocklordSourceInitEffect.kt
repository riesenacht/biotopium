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

package ch.riesenacht.biotopium.core.blockchain.effect

import ch.riesenacht.biotopium.core.blockchain.BlocklordSource
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.effect.EffectProfile
import ch.riesenacht.biotopium.core.effect.ModuleEffect
import ch.riesenacht.biotopium.core.effect.SideEffect

/**
 * The module effect for initializing the [BlocklordSource] with the list of trusted [blocklords].
 * The blocklord source is informed whether the list is [trusted] or not.
 * A [profile] can be set.
 *
 * @author Manuel Riesen
 */
@SideEffect(receiver = BlocklordSource::class)
abstract class BlocklordSourceInitEffect(
    blocklords: List<Address>,
    trusted: Boolean,
    profile: EffectProfile = EffectProfile.MAIN
) : ModuleEffect({
    // initialize blocklord source
    BlocklordSource.init(blocklords, trusted)
}, profile = profile)