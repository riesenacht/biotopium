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

import ch.riesenacht.biotopium.BiotopiumClient
import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.effect.EffectProfile
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.gui.bootstrap.BootstrapScene
import ch.riesenacht.biotopium.gui.menu.MainMenuScene
import ch.riesenacht.biotopium.gui.realm.RealmScene
import ch.riesenacht.biotopium.logging.LoggingConfig
import ch.riesenacht.biotopium.logging.LoggingLevel
import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.Size
import com.soywiz.korma.geom.SizeInt

/**
 * The biotopium game module.
 *
 * @author Manuel Riesen
 */
object BiotopiumModule : Module() {

    /**
     * The window dimensions.
     */
    private val SIZE = SizeInt(Size(800, 500))

    override val title = "biotopium"
    override val mainScene = MainMenuScene::class
    override val windowSize = SIZE
    override val size = SIZE
    override val bgcolor = neutralColor

    init {
        LoggingConfig.setLoggingLevel(LoggingLevel.DEBUG)
        applyEffect(CoreModuleEffect, EffectProfile.DEV)

        //TODO simple workaround to enforce initialization
        BiotopiumClient
    }

    override suspend fun AsyncInjector.configure() {
        mapInstance(GameConfig())
        mapPrototype { MainMenuScene(get()) }
        mapPrototype { BootstrapScene(get()) }
        mapPrototype { RealmScene(get()) }
    }
}