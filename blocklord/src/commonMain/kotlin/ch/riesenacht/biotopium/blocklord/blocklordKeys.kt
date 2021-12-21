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

import ch.riesenacht.biotopium.core.crypto.model.KeyPair
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.crypto.model.PublicKey

/**
 * The key pair of the blocklord.
 */
val blocklordKeys = KeyPair(
    privateKey = PrivateKey(base64="JR8g3OHi0ZD5DVFr4Np2kJNSRaivWKNTn60i2a5/5XSZavdyWMheSlAS3iTqwruF+/yV9vtIl+oynEhBo3T5pg=="),
    publicKey = PublicKey(base64="mWr3cljIXkpQEt4k6sK7hfv8lfb7SJfqMpxIQaN0+aY=")
)
