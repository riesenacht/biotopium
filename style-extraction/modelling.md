# Modelling

Data classes, value classes, sealed hierarchies, enums, interfaces, type aliases and objects.

### 1. Immutable data holders are `data class`es whose constructor properties are all `val`.
- Confidence: high (34 data classes, no `var` in any data class constructor)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/KeyPair.kt:28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/Block.kt:49`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Harvest.kt:35`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/config/P2pConfiguration.kt:33`
- Counterexamples: `var lastGrowth` in the body of a data class (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/plant/GrowingPlant.kt:50`); plain `class` with `var amount` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/item.kt:31`).

### 2. A single primitive or object with its own meaning is wrapped in a `@JvmInline value class` with one `val`.
- Confidence: high (13 value classes)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:33`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/BlockCandidate.kt:31`

```kotlin
@JvmInline
value class Hash(val hex: String)
```

### 3. Value classes carry their own arithmetic as operators and derived values as getter properties.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:46`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timespan.kt:37`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/RealmIndex.kt:38`

### 4. Closed hierarchies are `sealed interface`s; `sealed class` is not used.
- Confidence: high (5 sealed interfaces, 0 sealed classes)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Item.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Tile.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/Action.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/AbstractBlock.kt:35`

### 5. Each subtype of a hierarchy overrides a type-tag property with its constant from a parallel enum.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Hoe.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/DefaultTile.kt:37`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/SeedAction.kt:41`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/blockchain/BlockAddMessage.kt:38`

```kotlin
data class Hoe(
    override val owner: Owner
) : Item {
    override val type = ItemType.HOE
}
```

### 6. Enums are plain lists of constants without bodies or constructor properties.
- Confidence: high (10 of 11 enums)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/ItemType.kt:29`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionType.kt:29`, `logging/src/commonMain/kotlin/ch/riesenacht/biotopium/logging/LoggingLevel.kt:26`
- Counterexample: `EffectProfile(vararg val also: EffectProfile)` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/EffectProfile.kt:28`)

### 7. Capability interfaces are named as adjectives and declare one member (or none, as a marker).
- Confidence: high
- Evidence: `Hashed` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Hashed.kt:28`), `Signed` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Signed.kt:28`), `Consumable` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/Consumable.kt:28`), `Producible` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/Producible.kt:28`), marker `Hashable` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Hashable.kt:29`)

### 8. A data class implements several small interfaces at once and overrides their properties in its constructor.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/Block.kt:57`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ClaimRealmAction.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Seed.kt:40`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RawBlockRecord.kt:44`

### 9. A read-only interface has a `Mutable…` counterpart with the same members in mutable types.
- Confidence: high
- Evidence: `World`/`MutableWorld` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/World.kt:34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/MutableWorld.kt:34`), `ObservableMap`/`MutableObservableMap` (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/ObservableMap.kt:32`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMap.kt:26`), `Blockchain`/`MutableBlockchain` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Blockchain.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/MutableBlockchain.kt:30`)

### 10. `typealias` gives a domain name to a collection or an existing type.
- Confidence: high (8 type aliases)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Blockchain.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RecordBook.kt:29`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt:26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Owner.kt:28`

### 11. A named single-method type is a `fun interface` whose method is `operator fun invoke`.
- Confidence: high (5 of 5)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionListener.kt:34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/ActionRule.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/BlockRule.kt:37`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/MessageHandler.kt:35`
- Note: unnamed callbacks use plain function types, e.g. `onTileClick: (Tile) -> Unit` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:44`).

### 12. Single-instance components (utilities, validators, encoders, managers) are `object`s.
- Confidence: high (52 object declarations)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockUtils.kt:36`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionValidator.kt:32`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:29`

### 13. Configured instances of an abstract base class are body-less `object`s that pass values or a lambda to the super constructor.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/IncomingActionBus.kt:33`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/OutgoingBlockBus.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/effect/DevBlocklordSourceInitEffect.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/CoreModuleEffect.kt:36`

```kotlin
object BlocklordComponentInitEffect : ModuleEffect({

    // trigger block smith initialization
    BlockSmith
})
```

### 14. Collection wrappers implement the collection interface by delegation (`by`) and override only the methods they change.
- Confidence: high
- Evidence: `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:33`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMapImpl.kt:36`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMap2D.kt:28`

### 15. Companion objects hold only a factory function or constants.
- Confidence: high (4 of 4 non-external companions)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Address.kt:35`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:118`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:266`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:70`
