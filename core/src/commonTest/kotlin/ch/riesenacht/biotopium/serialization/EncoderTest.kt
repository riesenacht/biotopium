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

import ch.riesenacht.biotopium.core.action.model.Action
import ch.riesenacht.biotopium.core.action.model.ActionFrame
import ch.riesenacht.biotopium.core.action.model.ChunkGenesisAction
import ch.riesenacht.biotopium.core.blockchain.BlockUtils
import ch.riesenacht.biotopium.core.blockchain.model.Address
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.HashedBlock
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.crypto.model.Hash
import ch.riesenacht.biotopium.core.crypto.model.KeyPair
import ch.riesenacht.biotopium.core.crypto.model.PrivateKey
import ch.riesenacht.biotopium.core.crypto.model.PublicKey
import ch.riesenacht.biotopium.core.time.model.Timestamp
import ch.riesenacht.biotopium.core.world.model.Owner

/**
 * Basic encoder test, extended by all encoder test classes.
 *
 * @author Manuel Riesen
 */
abstract class EncoderTest {

    private val authorKeyPair = KeyPair(
        privateKey = PrivateKey("iv1qW7KDjJyBkKiLUaH9cr0cgVhhWDS7f5sBd8Lyt9UYIsd9QI7eQH/CcISqjNLeZjgpekdcPVnJlzJkySQ4dw=="),
        publicKey = PublicKey("GCLHfUCO3kB/wnCEqozS3mY4KXpHXD1ZyZcyZMkkOHc=")
    )

    private val validatorKeyPair = KeyPair(
        privateKey = PrivateKey("hDoUsC5cM9eF61wHdCX0F2L/Y4l0vrDEDK0HA3hv5b69M9x3IjLsdyPuVrp+b/VYbaGL8CMvPjoXmAirxvKZOw=="),
        publicKey = PublicKey("vTPcdyIy7Hcj7la6fm/1WG2hi/AjLz46F5gIq8bymTs=")
    )

    /**
     * A timestamp with value 0.
     */
    protected val zeroTimestamp: Timestamp
    get() = Timestamp(0)

    /**
     * The default owner.
     */
    protected val defaultOwner: Owner
    get() = Owner.fromBase64("me")

    /**
     * Generates a default test action.
     */
    private fun generateDefaultTestAction() = ChunkGenesisAction(emptyList())

    /**
     * Generates a default hashed block data object with a given [action].
     * If no [action] is given, the default test action is used.
     */
    protected fun generateDefaultHashedBlock(action: Action = generateDefaultTestAction()): HashedBlock {
        val raw = RawBlock(
            1u,
            Timestamp(1),
            Hash("prevHash"),
            Address.fromBase64("test"),
            Address.fromBase64("blocklord"),
            ActionFrame(zeroTimestamp, defaultOwner, action)
        )
        return raw.toHashedBlock(BlockUtils.hash(raw.toHashable()))
    }

    /**
     * Generates a default block data object with a given [action].
     * If [action] is given, the default test action is used.
     */
    protected fun generateDefaultBlock(action: Action = generateDefaultTestAction()): Block {
        val hashed = generateDefaultHashedBlock(action)
        return hashed.toSignedBlock(BlockUtils.sign(hashed, authorKeyPair.privateKey))
            .toBlock(BlockUtils.sign(hashed, validatorKeyPair.privateKey))
    }

}