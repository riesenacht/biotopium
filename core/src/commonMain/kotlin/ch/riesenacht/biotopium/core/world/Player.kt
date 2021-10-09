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

package ch.riesenacht.biotopium.core.world

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.world.model.item.Item

/**
 * Represents a player.
 * A player has a [name] and is identified by the [address].
 *
 * @author Manuel Riesen
 */
data class Player(
    val name: String,
    val address: Address
) {

    /**
     * Mutable variant of the player's inventory.
     */
    private val mutableItems: MutableList<Item> = mutableListOf()

    /**
     * The player's inventory.
     */
    val items: List<Item>
    get() = mutableItems

    /**
     * Adds an item to the player's inventory.
     */
    fun addItem(item: Item) {
        mutableItems.add(item)
    }

    fun removeItem(item: Item) {
        mutableItems.remove(item)
    }

    fun addAllItems(items: List<Item>) {
        mutableItems.addAll(items)
    }
}