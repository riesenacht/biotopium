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

package ch.riesenacht.biotopium.network

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.network.model.PeerId
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [PeerAddressBook].
 *
 * @author Manuel Riesen
 */
class PeerAddressBookTest {

    @Test
    fun testAddPeerIdAndAddress_positive() {
        val peerAddressBook = PeerAddressBook()
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")

        peerAddressBook.add(peerId, address)

        assertEquals(address, peerAddressBook.address[peerId])
        assertEquals(peerId, peerAddressBook.peerId[address])
    }


    @Test
    fun testAddPeerIdAndAddress_positive_doublePeerId() {
        val peerAddressBook = PeerAddressBook()
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val peerId1 = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")
        val peerId2 = PeerId("QmTcmQf63aYNkUyEiZzGqqVHeNi1tjSpTqGcwYkz19iGCF")
        peerAddressBook.add(peerId1, address)
        peerAddressBook.add(peerId2, address)

        assertEquals(address, peerAddressBook.address[peerId2])
        assertEquals(peerId2, peerAddressBook.peerId[address])
        assertEquals(1, peerAddressBook.address.filter { it.value == address }.size)
    }


    @Test
    fun testAddAddressAndPeerId_positive() {
        val peerAddressBook = PeerAddressBook()
        val address = Address.fromBase64("ePkipaiMvPxnkZ/+F+jlFBvj4IWcYOQewHVSFt64uGo=")
        val peerId = PeerId("QmWPDDVPfBSrkrHjxt2wQ9JNsH4RNCQ2NkpFi9GHxTQvz9")

        peerAddressBook.add(peerId, address)

        assertEquals(address, peerAddressBook.address[peerId])
        assertEquals(peerId, peerAddressBook.peerId[address])
    }
}