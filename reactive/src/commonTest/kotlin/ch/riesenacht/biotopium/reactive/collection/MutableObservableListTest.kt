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

import kotlin.test.*

/**
 * Test class for [MutableObservableList].
 *
 * @author Manuel Riesen
 */
class MutableObservableListTest {

    @Test
    fun testObserveListSubscriptionEvent() {
        val list = mutableObservableListOf<String>()
        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }
        recorder.assertEvents(1, true)
    }

    @Test
    fun testObserveListAdd() {
        val list = mutableObservableListOf<String>()

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val element = "A"

        list.add(element)

        recorder.assertEvents(1)
        assertContains(list, element)
    }

    @Test
    fun testObserveListAddAll() {
        val list = mutableObservableListOf<String>()

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val elements = listOf("A", "B", "C")

        list.addAll(elements)

        recorder.assertEvents(1)
        elements.forEach {
            assertContains(list, it)
        }
    }

    @Test
    fun testObserveAddIndexed() {
        val list = mutableObservableListOf<String>()

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val element = "A"

        list.add(0, element)

        recorder.assertEvents(1)
        assertContains(list, element)
    }

    @Test
    fun testObserveAddAllIndexed() {
        val list = mutableObservableListOf<String>()

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val elements = listOf("A", "B", "C")

        list.addAll(0, elements)

        recorder.assertEvents(1)
        elements.forEach {
            assertContains(list, it)
        }
    }

    @Test
    fun testObserveRemove() {
        val a = "A"
        val b = "B"
        val c = "C"
        val list = mutableObservableListOf(a, b, c)

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        list.remove(a)

        recorder.assertEvents(1)
        assertFalse(list.contains(a))
    }

    @Test
    fun testObserveRemoveAll() {
        val a = "A"
        val b = "B"
        val c = "C"
        val list = mutableObservableListOf(a, b, c)

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        val elements = listOf(a, b)
        list.removeAll(elements)

        recorder.assertEvents(1)
        elements.forEach {
            assertFalse(list.contains(it))
        }
    }

    @Test
    fun testObserveClear() {
        val list = mutableObservableListOf("A", "B", "C")

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        list.clear()

        recorder.assertEvents(1)
        assertTrue(list.isEmpty())
    }

    @Test
    fun testObserveRemoveAt() {
        val a = "A"
        val b = "B"
        val c = "C"
        val list = mutableObservableListOf(a, b, c)

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        list.removeAt(1)
        recorder.assertEvents(1)
        assertContains(list, a)
        assertFalse(list.contains(b))
        assertContains(list, c)
    }

    @Test
    fun testObserveSet() {
        val a = "A"
        val b = "B"
        val c = "C"
        val d = "D"
        val list = mutableObservableListOf(a, b, c)

        val recorder = EventRecorder()
        list.subscribe {
            recorder.record()
        }

        list[1] = d
        recorder.assertEvents(1)
        assertContains(list, a)
        assertFalse(list.contains(b))
        assertContains(list, c)
        assertContains(list, d)
    }
}