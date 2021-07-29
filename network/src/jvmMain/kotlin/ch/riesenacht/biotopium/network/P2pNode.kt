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

import ch.riesenacht.biotopium.network.go2p.GoP2p
import ch.riesenacht.biotopium.network.model.SerializedMessage

/**
 * Represents a peer-to-peer node.
 * Implementation for JVM target.
 *
 * @author Manuel Riesen
 */
actual class P2pNode : NetworkNode() {

    private val gop2p = GoP2p()

    override suspend fun start() {
        gop2p.start()
        //TODO call is blocking
        listen()
    }

    override suspend fun stop() {
        gop2p.stop()
    }

    override fun sendSerialized(message: SerializedMessage) {
        gop2p.send(message)
    }

    private fun listen() {
        val serialized = gop2p.listenBlocking()
        receive(serialized)
        listen()
    }
}