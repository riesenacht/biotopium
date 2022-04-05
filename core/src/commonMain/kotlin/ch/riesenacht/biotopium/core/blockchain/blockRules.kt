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
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.blockchain.rule.blockRuleset
import ch.riesenacht.biotopium.core.crypto.Ed25519

/**
 * The general block rules which apply to all type of blocks.
 */
val generalBlockRules = blockRuleset {

    // The block's hash is valid
    rule { block: Block, _: Block -> block.hash == BlockUtils.hash(block) }

    // The signature of the block author is valid
    rule { block: Block, _: Block -> Ed25519.verify(block.sign, block.hash.hex, block.author.publicKey) }

    // The hash of the block's data is valid
    rule { block: Block, _: Block ->
        block.data.all { data ->
            data.hash == BlockUtils.hash(data)
        }
    }

    // The author of the block is a blocklord
    rule { block: Block, _: Block -> BlocklordSource.isTrusted(block.author) }

    // The signature of the block data author is valid
    rule { block: Block, _: Block ->
        block.data.all { data ->
            Ed25519.verify(data.sign, data.hash.hex, data.author.publicKey)
        }
    }
}

/**
 * The block rules for the genesis block validation.
 * Since the genesis block is a unique and special block, not
 * all block rules can be applied to it.
 * Contains all [general block rules][generalBlockRules].
 */
val genesisRules = blockRuleset {

    // The content of the genesis block of the stem chain must be empty
    // (this does not apply to regional chains)
    rule { block: Block, _: Block -> block.location != Stem || block.data.isEmpty() }

    // The block's height is 0
    rule { block: Block, _: Block -> block.height == 0uL }

    // All general block rules apply to the genesis block as well
    include(generalBlockRules)
}

/**
 * The block rules for block validation.
 * Contains all [general block rules][generalBlockRules].
 */
val blockRules = blockRuleset {

    // The block's previous hash is equal to the hash of the previous block
    rule { block: Block, prev: Block -> block.prevHash == prev.hash }

    // The block's height equals the increment of the previous block's height
    rule { block: Block, prev: Block -> block.height == prev.height + 1u }

    // The block's timestamp is later in time than the previous block's timestamp
    rule { block: Block, prev: Block -> block.timestamp > prev.timestamp }

    // All general block rules apply to all blocks
    include(generalBlockRules)
}