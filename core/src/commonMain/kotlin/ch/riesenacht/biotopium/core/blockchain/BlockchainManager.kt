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
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.frame.ActionFrame
import ch.riesenacht.biotopium.core.blockchain.model.Blockchain
import ch.riesenacht.biotopium.core.blockchain.model.MutableBlockchain
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.bus.IncomingActionBus

/**
 * State manager of the blockchain.
 *
 * @author Manuel Riesen
 */
object BlockchainManager {

    /**
     * The maximum height on the blockchain.
     */
    val maxHeight: ULong
    get() = blockchain.last().height

    /**
     * Mutable variant of the blockchain.
     */
    private val mutableBlockchain: MutableBlockchain = mutableListOf()

    /**
     * Validator for validating new blocks
     * as well as the current blockchain.
     */
    private val validator = BlockValidator

    /**
     * The most recent version of the blockchain.
     */
    val blockchain: Blockchain
        get() = mutableBlockchain

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

        if(validator.validateNew(block, blockchain)) {
            if(mutableBlockchain.add(block)) {

                if(block.data is ActionFrame<out Action>) {
                    // publish the action
                    IncomingActionBus.onNext(block.data)
                }

                return true
            }
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