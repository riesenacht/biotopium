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

package ch.riesenacht.biotopium.network.model.config

/**
 * Represents the configuration of a [P2pNode].
 * Not all configuration values can and will be used by the [P2pNode].
 * The usability of the values is dependent to the underlying platform capabilities.
 *
 * @property listenPort port to listen on (if possible on platform)
 * @property bootstrapPeers bootstrap peers to use (if possible on platform)
 * @property privateKeyBase64 private key bytes, encoded in base64 to use (if possible on platform)
 * @property topic topic to use
 *
 * @author Manuel Riesen
 */
data class P2pConfiguration(
    val listenPort: Int,
    val bootstrapPeers: List<String> = emptyList(),
    val privateKeyBase64: String? = null,
    val topic: String
)
