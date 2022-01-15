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

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe

/**
 * Implementation of [BasicObservable].
 *
 * @property observable the underlying observable
 *
 * @author Manuel Riesen
 */
abstract class BasicObservableImpl<T>(
    private val observable: Observable<T>
) : BasicObservable<T> {

    /**
     * Subscribes to the observable.
     * [onNext] is called on each state change.
     */
    override fun subscribe(onNext: ((T) -> Unit)) = observable.subscribe(onNext = onNext)

}