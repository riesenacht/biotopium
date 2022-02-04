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

import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.world.WorldStateManager
import ch.riesenacht.biotopium.gui.GameConfig
import ch.riesenacht.biotopium.gui.darkSecondaryColor
import ch.riesenacht.biotopium.gui.toolbar.flexToolbar
import ch.riesenacht.biotopium.gui.tutorial.RealmClaimTutorial
import ch.riesenacht.biotopium.gui.tutorial.startTutorial
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.centerOn
import com.soywiz.korge.view.centerXOn
import com.soywiz.korge.view.text

/**
 * The realm scene.
 */
class RealmScene(config: GameConfig) : Scene() {

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

        val toolbar = flexToolbar(80.0, player.items)

        if(realm == null) {
            text("You don't have a realm yet. Claim one using a realm claim paper") {
                centerOn(root)
                color = darkSecondaryColor
            }
            startTutorial(RealmClaimTutorial(toolbar))
        }

    }
}