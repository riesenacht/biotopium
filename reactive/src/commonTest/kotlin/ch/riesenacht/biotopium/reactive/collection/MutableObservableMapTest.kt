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

package ch.riesenacht.biotopium.reactive.collection

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Test class for [MutableObservableMap].
 *
 * @author Manuel Riesen
 */
class MutableObservableMapTest {

    @Test
    fun testObserveMapSubscriptionEvent() {
        val list = mutableObservableMapOf<String, Int>()
        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }
        recorder.assertEvents(1, true)
    }

    @Test
    fun testObservePut() {
        val list = mutableObservableMapOf<String, Int>()
        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val key = "A"
        val value = 1

        list[key] = value

        recorder.assertEvents(1)
        assertEquals(list[key], value)
    }


    @Test
    fun testObservePutAll() {
        val list = mutableObservableMapOf<String, Int>()
        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val pairs = arrayOf(
            "A" to 1,
            "B" to 2,
            "C" to 3
        )

        list.putAll(pairs)

        recorder.assertEvents(3)
        pairs.forEach {
            assertEquals(it.second, list[it.first])
        }
    }


    @Test
    fun testObserveRemove() {
        val a = "A" to 1
        val b = "B" to 2
        val c = "C" to 3
        val list = mutableObservableMapOf(
            a,
            b,
            c
        )
        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        list.remove(b.first)

        recorder.assertEvents(1)
        assertNull(list[b.first])
        assertEquals(a.second, list[a.first])
        assertEquals(c.second, list[c.first])
    }


    @Test
    fun testObserveClear() {
        val a = "A" to 1
        val b = "B" to 2
        val c = "C" to 3
        val list = mutableObservableMapOf(
            a,
            b,
            c
        )
        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        list.clear()

        recorder.assertEvents(3)
        arrayOf(a, b, c).forEach {
            assertNull(list[it.first])
        }
    }

}