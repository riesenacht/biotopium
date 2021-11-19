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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\",\"sign\":\"Ad5UIZRMEsD78h8Ar43zHsXwNqRVf/wHikZR+PMx/obNUpLBDreM1Brg3OhMww7RkqHTTVWtCFTaZVwZcn4vCw==\",\"validSign\":\"3hVjQYiOONF1+TyeBgwpeU8Cn2oCEOWUA3Ys84lKbuHKph4anlLY+IDh44Nqzrfe8/uLR9Arq+yX4D2VNyv3Aw==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeBlockAddMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\",\"sign\":\"Ad5UIZRMEsD78h8Ar43zHsXwNqRVf/wHikZR+PMx/obNUpLBDreM1Brg3OhMww7RkqHTTVWtCFTaZVwZcn4vCw==\",\"validSign\":\"3hVjQYiOONF1+TyeBgwpeU8Cn2oCEOWUA3Ys84lKbuHKph4anlLY+IDh44Nqzrfe8/uLR9Arq+yX4D2VNyv3Aw==\"}}}"

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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\",\"sign\":\"Ad5UIZRMEsD78h8Ar43zHsXwNqRVf/wHikZR+PMx/obNUpLBDreM1Brg3OhMww7RkqHTTVWtCFTaZVwZcn4vCw==\",\"validSign\":\"3hVjQYiOONF1+TyeBgwpeU8Cn2oCEOWUA3Ys84lKbuHKph4anlLY+IDh44Nqzrfe8/uLR9Arq+yX4D2VNyv3Aw==\"}]}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainFwdMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\",\"sign\":\"Ad5UIZRMEsD78h8Ar43zHsXwNqRVf/wHikZR+PMx/obNUpLBDreM1Brg3OhMww7RkqHTTVWtCFTaZVwZcn4vCw==\",\"validSign\":\"3hVjQYiOONF1+TyeBgwpeU8Cn2oCEOWUA3Ys84lKbuHKph4anlLY+IDh44Nqzrfe8/uLR9Arq+yX4D2VNyv3Aw==\"}]}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            ChainFwdMessage(listOf(block))
        )

        val decoded: MessageEnvelope<ChainFwdMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    //TODO rewrite tests to match new messages
    /*@Test
    fun testEncodeSignReqMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultHashedBlock()
        val message = MessageEnvelope(
            peerId,
            ActionReqMessage(block)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignRequestMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignRequestMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultHashedBlock()
        val message = MessageEnvelope(
            peerId,
            ActionReqMessage(block)
        )

        val decoded: MessageEnvelope<ActionReqMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

    @Test
    fun testEncodeSignAckMessage() {
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            ActionAckMessage(block)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ActionAckMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\",\"sign\":\"Ad5UIZRMEsD78h8Ar43zHsXwNqRVf/wHikZR+PMx/obNUpLBDreM1Brg3OhMww7RkqHTTVWtCFTaZVwZcn4vCw==\",\"validSign\":\"3hVjQYiOONF1+TyeBgwpeU8Cn2oCEOWUA3Ys84lKbuHKph4anlLY+IDh44Nqzrfe8/uLR9Arq+yX4D2VNyv3Aw==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignAckMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ActionAckMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ActionFrame\",\"timestamp\":0,\"author\":\"me\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[]}},\"hash\":\"19e1d1e446948a99e21d9067f90c360772e86e754699eaaec9525efd03914b71\",\"sign\":\"Ad5UIZRMEsD78h8Ar43zHsXwNqRVf/wHikZR+PMx/obNUpLBDreM1Brg3OhMww7RkqHTTVWtCFTaZVwZcn4vCw==\",\"validSign\":\"3hVjQYiOONF1+TyeBgwpeU8Cn2oCEOWUA3Ys84lKbuHKph4anlLY+IDh44Nqzrfe8/uLR9Arq+yX4D2VNyv3Aw==\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val block = generateDefaultBlock()
        val message = MessageEnvelope(
            peerId,
            ActionAckMessage(block)
        )

        val decoded: MessageEnvelope<ActionAckMessage> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }*/
}