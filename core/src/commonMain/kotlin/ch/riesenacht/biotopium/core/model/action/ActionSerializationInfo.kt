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

package ch.riesenacht.biotopium.core.model.action

import ch.riesenacht.biotopium.core.model.blockchain.BlockData
import ch.riesenacht.biotopium.core.serialization.SerializationInfo
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Holds the serialization information about the action data classes.
 *
 * @author Manuel Riesen
 */
object ActionSerializationInfo : SerializationInfo {

    override val module = SerializersModule {

        fun PolymorphicModuleBuilder<Action>.registerActionSubclasses() {
            subclass(ChunkGenesisAction::class)
            subclass(ClaimRealmAction::class)
            subclass(CreatePlotAction::class)
            subclass(GrowAction::class)
            subclass(GrowAction::class)
            subclass(HarvestAction::class)
            subclass(IntroductionAction::class)
            subclass(SeedAction::class)
        }

        polymorphic(Action::class) { registerActionSubclasses() }
        polymorphic(BlockData::class) { registerActionSubclasses() }
    }
}