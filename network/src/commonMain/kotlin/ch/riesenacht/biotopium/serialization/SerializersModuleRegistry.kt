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

package ch.riesenacht.biotopium.serialization

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

/**
 *  The registry for [serializers modules][SerializersModule].
 *  Contains all serializers [modules].
 *
 *  @author Manuel Riesen
 */
object SerializersModuleRegistry {

    private val modules: MutableSet<SerializersModule> = mutableSetOf()

    private val emptyModule = SerializersModule {  }

    /**
     * Accumulates all modules to a single module.
     */
    val module: SerializersModule
        get() = modules.fold(emptyModule) { acc, m -> acc + m }

    /**
     * Registers a serializers [module] on the registry.
     */
    fun register(module: SerializersModule) {
        modules.add(module)
    }
}