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

package ch.riesenacht.biotopium.gui.toolbar

import ch.riesenacht.biotopium.core.world.model.item.Item
import com.soywiz.korge.view.*
import com.soywiz.korim.text.TextAlignment

/**
 * Represents a stack of items.
 * Consists of an [item] and an [amount].
 */
class ItemStack(
    val item: Item,
    var amount: UInt,
)

/**
 * The display of an item [stack] in a toolbar slot.
 * The [width] and [height] corresponds to the parent's dimensions.
 */
class ItemDisplay(
    width: Double,
    height: Double,
    private val stack: ItemStack
) : FixedSizeContainer(width, height) {

    init {
        val identifier = text(stack.item.identifier) {
            alignment = TextAlignment.CENTER
            centerXOn(root)
            centerYOn(root)
        }
        text("${stack.amount}") {
            alignment = TextAlignment.RIGHT
            alignTopToBottomOf(identifier)
            centerXOn(root)
        }
    }
}