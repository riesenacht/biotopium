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

package ch.riesenacht.biotopium.core.blockchain

import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.BlockRecordContent
import ch.riesenacht.biotopium.core.blockchain.model.record.StringRecord
import ch.riesenacht.biotopium.core.blockchain.model.record.StringRecordContent
import ch.riesenacht.biotopium.serialization.SerializersModuleRegistrar
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


/**
 * The effect for registering the serializers module for polymorphic types for testing.
 *
 * @author Manuel Riesen
 */
object BlockchainTestSerializersModuleEffect : SerializersModuleRegistrar(SerializersModule {

    // String record content for testing
    polymorphic(BlockRecordContent::class) {
        subclass(StringRecordContent::class)
    }

    // String record for testing
    polymorphic(BlockRecord::class) {
        subclass(StringRecord::class)
    }

})