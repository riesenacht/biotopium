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

import ch.riesenacht.biotopium.bus.BlockCandidateBus
import ch.riesenacht.biotopium.core.blockchain.BlockchainManager
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.BlockCandidate
import ch.riesenacht.biotopium.logging.Logging
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration
import ch.riesenacht.biotopium.network.model.message.Message
import ch.riesenacht.biotopium.network.model.message.PeerAddressInfoMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainFwdMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainReqMessage
import kotlin.reflect.KClass

/**
 * The manager for connections and incoming and outgoing messages over the network.
 *
 * @author Manuel Riesen
 */
class NetworkManager(p2pConfig: P2pConfiguration) {

    private val blockchainManager = BlockchainManager

    val peerAddressBook: PeerAddressBook = PeerAddressBook()

    private val logger = Logging.logger { }

    private val p2pNode: P2pNode = P2pNode(p2pConfig)

    init {
        p2pNode.registerMessageHandler(PeerAddressInfoMessage::class) { wrapper, _ ->
            val message = wrapper.message
            peerAddressBook.add(message.peerId, message.address)
            logger.debug { "added peer address book entry: ${message.peerId} <=> ${message.address}" }
        }
        p2pNode.registerMessageHandler(BlockAddMessage::class) { wrapper, _ ->
            val message = wrapper.message
            BlockCandidateBus.onNext(BlockCandidate(message.block))
            logger.debug { "added block: ${message.block}" }
        }
        p2pNode.registerMessageHandler(ChainFwdMessage::class) { wrapper, _ ->
            BlockCandidateBus.allOnNext(wrapper.message.blocks.map { block -> BlockCandidate(block) })
            logger.debug { "added missing chain parts: ${wrapper.message.blocks}" }
        }
        p2pNode.registerMessageHandler(ChainReqMessage::class) { wrapper, network ->
            val reqMessage = wrapper.message
            val height = reqMessage.height
            logger.debug { "received chain request for height: $height" }
            if(height <= blockchainManager.maxHeight) {
                val startIndex = blockchainManager.blockchain.indexOfFirst { it.height == height }
                val blockchain = blockchainManager.blockchain.drop(startIndex)

                val fwdMessage = ChainFwdMessage(blockchain)

                network.send(wrapper.peerId, fwdMessage)

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

    /**
     * Sends a [message] to the host of a given [peerId].
     */
    fun send(peerId: PeerId, message: Message) {
        p2pNode.send(peerId, message)
    }

    /**
     * Broadcasts a [message].
     */
    fun sendBroadcast(message: Message) {
        p2pNode.sendBroadcast(message)
    }

    /**
     * Sends the peer [address] info message to all reachable peers.
     */
    fun sendPeerAddressInfoMessage(address: Address) {
        p2pNode.sendBroadcast(PeerAddressInfoMessage(p2pNode.peerId, address))
    }

    /**
     * Starts the peer-to-peer node.
     */
    suspend fun startP2pNode() {
        p2pNode.start()
    }

    /**
     * Stops the peer-to-peer node.
     */
    suspend fun stopP2pNode() {
        p2pNode.stop()
    }

    /**
     * Registers a new message [handler] for a [type] of [Message] on the [p2pNode].
     * Delegates to the [p2pNode].
     */
    fun <T : Message> registerMessageHandler(type: KClass<T>, handler: MessageHandler<T>) = p2pNode.registerMessageHandler(type, handler)

}