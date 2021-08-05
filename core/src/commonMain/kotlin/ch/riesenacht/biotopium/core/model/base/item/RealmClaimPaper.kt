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

package ch.riesenacht.biotopium.core.model.base.item

import ch.riesenacht.biotopium.core.model.base.Owner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a claim paper for a realm.
 * With a claim paper a player can claim a realm.
 *
 * @property owner the realm claim paper's owner
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("RealmClaimPaper")
data class RealmClaimPaper(
    override val owner: Owner
) : Item {
    override val type = ItemType.REALM_CLAIM_PAPER
}