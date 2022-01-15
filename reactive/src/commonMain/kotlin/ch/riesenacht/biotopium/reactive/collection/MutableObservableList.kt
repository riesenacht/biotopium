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
    private val subject: BasicSubjectImpl<EmptyChange> = BasicSubjectImpl(EmptyChange)

    override fun add(element: E): Boolean {
        subject.onNext(EmptyChange)
        return list.add(element)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        subject.onNext(EmptyChange)
        return list.addAll(elements)
    }

    override fun add(index: Int, element: E) {
        subject.onNext(EmptyChange)
        return list.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        subject.onNext(EmptyChange)
        return list.addAll(index, elements)
    }

    override fun remove(element: E): Boolean {
        subject.onNext(EmptyChange)
        return list.remove(element)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        subject.onNext(EmptyChange)
        return list.removeAll(elements)
    }

    override fun clear() {
        subject.onNext(EmptyChange)
        list.clear()
    }

    override fun removeAt(index: Int): E {
        subject.onNext(EmptyChange)
        return list.removeAt(index)
    }

    override fun set(index: Int, element: E): E {
        subject.onNext(EmptyChange)
        return list.set(index, element)
    }

    override fun subscribe(onNext: ((EmptyChange) -> Unit)) = subject.subscribe(onNext = onNext)
}

/**
 * Creates a mutable observable list.
 * The list is populated with the given [items].
 */
fun <T> mutableObservableListOf(vararg items: T): MutableObservableList<T> {
    return MutableObservableList(mutableListOf(*items))
}