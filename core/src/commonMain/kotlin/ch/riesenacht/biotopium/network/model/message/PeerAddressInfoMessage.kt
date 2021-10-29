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

package ch.riesenacht.biotopium.network.model.message

import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.payload.PeerAddressPayload
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the peer address info message.
 * This message is used to publish the relationship between an address and a peer ID.
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("PeerAddressInfoMessage")
data class PeerAddressInfoMessage(
    override val peerId: PeerId,
    override val payload: PeerAddressPayload
) : Message<PeerAddressPayload>()