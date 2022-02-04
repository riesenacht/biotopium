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

import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.publish.PublishSubject

/**
 * Represents an event bus.
 * An event bus is used for reactive communication between components.
 * Items can be published to and read from the event bus.
 * A [PublishSubject] is used as underlying ReactiveX type.
 *
 * @author Manuel Riesen
 */
abstract class EventBus<T> {

    /**
     * The underlying subject.
     */
    private val subject: Subject<T> = PublishSubject()

    /**
     * Sends an [item] to the bus.
     */
    open fun onNext(item: T) = subject.onNext(item)

    /**
     * Sends multiple [items] to the bus.
     * The items are processed in the given order.
     */
    open fun allOnNext(items: List<T>) = items.forEach { onNext(it) }

    /**
     * Subscribes to the event bus.
     */
    open fun subscribe(onNext: ((T) -> Unit)) = subject.subscribe(onNext = onNext)

}