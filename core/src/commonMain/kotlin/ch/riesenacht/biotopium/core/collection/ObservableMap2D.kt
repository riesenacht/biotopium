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

package ch.riesenacht.biotopium.core.collection

/**
 * Represents a 2-dimensional observable map.
 * The key consists of a [Pair] of [x][K1] and [y][K2], pointing to a [value][V].
 *
 * @author Manuel Riesen
 */
class ObservableMap2D<K1, K2, V> : ObservableMap<Pair<K1, K2>, V> by mutableObservableMapOf() {

    /**
     * Gets the value at position [x];[y].
     */
    operator fun get(x: K1, y: K2) = get(x to y)

    /**
     * Sets the [value] at position [x];[y].
     */
    operator fun set(x: K1, y: K2, value: V) = set(x to y, value)
}