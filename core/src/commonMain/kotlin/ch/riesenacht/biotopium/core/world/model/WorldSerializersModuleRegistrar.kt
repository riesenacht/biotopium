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

package ch.riesenacht.biotopium.core.world.model

import ch.riesenacht.biotopium.core.world.model.item.*
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.map.Tile
import ch.riesenacht.biotopium.serialization.SerializersModuleRegistrar
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Holds the serialization information of the model data classes.
 * Registers the serializers module at the serializers module registry.
 *
 * @author Manuel Riesen
 */
object WorldSerializersModuleRegistrar : SerializersModuleRegistrar(SerializersModule {

    // Item class hierarchy
    polymorphic(Item::class) {
        subclass(HarvestedPlant::class)
        subclass(Hoe::class)
        subclass(RealmClaimPaper::class)
        subclass(Seed::class)
    }

    // Tile class hierarchy
    polymorphic(Tile::class) {
        subclass(DefaultTile::class)
        subclass(Plot::class)
    }
})
