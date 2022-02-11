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

import ch.riesenacht.biotopium.reactive.BasicSubject
import ch.riesenacht.biotopium.reactive.BasicSubjectImpl
import ch.riesenacht.biotopium.reactive.Mutation
import ch.riesenacht.biotopium.reactive.Operation
import com.badoo.reaktive.disposable.Disposable

/**
 * Implementation of the [MutableObservableMap].
 *
 * @see MutableObservableMap
 *
 * @author Manuel Riesen
 */
open class MutableObservableMapImpl<K, V>(
    private val map: MutableMap<K, V>
) : MutableMap<K, V> by map, MutableObservableMap<K, V> {

    /**
     * The underlying basic subject for change detection.
     */
    private lateinit var subject: BasicSubject<Mutation<K>>

    /**
     * Whether the map is reactive or not.
     * The list becomes reactive if at least one subscriber exists.
     */
    private var reactive: Boolean = false
    set(value) {
        if(field) return
        subject = BasicSubjectImpl(Mutation(Operation.NONE, null))
        field = value
    }

    /**
     * Notifies the subject about an occurred [operation][op] at a [key].
     */
    private fun mutation(op: Operation, key: K) {
        if(reactive) subject.onNext(Mutation(op, key))
    }

    /**
     * Notifies the subject about an occurred [operation][op] at [keys].
     */
    private fun mutation(op: Operation, keys: Collection<K>) {
        if(reactive) subject.allOnNext(keys.map { Mutation(op, it) })
    }

    override fun clear() {
        mutation(Operation.REMOVE, map.keys)
        map.clear()
    }

    override fun put(key: K, value: V): V? {
        mutation(Operation.ADD, key)
        return map.put(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        mutation(Operation.ADD, from.keys)
        return map.putAll(from)
    }

    override fun remove(key: K): V? {
        mutation(Operation.REMOVE, key)
        return map.remove(key)
    }

    override fun subscribe(onNext: (Mutation<K>) -> Unit): Disposable {
        reactive = true
        return subject.subscribe(onNext)
    }
}

/**
 * Creates a mutable observable map.
 * The map is populated with the given [pairs].
 */
fun <K, V> mutableObservableMapOf(vararg pairs: Pair<K, V>): MutableObservableMap<K, V> {
    return MutableObservableMapImpl(mutableMapOf(*pairs))
}
