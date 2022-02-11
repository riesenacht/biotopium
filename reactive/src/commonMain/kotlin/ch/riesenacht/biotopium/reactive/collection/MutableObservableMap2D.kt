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

/**
 * Represents an observable mutable 2-dimensional [map] consisting of [key][K]-[value][V] pairs.
 *
 * @author Manuel Riesen
 */
class MutableObservableMap2D<K1, K2, V>(
    map: MutableObservableMap<Pair<K1, K2>, V>
) : MutableObservableMap<Pair<K1, K2>, V> by map, ObservableMap2D<K1, K2, V> {

    /**
     * Sets the [value] at position [x];[y].
     */
    operator fun set(x: K1, y: K2, value: V) = set(x to y, value)

}

/**
 * Creates a mutable observable 2-dimensional map.
 * The map is populated with the given [pairs].
 */
fun <K1, K2, V> mutableObservableMap2dOf(vararg pairs: Pair<Pair<K1, K2>, V>): MutableObservableMap2D<K1, K2, V> {
    return MutableObservableMap2D(mutableObservableMapOf(*pairs))
}
