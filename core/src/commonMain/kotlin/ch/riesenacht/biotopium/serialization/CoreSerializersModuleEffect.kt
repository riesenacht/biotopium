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

package ch.riesenacht.biotopium.serialization

import ch.riesenacht.biotopium.core.action.model.ActionSerializersModuleRegistrar
import ch.riesenacht.biotopium.core.blockchain.model.BlockchainSerializersModuleRegistrar
import ch.riesenacht.biotopium.core.effect.ModuleEffect
import ch.riesenacht.biotopium.core.world.model.WorldSerializersModuleRegistrar
import ch.riesenacht.biotopium.network.model.MessageSerializersModuleRegistrar

/**
 * The effect for registering all serializer modules of the core
 * at the serializers module registry.
 *
 * @author Manuel Riesen
 */
object CoreSerializersModuleEffect : ModuleEffect(
    nested = arrayOf(
        WorldSerializersModuleRegistrar,
        ActionSerializersModuleRegistrar,
        BlockchainSerializersModuleRegistrar,
        MessageSerializersModuleRegistrar
    )
)