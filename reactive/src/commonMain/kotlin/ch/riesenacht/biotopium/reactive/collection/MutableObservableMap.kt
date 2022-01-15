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

import ch.riesenacht.biotopium.reactive.BasicSubjectImpl
import ch.riesenacht.biotopium.reactive.EmptyChange

/**
 * Represents an observable mutable [map] consisting of [key][K]-[value][V] pairs.
 *
 * @author Manuel Riesen
 */
class MutableObservableMap<K, V>(
    private val map: MutableMap<K, V>
) : MutableMap<K, V> by map, ObservableMap<K, V> {

    /**
     * The underlying basic subject for change detection.
     */
    private val subject: BasicSubjectImpl<EmptyChange> = BasicSubjectImpl(EmptyChange)

    override fun clear() {
        subject.onNext(EmptyChange)
        map.clear()
    }

    override fun put(key: K, value: V): V? {
        subject.onNext(EmptyChange)
        return map.put(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        subject.onNext(EmptyChange)
        return map.putAll(from)
    }

    override fun remove(key: K): V? {
        subject.onNext(EmptyChange)
        return map.remove(key)
    }

    override fun subscribe(onNext: (EmptyChange) -> Unit) = subject.subscribe(onNext)
}

/**
 * Creates a mutable observable map.
 * The map is populated with the given [pairs].
 */
fun <K, V> mutableObservableMapOf(vararg pairs: Pair<K, V>): MutableObservableMap<K, V> {
    return MutableObservableMap(mutableMapOf(*pairs))
}
