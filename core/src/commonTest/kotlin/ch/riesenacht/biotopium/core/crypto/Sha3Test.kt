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

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [Sha3].
 *
 * @author Manuel Riesen
 */
class Sha3Test {

    @Test
    fun testSha256HashEmptyString() {
        assertEquals(
            "a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a",
            Sha3.sha256("").hex
        )
    }

    @Test
    fun testSha256Hashing() {
        assertEquals(
    "d6ea8f9a1f22e1298e5a9506bd066f23cc56001f5d36582344a628649df53ae8",
            Sha3.sha256("Hello world!").hex
        )
    }
}