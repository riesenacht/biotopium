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
import com.goterl.lazysodium.LazySodiumJava
import com.goterl.lazysodium.SodiumJava
import com.goterl.lazysodium.interfaces.Sign
import java.util.*


/**
 * Ed25519 algorithm for cryptographic signatures.
 * Actual implementation for JVM platform.
 *
 * @author Manuel Riesen
 */
actual object Ed25519 {

    private val base64Encoder: Base64.Encoder = Base64.getEncoder()
    private val base64Decoder: Base64.Decoder = Base64.getDecoder()
    private val lazySodium: LazySodiumJava = LazySodiumJava(SodiumJava())

    /**
     * Generates a key pair.
     *
     * @return Ed25519 key pair
     */
    actual fun generateKeyPair(): KeyPair {
        val lzKeyPair = lazySodium.cryptoSignKeypair()
        val privateKeyBytes = lzKeyPair.secretKey.asBytes
        val publicKeyBytes = lzKeyPair.publicKey.asBytes
        val privateKey = PrivateKey(base64Encoder.encodeToString(privateKeyBytes))
        val publicKey = PublicKey(base64Encoder.encodeToString(publicKeyBytes))
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
        val messageBytes = message.encodeToByteArray()
        val privateKeyBytes = base64Decoder.decode(privateKey.base64)
        val signatureBytes = ByteArray(Sign.ED25519_BYTES)
        lazySodium.cryptoSignDetached(signatureBytes, messageBytes,
            messageBytes.size.toLong(), privateKeyBytes)
        return Signature(base64Encoder.encodeToString(signatureBytes))
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
        val messageBytes = message.encodeToByteArray()
        val publicKeyBytes = base64Decoder.decode(publicKey.base64)
        val signatureBytes = base64Decoder.decode(signature.base64)
        return lazySodium.cryptoSignVerifyDetached(signatureBytes, messageBytes,
            messageBytes.size, publicKeyBytes)
    }
}