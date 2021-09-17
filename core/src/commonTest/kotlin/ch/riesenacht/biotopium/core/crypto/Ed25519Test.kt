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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test class for [Ed25519].
 *
 * @author Manuel Riesen
 */
class Ed25519Test {

    @Test
    fun testGenerateKeyPair() {
        val keyPair = Ed25519.generateKeyPair()
        assertEquals(88, keyPair.privateKey.base64.length)
        assertEquals(44, keyPair.publicKey.base64.length)
    }

    @Test
    fun testSigning_withKeyPairFromJs() {
        val keyPair = KeyPair(
            PrivateKey("epRKupUuzYFAh1t2kELoLZIq7mCGB9IHW3gYYFI751bc0z2OOiRskQqxdv126DF4o+6RHBwlDzqUVaO5a8sqRg=="),
            PublicKey("3NM9jjokbJEKsXb9dugxeKPukRwcJQ86lFWjuWvLKkY=")
        )
        val message = "Hello world!"
        val signature = Ed25519.sign(message, keyPair.privateKey)
        assertEquals(
            "7RL1szrycVqExQRTdpk/0A3sEZhBCRVK6dSOdfIgOQW4wfUrxmiE2iFkGegrTOLGWQ4nK6Nzf58zdY9r9SoHDw==",
            signature.base64
        )
    }

    @Test
    fun testSigning_withKeyPairFromJvm() {
        val keyPair = KeyPair(
            PrivateKey("F96yOkZaENPOzQs+ihhSICfpx3pga4ZvhqxyWkTN+G6pJUku+OJ+EAPvW6NvGrqfkAftgWGQC/n0DABI5F2c3g=="),
            PublicKey("qSVJLvjifhAD71ujbxq6n5AH7YFhkAv59AwASORdnN4=")
        )

        val message = "Hello world!"
        val signature = Ed25519.sign(message, keyPair.privateKey)
        assertEquals(
            "EZbzNNPppQhfuCfWz5l6HvfnPirupcdon9u0mVT4LJaJQUI+fvUOpUuW9lk4oZt6c21/ev4RsGPoRp0CNqlsCA==",
            signature.base64
        )
    }

    @Test
    fun testVerify_positive_withKeyPairFromJs() {
        val message = "Hello world!"
        val signature = Signature("7RL1szrycVqExQRTdpk/0A3sEZhBCRVK6dSOdfIgOQW4wfUrxmiE2iFkGegrTOLGWQ4nK6Nzf58zdY9r9SoHDw==")
        val publicKey = PublicKey("3NM9jjokbJEKsXb9dugxeKPukRwcJQ86lFWjuWvLKkY=")
        val verified = Ed25519.verify(signature, message, publicKey)
        assertTrue(verified)
    }

    @Test
    fun testVerify_positive_withKeyPairFromJvm() {
        val message = "Hello world!"
        val signature = Signature("EZbzNNPppQhfuCfWz5l6HvfnPirupcdon9u0mVT4LJaJQUI+fvUOpUuW9lk4oZt6c21/ev4RsGPoRp0CNqlsCA==")
        val publicKey = PublicKey("qSVJLvjifhAD71ujbxq6n5AH7YFhkAv59AwASORdnN4=")
        val verified = Ed25519.verify(signature, message, publicKey)
        assertTrue(verified)
    }

    @Test
    fun testVerify_negative_withKeyPairFromJs() {
        val message = "Hello!"
        val signature = Signature("7RL1szrycVqExQRTdpk/0A3sEZhBCRVK6dSOdfIgOQW4wfUrxmiE2iFkGegrTOLGWQ4nK6Nzf58zdY9r9SoHDw==")
        val publicKey = PublicKey("3NM9jjokbJEKsXb9dugxeKPukRwcJQ86lFWjuWvLKkY=")
        val verified = Ed25519.verify(signature, message, publicKey)
        assertFalse(verified)
    }

    @Test
    fun testVerify_negative_withKeyPairFromJvm() {
        val message = "Hello!"
        val signature = Signature("EZbzNNPppQhfuCfWz5l6HvfnPirupcdon9u0mVT4LJaJQUI+fvUOpUuW9lk4oZt6c21/ev4RsGPoRp0CNqlsCA==")
        val publicKey = PublicKey("qSVJLvjifhAD71ujbxq6n5AH7YFhkAv59AwASORdnN4=")
        val verified = Ed25519.verify(signature, message, publicKey)
        assertFalse(verified)
    }
}