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

package ch.riesenacht.biotopium.gui

import com.soywiz.korim.color.Colors

/**
 * The primary color.
 */
val primaryColor = Colors["#50C878"]

/**
 * Darker variant of the primary color.
 */
val darkPrimaryColor = Colors["#257A41"]

/**
 * The accent color.
 */
val accentColor = Colors["#C763BE"]

/**
 * The secondary color.
 */
val secondaryColor = Colors["#C78F3C"]

/**
 * Darker variant of the secondary color.
 */
val darkSecondaryColor = Colors["#7A4025"]

/**
 * The neutral color.
 */
val neutralColor = Colors["#F8FCFA"]

/**
 * The color for tiles.
 */
object TileColor {

    /**
     * Default tile color.
     */
    val defaultTile = darkPrimaryColor

    /**
     * Empty plot color.
     */
    val emptyPlot = darkSecondaryColor

}

/**
 * The colors of plants.
 */
object PlantColor {

    /**
     * The colors for the wheat plant.
     */
    object Wheat {
        val seed = Colors["#F6E1B7"]
        val halfGrown = Colors["#F2D291"]
        val grown = Colors["#EEC36D"]
    }

    /**
     * The colors for the corn plant.
     */
    object Corn {
        val seed = Colors["#FBEC5D"]
        val halfGrown = Colors["#FAE738"]
        val grown = Colors["#EFD706"]
    }

}