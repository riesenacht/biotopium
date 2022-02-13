/*
 * Copyright (c) 2022 The biotopium Authors.
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

package ch.riesenacht.biotopium.core.world.model

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [CoordinateUnit].
 *
 * @author Manuel Riesen
 */
class CoordinateUnitTest {

    @Test
    fun testCreateZeroCoordinateUnit_usingConstructor_positive() {
        val d = CoordinateUnit(0)
        assertEquals(0, d.value)
    }

    @Test
    fun testCreatePositiveCoordinateUnit_usingConstructor_positive() {
        val d = CoordinateUnit(1)
        assertEquals(1, d.value)
    }

    @Test
    fun testCreateNegativeCoordinateUnit_usingConstructor_positive() {
        val d = CoordinateUnit(-1)
        assertEquals(-1, d.value)
    }

    @Test
    fun testCreateZeroCoordinateUnit_usingIntExtension_positive() {
        val d = 0.coordUnit
        assertEquals(0, d.value)
    }

    @Test
    fun testCreatePositiveCoordinateUnit_usingIntExtension_positive() {
        val d = 1.coordUnit
        assertEquals(1, d.value)
    }

    @Test
    fun testCreateNegativeCoordinateUnit_usingIntExtension_positive() {
        val d = (-1).coordUnit
        assertEquals(-1, d.value)
    }

    @Test
    fun testCreateZeroCoordinateUnit_usingUIntExtension_positive() {
        val d = 0u.coordUnit
        assertEquals(0, d.value)
    }

    @Test
    fun testCreatePositiveCoordinateUnit_usingUIntExtension_positive() {
        val d = 1u.coordUnit
        assertEquals(1, d.value)
    }
}