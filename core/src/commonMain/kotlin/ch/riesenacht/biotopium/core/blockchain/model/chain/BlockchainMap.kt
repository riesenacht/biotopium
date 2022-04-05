/*
 * Copyright (c) 2022 The biotopium Authors.
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

package ch.riesenacht.biotopium.core.blockchain.model.chain

import ch.riesenacht.biotopium.core.blockchain.model.location.Locator

/**
 * Represents a map of blockchains.
 *
 * @author Manuel Riesen
 */
open class BlockchainMap {

    /**
     * The underlying map of blockchains.
     */
    private val blockchains: MutableMap<Locator, MutableBlockchain> = mutableMapOf()

    /**
     * Gets the blockchain of the given [location].
     *
     * @return the blockchain of the [location]
     */
    open operator fun get(location: Locator): Blockchain {

        // ensure the existence of the blockchain
        if(!blockchains.containsKey(location)) {
            blockchains[location] = MutableBlockchain(location)
        }
        return blockchains[location]!!
    }
}