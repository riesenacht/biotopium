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
import kotlin.test.assertTrue

/**
 * Test class for [Coordinate].
 */
class CoordinateTest {

    @Test
    fun testCreateZeroCoordinate_usingConstructor_positive() {
        val x = Coordinate(0u)
        assertEquals(x.coordinate, 0u)
    }

    @Test
    fun testCreateZeroCoordinate_usingIntExtension_positive() {
        val x = 0.coord
        assertEquals(x.coordinate, 0u)
    }

    @Test
    fun testCreatePositiveCoordinate_usingIntExtension_positive() {
        val x = 1.coord
        assertEquals(x.coordinate, 1u)
    }

    @Test
    fun testCreateNegativeCoordinate_usingIntExtension_negative() {
        var exceptionThrown = false
        try {
            (-1).coord
        } catch(e: NegativeCoordinateException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)
    }

    @Test
    fun testCreateZeroCoordinate_usingUIntExtension_positive() {
        val x = 0u.coord
        assertEquals(x.coordinate, 0u)
    }

    @Test
    fun testCreatePositiveCoordinate_usingUIntExtension_positive() {
        val x = 1u.coord
        assertEquals(x.coordinate, 1u)
    }

    @Test
    fun testAddZeroCoordinateUnit_toPositiveCoordinate_positive() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(0)
        val y = x + d
        assertEquals(x, y)
    }

    @Test
    fun testAddPositiveCoordinateUnit_toPositiveCoordinate_positive() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(1)
        val y = x + d
        assertEquals(Coordinate(2u), y)
    }

    @Test
    fun testAddNegativeCoordinateUnit_toPositiveCoordinate_resultsInZeroCoordinate_positive() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(-1)
        val y = x + d
        assertEquals(Coordinate(0u), y)
    }

    @Test
    fun testAddNegativeCoordinateUnit_toPositiveCoordinate_resultsInNegativeCoordinate_negative() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(-2)
        var exceptionThrown = false
        try {
            x + d
        } catch(e: NegativeCoordinateException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)
    }

    @Test
    fun testSubtractPositiveCoordinateUnit_fromPositiveCoordinate_resultsInPositiveCoordinate_positive() {
        val x = Coordinate(3u)
        val d = CoordinateUnit(2)
        val y = x - d
        assertEquals(Coordinate(1u), y)
    }

    @Test
    fun testSubtractPositiveCoordinateUnit_fromPositiveCoordinate_resultsInZeroCoordinate_positive() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(1)
        val y = x - d
        assertEquals(Coordinate(0u), y)
    }

    @Test
    fun testSubtractPositiveCoordinateUnit_fromPositiveCoordinate_resultsInNegativeCoordinate_negative() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(2)
        var exceptionThrown = false
        try {
            x - d
        } catch(e: NegativeCoordinateException) {
            exceptionThrown = true
        }
        assertTrue(exceptionThrown)
    }

    @Test
    fun testSubtractNegativeCoordinateUnit_fromZeroCoordinate_resultsInPositiveCoordinate_positive() {
        val x = Coordinate(0u)
        val d = CoordinateUnit(-1)
        val y = x - d
        assertEquals(Coordinate(1u), y)
    }

    @Test
    fun testSubtractNegativeCoordinateUnit_fromPositiveCoordinate_resultsInPositiveCoordinate_positive() {
        val x = Coordinate(1u)
        val d = CoordinateUnit(-1)
        val y = x - d
        assertEquals(Coordinate(2u), y)
    }
}