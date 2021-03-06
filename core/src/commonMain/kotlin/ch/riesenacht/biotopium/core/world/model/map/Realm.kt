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

package ch.riesenacht.biotopium.core.world.model.map

import ch.riesenacht.biotopium.core.world.model.Element
import ch.riesenacht.biotopium.core.world.model.Owner
import ch.riesenacht.biotopium.core.world.model.RealmIndex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a realm of a player.
 * A realm is owned by an [owner]
 * and identifiable by the indices [ix] and [iy].
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("Realm")
data class Realm(
    override val owner: Owner,
    val ix: RealmIndex,
    val iy: RealmIndex
) : Element

/**
 * The size of a realm.
 */
const val realmSize: UInt = 8u
