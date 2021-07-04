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
import org.khronos.webgl.Uint8Array

/**
 * Ed25519 algorithm for cryptographic signatures.
 * Actual implementation for JS platform.
 *
 * @author Manuel Riesen
 */
actual object Ed25519 {

    /**
     * Generates a key pair.
     *
     * @return Ed25519 key pair
     */
    actual fun generateKeyPair(): KeyPair {
        val naclKeyPair = nacl.sign.keyPair()
        val privateKey = util.encodeBase64(naclKeyPair.secretKey)
        val publicKey = util.encodeBase64(naclKeyPair.publicKey)
        return KeyPair(privateKey, publicKey)
    }

    /**
     * Signs a message.
     * Creates a detached signature for a message.
     *
     * @param message message to sign
     * @param privateKey private key to use for signing
     * @return detached signature
     */
    actual fun sign(message: String, privateKey: PrivateKey): Signature {
        val pkBytes = util.decodeBase64(privateKey)
        val msgBytes = util.decodeUTF8(message)
        val signature = nacl.sign.detached(msgBytes, pkBytes)
        return util.encodeBase64(signature)
    }

    /**
     * Verifies a signature of a message.
     *
     * @param signature signature
     * @param message signed message
     * @param publicKey public key
     * @return the signature's validity
     */
    actual fun verify(signature: Signature, message: String, publicKey: PublicKey): Boolean {
        val signatureBytes = util.decodeBase64(signature)
        val messageBytes = util.decodeUTF8(message)
        val publicKeyBytes = util.decodeBase64(publicKey)
        return nacl.sign.detached.verify(messageBytes, signatureBytes, publicKeyBytes)
    }
}

/**
 * External nacl variable from the tweetnacl npm package.
 */
@JsModule("tweetnacl")
@JsNonModule
private external val nacl: Nacl

/**
 * External root interface of the tweetnacl library.
 */
private external interface Nacl {

    /**
     * Access provider to the cryptographic signature functions of NaCl.
     */
    val sign: NaclSign

    /**
     * External interface for access to cryptographic signature functions.
     */
    interface NaclSign {

        /**
         * Signature key pair representation of NaCl.
         */
        interface SignKeyPair {
            val publicKey: Uint8Array
            val secretKey: Uint8Array
        }

        /**
         * Access provider to the detached signature functions of NaCl.
         */
        val detached: NaclSignDetached

        /**
         * Creates a key pair out of a seed.
         *
         * @param seed seed to use
         * @return key pair
         */
        fun keyPair(): SignKeyPair

        /**
         * Signs a message using a secret key.
         * @param message message to sign
         * @param secretKey secret key to use for signing
         * @return signature
         */
        fun detached(message: Uint8Array, secretKey: Uint8Array): Uint8Array

        /**
         * External interface for access to detached signature functions of NaCl.
         */
        interface NaclSignDetached {

            /**
             * Verifies a signature of a message.
             *
             * @param signature signature
             * @param message signed message
             * @param publicKey public key
             * @return the signature's validity
             */
            fun verify(message: Uint8Array, signature: Uint8Array, publicKey: Uint8Array): Boolean
        }

    }
}

/**
 * External nacl variable from the tweetnacl-util npm package.
 */
@JsModule("tweetnacl-util")
@JsNonModule
private external val util: NaclUtil

/**
 * External root interface of the tweetnacl-util library.
 */
private external interface NaclUtil {

    /**
     * Encodes bytes into base64.
     * @param array byte array
     * @return base64 string
     */
    fun encodeBase64(array: Uint8Array): String

    /**
     * Decodes a base64 string.
     * @param string base64 string
     * @return bytes
     */
    fun decodeBase64(string: String): Uint8Array

    /**
     * Encodes bytes into UTF-8.
     * @param array byte array
     * @return UTF-8 string
     */
    fun encodeUTF8(array: Uint8Array): String

    /**
     * Decodes a UTF-8 string.
     * @param string UTF-8 string
     * @return bytes
     */
    fun decodeUTF8(string: String): Uint8Array

}