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

package ch.riesenacht.biotopium.core

import ch.riesenacht.biotopium.core.crypto.model.KeyPair
import ch.riesenacht.biotopium.network.model.PeerId
import ch.riesenacht.biotopium.network.model.config.P2pConfiguration

/**
 * Represents the configuration for the biotopium instance.
 *
 * @property p2pConfig peer-to-peer configuration
 * @property blocklordPeerIds peer IDs of blocklords
 * @property keyPair key pair
 *
 * @author Manuel Riesen
 */
class BiotopiumConfig(
    val p2pConfig: P2pConfiguration,
    val blocklordPeerIds: List<PeerId>,
    val keyPair: KeyPair,
)