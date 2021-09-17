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

package ch.riesenacht.biotopium.serialization

import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.model.*
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.crypto.Ed25519
import ch.riesenacht.biotopium.core.crypto.model.*

/**
 * Basic encoder test, extended by all encoder test classes.
 *
 * @author Manuel Riesen
 */
abstract class EncoderTest {

    protected val authorKeyPair = KeyPair(
        privateKey = PrivateKey("iv1qW7KDjJyBkKiLUaH9cr0cgVhhWDS7f5sBd8Lyt9UYIsd9QI7eQH/CcISqjNLeZjgpekdcPVnJlzJkySQ4dw=="),
        publicKey = PublicKey("GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=")
    )

    protected val validatorKeyPair = KeyPair(
        privateKey = PrivateKey("hDoUsC5cM9eF61wHdCX0F2L/Y4l0vrDEDK0HA3hv5b69M9x3IjLsdyPuVrp+b/VYbaGL8CMvPjoXmAirxvKZOw=="),
        publicKey = PublicKey("vTPcdyIy7Hcj7la6fm/1WG2hi/AjLz46F5gIq8bymTs=")
    )

    /**
     * Generates a default block data object with a given block [data].
     */
    protected fun generateDefaultBlock(data: BlockData): Block {
        val raw = RawBlock(
            1u,
            Timestamp(1),
            Hash("prevHash"),
            Address.fromBase64("test"),
            Address.fromBase64("blocklord"),
            data
        )
        val hashed = raw.toHashedBlock(BlockUtils.hash(raw.toHashable()))
        return hashed.toSignedBlock(BlockUtils.sign(hashed, authorKeyPair.privateKey))
            .toBlock(BlockUtils.sign(hashed, validatorKeyPair.privateKey))
    }
}