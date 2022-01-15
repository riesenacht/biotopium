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

import ch.riesenacht.biotopium.reactive.BasicObservable
import ch.riesenacht.biotopium.reactive.EmptyChange

/**
 * Represents an observable [map][Map] consisting of [key][K]-[value][V] pairs.
 *
 * @see Map
 * @see BasicObservable
 *
 * @author Manuel Riesen
 */
interface ObservableMap<K, V> : Map<K, V>, BasicObservable<EmptyChange>