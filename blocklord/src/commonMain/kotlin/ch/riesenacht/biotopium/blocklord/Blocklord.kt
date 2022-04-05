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

package ch.riesenacht.biotopium.blocklord

import ch.riesenacht.biotopium.BlocklordModuleEffect
import ch.riesenacht.biotopium.bus.ActionCandidateBus
import ch.riesenacht.biotopium.bus.OutgoingBlockBus
import ch.riesenacht.biotopium.core.Biotopium
import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.action.ActionManager
import ch.riesenacht.biotopium.core.action.model.ActionCandidate
import ch.riesenacht.biotopium.core.action.model.ChunkGenesisAction
import ch.riesenacht.biotopium.core.blockchain.BlockSmith
import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.BlockchainManager
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.blockchain.model.record.EmptyRecordBook
import ch.riesenacht.biotopium.core.blockchain.model.record.RecordBook
import ch.riesenacht.biotopium.core.blockchain.model.record.recordBookOf
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.effect.EffectProfile
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.world.model.coord
import ch.riesenacht.biotopium.core.world.model.map.DefaultTile
import ch.riesenacht.biotopium.logging.LoggingConfig
import ch.riesenacht.biotopium.logging.LoggingLevel
import ch.riesenacht.biotopium.network.model.message.blockchain.ActionReqMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import kotlinx.coroutines.awaitCancellation
import ch.riesenacht.biotopium.core.time.DateUtils

/**
 * The blocklord biotopium instance.
 *
 * @author Manuel Riesen
 */
object Blocklord : Biotopium(blocklordConfig) {

    init {
        networkManager.registerMessageHandler(ActionReqMessage::class) { wrapper, _ ->
            val action = wrapper.message.action
            ActionCandidateBus.onNext(ActionCandidate(action))
        }

        OutgoingBlockBus.subscribe {

            // publish new blocks
            val message = BlockAddMessage(it)

            if(it.location == Stem) {
                networkManager.sendBroadcastGlobal(message)
            } else {
                networkManager.sendBroadcastRegional(message)
            }

        }

    }

    /**
     * Starts the blockchain by generating the genesis block and adding it to the blockchain.
     * //TODO fix bootstrap workaround
     */
    fun startBlockchain() {
        val stemGenesisBlock = generateGenesisBlock()
        OutgoingBlockBus.onNext(stemGenesisBlock)
        BlockchainManager.add(stemGenesisBlock)
        val chunkGenesisAction = ChunkGenesisAction(
            (0 until 8).flatMap { x -> (0 until 8).map { y -> DefaultTile(x.coord, y.coord) } }
        )
        val chunkGenesisActionRecord = ActionManager.envelope(chunkGenesisAction)
        val regionGenesisBlock = generateGenesisBlock(chunkGenesisAction.location, recordBookOf(chunkGenesisActionRecord))
        val regionGenesisRefBlock = BlockSmith.createStemBlockFor(regionGenesisBlock)
        BlockchainManager.add(regionGenesisRefBlock)
        //BlockchainManager.add(regionGenesisBlock)
        ActionCandidateBus.onNext(ActionCandidate(chunkGenesisActionRecord))
    }


    /**
     * Generates the genesis block.
     */
    private fun generateGenesisBlock(location: Locator = Stem, records: RecordBook = EmptyRecordBook): Block {

        val raw = RawBlock(
            height = 0u,
            location = location,
            timestamp = DateUtils.currentTimestamp(),
            author = Address(blocklordConfig.keyPair.publicKey),
            data = records,
            prevHash = Hash("")
        )
        val hash = BlockUtils.hash(raw)

        val hashed = raw.toHashedBlock(hash)
        return hashed
            .toBlock(BlockUtils.sign(hashed, blocklordConfig.keyPair.privateKey))
    }
}

suspend fun main() {
    LoggingConfig.setLoggingLevel(LoggingLevel.DEBUG)
    applyEffect(CoreModuleEffect, EffectProfile.DEV)
    applyEffect(BlocklordModuleEffect, EffectProfile.DEV)
    Blocklord.startP2pNode()
    Blocklord.startBlockchain()
    println("blocklord started")
    awaitCancellation()
}