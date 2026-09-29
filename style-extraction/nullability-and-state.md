# Nullability and mutable state

`val`/`var`, exposing mutable state, nullable handling and collection types.

### 1. `val` is the default; `var` appears only for state that really changes.
- Confidence: high (1347 `val`, 35 `var`, comments and strings excluded)
- Evidence of the `var` cases: `lateinit var keyPair` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:34`), `var stack` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:65`), `var startOption` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/GameConfig.kt:31`)

### 2. Mutable state is held in a private `mutable<Name>` property and exposed as a read-only type through a getter.
- Confidence: high for the pattern, medium for the name
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:41` / `:47`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt:35` / `:40`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/Player.kt:41` / `:46`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContractManager.kt:33` / `:38`
- Counterexamples in naming: `_trusted` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:40` / `:47`), `_selected` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:57` / `:62`)

```kotlin
private val mutablePeerId: MutableMap<Address, PeerId> = mutableMapOf()

val peerId: Map<Address, PeerId>
get() = mutablePeerId
```

### 3. The private backing property is declared directly above its public view.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:53` / `:59`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt:46` / `:51`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:57` / `:62`
- Counterexample: other properties in between in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:46` / `:59`.

### 4. A property initialized with an empty mutable collection declares its type on the property, not as type arguments of the factory.
- Confidence: high in main code, tests differ
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:45`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:37`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/SerializersModuleRegistry.kt:32`, `src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:38`
- Counterexamples: `mutableObservableMap2dOf<Coordinate, Coordinate, Tile>()` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:68`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:94`), local `mutableListOf<Slot>()` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:146`)

### 5. Public properties expose read-only collection types; `MutableList`/`MutableMap` stay private.
- Confidence: medium
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContractManager.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:59`
- Counterexamples: public `val rules: MutableList<BlockRule>` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/blockRulesetDsl.kt:31`), public `val list: MutableList<Any>` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoder.kt:53`)

### 6. A map lookup whose key is known to exist is dereferenced with `!!`.
- Confidence: high (40 `!!` in total; `requireNotNull` is not used)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/claimRealmContract.kt:57`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/harvestContract.kt:81`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:59`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:99`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:101`

### 7. A nullable Boolean chain is closed with `?: false`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/claimRealmContract.kt:46`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/createPlotContract.kt:45`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/growContract.kt:68`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionValidator.kt:40`

```kotlin
claimPaper.owner == origin.author
        && world.players[claimPaper.owner]?.items?.contains(claimPaper) ?: false
```

### 8. Null and invalid cases exit early with a guard (`if (x == null) return`, `?: return@label`) instead of wrapping the rest in an `if`.
- Confidence: high
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:108`, `src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:76`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:94`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:194`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:45`
- Counterexample: nested `if` pyramid in `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/RealmScene.kt:107`.

### 9. A listener that removes itself is stored in a nullable `var` that is declared as `null` first and assigned in the next statement.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:124`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:46`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/bootstrap/BootstrapScene.kt:63`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:53`

```kotlin
var handler: MessageHandler<ChainFwdMessage>? = null
handler = MessageHandler { _, _ ->
    this.networkManager.removeMessageHandler(ChainFwdMessage::class, handler!!)
    callback.invoke()
}
```

### 10. Values set after construction are `lateinit var`; values computed once on first use are `by lazy`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:35`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:39`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:40`
