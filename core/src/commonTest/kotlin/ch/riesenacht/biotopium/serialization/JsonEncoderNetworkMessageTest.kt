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

package ch.riesenacht.biotopium.serialization

import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.message.DebugMessage
import ch.riesenacht.biotopium.network.model.message.Message
import ch.riesenacht.biotopium.network.model.message.PeerAddressInfoMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [JsonEncoder], testing network message serialization.
 *
 * @author Manuel Riesen
 */
class JsonEncoderNetworkMessageTest : EncoderTest() {

    @BeforeTest
    fun init() {
        applyEffect(CoreModuleEffect)
    }

    @Test
    fun testEncodeDebugMessage() {
        val message = DebugMessage("Hello world!")

        val expected = "{\"text\":\"Hello world!\"}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }


    @Test
    fun testDecodeDebugMessage() {

        val encoded = "{\"text\":\"Hello world!\"}"

        val message = DebugMessage("Hello world!")

        val decoded: DebugMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodePeerAddressInfoMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val message = PeerAddressInfoMessage(peerId, address)

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"address\":\"ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=\"}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }


    @Test
    fun testDecodePeerAddressInfoMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"address\":\"ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=\"}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val message = PeerAddressInfoMessage(peerId, address)

        val decoded: PeerAddressInfoMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeBlockAddMessage() {
        val block = generateDefaultBlock()
        val message = BlockAddMessage(block)

        val expected = "{\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\",\"sign\":\"CkB7P3M9UL40jPhoUuCA4JCP9wgEGt4eVHkywqcX/SfvrlYoQlfpVc8UXddv0d2/1VxJQSF+5B5PCXMQO6RTAA==\",\"validSign\":\"i1CGH6yAzv1YcAZukyA9pIqRQ6R/D6VIoRaXXERJ5ds+p3mZsYOa/IwCkmKtGmtaRFioACxq7/XY4fzcYKURCA==\"}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeBlockAddMessage() {

        val encoded = "{\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\",\"sign\":\"CkB7P3M9UL40jPhoUuCA4JCP9wgEGt4eVHkywqcX/SfvrlYoQlfpVc8UXddv0d2/1VxJQSF+5B5PCXMQO6RTAA==\",\"validSign\":\"i1CGH6yAzv1YcAZukyA9pIqRQ6R/D6VIoRaXXERJ5ds+p3mZsYOa/IwCkmKtGmtaRFioACxq7/XY4fzcYKURCA==\"}}"

        val block = generateDefaultBlock()
        val message = BlockAddMessage(block)

        val decoded: BlockAddMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }


    @Test
    fun testEncodeChainReqMessage() {
        val message = ChainReqMessage(1u)

        val expected = "{\"height\":1}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainReqMessage() {

        val encoded = "{\"height\":1}"

        val message = ChainReqMessage(1u)

        val decoded: ChainReqMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeChainFwdMessage() {
        val block = generateDefaultBlock()
        val message = ChainFwdMessage(listOf(block))

        val expected = "{\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\",\"sign\":\"CkB7P3M9UL40jPhoUuCA4JCP9wgEGt4eVHkywqcX/SfvrlYoQlfpVc8UXddv0d2/1VxJQSF+5B5PCXMQO6RTAA==\",\"validSign\":\"i1CGH6yAzv1YcAZukyA9pIqRQ6R/D6VIoRaXXERJ5ds+p3mZsYOa/IwCkmKtGmtaRFioACxq7/XY4fzcYKURCA==\"}]}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainFwdMessage() {

        val encoded = "{\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\",\"sign\":\"CkB7P3M9UL40jPhoUuCA4JCP9wgEGt4eVHkywqcX/SfvrlYoQlfpVc8UXddv0d2/1VxJQSF+5B5PCXMQO6RTAA==\",\"validSign\":\"i1CGH6yAzv1YcAZukyA9pIqRQ6R/D6VIoRaXXERJ5ds+p3mZsYOa/IwCkmKtGmtaRFioACxq7/XY4fzcYKURCA==\"}]}"

        val block = generateDefaultBlock()
        val message = ChainFwdMessage(listOf(block))

        val decoded: ChainFwdMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeSignReqMessage() {
        val block = generateDefaultHashedBlock()
        val message = SignReqMessage(block)

        val expected = "{\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\"}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignReqMessage() {

        val encoded = "{\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\"}}"

        val block = generateDefaultHashedBlock()
        val message = SignReqMessage(block)

        val decoded: SignReqMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeSignAckMessage() {
        val block = generateDefaultBlock()
        val message = SignAckMessage(block)

        val expected = "{\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\",\"sign\":\"CkB7P3M9UL40jPhoUuCA4JCP9wgEGt4eVHkywqcX/SfvrlYoQlfpVc8UXddv0d2/1VxJQSF+5B5PCXMQO6RTAA==\",\"validSign\":\"i1CGH6yAzv1YcAZukyA9pIqRQ6R/D6VIoRaXXERJ5ds+p3mZsYOa/IwCkmKtGmtaRFioACxq7/XY4fzcYKURCA==\"}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignAckMessage() {

        val encoded = "{\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"1f071ca5d4853022e5ab49ca45c9c48fd74f09b8ee4dc583441999d690fff177\",\"sign\":\"CkB7P3M9UL40jPhoUuCA4JCP9wgEGt4eVHkywqcX/SfvrlYoQlfpVc8UXddv0d2/1VxJQSF+5B5PCXMQO6RTAA==\",\"validSign\":\"i1CGH6yAzv1YcAZukyA9pIqRQ6R/D6VIoRaXXERJ5ds+p3mZsYOa/IwCkmKtGmtaRFioACxq7/XY4fzcYKURCA==\"}}"

        val block = generateDefaultBlock()
        val message = SignAckMessage(block)

        val decoded: SignAckMessage = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }
}