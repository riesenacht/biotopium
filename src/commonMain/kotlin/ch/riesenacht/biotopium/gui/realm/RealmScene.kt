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

import ch.riesenacht.biotopium.BiotopiumClient
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.world.WorldStateManager
import ch.riesenacht.biotopium.core.world.model.item.Hoe
import ch.riesenacht.biotopium.core.world.model.item.ItemType
import ch.riesenacht.biotopium.core.world.model.item.Seed
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.gui.GameConfig
import ch.riesenacht.biotopium.gui.darkSecondaryColor
import ch.riesenacht.biotopium.gui.toolbar.Toolbar
import ch.riesenacht.biotopium.gui.toolbar.flexToolbar
import ch.riesenacht.biotopium.gui.tutorial.RealmClaimTutorial
import ch.riesenacht.biotopium.gui.tutorial.startTutorial
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import kotlin.math.min

/**
 * The realm scene.
 */
class RealmScene(config: GameConfig) : Scene() {

    private lateinit var toolbar: Toolbar

    override suspend fun Container.sceneInit() {

        val realmScene = this
        val player = WorldStateManager.players[KeyManager.address]
        if(player == null) {
            text("An error occurred: Player cannot be loaded") {
                centerXOn(root)
            }
            return
        }

        val realm = WorldStateManager.realms.values.find { it.owner == player.address }
        toolbar = flexToolbar(80.0, player.items)

        if(realm == null) {
            val noRealmText = text("You don't have a realm yet. Claim one using a realm claim paper") {
                centerOn(root)
                color = darkSecondaryColor
            }
            startTutorial(RealmClaimTutorial(toolbar) {
                noRealmText.removeFromParent()
                val newRealm = WorldStateManager.realms.values.find { it.owner == player.address }
                if(newRealm != null) {
                    showRealm(newRealm)
                }
            })

            return
        }

        showRealm(realm)
    }

    /**
     * Shows a [realm].
     */
    private fun Container.showRealm(realm: Realm) {

        val borderMargin = 10.0
        val outerMargin = 120
        val realmSize = min(views.virtualWidth - outerMargin, views.virtualHeight - outerMargin).toDouble()
        val marginSides = (views.virtualWidth - realmSize) / 2.0

        graphics {
            position(marginSides, borderMargin)
            fill(Colors.WHEAT) {
                realmDisplay(realm, marginSides, borderMargin, realmSize, this@RealmScene::onTileClick, ownRealm = true)
            }
        }
    }

    /**
     * Handler for [tile] click event.
     */
    private fun onTileClick(tile: Tile) {
        val slot = toolbar.selectedSlot
        val item = slot.stack?.item

        if(item == null) {

            if(tile is Plot) {
                val plant = tile.plant
                if(plant != null) {
                    if(plant.growth == PlantGrowth.GROWN) {
                        BiotopiumClient.createHarvestAction(plant, tile)
                    }
                }
            }

            return
        }

        when(item.type) {

            ItemType.HOE -> {
                BiotopiumClient.createCreatePlotAction(tile, item as Hoe)
            }
            ItemType.SEED -> {
                if(tile is Plot) {
                    BiotopiumClient.createSeedAction(tile, item as Seed)
                }
            }

            // ignore other items
            ItemType.REALM_CLAIM_PAPER -> { }
            ItemType.PLANT -> { }
        }
    }
}