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

package ch.riesenacht.biotopium.network

import ch.riesenacht.biotopium.core.blockchain.BlockchainManager
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import ch.riesenacht.biotopium.network.model.message.PeerAddressInfoMessage
import ch.riesenacht.biotopium.network.model.payload.PeerAddressPayload

/**
 * The manager for connections and incoming and outgoing messages over the network.
 *
 * @author Manuel Riesen
 */
class NetworkManager(p2pConfig: P2pConfiguration) {

    val peerAddressBook: PeerAddressBook = PeerAddressBook()

    val p2pNode: P2pNode = P2pNode(p2pConfig)

    init {

        p2pNode.registerMessageHandler(PeerAddressInfoMessage::class) {
            val payload = it.payload as PeerAddressPayload
            peerAddressBook.add(payload.peerId, payload.address)
        }
        p2pNode.registerMessageHandler(BlockAddMessage::class) {
            val message = it as BlockAddMessage
            BlockchainManager.add(message.payload.block)
        }

    }

}