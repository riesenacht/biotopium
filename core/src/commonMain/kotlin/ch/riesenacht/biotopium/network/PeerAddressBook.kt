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

/**
 * Represents the book for peer IDs and (crypto) addresses.
 * Contains dictionaries for translating between peer IDs and (crypto) addresses and vice versa.
 *
 * @author Manuel Riesen
 */
object PeerAddressBook {

    /**
     * Mutable version of the address to peer ID map.
     */
    private val mutablePeerId: MutableMap<Address, PeerId> = mutableMapOf()

    /**
     * Address to peer ID map.
     */
    val peerId: Map<Address, PeerId>
    get() = mutablePeerId

    /**
     * Mutable version of the peer ID to address map.
     */
    private val mutableAddress: MutableMap<PeerId, Address> = mutableMapOf()

    /**
     * Peer ID to address map.
     */
    val address: Map<PeerId, Address>
    get() = mutableAddress

    /**
     * Adds the relation between a [peerId] and an [address].
     */
    fun add(peerId: PeerId, address: Address) {
        mutableAddress[peerId] = address
        mutablePeerId[address] = peerId
    }

    /**
     * Adds the relation between an [address] and a [peerId].
     */
    fun add(address: Address, peerId: PeerId) {
        mutableAddress[peerId] = address
        mutablePeerId[address] = peerId
    }
}