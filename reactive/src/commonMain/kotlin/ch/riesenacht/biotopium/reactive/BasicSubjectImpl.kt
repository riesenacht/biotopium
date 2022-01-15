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

package ch.riesenacht.biotopium.reactive

import com.badoo.reaktive.subject.behavior.BehaviorSubject

/**
 * Implementation of [BasicSubject].
 *
 * @param initialValue initial value for the subject if no subject is provided directly
 * @property subject the underlying subject
 *
 * @author Manuel Riesen
 */
class BasicSubjectImpl<T>(
    initialValue: T,
    private val subject: BehaviorSubject<T> = BehaviorSubject(initialValue)
) : BasicObservableImpl<T>(subject), BasicSubject<T> {

    /**
     * Sets the next [item] of the subject.
     */
    override fun onNext(item: T) = subject.onNext(item)

    /**
     * Sets multiple next [items] of the subject.
     */
    override fun allOnNext(items: List<T>) = items.forEach { onNext(it) }
}