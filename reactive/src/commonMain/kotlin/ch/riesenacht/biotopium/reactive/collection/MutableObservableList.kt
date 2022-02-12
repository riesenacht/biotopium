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
        val res = list.add(element)
        mutation(Operation.ADD, element)
        return res
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val res = list.addAll(elements)
        mutation(Operation.ADD, elements)
        return res
    }

    override fun add(index: Int, element: E) {
        val res = list.add(index, element)
        mutation(Operation.ADD, element)
        return res
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val res = list.addAll(index, elements)
        mutation(Operation.ADD, elements)
        return res
    }

    override fun remove(element: E): Boolean {
        val res = list.remove(element)
        mutation(Operation.REMOVE, element)
        return res
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        val res = list.removeAll(elements)
        mutation(Operation.REMOVE, elements)
        return res
    }

    override fun clear() {
        val removedList = list.toList()
        list.clear()
        mutation(Operation.REMOVE, removedList)
    }

    override fun removeAt(index: Int): E {
        val item = list[index]
        val res = list.removeAt(index)
        mutation(Operation.REMOVE, item)
        return res
    }

    override fun set(index: Int, element: E): E {
        val item = list[index]
        val res = list.set(index, element)
        if(item != null) {
            mutation(Operation.REMOVE, item)
        }
        mutation(Operation.ADD, element)
        return res
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