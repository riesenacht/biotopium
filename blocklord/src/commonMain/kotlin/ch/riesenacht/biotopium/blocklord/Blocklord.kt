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

package ch.riesenacht.biotopium.blocklord

import ch.riesenacht.biotopium.BlocklordModuleEffect
import ch.riesenacht.biotopium.bus.ActionCandidateBus
import ch.riesenacht.biotopium.bus.OutgoingBlockBus
import ch.riesenacht.biotopium.core.Biotopium
import ch.riesenacht.biotopium.core.CoreModuleEffect
import ch.riesenacht.biotopium.core.action.model.ActionCandidate
import ch.riesenacht.biotopium.core.effect.applyEffect
import ch.riesenacht.biotopium.logging.LoggingConfig
import ch.riesenacht.biotopium.logging.LoggingLevel
import ch.riesenacht.biotopium.network.model.message.blockchain.ActionReqMessage
import ch.riesenacht.biotopium.network.model.message.blockchain.BlockAddMessage
import kotlinx.coroutines.awaitCancellation

/**
 * The blocklord biotopium instance.
 *
 * @author Manuel Riesen
 */
object Blocklord : Biotopium(blocklordP2pConfig, emptyList()) {

    init {
        networkManager.registerMessageHandler(ActionReqMessage::class) { wrapper, _ ->
            val action = wrapper.message.action
            ActionCandidateBus.onNext(ActionCandidate(action))
        }

        OutgoingBlockBus.subscribe {
            // publish new blocks
            val message = BlockAddMessage(it)
            networkManager.sendBroadcast(message)
        }
    }
}

suspend fun main() {
    LoggingConfig.setLoggingLevel(LoggingLevel.DEBUG)
    applyEffect(CoreModuleEffect)
    applyEffect(BlocklordModuleEffect)
    println("blocklord started")
    awaitCancellation()
}