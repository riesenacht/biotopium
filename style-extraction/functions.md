# Functions

Function bodies, return types, arguments, overloads, extensions, operators and DSL builders.

### 1. A function whose body is one expression uses an expression body.
- Confidence: medium (51 expression bodies, 9 block bodies with a single `return`)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:70`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/ActionRuleSet.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt:31`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/EventBus.kt:43`
- Counterexamples: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockUtils.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:97`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:76`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:134`

### 2. Expression-bodied functions usually leave out the return type.
- Confidence: low (34 without, 17 with)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/HashedBlock.kt:54`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:70`, `core/src/jvmMain/kotlin/ch/riesenacht/biotopium/core/time/DateUtils.kt:34`
- Counterexamples: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Address.kt:40`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:51`, `core/src/jvmMain/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3.kt:47`

### 3. A function that converts one data class into another calls the target constructor with named arguments, one per line, in property order.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/HashedBlock.kt:54`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/RawBlock.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RawBlockRecord.kt:50`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/record/HashedBlockRecordExt.kt:29`

```kotlin
fun toHashedRecord(hash: Hash) = HashedBlockRecord(
    timestamp = timestamp,
    author = author,
    content = content,
    hash = hash
)
```

### 4. Constructor calls with many arguments use named arguments; calls with up to three arguments are positional.
- Confidence: medium
- Evidence named: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:88`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:104`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/blocklordP2pConfig.kt:27`
- Evidence positional: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:66`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/blocklordConfig.kt:26`
- Counterexample: five positional arguments to `RawBlock` in `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/EncoderTest.kt:86`.

### 5. Arguments that skip a default or pass a flag are named at the call site.
- Confidence: high
- Evidence: `trusted = false, profile = EffectProfile.DEV` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/effect/DevBlocklordSourceInitEffect.kt:35`), `ownRealm = true` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/RealmScene.kt:95`), `nested = arrayOf(...)` (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/BlocklordModuleEffect.kt:31`), `removeOn = { ... }` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:71`)

### 6. Optional parameters have default values; nullable optional parameters default to `null`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/applyEffect.kt:28`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/config/P2pConfiguration.kt:35`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Plot.kt:40`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:45`

### 7. Overloads are used for other argument shapes: `vararg` vs `List`, one element vs a collection, a different key type, or swapped argument order.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt:31` / `:36`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:54` / `:61`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:55` / `:66`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt:57` / `:67`

### 8. Conversions from primitive numbers to wrapper types are extension properties, not functions.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:25`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/UIntExt.kt:37`

```kotlin
val Int.coordUnit: CoordinateUnit
    get() = CoordinateUnit(this)
```
Used as `DefaultTile(1.coord, 1.coord)`.

### 9. Types that behave like numbers or grids get operator functions (`plus`, `minus`, `compareTo`, `get`, `set`) instead of named methods.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:46`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:36`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/ObservableMap2D.kt:32`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMap2D.kt:33`

### 10. A value computed from state without arguments is a property with a getter, not a function.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:40`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timespan.kt:37`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:51`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:85`

### 11. A DSL is a builder class plus a top-level function that takes `init: Builder.() -> Unit`, runs it on a new builder and returns the result.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:78`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/blockRulesetDsl.kt:53`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:31`

```kotlin
fun blockRuleset(init: BlockRuleSetBuilder.() -> Unit): BlockRuleSet {
    val ruleSetBuilder = BlockRuleSetBuilder()
    ruleSetBuilder.init()
    return ruleSetBuilder.rules.toTypedArray()
}
```

### 12. A function-typed parameter comes last so callers can pass a trailing lambda.
- Confidence: medium
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:123`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:95`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:44`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:78`
- Counterexamples: lambda in the middle of `Timer(time, doAfter, plot)` (`src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:48`, call at `:79`); `block = { ... }` passed by name (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/menu/mainMenu.kt:135`).
