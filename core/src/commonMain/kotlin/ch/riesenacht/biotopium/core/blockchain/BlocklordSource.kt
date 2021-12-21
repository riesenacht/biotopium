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

import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.logging.Logging
import kotlin.properties.Delegates

/**
 * Represents the provider or source of trusted blocklord addresses.
 *
 * @author Manuel Riesen
 */
object BlocklordSource {

    /**
     * The internal list of trusted blocklord addresses.
     */
    private lateinit var blocklords: List<Address>

    /**
     * The internal state of the blocklord source's trust.
     */
    private var _trusted by Delegates.notNull<Boolean>()

    private val logger = Logging.logger {  }

    /**
     * Whether the blocklord source is trusted.
     */
    val trusted: Boolean
    get() = _trusted

    /**
     * Initializes the blocklord source with a given list of trusted [blocklords].
     * The initialization can only occur once.
     * Any other invocation will have no effect on the set list of trusted blocklords.
     */
    fun init(blocklords: List<Address>, trusted: Boolean) {
        if(BlocklordSource::blocklords.isInitialized) {
            logger.debug { "blocklord source tried to set trusted blocklords after initialization" }
            return
        }
        this.blocklords = blocklords
        this._trusted = trusted
    }

    /**
     * Checks if an [address] of a possible blocklord is trusted.
     * Trusted means, the address is officially a blocklord address.
     *
     * @return whether the [address] is a trusted blocklord address
     */
    fun isTrusted(address: Address) = blocklords.contains(address)

}