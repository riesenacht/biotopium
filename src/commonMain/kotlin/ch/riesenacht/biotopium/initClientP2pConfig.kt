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

package ch.riesenacht.biotopium

import ch.riesenacht.biotopium.network.BiotopiumProtocol
import ch.riesenacht.biotopium.network.model.Topic
import ch.riesenacht.biotopium.network.model.TopicType
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration

/**
 * The peer-to-peer configuration of the client.
 */

val clientP2pConfig: P2pConfiguration
    get() = P2pConfiguration(
        topics = listOf(
            BiotopiumProtocol.globalTopic,

            //TODO remove hardcoded regional topic
            Topic("${BiotopiumProtocol.regionTopicPrefix}0/0", TopicType.REGIONAL)
        ),
        protocolName = BiotopiumProtocol.protocolName,
        listenPort = 5559,
        bootstrapPeers = listOf(
            "/ip4/127.0.0.1/tcp/5558/ws/p2p/12D3KooWFBmiRR8avwf28vQXFZgF5PUxag9Nd3vQM7CMBdegGdh6"
        )
    )