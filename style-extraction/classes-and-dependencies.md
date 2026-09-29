# Classes and dependencies

Constructor shape, how dependencies are reached, `init` blocks and member order.

### 1. A primary-constructor parameter is `private val` when it is used after construction and a plain parameter when only initializers use it.
- Confidence: high
- Evidence `private val`: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:40`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/ActionRuleSet.kt:33`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:46`
- Evidence plain: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:34`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:66`, `network/src/jvmMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:34`
- Both in one constructor: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:61`

### 2. Dependencies on singletons are referenced by object name; some classes alias them as a private property typed with the object.
- Confidence: medium
- Evidence alias: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:41`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:45`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:52`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:47`
- Evidence direct: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:92`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:67`, local alias `val world = WorldStateManager` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:79`)

```kotlin
private val actionManager: ActionManager = ActionManager

private val blockchainManager: BlockchainManager = BlockchainManager
```

### 3. Subscriptions and handler registrations are set up in the `init` block.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:49`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:62`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:42`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:59`

### 4. Class members are ordered: properties, `init`, functions, companion object last.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:41`–`133`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:53`–`129`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:100`–`272`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:36`–`109`
- Counterexamples: companion object before `init` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:70`); nested class between properties and functions (`src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:48`).
- Note: private functions have no fixed place: first (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:35`), in the middle (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:76`) or last (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:102`).

### 5. A helper class used by one class only is nested in it as `private class` (`protected class` in test base classes).
- Confidence: high
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:48`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/bus/EventBusTest.kt:33`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:92`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:55`
