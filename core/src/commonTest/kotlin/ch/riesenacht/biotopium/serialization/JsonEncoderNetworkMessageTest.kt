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

import ch.riesenacht.biotopium.TestCoreModuleEffect
import ch.riesenacht.biotopium.core.action.model.ChunkGenesisAction
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.effect.EffectProfile
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.message.DebugMessage
import ch.riesenacht.biotopium.network.model.message.MessageEnvelope
import ch.riesenacht.biotopium.network.model.message.PeerAddressInfoMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ActionReqMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainFwdMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.ChainReqMessage
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
        applyEffect(TestCoreModuleEffect, EffectProfile.TEST)
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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":0,\"y\":0}]},\"hash\":\"fc4fbbb1f3902060454c48c576886d556da7f92f144165417e65158cb94677de\",\"sign\":\"3WsIg1gDubmkNamNZ3UN7HJae71ZdIGRAVM5rEt1P5lI7AqYChGI0dGs8SUBap3zZqnrdbxdsG+3bGGc4H3ACA==\"}],\"hash\":\"cc2eef2a1c5f30c78ac32e9bd0e38f7f788042d45d2de973c05a39d6bed82434\",\"sign\":\"bjhkTBiJjPhUtstXkKEl2oaW3y7RnNYgdquKQ7jydHuLlZecoWNzxgPARSdf8rv8BZeh3ho2c8BEFFd9WKssAA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeBlockAddMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"BlockAddMessage\",\"block\":{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":0,\"y\":0}]},\"hash\":\"fc4fbbb1f3902060454c48c576886d556da7f92f144165417e65158cb94677de\",\"sign\":\"3WsIg1gDubmkNamNZ3UN7HJae71ZdIGRAVM5rEt1P5lI7AqYChGI0dGs8SUBap3zZqnrdbxdsG+3bGGc4H3ACA==\"}],\"hash\":\"cc2eef2a1c5f30c78ac32e9bd0e38f7f788042d45d2de973c05a39d6bed82434\",\"sign\":\"bjhkTBiJjPhUtstXkKEl2oaW3y7RnNYgdquKQ7jydHuLlZecoWNzxgPARSdf8rv8BZeh3ho2c8BEFFd9WKssAA==\"}}}"

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
            ChainReqMessage(1u, Stem)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainRequestMessage\",\"height\":1,\"location\":{\"class\":\"Stem\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainRequestMessage\",\"height\":1,\"location\":{\"class\":\"Stem\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val message = MessageEnvelope(
            peerId,
            ChainReqMessage(1u, Stem)
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

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":0,\"y\":0}]},\"hash\":\"fc4fbbb1f3902060454c48c576886d556da7f92f144165417e65158cb94677de\",\"sign\":\"3WsIg1gDubmkNamNZ3UN7HJae71ZdIGRAVM5rEt1P5lI7AqYChGI0dGs8SUBap3zZqnrdbxdsG+3bGGc4H3ACA==\"}],\"hash\":\"cc2eef2a1c5f30c78ac32e9bd0e38f7f788042d45d2de973c05a39d6bed82434\",\"sign\":\"bjhkTBiJjPhUtstXkKEl2oaW3y7RnNYgdquKQ7jydHuLlZecoWNzxgPARSdf8rv8BZeh3ho2c8BEFFd9WKssAA==\"}]}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeChainFwdMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ChainForwardMessage\",\"blocks\":[{\"height\":1,\"location\":{\"class\":\"Region\",\"rx\":0,\"ry\":0},\"timestamp\":1,\"prevHash\":\"prevHash\",\"author\":\"test\",\"data\":[{\"class\":\"ActionRecord\",\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":0,\"y\":0}]},\"hash\":\"fc4fbbb1f3902060454c48c576886d556da7f92f144165417e65158cb94677de\",\"sign\":\"3WsIg1gDubmkNamNZ3UN7HJae71ZdIGRAVM5rEt1P5lI7AqYChGI0dGs8SUBap3zZqnrdbxdsG+3bGGc4H3ACA==\"}],\"hash\":\"cc2eef2a1c5f30c78ac32e9bd0e38f7f788042d45d2de973c05a39d6bed82434\",\"sign\":\"bjhkTBiJjPhUtstXkKEl2oaW3y7RnNYgdquKQ7jydHuLlZecoWNzxgPARSdf8rv8BZeh3ho2c8BEFFd9WKssAA==\"}]}}"

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
        val action = createActionRecord(zeroTimestamp, defaultOwner, generateDefaultTestAction())
        val message = MessageEnvelope(
            peerId,
            ActionReqMessage(action)
        )

        val expected = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ActionReqMessage\",\"action\":{\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":0,\"y\":0}]},\"hash\":\"fc4fbbb1f3902060454c48c576886d556da7f92f144165417e65158cb94677de\",\"sign\":\"3WsIg1gDubmkNamNZ3UN7HJae71ZdIGRAVM5rEt1P5lI7AqYChGI0dGs8SUBap3zZqnrdbxdsG+3bGGc4H3ACA==\"}}}"

        val encoded = JsonEncoder.encode(message)

        assertEquals(expected, encoded)
    }

    @Test
    fun testDecodeActionReqMessage() {

        val encoded = "{\"peerId\":\"QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9\",\"message\":{\"class\":\"ActionReqMessage\",\"action\":{\"timestamp\":0,\"author\":\"GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=\",\"content\":{\"class\":\"ChunkGenesisAction\",\"produce\":[{\"class\":\"DefaultTile\",\"x\":0,\"y\":0}]},\"hash\":\"fc4fbbb1f3902060454c48c576886d556da7f92f144165417e65158cb94677de\",\"sign\":\"3WsIg1gDubmkNamNZ3UN7HJae71ZdIGRAVM5rEt1P5lI7AqYChGI0dGs8SUBap3zZqnrdbxdsG+3bGGc4H3ACA==\"}}}"

        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val action = createActionRecord(zeroTimestamp, defaultOwner, generateDefaultTestAction())
        val message = MessageEnvelope(
            peerId,
            ActionReqMessage(action)
        )

        val decoded: MessageEnvelope<ActionReqMessage<ChunkGenesisAction>> = JsonEncoder.decode(encoded)

        assertEquals(message, decoded)
    }

}