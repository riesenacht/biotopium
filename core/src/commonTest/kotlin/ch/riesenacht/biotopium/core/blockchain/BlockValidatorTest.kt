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

import ch.riesenacht.biotopium.TestCoreModuleEffect
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.chain.Blockchain
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.blockchain.model.chain.MutableBlockchain
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.blockchain.model.location.regionIndex
import ch.riesenacht.biotopium.core.blockchain.model.record.*
import ch.riesenacht.biotopium.core.crypto.Ed25519
import ch.riesenacht.biotopium.core.crypto.Sha3
import ch.riesenacht.biotopium.core.crypto.model.*
import ch.riesenacht.biotopium.core.effect.EffectProfile
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.core.time.model.Timestamp
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

    private val authorKeyPair = KeyPair(
        privateKey = PrivateKey("AAYWz/KEvts4DfiqhXI+aO/G+yCC27OUXxgc9bbr8ZHlszjCgLKGKqSzy9n5l3JfgHse+O90RrCWOcn3pvug7A=="),
        publicKey = PublicKey("5bM4woCyhiqks8vZ+ZdyX4B7HvjvdEawljnJ96b7oOw=")
    )

    private val zeroTimestamp: Timestamp
    get() = Timestamp(0)

    private val region: Region
    get() = Region(0.regionIndex, 0.regionIndex)

    /**
     * Creates a copy of the current block, hashes its content and signs it
     * with the [authorKeyPair]'s private key.
     */
    private fun Block.copyAndAutoSign(
        height: ULong = this.height,
        location: Locator = this.location,
        timestamp: Timestamp = this.timestamp,
        prevHash: Hash = this.prevHash,
        author: Address = this.author,
        data: RecordBook = this.data,
        hash: Hash? = null,
        sign: Signature? = null
    ): Block {
        val modified = this.copy(height, location, timestamp, prevHash, author, data)
        val hashed = modified.copy(hash = hash ?: BlockUtils.hash(modified))
        return hashed.copy(sign = sign ?: BlockUtils.sign(hashed, authorKeyPair.privateKey))
    }

    /**
     * Generates a genesis block.
     */
    private fun generateGenesisBlock(keyPair: KeyPair = authorKeyPair, location: Locator = Stem): Block {
        val raw = RawBlock(
            height = 0u,
            location = location,
            timestamp = zeroTimestamp,
            author = Address(keyPair.publicKey),
            data = EmptyRecordBook,
            prevHash = Hash("")
        )
        val hash = BlockUtils.hash(raw)

        val hashed = raw.toHashedBlock(hash)
        return hashed.toBlock(BlockUtils.sign(hashed, keyPair.privateKey))
    }


    private fun generateTestStringBlockRecord(keyPair: KeyPair = authorKeyPair): StringRecord {
        val raw = RawBlockRecord(
            timestamp = zeroTimestamp,
            author = Address(keyPair.publicKey),
            content = StringRecordContent("", region)
        )
        val hash = BlockUtils.hash(raw)

        val hashed = raw.toHashedRecord(hash)

        return StringRecord(
            timestamp = hashed.timestamp,
            author = hashed.author,
            hash = hashed.hash,
            content = hashed.content,
            sign = BlockUtils.sign(hashed, keyPair.privateKey)
        )
    }

    private fun generateNextBlock(prev: Block, keyPair: KeyPair = authorKeyPair): Block {
        val timestamp = Timestamp(prev.timestamp.epochMillis + 1)
        val raw = RawBlock(
            height = prev.height + 1u,
            location = region,
            timestamp = timestamp,
            author = Address(keyPair.publicKey),
            data = recordBookOf(generateTestStringBlockRecord(keyPair)),
            prevHash = prev.hash
        )
        val hashed = raw.toHashedBlock(BlockUtils.hash(raw))
        return hashed.toBlock(BlockUtils.sign(hashed, keyPair.privateKey))
    }

    private fun generateBlockchain(size: Int): Blockchain {
        val genesisBlock = generateGenesisBlock()
        var prev: Block = genesisBlock
        val chain = MutableBlockchain(region, (1..size).map {
            val block = generateNextBlock(prev)
            prev = block
            block
        }.toMutableList())
        chain.add(0, genesisBlock)
        return chain
    }

    @BeforeTest
    fun init() {
        applyEffect(TestCoreModuleEffect, EffectProfile.TEST)
    }

    /**
     * The implementation of an immutable blockchain.
     *
     * @property location the blockchain's location
     * @param list the list of blocks
     */
    private class ImmutableBlockchain(override val location: Locator, list: List<Block>) : Blockchain, List<Block> by list

    /**
     * Creates a blockchain out of a list of blocks.
     * The [location] can be set, defaults to [Stem].
     *
     * @return immutable blockchain
     */
    private fun List<Block>.toBlockchain(location: Locator = Stem): Blockchain {
        return ImmutableBlockchain(location, this)
    }

    @Test
    fun testValidateNew_genesisBlock_positive() {
        val genesisBlock = generateGenesisBlock()
        assertTrue(BlockValidator.validateNew(genesisBlock, emptyList<Block>().toBlockchain()))
    }

    @Test
    fun testValidateNew_negative_genesisBlock_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val invalidHash = Sha3.sha256("")
        val invalidGenesisBlock = genesisBlock.copyAndAutoSign(hash = invalidHash)
        assertFalse(BlockValidator.validateNew(invalidGenesisBlock, emptyList<Block>().toBlockchain()))
    }

    @Test
    fun testValidateNew_negative_genesisBlock_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val invalidGenesisBlock = genesisBlock.copyAndAutoSign(sign = BlockUtils.sign(genesisBlock, newKeyPair.privateKey))
        assertFalse(BlockValidator.validateNew(invalidGenesisBlock, emptyList<Block>().toBlockchain()))
    }

    @Test
    fun testValidateNew_negative_genesisBlock_invalidHeight() {
        val genesisBlock = generateGenesisBlock()
        val invalidGenesisBlock = genesisBlock.copyAndAutoSign(height = 1u)

        assertFalse(BlockValidator.validateNew(invalidGenesisBlock, emptyList<Block>().toBlockchain()))
    }

    @Test
    fun testValidateNew_positive_firstBlockAfterGenesis() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock)

        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertTrue(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidPrevHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(prevHash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidHeightEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(height = genesisBlock.height)

        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }


    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidTimestampEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(timestamp = genesisBlock.timestamp)

        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(hash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateNew(block, blockchain))
    }

    @Test
    fun testValidateNew_negative_firstBlockAfterGenesis_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val invalidKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidSignedBlock = block.copyAndAutoSign(sign = Ed25519.sign(block.hash.hex, invalidKeyPair.privateKey))

        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateNew(invalidSignedBlock, blockchain))
    }

    @Test
    fun testValidateChain_positive_genesisBlock() {
        val genesisBlock = generateGenesisBlock()
        val blockchain = listOf(genesisBlock).toBlockchain(region)
        assertTrue(BlockValidator.validateChain(blockchain))
    }


    @Test
    fun testValidateChain_negative_genesisBlock_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val invalidHash = Sha3.sha256("")
        val invalidGenesisBlock = genesisBlock.copyAndAutoSign(hash = invalidHash)
        val blockchain = listOf(invalidGenesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_genesisBlock_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val invalidGenesisBlock = genesisBlock.copyAndAutoSign(sign = BlockUtils.sign(genesisBlock, newKeyPair.privateKey))
        val blockchain = listOf(invalidGenesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_positive_firstBlockAfterGenesis() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock)

        val blockchain = listOf(genesisBlock, block).toBlockchain(region)
        assertTrue(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidPrevHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(prevHash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock, block).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidHeightEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(height = genesisBlock.height)

        val blockchain = listOf(genesisBlock, block).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }


    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidTimestampEqualLast() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(timestamp = genesisBlock.timestamp)

        val blockchain = listOf(genesisBlock, block).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock).copyAndAutoSign(hash = Sha3.sha256(""))

        val blockchain = listOf(genesisBlock, block).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidSign() {
        val genesisBlock = generateGenesisBlock()
        val invalidKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidSignedBlock = block.copyAndAutoSign(sign = Ed25519.sign(block.hash.hex, invalidKeyPair.privateKey))

        val blockchain = listOf(genesisBlock, invalidSignedBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_genesisBlockStem_dataNotEmpty() {
        val genesisBlock = generateGenesisBlock()

        // add record to genesis record book
        val invalidBlock = genesisBlock.copyAndAutoSign(data = recordBookOf(generateTestStringBlockRecord()))

        val blockchain = listOf(genesisBlock, invalidBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidDataHash() {
        val genesisBlock = generateGenesisBlock()
        val block = generateNextBlock(genesisBlock)
        val invalidBlock = block.copyAndAutoSign(data = block.data.map { data -> (data as StringRecord).copy(hash = Sha3.sha256("")) }.toList())

        val blockchain = listOf(genesisBlock, invalidBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidDataSignature() {
        val genesisBlock = generateGenesisBlock()
        val newKeyPair = Ed25519.generateKeyPair()
        val block = generateNextBlock(genesisBlock)
        val invalidBlock = block.copyAndAutoSign(data = block.data.map { data -> (data as StringRecord).copy(sign = BlockUtils.sign(data, newKeyPair.privateKey)) }.toList())

        val blockchain = listOf(genesisBlock, invalidBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_genesisBlock_invalidBlocklord() {
        val invalidBlocklord = Ed25519.generateKeyPair()
        val invalidGenesisBlock = generateGenesisBlock(invalidBlocklord)

        val blockchain = listOf(invalidGenesisBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }

    @Test
    fun testValidateChain_negative_firstBlockAfterGenesis_invalidBlocklord() {
        val invalidBlocklord = Ed25519.generateKeyPair()
        val genesisBlock = generateGenesisBlock()
        val invalidBlock = generateNextBlock(genesisBlock, invalidBlocklord)

        val blockchain = listOf(genesisBlock, invalidBlock).toBlockchain(region)
        assertFalse(BlockValidator.validateChain(blockchain))
    }
}