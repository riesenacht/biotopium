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
 * Represents a profile (or context) of a module effect.
 *
 * @property also all profiles which are inherited or applied as well
 *
 * @author Manuel Riesen
 */
enum class EffectProfile(
    vararg val also: EffectProfile = emptyArray()
) {
    /**
     * The main profile.
     * Can be seen as the default context.
     */
    MAIN,

    /**
     * The development profile.
     * This profile is used for development instances only.
     */
    DEV(MAIN),

    /**
     * The test profile.
     * This profile is used for testing only (e.g. unit tests).
     */
    TEST(MAIN)
}