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

package ch.riesenacht.biotopium.gui.tutorial

import ch.riesenacht.biotopium.gui.neutralColor
import ch.riesenacht.biotopium.gui.toolbar.Slot
import com.soywiz.korge.view.*
import com.soywiz.korim.color.RGBA
import com.soywiz.korma.geom.Angle
import kotlin.math.sqrt

/**
 * The currently highlighted slot.
 */
private var currentSlotHighlight: Container? = null

/**
 * Highlights a toolbar's [slot].
 * @param color the arrow's color
 * @param backgroundColor the background color
 */
fun Container.highlightSlot(color: RGBA, slot: Slot, backgroundColor: RGBA = neutralColor) {
    clearHighlight()
    currentSlotHighlight = container {
        val arrow = this
        val arrowSize = slot.width * 0.3
        val arrowWidth = sqrt(2 * arrowSize * arrowSize)
        val arrowPadding = (arrowSize - arrowWidth) / 2
        val arrowEnd = solidRect(arrowSize, arrowSize, color) {
            alignBottomToTopOf(slot, 25.0)
            alignLeftToLeftOf(arrow, arrowPadding)
            rotation(Angle.Companion.fromDegrees(45))
        }
        val overlapHider = solidRect(arrowWidth, arrowWidth / 2, backgroundColor) {
            alignTopToTopOf(arrowEnd)
            alignLeftToLeftOf(arrow)
        }
        solidRect(arrowSize / 2, arrowSize * 2, color) {
            centerXOn(overlapHider)
            alignBottomToBottomOf(overlapHider)
        }
        centerXOn(slot)
    }
}

/**
 * Clears the highlight of a slot.
 */
fun clearHighlight() {
    currentSlotHighlight?.removeAllComponents()
    currentSlotHighlight?.removeFromParent()
}