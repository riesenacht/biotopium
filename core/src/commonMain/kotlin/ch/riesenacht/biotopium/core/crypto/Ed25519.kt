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

package ch.riesenacht.biotopium.core.crypto

import ch.riesenacht.biotopium.core.crypto.model.KeyPair
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.crypto.model.PublicKey
import ch.riesenacht.biotopium.core.crypto.model.Signature

/**
 * Ed25519 algorithm for cryptographic signatures.
 * Ed25519 is a public-key signature system based on EdDSA using the elliptic curve Curve25519.
 * For more information see: https://ed25519.cr.yp.to/index.html
 * Includes generation of keys, signing and verification of signatures.
 *
 * @author Manuel Riesen
 */
expect object Ed25519 {

    /**
     * Generates a key pair.
     *
     * @return Ed25519 key pair
     */
    fun generateKeyPair(): KeyPair

    /**
     * Signs a message.
     * Creates a detached signature for a message.
     *
     * @param message message to sign
     * @param privateKey private key to use for signing
     * @return detached signature
     */
    fun sign(message: String, privateKey: PrivateKey): Signature

    /**
     * Verifies a signature of a message.
     *
     * @param signature signature
     * @param message signed message
     * @param publicKey public key
     * @return the signature's validity
     */
    fun verify(signature: Signature, message: String, publicKey: PublicKey): Boolean
}