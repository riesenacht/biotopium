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
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.logging.Logging
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration
import ch.riesenacht.biotopium.network.model.message.Message
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import ch.riesenacht.biotopium.network.model.message.PeerAddressInfoMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainFwdMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainReqMessage

/**
 * The manager for connections and incoming and outgoing messages over the network.
 *
 * @author Manuel Riesen
 */
class NetworkManager(p2pConfig: P2pConfiguration) {

    private val blockchainManager = BlockchainManager

    val peerAddressBook: PeerAddressBook = PeerAddressBook()

    private val logger = Logging.logger { }

    val p2pNode: P2pNode = P2pNode(p2pConfig)

    init {
        p2pNode.registerMessageHandler(PeerAddressInfoMessage::class) {
            val message = it.message
            peerAddressBook.add(message.peerId, message.address)
            logger.debug { "added peer address book entry: ${message.peerId} <=> ${message.address}" }
        }
        p2pNode.registerMessageHandler(BlockAddMessage::class) {
            val message = it.message
            blockchainManager.add(message.block)
            logger.debug { "added block: ${message.block}" }
        }
        p2pNode.registerMessageHandler(ChainFwdMessage::class) {
            val message = it.message
            blockchainManager.addAll(message.blocks)
            logger.debug { "added missing chain parts: ${message.blocks}" }
        }
        p2pNode.registerMessageHandler(ChainReqMessage::class) { wrapper ->
            val reqMessage = wrapper.message
            val height = reqMessage.height
            logger.debug { "received chain request for height: $height" }
            if(height <= blockchainManager.maxHeight) {
                val startIndex = blockchainManager.blockchain.indexOfFirst { it.height == height }
                val blockchain = blockchainManager.blockchain.drop(startIndex)

                val fwdMessage = ChainFwdMessage(blockchain)

                p2pNode.send(wrapper.peerId, fwdMessage)

                logger.debug { "answered chain request with $fwdMessage" }
            } else {
                logger.debug { "could not answer chain request, height too high" }
            }
        }

    }

    /**
     * Sends a [message] to the host of a [address].
     * @throws AddressUnreachableException the address cannot be mapped to a peer ID
     */
    @Throws(AddressUnreachableException::class)
    fun send(address: Address, message: Message) {
        if(!peerAddressBook.peerId.containsKey(address)) {
            throw AddressUnreachableException(address)
        }
        val peerId = peerAddressBook.peerId[address]!!
        p2pNode.send(peerId, message)
    }

}