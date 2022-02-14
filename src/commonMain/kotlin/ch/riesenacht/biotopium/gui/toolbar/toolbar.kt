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

import ch.riesenacht.biotopium.core.world.model.item.Item
import ch.riesenacht.biotopium.core.world.model.item.ItemType
import ch.riesenacht.biotopium.gui.darkSecondaryColor
import ch.riesenacht.biotopium.gui.toolbarSlotKeys
import ch.riesenacht.biotopium.reactive.BasicSubject
import ch.riesenacht.biotopium.reactive.BasicSubjectImpl
import ch.riesenacht.biotopium.reactive.Operation
import ch.riesenacht.biotopium.reactive.collection.ObservableList
import com.badoo.reaktive.observable.subscribe
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.*

/**
 * The toolbar layout options.
 */
enum class ToolbarLayout {

    /**
     * Full width toolbar.
     */
    FULL_WIDTH,

    /**
     * Flexible toolbar.
     */
    FLEX,

    /**
     * Fixed size toolbar.
     */
    FIXED
}

/**
 * Creates a toolbar with the full width.
 * The toolbar displays the given [items].
 */
fun Container.fullWidthToolbar(items: ObservableList<Item>): Toolbar {
    val root = this.containerRoot
    val toolbar = Toolbar(root, ToolbarLayout.FULL_WIDTH, items = items)
    toolbar.addTo(this)
    return toolbar
}

/**
 * Creates a toolbar width a fixed width based on the [slotWidth].
 * The toolbar displays the given [items].
 */
fun Container.fixedToolbar(slotWidth: Double, items: ObservableList<Item>): Toolbar {
    val root = this.containerRoot
    val toolbar = Toolbar(root, ToolbarLayout.FIXED, slotWidth, items)
    toolbar.addTo(this)
    return toolbar
}

/**
 * Creates a flexible toolbar.
 * If fitting, the [preferredSlotWidth] is used as slot width, otherwise the full width is used.
 * The toolbar displays the given [items].
 */
fun Container.flexToolbar(preferredSlotWidth: Double, items: ObservableList<Item>): Toolbar {
    val root = this.containerRoot
    val toolbar = Toolbar(root, ToolbarLayout.FLEX, preferredSlotWidth, items)
    toolbar.addTo(this)
    return toolbar
}

/**
 * Represents the toolbar.
 * @param root the root container
 * @param layout the layout option
 * @param preferredSlotWidth the preferred slot width
 * @param items the items to display
 */
class Toolbar(root: Container, layout: ToolbarLayout, preferredSlotWidth: Double = 0.0, private val items: ObservableList<Item>): Container() {

    /**
     * The toolbar's slots
     */
    val slots: List<Slot>

    /**
     * The selected slot in its reactive form.
     */
    val slotSelected: BasicSubject<Slot>

    /**
     * The currently selected slot.
     */
    var selectedSlot: Slot

