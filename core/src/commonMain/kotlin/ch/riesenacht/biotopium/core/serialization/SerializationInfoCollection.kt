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

package ch.riesenacht.biotopium.core.serialization

import ch.riesenacht.biotopium.core.model.base.ModelSerializationInfo
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

/**
 *  A collection of [serialization information][SerializationInfo].
 *
 *  @author Manuel Riesen
 */
object SerializationInfoCollection {

    private val infos: List<SerializationInfo> = listOf(
        ModelSerializationInfo,
        ActionSerializationInfo
    )

    private val emptyModule = SerializersModule { }

    /**
     * Sums up all modules to a single module.
     */
    val module: SerializersModule
        get() = infos.fold(emptyModule) { acc, info -> acc + info.module }
}