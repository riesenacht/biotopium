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
import ch.riesenacht.biotopium.core.blockchain.rule.blockRuleset
import ch.riesenacht.biotopium.core.crypto.Ed25519

/**
 * The block rules for the genesis block validation.
 * Since the genesis block is a unique and special block, not
 * all block rules can be applied to it.
 */
val genesisRules = blockRuleset {

    // The block's hash is valid
    rule { block: Block, _: Block -> block.hash == BlockUtils.hash(block) }

    // The author's signature is valid
    rule { block: Block, _: Block -> Ed25519.verify(block.sign, block.hash.hex, block.author.publicKey) }

    // The validator's signature is valid
    rule { block: Block, _: Block -> Ed25519.verify(block.validSign, block.hash.hex, block.validator.publicKey) }

    // TODO remove after standalone integrity verification of block data
    // The author of the block equals the author of the block data
    // This rule is required until the integrity of a block data can be ensured on its own
    rule { block: Block, _: Block -> block.author == block.data.author }

    // TODO remove after standalone integrity verification of block data
    // The timestamp of the block equals the timestamp of the block data
    // This rule is required until the integrity of a block data can be ensured on its own
    rule { block, _ -> block.timestamp == block.data.timestamp }
}

/**
 * The block rules for block validation.
 * Contains all [genesis block rules][genesisRules].
 */
val blockRules = blockRuleset {

    // The block's previous hash is equal to the hash of the previous block
    rule { block: Block, prev: Block -> block.prevHash == prev.hash }

    // The block's height equals the increment of the previous block's height
    rule { block: Block, prev: Block -> block.height == prev.height + 1u }

    // The block's timestamp is later in time than the previous block's timestamp
    rule { block: Block, prev: Block -> block.timestamp > prev.timestamp }

    // All genesis rules apply to all other blocks too
    include(genesisRules)
}