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

package ch.riesenacht.biotopium.network.model.message.blockchain

import ch.riesenacht.biotopium.network.model.BlockchainSignal
import ch.riesenacht.biotopium.network.model.message.Message
import ch.riesenacht.biotopium.network.model.payload.MessagePayload

/**
 * Represents a message following the blockchain network protocol.
 *
 * @author Manuel Riesen
 */
abstract class BlockchainMessage<T : MessagePayload> : Message<T>() {

    /**
     * The signal of the blockchain network protocol.
     */
    abstract val signal: BlockchainSignal //TODO could be transient
}