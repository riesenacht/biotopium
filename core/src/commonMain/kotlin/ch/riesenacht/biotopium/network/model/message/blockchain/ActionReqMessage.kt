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

import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.frame.ActionFrame
import ch.riesenacht.biotopium.network.model.BlockchainSignal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the message sent for requesting an action.
 * The message contains the [action] to acknowledge.
 *
 * @author Manuel Riesen
 */
@Serializable
@SerialName("ActionReqMessage")
data class ActionReqMessage<T : Action>(
    val action: ActionFrame<T>
) : BlockchainMessage() {

    override val signal = BlockchainSignal.ACTION_REQ
}
