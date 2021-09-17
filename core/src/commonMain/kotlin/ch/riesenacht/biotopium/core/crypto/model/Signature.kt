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

package ch.riesenacht.biotopium.core.crypto.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a signature of a message.
 * A signature can be created for a message using a [PrivateKey].
 * The validity of a signature can be verified using the corresponding [PublicKey].
 * The signature is a so-called detached signature,
 * which means the signature itself does not contain its signed message.
 * The signature is formatted as base64 string.
 *
 * @author Manuel Riesen
 */
@Serializable
@JvmInline
value class Signature(val base64: String)