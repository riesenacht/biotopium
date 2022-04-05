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

package ch.riesenacht.biotopium.core.blockchain.model

import ch.riesenacht.biotopium.core.blockchain.model.block.AbstractBlock
import ch.riesenacht.biotopium.core.blockchain.model.block.Block
import ch.riesenacht.biotopium.core.blockchain.model.block.HashedBlock
import ch.riesenacht.biotopium.core.blockchain.model.block.RawBlock
import ch.riesenacht.biotopium.core.blockchain.model.location.Locator
import ch.riesenacht.biotopium.core.blockchain.model.location.Region
import ch.riesenacht.biotopium.core.blockchain.model.location.Stem
import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecordContent
import ch.riesenacht.biotopium.core.blockchain.model.record.RawBlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.ref.ReferenceRecord
import ch.riesenacht.biotopium.core.blockchain.model.ref.RegionBlockReference
import ch.riesenacht.biotopium.serialization.SerializersModuleRegistrar
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Holds the serialization information about the blockchain data classes.
 * Registers the serializers module at the serializers module registry.
 *
 * @author Manuel Riesen
 */
object BlockchainSerializersModuleRegistrar : SerializersModuleRegistrar(SerializersModule {

    // Block class hierarchy from AbstractBlock perspective
    polymorphic(AbstractBlock::class) {
        subclass(RawBlock::class)
        subclass(HashedBlock::class)
        subclass(Block::class)
    }

    // Block class hierarchy from Hashed perspective
    polymorphic(Hashed::class) {
        subclass(HashedBlock::class)
        subclass(Block::class)
    }

    // Block class hierarchy from Signed perspective
    polymorphic(Signed::class) {
        subclass(Block::class)
    }

    // Hashable type hierarchy
    polymorphic(Hashable::class) {
        subclass(RawBlock::class)

        //TODO replace workaround for supporting serialization of a type with generics
        // see: https://github.com/Kotlin/kotlinx.serialization/issues/944
        @Suppress("UNCHECKED_CAST")
        subclass(RawBlockRecord::class, RawBlockRecord.serializer( PolymorphicSerializer(BlockRecordContent::class)) as KSerializer<RawBlockRecord<*>>)
    }

    // Locator class hierarchy
    polymorphic(Locator::class) {
        subclass(Stem::class)
        subclass(Region::class)
    }

    // Block record content hierarchy
    polymorphic(BlockRecordContent::class) {
        subclass(RegionBlockReference::class)
    }

    // Block record hierarchy
    polymorphic(BlockRecord::class) {
        subclass(ReferenceRecord::class)
    }

})