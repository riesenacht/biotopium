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

package ch.riesenacht.biotopium.gui.bootstrap

import ch.riesenacht.biotopium.BiotopiumClient
import ch.riesenacht.biotopium.bus.IncomingActionBus
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.ActionType
import ch.riesenacht.biotopium.core.action.model.IntroductionAction
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.world.Player
import ch.riesenacht.biotopium.core.world.WorldStateManager
import ch.riesenacht.biotopium.gui.GameConfig
import ch.riesenacht.biotopium.gui.StartOption
import ch.riesenacht.biotopium.gui.primaryColor
import ch.riesenacht.biotopium.gui.realm.RealmScene
import com.badoo.reaktive.disposable.Disposable
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.scene.MaskTransition
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.centerOn
import com.soywiz.korge.view.centerXOn
import com.soywiz.korge.view.filter.TransitionFilter
import com.soywiz.korge.view.text
import com.soywiz.korio.async.launchImmediately

class BootstrapScene(private val config: GameConfig) : Scene() {

    override suspend fun Container.sceneInit() {
        text("Loading...", 100.0) {
            color = primaryColor
            centerOn(root)
        }
    }

    override suspend fun Container.sceneMain() {

        BiotopiumClient.startP2pNode()
        BiotopiumClient.updateChain {
            if(config.startOption == StartOption.NEW_ACCOUNT) {
                BiotopiumClient.createIntroductionAction()
            }
            var disposable: Disposable? = null
            disposable = IncomingActionBus.subscribe {
                if(
                    it.content.type == ActionType.INTRODUCTION
                    && (it.content as IntroductionAction).produce.realmClaimPaper.owner == KeyManager.address) {
                    disposable?.dispose()

                    launchImmediately(sceneContainer.coroutineContext) {
                        sceneContainer.changeTo<RealmScene>(config,
                            transition = MaskTransition(TransitionFilter.Transition.VERTICAL),
                            time = 0.5.seconds
                        )
                    }
                }
            }

        }

    }
}