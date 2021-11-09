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
import ch.riesenacht.biotopium.network.model.message.MessageWrapper
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

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val message = MessageWrapper(
            peerId,
            DebugMessage("Hello world!")
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"DebugMessage\",\"text\":\"Hello world!\"}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }


    @Test
    fun testDecodeDebugMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"DebugMessage\",\"text\":\"Hello world!\"}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val message = MessageWrapper(
            peerId,
            DebugMessage("Hello world!")
        )

        val decoded: MessageWrapper<DebugMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodePeerAddressInfoMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val message = MessageWrapper(
            peerId,
            PeerAddressInfoMessage(peerId, address)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"PeerAddressInfoMessage\",\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"address\":\"ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=\"}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }


    @Test
    fun testDecodePeerAddressInfoMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"PeerAddressInfoMessage\",\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"address\":\"ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=\"}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val message = MessageWrapper(
            peerId,
            PeerAddressInfoMessage(peerId, address)
        )

        val decoded: MessageWrapper<PeerAddressInfoMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeBlockAddMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageWrapper(
            peerId,
            BlockAddMessage(block)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\",\"sign\":\"imDE7V8SppTf3QIB/649jSYbYBfJdc/uKJ8jm4t+CqfJ36A4teWzfz6mPgyaDJwGJf8Ey/I5lba03sPT20vJDw==\",\"validSign\":\"KrLmhzzplx/kX8WHOltxHd5M+AzaWmJMP8zWgjLxXV5cL7bOHI60OFsy8clfRrCJBDh7/IrqQT2wiBc3eVhoCA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeBlockAddMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\",\"sign\":\"imDE7V8SppTf3QIB/649jSYbYBfJdc/uKJ8jm4t+CqfJ36A4teWzfz6mPgyaDJwGJf8Ey/I5lba03sPT20vJDw==\",\"validSign\":\"KrLmhzzplx/kX8WHOltxHd5M+AzaWmJMP8zWgjLxXV5cL7bOHI60OFsy8clfRrCJBDh7/IrqQT2wiBc3eVhoCA==\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageWrapper(
            peerId,
            BlockAddMessage(block)
        )

        val decoded: MessageWrapper<BlockAddMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }


    @Test
    fun testEncodeChainReqMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val message = MessageWrapper(
            peerId,
            ChainReqMessage(1u)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainRequestMessage\",\"height\":1}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainRequestMessage\",\"height\":1}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val message = MessageWrapper(
            peerId,
            ChainReqMessage(1u)
        )

        val decoded: MessageWrapper<ChainReqMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeChainFwdMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageWrapper(
            peerId,
            ChainFwdMessage(listOf(block))
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\",\"sign\":\"imDE7V8SppTf3QIB/649jSYbYBfJdc/uKJ8jm4t+CqfJ36A4teWzfz6mPgyaDJwGJf8Ey/I5lba03sPT20vJDw==\",\"validSign\":\"KrLmhzzplx/kX8WHOltxHd5M+AzaWmJMP8zWgjLxXV5cL7bOHI60OFsy8clfRrCJBDh7/IrqQT2wiBc3eVhoCA==\"}]}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainFwdMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\",\"sign\":\"imDE7V8SppTf3QIB/649jSYbYBfJdc/uKJ8jm4t+CqfJ36A4teWzfz6mPgyaDJwGJf8Ey/I5lba03sPT20vJDw==\",\"validSign\":\"KrLmhzzplx/kX8WHOltxHd5M+AzaWmJMP8zWgjLxXV5cL7bOHI60OFsy8clfRrCJBDh7/IrqQT2wiBc3eVhoCA==\"}]}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageWrapper(
            peerId,
            ChainFwdMessage(listOf(block))
        )

        val decoded: MessageWrapper<ChainFwdMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeSignReqMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultHashedBlock()
        val message = MessageWrapper(
            peerId,
            SignReqMessage(block)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignRequestMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignRequestMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultHashedBlock()
        val message = MessageWrapper(
            peerId,
            SignReqMessage(block)
        )

        val decoded: MessageWrapper<SignReqMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeSignAckMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageWrapper(
            peerId,
            SignAckMessage(block)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignAckMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\",\"sign\":\"imDE7V8SppTf3QIB/649jSYbYBfJdc/uKJ8jm4t+CqfJ36A4teWzfz6mPgyaDJwGJf8Ey/I5lba03sPT20vJDw==\",\"validSign\":\"KrLmhzzplx/kX8WHOltxHd5M+AzaWmJMP8zWgjLxXV5cL7bOHI60OFsy8clfRrCJBDh7/IrqQT2wiBc3eVhoCA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignAckMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignAckMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionEnvelope\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"6141f5079924719cd70c69afe6b1ab5b48556533eda594e6d128064307b8105f\",\"sign\":\"imDE7V8SppTf3QIB/649jSYbYBfJdc/uKJ8jm4t+CqfJ36A4teWzfz6mPgyaDJwGJf8Ey/I5lba03sPT20vJDw==\",\"validSign\":\"KrLmhzzplx/kX8WHOltxHd5M+AzaWmJMP8zWgjLxXV5cL7bOHI60OFsy8clfRrCJBDh7/IrqQT2wiBc3eVhoCA==\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageWrapper(
            peerId,
            SignAckMessage(block)
        )

        val decoded: MessageWrapper<SignAckMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }
}