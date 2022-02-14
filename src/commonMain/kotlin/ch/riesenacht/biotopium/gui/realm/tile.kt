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

package ch.riesenacht.biotopium.gui.realm

import ch.riesenacht.biotopium.GrowthScheduler
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.core.world.model.map.TileType
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.PlantType
import ch.riesenacht.biotopium.gui.PlantColor
import ch.riesenacht.biotopium.gui.TileColor
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*
import com.soywiz.korim.text.TextAlignment

/**
 * Creates a [tile] display at position [x];[y] with a given [size].
 *
 * @param onTileClick callback for tile click event
 * @param ownRealm whether the realm belongs to the current player
 */
fun Container.tileDisplay(
    x: Double,
    y: Double,
    size: Double,
    tile: Tile,
    onTileClick: (Tile) -> Unit,
    ownRealm: Boolean = false
): TileDisplay {
    return TileDisplay(tile, size, onTileClick, ownRealm).position(x, y).addTo(this)
}

/**
 * The display of a [Tile].
 *
 * @property tile tile to display
 * @param size tile size
 * @param onTileClick callback for tile click event
 * @param ownRealm whether the realm belongs to the current player
 *
 * @author Manuel Riesen
 * @author Sandro Rüfenacht
 */
class TileDisplay(private val tile: Tile, size: Double, onTileClick: (Tile) -> Unit, ownRealm: Boolean = false) : Container() {

    private val tileRect: SolidRect

    private var plantNameText: Text? = null

    private var growthText: Text? = null

    private val tileColor
    get() = when(tile.type) {
        TileType.DEFAULT -> TileColor.defaultTile
        TileType.PLOT -> {
            val plot = tile as Plot
            val plant = plot.plant
            if(plant == null) {
                TileColor.emptyPlot
            } else {
                when(plant.type) {
                    PlantType.WHEAT -> when(plant.growth) {
                        PlantGrowth.SEED -> PlantColor.Wheat.seed
                        PlantGrowth.HALF_GROWN -> PlantColor.Wheat.halfGrown
                        PlantGrowth.GROWN -> PlantColor.Wheat.grown
                    }
                    PlantType.CORN -> when(plant.growth) {
                        PlantGrowth.SEED -> PlantColor.Corn.seed
                        PlantGrowth.HALF_GROWN -> PlantColor.Corn.halfGrown
                        PlantGrowth.GROWN -> PlantColor.Corn.grown
                    }
                }
            }
        }
    }

    init {
        tileRect = solidRect(size, size, tileColor)
            .position(x, y)
            .addTo(this)

        if(tile.type == TileType.PLOT) {
            val plot = tile as Plot
            if(plot.plant != null) {

                plantNameText = text(plot.plant?.identifier ?: "") {
                    alignment = TextAlignment.CENTER
                    centerXOn(tileRect)
                    centerYOn(tileRect)
                }

                growthText = text(plot.plant?.growth?.name?.lowercase() ?: "", textSize = 10.0) {
                    alignTopToBottomOf(plantNameText!!)
                    alignment = TextAlignment.CENTER
                    centerXOn(tileRect)
                }


                if(ownRealm && plot.plant!!.growth != PlantGrowth.GROWN) {
                    GrowthScheduler.watchPlot(plot)
                }

            }
        }

        tileRect.onClick {
            onTileClick(tile)
        }
    }

    /**
     * Removes the tile from the parent.
     * Removes all containers recursively.
     */
    fun remove() {
        tileRect.removeAllComponents()
        growthText?.removeAllComponents()
        plantNameText?.removeAllComponents()
        removeAllComponents()
        removeFromParent()
    }

}