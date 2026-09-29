# Control flow and Kotlin idioms

`when`, `if`, scope functions, loops, collection operations and lambdas.

### 1. A `when` over an enum lists every constant and has no `else`; ignored constants get an empty branch.
- Confidence: high
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:70`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:121`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:196`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/RealmScene.kt:121`
- Counterexample: `else` branch when the subject is nullable (`src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:110`).

```kotlin
// ignore other items
ItemType.REALM_CLAIM_PAPER -> { }
ItemType.PLANT -> { }
```

### 2. `when` is used as an expression and assigned to a `val` or returned from a getter.
- Confidence: high
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:121`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:128`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:110`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:70`

### 3. A two-way choice is an `if … else` expression, often on one line.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:41`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:46`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:124`

### 4. The only scope function used is `let`, mostly as `?.let`; `apply`, `also`, `run` and `with` do not occur.
- Confidence: high (5 `let`, 0 of the others)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/growContract.kt:68`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:74`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:67`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:242`

### 5. Collections are processed with `forEach`, `map`, `all`, `find`, `groupBy`, `zipWithNext` instead of loops with accumulators; `for` loops appear for index ranges only.
- Confidence: high (21 `forEach`, 5 `for`)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:61`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/harvestContract.kt:54`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:210`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:76`
- `for` loops: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:81`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:158`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:44`

### 6. `map { … }` on a range or list is often followed by a redundant `.toList()`.
- Confidence: medium (8 occurrences; other `map` calls go without it, e.g. `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:64`)
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:63`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:305`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderCoreModelTest.kt:251`

### 7. Short single-level lambdas use `it`; nested lambdas name their parameters.
- Confidence: medium
- Evidence `it`: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:65`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/harvestContract.kt:54`
- Evidence named: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:63`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:167`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:50`
- Counterexamples: named in a short single lambda (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:64`); `it` in a 35-line lambda (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:60`).

### 8. Inside nested builder lambdas the current receiver is captured in a named `val` (`val arrow = this`).
- Confidence: high
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/menu/mainMenu.kt:78`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:149`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/highlightSlot.kt:41`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:57`

### 9. Early exit from a lambda uses a labelled return (`return@subscribe`).
- Confidence: high
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:94`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:194`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:199`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:108`

### 10. Destructuring is used only for pairs.
- Confidence: high (3 of 3)
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:95`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:181`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:44`

### 11. Function-typed values are called with `.invoke(...)` in some places and directly in others.
- Confidence: low (mixed)
- Evidence `.invoke`: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:126`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:49`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:75`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:128`
- Evidence direct call: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:127`, `src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:59`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:124`

### 12. `this.` is written for member access in some classes even where nothing is shadowed.
- Confidence: low (mixed)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:44`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:45`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:256`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:108`
- Counterexamples: same kind of access without `this.` in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/blockRulesetDsl.kt:38` and `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:96`.
