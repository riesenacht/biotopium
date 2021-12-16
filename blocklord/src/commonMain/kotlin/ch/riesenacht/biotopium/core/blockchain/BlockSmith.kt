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

package ch.riesenacht.biotopium.core.blockchain

import ch.riesenacht.biotopium.blocklord.actionPoolSize
import ch.riesenacht.biotopium.bus.ActionCandidateBus
import ch.riesenacht.biotopium.bus.OutgoingBlockBus
import ch.riesenacht.biotopium.core.action.ActionManager
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.blockchain.model.record.RecordBook
import ch.riesenacht.biotopium.core.blockchain.model.record.recordBookOf
import ch.riesenacht.biotopium.core.time.DateUtils
import ch.riesenacht.biotopium.logging.Logging

/**
 * The block smith is an autonomous factory for blocks.
 * Whenever an action candidate is published on the [ActionCandidateBus],
 * the block smith validated the action candidate before creating a new block containing the validated action record.
 * The produced new blocks are then published on the [OutgoingBlockBus].
 *
 * @author Manuel Riesen
 */
object BlockSmith {

    private val actionManager: ActionManager = ActionManager

    private val blockchainManager: BlockchainManager = BlockchainManager

    private val keyManager: KeyManager = KeyManager

    private val logger = Logging.logger { }

    /**
     * The action pool.
     * Contains validated actions which are not yet written on the blockchain.
     */
    private val actionPool: MutableList<ActionRecord<out Action>> = mutableListOf()

    init {
        ActionCandidateBus.subscribe {

            logger.debug { "found action candidate: ${it.candidate}" }

            // validate action
            if(actionManager.isValid(it.candidate)) {

                // add action to pool
                actionPool.add(it.candidate)
                logger.debug { "action added to pool: ${it.candidate}" }

                if(actionPool.size >= actionPoolSize) {

                    // create new block
                    val block = createBlock(actionPool)

                    // add the new block to the blockchain
                    val added = blockchainManager.add(block)

                    if(added) {
                        logger.debug { "new block added: $block" }

                        // publish the new block
                        OutgoingBlockBus.onNext(block)
                    } else {
                        logger.debug { "failed to create new block: $block" }
                    }

                    // clear pool
                    actionPool.clear()

                }
            } else {
                logger.debug { "action not valid: ${it.candidate}" }
            }
        }
    }

    /**
     * Creates a new block out of the given [actions].
     * @return the new block
     */
    private fun createBlock(actions: List<ActionRecord<out Action>>): Block {
        val last = blockchainManager.blockchain.last()
        val raw = RawBlock(
            height = last.height + 1u,
            timestamp = DateUtils.currentTimestamp(),
            author = Address(keyManager.keyPair.publicKey),
            data = recordBookOf(actions),
            prevHash = last.hash
        )
        val hash = BlockUtils.hash(raw)

        val hashed = raw.toHashedBlock(hash)
        return hashed.toBlock(BlockUtils.sign(hashed, keyManager.keyPair.privateKey))
    }

}