    init {
        val frameWidth = root.width
        val toolbarFixMarginLeftRight = 30.0
        val toolbarFixMarginTopBottom = 5.0
        val maxToolbarWidth = frameWidth - 2 * toolbarFixMarginLeftRight

        val slotMargin = Slot.slotMargin

        val maxSlotWidth = (maxToolbarWidth - (numSlots + 1) * slotMargin) / numSlots
        val slotWidth = when(layout) {
            ToolbarLayout.FULL_WIDTH -> maxSlotWidth
            ToolbarLayout.FIXED -> preferredSlotWidth
            ToolbarLayout.FLEX -> if(preferredSlotWidth < maxSlotWidth) preferredSlotWidth else maxSlotWidth
        }

        val preferredToolbarWidth = slotWidth * numSlots + slotMargin * (numSlots + 1)
        val toolbarWidth = when(layout) {
            ToolbarLayout.FULL_WIDTH -> maxToolbarWidth
            ToolbarLayout.FIXED -> preferredToolbarWidth
            ToolbarLayout.FLEX -> if(preferredSlotWidth < maxSlotWidth) preferredToolbarWidth else maxToolbarWidth
        }

        val preferredToolbarMarginLeftRight = (frameWidth - toolbarWidth) / 2
        val toolbarMarginLeftRight = when(layout) {
            ToolbarLayout.FULL_WIDTH -> toolbarFixMarginLeftRight
            ToolbarLayout.FIXED -> preferredToolbarMarginLeftRight
            ToolbarLayout.FLEX -> if(preferredSlotWidth < maxSlotWidth) preferredToolbarMarginLeftRight else toolbarFixMarginLeftRight

        }

        val toolbarHeight = slotWidth + 2 * slotMargin
        val frameHeight = toolbarHeight + 2 * toolbarFixMarginTopBottom
        val slotHeight = slotWidth

        val slots = mutableListOf<Slot>()

        val toolbarFrame = fixedSizeContainer(frameWidth, frameHeight) {
            val toolbarFrame = this
            alignBottomToBottomOf(root)
            //solidRect(toolbarFrame.width, toolbarFrame.height, secondaryColor)
            val toolbar = container {
                alignTopToTopOf(toolbarFrame, toolbarFixMarginTopBottom)
                alignLeftToLeftOf(toolbarFrame, toolbarMarginLeftRight)

                solidRect(toolbarWidth, toolbarHeight, darkSecondaryColor)

                for(i in 0 until numSlots) {
                    slots += slot(i, slotWidth, slotHeight)
                }
            }
        }

        this.slots = slots

        // setup selection handling
        this.slots.forEach { slot ->
            slot.selected.subscribe { selected ->
                if(selected) {
                    select(slot)
                }
            }
        }
        slotSelected = BasicSubjectImpl(slots[0])
        selectedSlot = slots[0]
        slots[0].select()

        // key bindings
        keys {
            slots.mapIndexed { index, slot -> toolbarSlotKeys[index] to slot }.forEach {
                val (keys, slot) = it
                keys?.forEach { key ->
                    down(key) {
                        slot.select()
                    }
                }
            }
        }

        populateSlots(items)

        // data binding
        items.subscribe { mutation ->
            if(mutation.resource == null) return@subscribe
            val item: Item = mutation.resource!!
            when(mutation.op) {
                Operation.ADD -> addItem(item)
                Operation.REMOVE -> removeItem(item)
                Operation.NONE -> return@subscribe
            }
        }
    }

    /**
     * Populates the slots with a given list of [items].
     * Before populating the slots, all slots are cleared.
     */
    private fun populateSlots(items: List<Item>) {
        clearSlots()
        val stacks = items.groupBy { it }.entries
            .map { ItemStack(it.value.first(), it.value.count().toUInt()) }

        stacks.forEachIndexed { i, stack ->
            slots[i].stack = stack
        }
    }

    /**
     * Clears all slots.
     */
    private fun clearSlots() {
        slots.forEach { it.stack = null }
    }

    /**
     * Adds an [item] to the toolbar.
     */
    private fun addItem(item: Item) {
        val slot = slots.find { it.stack?.item == item } ?: slots.find { it.stack == null }
        ?: throw Exception("item cannot be added to full toolbar")
        slot.stack = if (slot.stack != null)
            ItemStack(item, slot.stack!!.amount + 1u)
        else
            ItemStack(item, 1u)
    }

    /**
     * Removes an [item] from the toolbar.
     */
    private fun removeItem(item: Item) {
        slots.find { it.stack?.item == item }.let { slot ->
            slot?.stack?.let { stack ->
                if(slot.stack!!.amount > 1u) {
                    slot.stack = ItemStack(stack.item, stack.amount - 1u)
                } else {
                    slot.stack = null
                }
            }
        }
    }

    /**
     * Selects a [slot].
     */
    private fun select(slot: Slot) {
        this.slots.forEach { if(it != slot) it.deselect() }
        this.slotSelected.onNext(slot)
        this.selectedSlot = slot
    }

    /**
     * Finds a slot by item [type].
     */
    fun findSlotByItemType(type: ItemType): Slot? = slots.find { it.stack?.item?.type == type }

    companion object {

        /**
         * The number of slots.
         */
        const val numSlots = 8
    }

}