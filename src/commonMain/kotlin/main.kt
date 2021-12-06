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


import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.logging.LoggingConfig
import ch.riesenacht.biotopium.logging.LoggingLevel
import com.soywiz.korge.Korge
import com.soywiz.korim.color.Colors

suspend fun main() = Korge(width = 800, height = 500, title = "biotopium", bgcolor = Colors.LIGHTGREY) {

    LoggingConfig.setLoggingLevel(LoggingLevel.DEBUG)

    applyEffect(CoreModuleEffect)

}