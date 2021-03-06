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

package ch.riesenacht.biotopium.core

import ch.riesenacht.biotopium.bus.BlockCandidateBus
import ch.riesenacht.biotopium.bus.OutgoingActionBus
import ch.riesenacht.biotopium.core.blockchain.BlockchainManager
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.blockchain.model.BlockCandidate
import ch.riesenacht.biotopium.logging.Logging
import ch.riesenacht.biotopium.network.NetworkManager
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.message.blockchain.ActionReqMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainFwdMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainReqMessage

/**
 * Represents the root of a biotopium instance.
 * @property config the biotopium configuration
 *
 * @author Manuel Riesen
 */
abstract class Biotopium(private val config: BiotopiumConfig) {

    /**
     * The network manager of the current biotopium instance.
     */
    protected val networkManager: NetworkManager = NetworkManager(config.p2pConfig)

    private val blockchainManager = BlockchainManager

    protected val logger = Logging.logger { }

    private val randomBlocklordPeer: PeerId
    get() = config.blocklordPeerIds.random()

    init {

        KeyManager.keyPair = config.keyPair

        networkManager.registerMessageHandler(BlockAddMessage::class) { wrapper, _ ->
            val message = wrapper.message
            BlockCandidateBus.onNext(BlockCandidate(message.block))
            logger.debug { "added block: ${message.block}" }
        }
        networkManager.registerMessageHandler(ChainFwdMessage::class) { wrapper, _ ->
            BlockCandidateBus.allOnNext(wrapper.message.blocks.map { block -> BlockCandidate(block) })
            logger.debug { "added missing chain parts: ${wrapper.message.blocks}" }
        }
        networkManager.registerMessageHandler(ChainReqMessage::class) { wrapper, network ->
            val reqMessage = wrapper.message
            val height = reqMessage.height
            logger.debug { "received chain request for height: $height" }
            if(height < blockchainManager.maxHeight) {
                val startIndex = blockchainManager.blockchain.indexOfFirst { it.height == height }
                val blockchain = blockchainManager.blockchain.drop(startIndex)

                val fwdMessage = ChainFwdMessage(blockchain)

                network.send(wrapper.peerId, fwdMessage)

                logger.debug { "answered chain request with $fwdMessage" }
            } else {
                logger.debug { "could not answer chain request, height too high" }
            }
        }
        OutgoingActionBus.subscribe { action ->
            networkManager.send(randomBlocklordPeer, ActionReqMessage(action))
        }
    }

    /**
     * Starts the underlying peer-to-peer node over the network manager.
     */
    suspend fun startP2pNode() {
        networkManager.startP2pNode()
    }

    /**
     * Stops the underlying peer-to-peer node over the network manager.
     */
    suspend fun stopP2pNode() {
        networkManager.stopP2pNode()
    }
}