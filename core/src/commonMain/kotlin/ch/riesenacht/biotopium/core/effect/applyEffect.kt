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
 * Applies an [effect].
 * In order to trigger the init block of an object (singleton), it has to be used.
 * While mentioning the object in the code is sufficient for triggering initialization,
 * it is not very expressive.
 * It is strongly recommended using this function for applying effects.
 */
fun applyEffect(effect: ModuleEffect, profile: EffectProfile = EffectProfile.MAIN) {
    effect.apply(profile)
}