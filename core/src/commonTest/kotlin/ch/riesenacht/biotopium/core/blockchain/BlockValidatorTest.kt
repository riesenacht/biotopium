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

import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.Blockchain
import ch.riesenacht.biotopium.core.blockchain.model.EmptyBlockData
import ch.riesenacht.biotopium.core.blockchain.model.Timestamp
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.crypto.Ed25519
import ch.riesenacht.biotopium.core.crypto.Sha3
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.effect.applyEffect
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test class for [BlockValidator].
 *
 * @author Manuel Riesen
 */
class BlockValidatorTest {

    private val authorKeyPair = Ed25519.generateKeyPair()
    private val validatorKeyPair = Ed25519.generateKeyPair()

    /**
     * Generates a genesis block.
     */
    private fun generateGenesisBlock(): Block {
        val raw = RawBlock(
            height = 0u,
            timestamp = Timestamp(0),
            author = Address(authorKeyPair.publicKey),
            validator = Address(validatorKeyPair.publicKey),
            data = EmptyBlockData(),
            prevHash = Hash("")
        )
        val hash = BlockUtils.hash(raw)

        val hashed = raw.toHashedBlock(hash)
        return hashed
            .toSignedBlock(BlockUtils.sign(hashed, authorKeyPair.privateKey))
            .toBlock(BlockUtils.sign(hashed, validatorKeyPair.privateKey))
    }

    private fun generateNextBlock(prev: Block): Block {
        val raw = RawBlock(
            height = prev.height + 1u,
            timestamp = Timestamp(prev.timestamp.epochMillis + 1),
            author = Address(authorKeyPair.publicKey),
            validator = Address(validatorKeyPair.publicKey),
            data = EmptyBlockData(),
            prevHash = prev.hash
        )
        val hashed = raw.toHashedBlock(BlockUtils.hash(raw))
        return hashed
            .toSignedBlock(BlockUtils.sign(hashed, authorKeyPair.privateKey))
            .toBlock(BlockUtils.sign(hashed, validatorKeyPair.privateKey))
    }

    private fun generateBlockchain(size: Int): Blockchain {
        val genesisBlock = generateGenesisBlock()
        var prev: Block = genesisBlock
        val chain = (1..size).map {
            val block = generateNextBlock(prev)
            prev = block
            block
        }.toMutableList()
        chain.add(0, genesisBlock)
        return chain
    }

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    @Test
    fun testValidateNew_genesisBlock_positive() {
        val genesisBlock = generateGenesisBlock()
        assertTrue(BlockValidator.validateNew(genesisBlock, emptyList()))
    }

    @Test
    fun testValidateNew_negative_genesisBlock_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val invalidHash = Sha3.sha256("")
        val invalidGenesisBlock = genesisBlock.copy(hash = invalidHash)
        assertFalse(BlockValidator.validateNew(invalidGenesisBlock, emptyList()))
    }

    @Test
    fun testValidateNew_negative_genesisBlock_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val invalidGenesisBlock = genesisBlock.copy(sign = BlockUtils.sign(genesisBlock, newKeyPair.privateKey))
        assertFalse(BlockValidator.validateNew(invalidGenesisBlock, emptyList()))
    }

    @Test
    fun testValidateNew_negative_genesisBlock_invalidValidSign() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val invalidGenesisBlock = genesisBlock.copy(validSign = BlockUtils.sign(genesisBlock, newKeyPair.privateKey))
        assertFalse(BlockValidator.validateNew(invalidGenesisBlock, emptyList()))
    }

    @Test
    fun testValidateNew_positive_firstBlockAfterGenesis() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock)

        val blockchain = listOf(genesisBlock)
        assertTrue(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidPrevHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(prevHash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidHeightEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(height = genesisBlock.height)

        val blockchain = listOf(genesisBlock)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }


    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidTimestampEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(timestamp = genesisBlock.timestamp)

        val blockchain = listOf(genesisBlock)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(hash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val invalidKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidSignedBlock = block.copy(sign = Ed25519.sign(block.hash.hex, invalidKeyPair.privateKey))

        val blockchain = listOf(genesisBlock)
        assertFalse(BlockValidator.validateNew(invalidSignedBlock, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidValidSign() {
        val genesisBlock = generateGenesisBlock()
        val invalidKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidValidSignedBlock = block.copy(validSign = Ed25519.sign(block.hash.hex, invalidKeyPair.privateKey))

        val blockchain = listOf(genesisBlock)
        assertFalse(BlockValidator.validateNew(invalidValidSignedBlock, blockchain))
    }

    @Test
    fun testValidateChain_positive_genesisBlock() {
        val genesisBlock = generateGenesisBlock()
        val blockchain = listOf(genesisBlock)
        assertTrue(BlockValidator.validateChain(blockchain))
    }


    @Test
    fun testValidateChain_negative_genesisBlock_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val invalidHash = Sha3.sha256("")
        val invalidGenesisBlock = genesisBlock.copy(hash = invalidHash)
        val blockchain = listOf(invalidGenesisBlock)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_genesisBlock_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val invalidGenesisBlock = genesisBlock.copy(sign = BlockUtils.sign(genesisBlock, newKeyPair.privateKey))
        val blockchain = listOf(invalidGenesisBlock)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_genesisBlock_invalidValidSign() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val invalidGenesisBlock = genesisBlock.copy(validSign = BlockUtils.sign(genesisBlock, newKeyPair.privateKey))
        val blockchain = listOf(invalidGenesisBlock)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_positive_firstBlockAfterGenesis() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock)

        val blockchain = listOf(genesisBlock, block)
        assertTrue(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidPrevHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(prevHash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock, block)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidHeightEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(height = genesisBlock.height)

        val blockchain = listOf(genesisBlock, block)
        assertFalse(BlockValidator.validateChain(blockchain))
    }


    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidTimestampEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(timestamp = genesisBlock.timestamp)

        val blockchain = listOf(genesisBlock, block)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copy(hash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock, block)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val invalidKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidSignedBlock = block.copy(sign = Ed25519.sign(block.hash.hex, invalidKeyPair.privateKey))

        val blockchain = listOf(genesisBlock, invalidSignedBlock)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidValidSign() {
        val genesisBlock = generateGenesisBlock()
        val invalidKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidValidSignedBlock = block.copy(validSign = Ed25519.sign(block.hash.hex, invalidKeyPair.privateKey))

        val blockchain = listOf(genesisBlock, invalidValidSignedBlock)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

}