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

package ch.riesenacht.biotopium.core.action.model

import ch.riesenacht.biotopium.core.blockchain.model.BlockData
import ch.riesenacht.biotopium.serialization.SerializersModuleRegistrar
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Holds the serialization information about the action data classes.
 * Registers the serializers module at the serializers module registry.
 *
 * @author Manuel Riesen
 */
object ActionSerializersModuleRegistrar : SerializersModuleRegistrar(SerializersModule {

    polymorphic(Action::class) {
        subclass(ChunkGenesisAction::class)
        subclass(ClaimRealmAction::class)
        subclass(CreatePlotAction::class)
        subclass(GrowAction::class)
        subclass(GrowAction::class)
        subclass(HarvestAction::class)
        subclass(IntroductionAction::class)
        subclass(SeedAction::class)
    }

    polymorphic(BlockData::class) {
        //TODO replace workaround for supporting serialization of a type with generics
        // see: https://github.com/Kotlin/kotlinx.serialization/issues/944
        @Suppress("UNCHECKED_CAST")
        subclass(ActionFrame::class, ActionFrame.serializer( PolymorphicSerializer(Action::class)) as KSerializer<ActionFrame<*>>)
    }
})