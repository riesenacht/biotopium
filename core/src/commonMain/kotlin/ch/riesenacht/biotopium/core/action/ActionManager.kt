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

package ch.riesenacht.biotopium.core.action

import ch.riesenacht.biotopium.bus.OutgoingActionBus
import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.frame.ActionFrame
import ch.riesenacht.biotopium.core.action.model.frame.toActionFrame
import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.KeyManager
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.time.DateUtils
import ch.riesenacht.biotopium.core.world.WorldStateManager

/**
 * The manager for actions.
 *
 * @author Manuel Riesen
 */
object ActionManager {

    private val keyManager: KeyManager = KeyManager

    /**
     * Envelopes an [action].
     * @return the action envelope
     */
    private fun <T : Action> envelope(action: T): ActionFrame<T> {
        val timestamp = DateUtils.currentTimestamp()
        val author = keyManager.address
        val raw = RawBlockRecord(
            timestamp,
            author,
            action
        )
        val hashed = raw.toHashedRecord(BlockUtils.hash(raw))
        return hashed.toActionFrame(BlockUtils.sign(hashed, keyManager.keyPair.privateKey))
    }

    /**
     * Checks whether a given [action] is valid.
     */
    private fun <T : Action> isValid(action: ActionFrame<T>): Boolean {
        val world = WorldStateManager
        return ActionValidator.validate(action, world)
    }

    /**
     * Creates an [action] and publishes it to the [OutgoingActionBus].
     * @return whether the [action] is valid and published
     */
    fun <T : Action> createAction(action: T): Boolean {
        val envelope = envelope(action)
        
        // check the action's validity
        if(isValid(envelope)) {
            OutgoingActionBus.onNext(envelope)
            return true
        }

        return false
    }
}