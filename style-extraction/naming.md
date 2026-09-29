# Naming

Names of types, functions, properties, constants and parameters.
Test names are in [tests.md](tests.md). File names are in [files-and-packages.md](files-and-packages.md).

### 1. Type names end with a role suffix that says what kind of thing they are (`Manager`, `Validator`, `Utils`, `Builder`, `Impl`, `Registry`/`Registrar`, `Bus`, `Effect`, `Display`, `Exception`).
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:29`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockUtils.kt:36`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:32`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:61`

### 2. A class implementing an interface of the same base name is named `<Interface>Impl`.
- Confidence: medium
- Evidence: `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/BasicObservableImpl.kt:31`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/BasicSubjectImpl.kt:31`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMapImpl.kt:34`
- Counterexamples: `WorldStateManager : World` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:36`), `NetworkNode : NetworkContext` (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:35`)

### 3. Acronyms in own identifiers are written as words, only the first letter capitalized (`P2p`, `Id`, `Json`, `Sha3`).
- Confidence: medium
- Evidence: `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:28`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/PeerId.kt:31`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3.kt:32`
- Counterexamples: `BiotopiumUISkin` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/skin/skin.kt:63`), `MutableObservableMap2D` next to `mutableObservableMap2dOf` (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMap2D.kt:26`, `:41`)

### 4. `const val` names are lowerCamelCase, never SCREAMING_SNAKE_CASE.
- Confidence: high (11 of 11 `const val`)
- Evidence: `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/BiotopiumProtocol.kt:27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoder.kt:40`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Realm.kt:45`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:123`
- Counterexamples (non-`const` values): `private val SIZE` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/BiotopiumModule.kt:45`), `BIOTOPIUM_UI_SKIN_IMG` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/skin/skin.kt:48`), PascalCase `val EmptyRecordBook` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/EmptyRecordBook.kt:26`)

### 5. Boolean properties, parameters and locals have no `is`/`has` prefix; they are adjectives or participles.
- Confidence: high
- Evidence: `val trusted` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:47`), `var reactive` (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:44`), `ownRealm` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:45`), `countFirst` (`reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/EventRecorder.kt:45`), `val added` (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:77`)
- Note: Boolean-returning functions do use `is` (`isValid`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:78`; `isTrusted`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:70`).

### 6. Counts are named `num<Things>`.
- Confidence: high
- Evidence: `numHoes` (`src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:59`), `numTiles` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:71`), `numSlots` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:271`), `numEvents` (`reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/EventRecorder.kt:33`)

### 7. Short abbreviations are accepted in names: `prev`, `res`, `op`, `i`, `Req`, `Fwd`.
- Confidence: medium
- Evidence: `prev: Block` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/BlockRule.kt:37`), `val res` (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:66`), `val op: Operation` (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/Mutation.kt:26`), `ChainReqMessage` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/blockchain/ChainReqMessage.kt:33`), `i: Int` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:36`)
- Counterexamples: the class is `P2pConfiguration` (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/config/P2pConfiguration.kt:33`) while its variables are `p2pConfig`.

### 8. Conversion functions are named `to<Target>` and return a new instance.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/RawBlock.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/HashedBlock.kt:54`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RawBlockRecord.kt:50`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/record/HashedBlockRecordExt.kt:29`

### 9. Collection factory functions are named `<type>Of` and take `vararg` elements.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RecordBook.kt:34`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:134`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMapImpl.kt:102`

### 10. Functions that build new objects start with `create` or `generate`; both verbs are used for the same purpose.
- Confidence: low (mixed)
- Evidence `create`: `createAction` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:87`), `createBlock` (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:102`), `createIntroductionAction` (`src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:57`)
- Evidence `generate`: `generateGenesisBlock` (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:86`), `generateKeyPair` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:41`), `generateNextBlock` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:93`)

### 11. Callback parameters and properties are named `on<Event>`.
- Confidence: medium
- Evidence: `onTileClick` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:44`), `onEnd` (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/RealmClaimTutorial.kt:46`), `onNext` (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/EventBus.kt:54`), `removeOn` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:123`)
- Counterexamples: `callback` (`src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:44`), `doAfter` (`src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:50`), `applier` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:34`)

### 12. DSL entry functions are lowercase nouns (`rules`, `exec`, `blockRuleset`, `actionContract`).
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:78`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/exec/actionExecDsl.kt:27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/blockRulesetDsl.kt:53`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/actionContractDsl.kt:30`
- Counterexample in casing: `RuleSet` (`ActionRuleSet`, `BlockRuleSet`, `actionRuleSetDsl.kt`) vs `Ruleset` (`blockRuleset`, `blockRulesetDsl.kt`).

### 13. The single property of a value class is named after its encoding or unit (`hex`, `base64`, `base58`, `seconds`, `epochMillis`).
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Signature.kt:36`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/PeerId.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timespan.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:32`
- Counterexamples: `CoordinateUnit(val value)`, `Coordinate(val coordinate)`, `RealmIndex(val index)` name the property after the type.
