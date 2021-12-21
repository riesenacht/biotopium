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

package ch.riesenacht.biotopium.core.effect

/**
 * Represents an effect of a module.
 * An effect is a component which applies an effect to other components.
 * Effects and their effect receivers are usually objects (singletons).
 *
 * Effects should be applied using the [applyEffect] function.
 *
 * @property applier the function invoked on init
 * @param nested the nested module effects
 *
 * @author Manuel Riesen
 */
abstract class ModuleEffect(
        private val applier: (() -> Unit)? = null,
        private vararg val nested: ModuleEffect,
        private val profile: EffectProfile = EffectProfile.MAIN
) {

    /**
     * Applies the current module effect.
     */
    fun apply(profile: EffectProfile = EffectProfile.MAIN) {

        // exclude self and nested effects
        if(profile != this.profile && !profile.also.contains(this.profile)) return

        applier?.invoke()
        nested.forEach { it.apply(profile) }
    }

}