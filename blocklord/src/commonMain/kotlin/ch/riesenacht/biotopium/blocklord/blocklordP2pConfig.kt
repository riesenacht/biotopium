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

import ch.riesenacht.biotopium.network.BiotopiumProtocol
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration

/**
 * The peer-to-peer configuration of the blocklord.
 */
val blocklordP2pConfig = P2pConfiguration(
    topic = BiotopiumProtocol.topic,
    protocolName = BiotopiumProtocol.protocolName,
    listenPort = 5558,
    privateKeyBase64 = "CAESQKxECQQuSwd7gVEBAPt9S7GryYvdRNYSkSDElYJSPFLGT8ZBXrURWrKud91anxrBE1Ym0bWYiQJnNq7mVNHajA0="
)