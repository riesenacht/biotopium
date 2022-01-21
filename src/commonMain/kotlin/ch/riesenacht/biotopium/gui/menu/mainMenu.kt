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

package ch.riesenacht.biotopium.gui.menu

import ch.riesenacht.biotopium.gui.*
import ch.riesenacht.biotopium.gui.bootstrap.BootstrapScene
import ch.riesenacht.biotopium.gui.realm.RealmScene
import ch.riesenacht.biotopium.gui.skin.BiotopiumUISkin
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.SceneContainer
import com.soywiz.korge.ui.korui.korui
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiSkin
import com.soywiz.korge.view.*
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korui.layout.horizontal
import com.soywiz.korui.layout.preferredWidth
import com.soywiz.korui.layout.vertical

/**
 * Starts the biotopium game.
 * Changes the scene to the realm scene.
 */
private fun startBiotopium(sceneContainer: SceneContainer, config: GameConfig) {
    launchImmediately(sceneContainer.coroutineContext) {
        sceneContainer.changeTo<BootstrapScene>(config)
    }
}

/**
 * Creates the main menu in the current container.
 * The menu has the given [width] and [height].
 */
suspend fun Container.mainMenu(width: Double, height: Double, config: GameConfig, scene: MainMenuScene) {
    uiSkin = BiotopiumUISkin()
    val mainMenu = fixedSizeContainer(width, height, clip = true) { }
    mainMenu.korui {

        uiSkin = this@mainMenu.uiSkin

        val defaultPadding = 12.0

        vertical {
            horizontal {
                preferredWidth = 100.percent

                val title = text("biotopium", 100.0) {
                    alignTopToTopOf(mainMenu, defaultPadding)
                    centerXOn(mainMenu)
                    adjustBounds()
                    color = primaryColor
                }

                val startText = text("Start Demo", 30.0) {
                    alignTopToBottomOf(title, defaultPadding)
                    centerXOn(mainMenu)
                    adjustBounds()
                    color = accentColor
                }

                container {
                    val startBar = this@container
                    val startBarSidePadding = 20.0
                    val startBarWidth = mainMenu.width - 2 * startBarSidePadding
                    val startBarHeight = 100.0


                    alignTopToBottomOf(startText, defaultPadding)
                    alignLeftToLeftOf(mainMenu, startBarSidePadding)
                    alignRightToRightOf(mainMenu, startBarSidePadding)
                    centerXOn(mainMenu)

                    preferredWidth = 100.percent

                    solidRect(startBarWidth, startBarHeight) {
                        alignTopToTopOf(startBar)
                        centerXOn(mainMenu)
                    }


                    val left = container {
                        val menuBarSection = this
                        alignTopToTopOf(startBar)
                        alignLeftToLeftOf(startBar)


                        solidRect(startBar.width / 2, startBar.height) {
                            alignTopToTopOf(menuBarSection)
                            alignLeftToLeftOf(menuBarSection)
                            color = neutralColor
                        }

                        uiButton(width = 200.0, text = "New Account") {
                            centerXOn(menuBarSection)
                            centerYOn(menuBarSection)

                            enable()

                            onClick {
                                config.startOption = StartOption.NEW_ACCOUNT
                                startBiotopium(scene.sceneContainer, config)
                            }
                        }

                    }

                    val right = container {
                        val menuBarSection = this
                        alignTopToTopOf(startBar)
                        alignRightToRightOf(startBar)
                        alignLeftToRightOf(left)

                        solidRect(startBar.width / 2, startBar.height) {
                            alignTopToTopOf(menuBarSection)
                            alignRightToRightOf(menuBarSection)
                            color = neutralColor
                        }

                        uiButton(width = 200.0, text = "Import Keys", block = {
                            centerXOn(menuBarSection)
                            centerYOn(menuBarSection)

                            disable()

                            onClick {
                                config.startOption = StartOption.IMPORT_KEY
                                startBiotopium(scene.sceneContainer, config)
                            }
                        })
                    }

                }

            }
        }

    }

}
