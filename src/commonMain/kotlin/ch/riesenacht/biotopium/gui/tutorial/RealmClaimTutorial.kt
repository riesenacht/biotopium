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

package ch.riesenacht.biotopium.gui.tutorial

import ch.riesenacht.biotopium.core.action.ActionManager
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.model.ClaimRealmAction
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.world.WorldStateManager
import ch.riesenacht.biotopium.core.world.model.item.ItemType
import ch.riesenacht.biotopium.core.world.model.item.RealmClaimPaper
import ch.riesenacht.biotopium.core.world.model.map.Realm
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.gui.darkSecondaryColor
import ch.riesenacht.biotopium.gui.skin.BiotopiumUISkinDirect
import ch.riesenacht.biotopium.gui.toolbar.Toolbar
import com.badoo.reaktive.disposable.Disposable
import com.soywiz.korge.input.onClick
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiSkin
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.centerOn

/**
 * The tutorial for claiming a realm.
 * The [toolbar] is used to locate a realm claim paper.
 *
 * @author Manuel Riesen
 */
class RealmClaimTutorial(private val toolbar: Toolbar) : Tutorial {

    override fun start(container: Container) {
        val realmClaimPaperSlot = toolbar.findSlotByItemType(ItemType.REALM_CLAIM_PAPER)
        if(realmClaimPaperSlot != null) {
            toolbar.highlightSlot(darkSecondaryColor, realmClaimPaperSlot)
        }
        var onSelected: Disposable? = null
        onSelected = toolbar.selectedSlot.subscribe { slot ->
            if(slot.stack?.item?.type == ItemType.REALM_CLAIM_PAPER) {
                container.uiButton(350.0, 40.0, text = "Claim a Realm at a random position") {
                    val claimRealmButton = this
                    centerOn(root)
                    uiSkin = BiotopiumUISkinDirect
                    onClick {
                        var tileCandidate: Tile
                        do {
                            tileCandidate = WorldStateManager.tiles.values.random()
                        } while(WorldStateManager.realms[tileCandidate.x.realmIndex to tileCandidate.y.realmIndex] != null)
                        val tile = tileCandidate
                        val realm = Realm(KeyManager.address, tile.x.realmIndex, tile.y.realmIndex)
                        slot.stack?.let {
                            ActionManager.createAction(ClaimRealmAction(realm, slot.stack!!.item as RealmClaimPaper))
                            ActionManager.registerActionListener(
                                ActionType.CLAIM_REALM,
                                removeOn = {action -> action.author == KeyManager.address }
                            ) {
                                claimRealmButton.removeFromParent()
                            }
                        }
                    }
                }
                onSelected?.dispose()
            }
        }
    }
}