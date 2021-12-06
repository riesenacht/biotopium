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
import ch.riesenacht.biotopium.core.action.model.ChunkGenesisAction
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.message.DebugMessage
import ch.riesenacht.biotopium.network.model.message.MessageEnvelope
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
        val message = MessageEnvelope(
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
        val message = MessageEnvelope(
            peerId,
            DebugMessage("Hello world!")
        )

        val decoded: MessageEnvelope<DebugMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodePeerAddressInfoMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val message = MessageEnvelope(
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
        val message = MessageEnvelope(
            peerId,
            PeerAddressInfoMessage(peerId, address)
        )

        val decoded: MessageEnvelope<PeerAddressInfoMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeBlockAddMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            BlockAddMessage(block)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"eadc67384a4fee1a01286bc23a62440f82616884b53893d57ad170553c070b88\",\"sign\":\"20vvOr6U6iBz3XK0ha0UUTj/dbOq0G+IbPPd7Gns/HJ5nr96xBQP4ijz2qkI2EjA3rQmNUQJMDEKsQ0srHrfDQ==\"},\"hash\":\"2460e2784a317d24e16f73d3f135466c81be9164f8e0142d512e983ea061a15a\",\"sign\":\"6j62CuIlBpKSO4QFbHUkTyFIRddgwRb/267wQoWHsfjp5/SBTN/2acwzM+81i/uBTdUPTG2DpKbtDWImrnUWDA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeBlockAddMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"eadc67384a4fee1a01286bc23a62440f82616884b53893d57ad170553c070b88\",\"sign\":\"20vvOr6U6iBz3XK0ha0UUTj/dbOq0G+IbPPd7Gns/HJ5nr96xBQP4ijz2qkI2EjA3rQmNUQJMDEKsQ0srHrfDQ==\"},\"hash\":\"2460e2784a317d24e16f73d3f135466c81be9164f8e0142d512e983ea061a15a\",\"sign\":\"6j62CuIlBpKSO4QFbHUkTyFIRddgwRb/267wQoWHsfjp5/SBTN/2acwzM+81i/uBTdUPTG2DpKbtDWImrnUWDA==\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            BlockAddMessage(block)
        )

        val decoded: MessageEnvelope<BlockAddMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }


    @Test
    fun testEncodeChainReqMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val message = MessageEnvelope(
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
        val message = MessageEnvelope(
            peerId,
            ChainReqMessage(1u)
        )

        val decoded: MessageEnvelope<ChainReqMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeChainFwdMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            ChainFwdMessage(listOf(block))
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"eadc67384a4fee1a01286bc23a62440f82616884b53893d57ad170553c070b88\",\"sign\":\"20vvOr6U6iBz3XK0ha0UUTj/dbOq0G+IbPPd7Gns/HJ5nr96xBQP4ijz2qkI2EjA3rQmNUQJMDEKsQ0srHrfDQ==\"},\"hash\":\"2460e2784a317d24e16f73d3f135466c81be9164f8e0142d512e983ea061a15a\",\"sign\":\"6j62CuIlBpKSO4QFbHUkTyFIRddgwRb/267wQoWHsfjp5/SBTN/2acwzM+81i/uBTdUPTG2DpKbtDWImrnUWDA==\"}]}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainFwdMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"eadc67384a4fee1a01286bc23a62440f82616884b53893d57ad170553c070b88\",\"sign\":\"20vvOr6U6iBz3XK0ha0UUTj/dbOq0G+IbPPd7Gns/HJ5nr96xBQP4ijz2qkI2EjA3rQmNUQJMDEKsQ0srHrfDQ==\"},\"hash\":\"2460e2784a317d24e16f73d3f135466c81be9164f8e0142d512e983ea061a15a\",\"sign\":\"6j62CuIlBpKSO4QFbHUkTyFIRddgwRb/267wQoWHsfjp5/SBTN/2acwzM+81i/uBTdUPTG2DpKbtDWImrnUWDA==\"}]}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            ChainFwdMessage(listOf(block))
        )

        val decoded: MessageEnvelope<ChainFwdMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeActionReqMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val action = createActionFrame(zeroTimestamp, defaultOwner, generateDefaultTestAction())
        val message = MessageEnvelope(
            peerId,
            ActionReqMessage(action)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ActionReqMessage\",\"action\":{\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"eadc67384a4fee1a01286bc23a62440f82616884b53893d57ad170553c070b88\",\"sign\":\"20vvOr6U6iBz3XK0ha0UUTj/dbOq0G+IbPPd7Gns/HJ5nr96xBQP4ijz2qkI2EjA3rQmNUQJMDEKsQ0srHrfDQ==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeActionReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ActionReqMessage\",\"action\":{\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]},\"hash\":\"eadc67384a4fee1a01286bc23a62440f82616884b53893d57ad170553c070b88\",\"sign\":\"20vvOr6U6iBz3XK0ha0UUTj/dbOq0G+IbPPd7Gns/HJ5nr96xBQP4ijz2qkI2EjA3rQmNUQJMDEKsQ0srHrfDQ==\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val action = createActionFrame(zeroTimestamp, defaultOwner, generateDefaultTestAction())
        val message = MessageEnvelope(
            peerId,
            ActionReqMessage(action)
        )

        val decoded: MessageEnvelope<ActionReqMessage<ChunkGenesisAction>> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

}