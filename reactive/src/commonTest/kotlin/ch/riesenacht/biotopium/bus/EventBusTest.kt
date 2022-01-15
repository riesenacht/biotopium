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

package ch.riesenacht.biotopium.bus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test class for [EventBus].
 *
 * @author Manuel Riesen
 */
class EventBusTest {

    private class TestEventBus : EventBus<String>()

    @Test
    fun testOnNext_positive_singleValue() {
        val value = "test"
        val eventBus = TestEventBus()
        var consumed = false
        eventBus.subscribe {
            assertFalse(consumed)
            assertEquals(value, it)
            consumed = true
        }
        eventBus.onNext(value)
        assertTrue(consumed)
    }

    @Test
    fun testAllOnNext_positive_singleValue() {
        val value = "test"
        val eventBus = TestEventBus()
        var consumed = false
        eventBus.subscribe {
            assertFalse(consumed)
            assertEquals(value, it)
            consumed = true
        }
        eventBus.allOnNext(listOf(value))
        assertTrue(consumed)
    }

    @Test
    fun testAllOnNext_positive_multipleValues() {
        val values = listOf("a", "b", "c")
        val eventBus = TestEventBus()
        val consumed = mutableListOf(false, false, false)
        var index = 0
        eventBus.subscribe {
            assertFalse(consumed[index])
            assertEquals(values[index], it)
            consumed[index++] = true
        }
        eventBus.allOnNext(values)
        consumed.forEach { assertTrue(it) }
    }

}