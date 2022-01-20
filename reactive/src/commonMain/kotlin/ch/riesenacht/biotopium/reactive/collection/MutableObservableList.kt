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
import ch.riesenacht.biotopium.reactive.Mutation
import ch.riesenacht.biotopium.reactive.Operation
import com.badoo.reaktive.disposable.Disposable

/**
 * Represents an observable mutable [list] of type [E].
 *
 * @author Manuel Riesen
 */
class MutableObservableList<E>(
    private val list: MutableList<E>,
) : MutableList<E> by list, ObservableList<E> {

    /**
     * The underlying basic subject for change detection.
     */
    private lateinit var subject: BasicSubjectImpl<Mutation<E>>

    /**
     * Whether the list is reactive or not.
     * The list becomes reactive if at least one subscriber exists.
     */
    private var reactive: Boolean = false
    set(value) {
        if(field) return
        subject = BasicSubjectImpl(Mutation(Operation.NONE, null))
        field = value
    }

    /**
     * Notifies the subject about an occurred [operation][op] on an [element].
     */
    private fun mutation(op: Operation, element: E) {
        if(reactive) subject.onNext(Mutation(op, element))
    }

    /**
     * Notifies the subject about an occurred [operation][op] on [elements].
     */
    private fun mutation(op: Operation, elements: Collection<E>) {
        if(reactive) subject.allOnNext(elements.map { Mutation(op, it) })
    }

    override fun add(element: E): Boolean {
        mutation(Operation.ADD, element)
        return list.add(element)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        mutation(Operation.ADD, elements)
        return list.addAll(elements)
    }

    override fun add(index: Int, element: E) {
        mutation(Operation.ADD, element)
        return list.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        mutation(Operation.ADD, elements)
        return list.addAll(index, elements)
    }

    override fun remove(element: E): Boolean {
        mutation(Operation.REMOVE, element)
        return list.remove(element)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        mutation(Operation.REMOVE, elements)
        return list.removeAll(elements)
    }

    override fun clear() {
        mutation(Operation.REMOVE, list)
        list.clear()
    }

    override fun removeAt(index: Int): E {
        mutation(Operation.REMOVE, list[index])
        return list.removeAt(index)
    }

    override fun set(index: Int, element: E): E {
        if(list[index] != null) {
            mutation(Operation.REMOVE, list[index])
        }
        mutation(Operation.ADD, element)
        return list.set(index, element)
    }

    override fun subscribe(onNext: ((Mutation<E>) -> Unit)): Disposable {
        reactive = true
        return subject.subscribe(onNext = onNext)
    }
}

/**
 * Creates a mutable observable list.
 * The list is populated with the given [items].
 */
fun <T> mutableObservableListOf(vararg items: T): MutableObservableList<T> {
    return MutableObservableList(mutableListOf(*items))
}