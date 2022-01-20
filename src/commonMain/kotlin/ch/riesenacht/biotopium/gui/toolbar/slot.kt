/*
 * Copyright (c) 2021 The biotopium Authors.
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

package ch.riesenacht.biotopium.gui.toolbar

import ch.riesenacht.biotopium.gui.accentColor
import ch.riesenacht.biotopium.gui.primaryColor
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*

/**
 * Creates a new slot and adds it to the current container.
 *
 * @param i slot index
 * @param slotWidth slot width
 * @param slotHeight slot height
 */
fun Container.slot(i: Int, slotWidth: Double, slotHeight: Double): Slot {
    return Slot(i, slotWidth, slotHeight).addTo(this)
}

/**
 * Represents a slot in the toolbar.
 *
 * @param i slot index
 * @param slotWidth slot width
 * @param slotHeight slot height
 */
class Slot(
    i: Int,
    slotWidth: Double,
    slotHeight: Double
): Container() {

    private val container: Container

    private var itemDisplay: ItemDisplay? = null

    private val _selected: BehaviorSubject<Boolean> = BehaviorSubject(false)

    /**
     * Whether the slot is selected.
     */
    val selected: Observable<Boolean>
    get() = _selected

    var stack: ItemStack? = null
    set(value) {
        field = value
        value?.let {
            if(itemDisplay != null) {
                container.removeChild(itemDisplay)
            }
            itemDisplay = ItemDisplay(width, height, value)
            itemDisplay?.let {
                container.addChild(it)
            }
        }
    }

    init {

        // the slot's border
        val border = fixedSizeContainer(slotWidth + 2 * borderSize, slotHeight + 2 * borderSize) {
            val border = solidRect(width, height, primaryColor) {
                visible = false
            }

            selected.subscribe { border.visible(it) }

        }.position(i * slotWidth + (i + 1) * slotMargin - borderSize, slotMargin - borderSize)

        // the actual slot container
        container = fixedSizeContainer(slotWidth, slotHeight) {
            solidRect(width, height, accentColor)

            onClick {
                select()
            }


        }.position(i * slotWidth + (i + 1) * slotMargin, slotMargin)
    }

    /**
     * Selects the slot.
     */
    fun select() {
        this._selected.onNext(true)
    }

    /**
     * Deselects the slot.
     */
    fun deselect() {
        this._selected.onNext(false)
    }

    companion object {

        /**
         * The slot margin.
         */
        const val slotMargin = 4.0

        /**
         * The size of the border.
         */
        const val borderSize = 3.0
    }

}