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

import ch.riesenacht.biotopium.bus.BlockCandidateBus
import ch.riesenacht.biotopium.bus.IncomingActionBus
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.record.ActionRecord
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.chain.BlockchainMap
import ch.riesenacht.biotopium.core.blockchain.model.chain.MutableBlockchainMap
import ch.riesenacht.biotopium.core.blockchain.model.chain.StemBlockchain
import ch.riesenacht.biotopium.core.blockchain.model.chain.findLastRef
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.logging.Logging

/**
 * State manager of the blockchain.
 *
 * @author Manuel Riesen
 */
object BlockchainManager {

    /**
     * Mutable variant of the blockchain.
     */
    private val mutableBlockchain: MutableBlockchainMap = MutableBlockchainMap()

    /**
     * Validator for validating new blocks
     * as well as the current blockchain.
     */
    private val validator = BlockValidator

    private val logger = Logging.logger { }

    /**
     * The most recent version of the blockchain.
     */
    val blockchain: BlockchainMap
        get() = mutableBlockchain

    /**
     * The stem blockchain.
     */
    val stemBlockchain: StemBlockchain
        get() = blockchain[Stem]

    /**
     * Represents a data structure providing the lookup for the stem distance.
     */
    interface StemDistanceLookup {

        /**
         * Looks up the stem distance for a [region].
         */
        operator fun get(region: Region): ULong
    }

    /**
     * The stem distance lookup.
     */
    val stemDistance = object : StemDistanceLookup {
        override operator fun get(region: Region): ULong {
            val ref = stemBlockchain.findLastRef(region)
            val maxHeight = blockchain[region].maxHeight
            return maxHeight - (ref?.height ?: 0uL)
        }
    }

    init {
        BlockCandidateBus.subscribe {
            // add all incoming blocks
            add(it.candidate)
        }
    }

    /**
     * Adds a [block] to the blockchain.
     * @return block is valid and was added
     */
    fun add(block: Block): Boolean {
        if(validator.validateNew(block, blockchain[block.location])) {
            if(mutableBlockchain[block.location].add(block)) {

                block.data.forEach {
                    if(it is ActionRecord<out Action>) {
                        // publish the action
                        IncomingActionBus.onNext(it)
                    }
                }

                return true
            }
        } else {
            logger.debug { "dropping invalid block: $block" }
        }
        return false
    }

    /**
     * Adds all [blocks] to the blockchain.
     * @return all blocks are valid and were added
     */
    fun addAll(blocks: List<Block>): Boolean {
        return blocks.all { add(it) }
    }

}