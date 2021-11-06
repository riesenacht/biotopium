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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\",\"sign\":\"D9KPN2yij7mWZVgEq3dv5Nl3yP/Az1ZA3uGGwPPsdRXoQeUEwl+v/MtKH9YaXNBJLoOarJmgpyNC94vJbGRhDQ==\",\"validSign\":\"sULtFr74J2QaviMNr/EJQM+9Xg0S0rNyOY4pXelW1W3HQw1wXKyDwQnCJl+R4BFHtxYr3XQBmoO2iGWJJMk9DA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeBlockAddMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\",\"sign\":\"D9KPN2yij7mWZVgEq3dv5Nl3yP/Az1ZA3uGGwPPsdRXoQeUEwl+v/MtKH9YaXNBJLoOarJmgpyNC94vJbGRhDQ==\",\"validSign\":\"sULtFr74J2QaviMNr/EJQM+9Xg0S0rNyOY4pXelW1W3HQw1wXKyDwQnCJl+R4BFHtxYr3XQBmoO2iGWJJMk9DA==\"}}}"

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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\",\"sign\":\"D9KPN2yij7mWZVgEq3dv5Nl3yP/Az1ZA3uGGwPPsdRXoQeUEwl+v/MtKH9YaXNBJLoOarJmgpyNC94vJbGRhDQ==\",\"validSign\":\"sULtFr74J2QaviMNr/EJQM+9Xg0S0rNyOY4pXelW1W3HQw1wXKyDwQnCJl+R4BFHtxYr3XQBmoO2iGWJJMk9DA==\"}]}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainFwdMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\",\"sign\":\"D9KPN2yij7mWZVgEq3dv5Nl3yP/Az1ZA3uGGwPPsdRXoQeUEwl+v/MtKH9YaXNBJLoOarJmgpyNC94vJbGRhDQ==\",\"validSign\":\"sULtFr74J2QaviMNr/EJQM+9Xg0S0rNyOY4pXelW1W3HQw1wXKyDwQnCJl+R4BFHtxYr3XQBmoO2iGWJJMk9DA==\"}]}}"

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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignRequestMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignRequestMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\"}}}"

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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignAckMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\",\"sign\":\"D9KPN2yij7mWZVgEq3dv5Nl3yP/Az1ZA3uGGwPPsdRXoQeUEwl+v/MtKH9YaXNBJLoOarJmgpyNC94vJbGRhDQ==\",\"validSign\":\"sULtFr74J2QaviMNr/EJQM+9Xg0S0rNyOY4pXelW1W3HQw1wXKyDwQnCJl+R4BFHtxYr3XQBmoO2iGWJJMk9DA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeSignAckMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"SignAckMessage\",\"block\":{\"height\":1,\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"validator\":\"blocklord\",\"data\":{\"class\":\"ChunkGenesisAction\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"produce\":[]},\"hash\":\"c96bb08de634afc71f687275be1eb5b71552f746e5fa31382d5f48e2da140098\",\"sign\":\"D9KPN2yij7mWZVgEq3dv5Nl3yP/Az1ZA3uGGwPPsdRXoQeUEwl+v/MtKH9YaXNBJLoOarJmgpyNC94vJbGRhDQ==\",\"validSign\":\"sULtFr74J2QaviMNr/EJQM+9Xg0S0rNyOY4pXelW1W3HQw1wXKyDwQnCJl+R4BFHtxYr3XQBmoO2iGWJJMk9DA==\"}}}"

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