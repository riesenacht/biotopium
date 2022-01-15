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

import kotlin.test.assertEquals

/**
 * Event recorder for testing events.
 *
 * @author Manuel Riesen
 */
class EventRecorder {

    /**
     * The number of occurred events.
     */
    private var numEvents: Int = 0

    /**
     * Records the event.
     * Increments the number of occured events.
     */
    fun record() = numEvents++

    /**
     * Asserts that an [expected] number of events occurred.
     * @param countFirst whether the first event is counted as well.
     */
    fun assertEvents(expected: Int, countFirst: Boolean = false) {
        assertEquals(expected, this.numEvents - (if(countFirst) 0 else 1))
    }
}