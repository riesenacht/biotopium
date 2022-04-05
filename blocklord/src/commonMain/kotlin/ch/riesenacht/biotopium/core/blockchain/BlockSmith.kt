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

import ch.riesenacht.biotopium.blocklord.actionPoolThreshold
import ch.riesenacht.biotopium.blocklord.blocklordConfig
import ch.riesenacht.biotopium.bus.ActionCandidateBus
import ch.riesenacht.biotopium.bus.OutgoingBlockBus
import ch.riesenacht.biotopium.core.action.ActionManager
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.ref.ReferenceRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.recordBookOf
import ch.riesenacht.biotopium.core.blockchain.model.record.ref.toReferenceRecord
import ch.riesenacht.biotopium.core.blockchain.model.ref.RegionBlockReference
import ch.riesenacht.biotopium.core.crypto.model.Hash
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

    private const val stemDistanceThreshold = 1u

    /**
     * The action pools.
     * Contains validated actions which are not yet written on the blockchain.
     */
    private val actionPools: MutableMap<Locator, MutableList<ActionRecord<out Action>>> = mutableMapOf()

    init {
        ActionCandidateBus.subscribe {

            logger.debug { "found action candidate: ${it.candidate}" }

            // guard clause for action validation
            if(!actionManager.isValid(it.candidate)) {
                logger.debug { "action not valid: ${it.candidate}" }
                return@subscribe
            }

            // action is valid here

            val region = it.candidate.content.location as Region

            // ensure the existence of an action pool
            if(!actionPools.containsKey(region)) {
                actionPools[region] = mutableListOf()
            }
            val actionPool = actionPools[region]!!

            // add action to pool
            actionPool.add(it.candidate)
            logger.debug { "action added to pool: ${it.candidate}" }

            // guard clause for action pool size check
            if(actionPool.size < actionPoolThreshold) {
                return@subscribe
            }

            // action pool threshold is reached, a new block is created

            // create new block
            val block = createRegionalBlock(region, actionPool)

            // add the new block to the blockchain
            val added = blockchainManager.add(block)

            // clear pool
            actionPool.clear()

            // guard clause for validation check
            if(!added) {
                logger.debug { "failed to create new block: $block" }
                return@subscribe
            }

            // block has been added

            logger.debug { "new block added: $block" }

            // publish the new block
            OutgoingBlockBus.onNext(block)

            // guard clause for stem distance threshold check
            if(blockchainManager.stemDistance[region] < stemDistanceThreshold) {
                return@subscribe
            }

            // stem distance threshold reached, adding new stem block

            // create stem block
            val stemBlock = createStemBlockFor(block)

            // add the new stem block to the blockchain
            val stemAdded = blockchainManager.add(stemBlock)

            if(stemAdded) {
                logger.debug { "new stem block added: $stemBlock" }

                // publish the new stem block
                OutgoingBlockBus.onNext(stemBlock)
            }

        }
    }

    /**
     * Creates a stem block for a given [block] on a regional blockchain.
     *
     * @return stem block
     */
    fun createStemBlockFor(block: Block): Block {
        val reference = RegionBlockReference(
            height = block.height,
            hash = block.hash,
            region = block.location as Region
        )
        val record = createReferenceRecord(reference)
        return createStemBlock(record)
    }

    /**
     * Creates a reference record out of a given [reference].
     *
     * @return reference record
     */
    private fun createReferenceRecord(reference: RegionBlockReference): ReferenceRecord {
        val raw = RawBlockRecord(
            timestamp = DateUtils.currentTimestamp(),
            author = Address(keyManager.keyPair.publicKey),
            content = reference,
        )
        val hash = BlockUtils.hash(raw)
        val hashed = raw.toHashedRecord(hash)
        return hashed.toReferenceRecord(
            BlockUtils.sign(hashed, blocklordConfig.keyPair.privateKey)
        )
    }

    /**
     * Creates a new regional block with the given [region] out of the given [actions].
     * @return the new block
     */
    private fun createRegionalBlock(region: Region, actions: List<ActionRecord<out Action>>) = createBlock(region, actions)

    /**
     * Creates a new stem block with the given [references].
     * @return the new block
     */
    private fun createStemBlock(references: ReferenceRecord) = createBlock(Stem, listOf(references))

    /**
     * Creates a new block with the given [locator] out of the given [records].
     * @return the new block
     */
    private fun <T : BlockRecord<*>> createBlock(locator: Locator, records: List<T>): Block {
        val last = blockchainManager.blockchain[locator].lastOrNull()
        val raw = RawBlock(
            height = last?.height?.let { it + 1u } ?: 0u,
            location = locator,
            timestamp = DateUtils.currentTimestamp(),
            author = Address(keyManager.keyPair.publicKey),
            data = recordBookOf(records),
            prevHash = last?.hash ?: Hash("")
        )
        val hash = BlockUtils.hash(raw)

        val hashed = raw.toHashedBlock(hash)
        return hashed.toBlock(BlockUtils.sign(hashed, keyManager.keyPair.privateKey))
    }
}