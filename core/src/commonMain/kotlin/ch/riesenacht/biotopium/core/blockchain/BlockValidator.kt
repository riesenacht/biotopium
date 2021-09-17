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

import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.Blockchain

/**
 * Validator for blocks and chains of blocks.
 *
 * @author Manuel Riesen
 */
object BlockValidator {

    /**
     * Validates the genesis [block].
     * @return block is valid
     */
    private fun validateGenesis(block: Block): Boolean = genesisRules.all { it(block, block) }

    /**
     * Validates a new [block] before the block is added to the [blockchain].
     * @return block is valid
     */
    fun validateNew(block: Block, blockchain: Blockchain): Boolean {
        if(blockchain.isEmpty()) {
            return validateGenesis(block)
        }
        val prev = blockchain.last()
        return blockRules.all { it(block, prev) }
    }

    /**
     * Validates the current [blockchain].
     * @return blockchain is valid
     */
    fun validateChain(blockchain: Blockchain): Boolean {

        if(blockchain.isEmpty()) {
            return true
        }

        val genesis = blockchain.first()

        return validateGenesis(genesis) && blockchain
                .zipWithNext()
                .all { blocks ->
                blockRules.all { rule ->
                    rule(blocks.second, blocks.first)
                }
            }
    }
}