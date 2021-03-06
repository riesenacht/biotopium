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
import ch.riesenacht.biotopium.reactive.collection.MutableObservableList
import ch.riesenacht.biotopium.reactive.collection.ObservableList
import ch.riesenacht.biotopium.reactive.collection.mutableObservableListOf

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
    private val mutableItems: MutableObservableList<Item> = mutableObservableListOf()

    /**
     * The player's inventory.
     */
    val items: ObservableList<Item>
    get() = mutableItems

    /**
     * Adds an [item] to the player's inventory.
     */
    fun addItem(item: Item) {
        mutableItems.add(item)
    }

    /**
     * Removes an [item] from the player's inventory.
     */
    fun removeItem(item: Item) {
        mutableItems.remove(item)
    }

    /**
     * Adds all [items] to the player's inventory.
     */
    fun addAllItems(items: List<Item>) {
        mutableItems.addAll(items)
    }
}