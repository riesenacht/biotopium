# Comments and KDoc

All rules about KDoc, inline comments and comment markers. No other file covers comments.

## Coverage

Counted over all 239 `.kt` files. "With" means a `/** … */` block directly above the declaration (annotations in between allowed).

| Declaration (main code)                                   | With KDoc | Share  |
|-----------------------------------------------------------|-----------|--------|
| public class / interface / typealias                      | 138 / 141 | ≈ 98 % |
| public object (companion objects are the misses)          | 49 / 54   | ≈ 91 % |
| public function (external JS members excluded)            | 134 / 140 | ≈ 96 % |
| public member property (constructor properties excluded)  | ≈ 86 / 100| ≈ 86 % |
| private function                                          | 19 / 23   | ≈ 83 % |
| private property                                          | 23 / 77   | ≈ 30 % |
| `override` function                                       | 11 / 42   | ≈ 26 % |
| `override` property                                       | 3 / 83    | ≈ 4 %  |

| Declaration (test code)       | With KDoc | Share  |
|-------------------------------|-----------|--------|
| test class                    | 24 / 25   | ≈ 96 % |
| `@Test` function              | 0 / 154   | 0 %    |
| fixture helper function       | 4 / 15    | ≈ 27 % |

Share of public declarations with KDoc overall: about 90–95 %.

## Which declarations have KDoc

### 1. Every public class, interface, object, enum and typealias has KDoc.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:24`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Hashable.kt:21`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Owner.kt:23`
- Counterexamples: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/bootstrap/BootstrapScene.kt:47`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/menu/MainMenuScene.kt:25`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoder.kt:45`, all companion objects (e.g. `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Address.kt:35`).

### 2. Every public function has KDoc, including top-level and extension functions.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt:28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:21`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/TextExt.kt:24`
- Counterexamples: `createGrowAction` (`src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:106`), `Timestamp` operators (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:34`), both `main` functions (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:103`, `src/commonMain/kotlin/main.kt:23`).

### 3. Every non-`override` public property declared in a class or interface body gets its own KDoc, even when trivial; constructor properties do not (they are covered in the class KDoc, see rule 17).
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/AbstractBlock.kt:37`
- Counterexamples: `val encoder` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoder.kt:42`), nested colour values (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/colors.kt:79`), members of `Tile` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Tile.kt:31`).

### 4. Private backing properties and private helper functions have KDoc; private loggers and private singleton aliases do not.
- Confidence: high
- Evidence with KDoc: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:32`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:98`
- Evidence without: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:41`, `:43`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:45`

### 5. `override` members usually have no KDoc; when they have one, it repeats the text of the overridden declaration.
- Confidence: high
- Evidence without: `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:65`, `network/src/jvmMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:52`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Hoe.kt:38`
- Evidence repeated: `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/BasicSubjectImpl.kt:36` (= `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/BasicSubject.kt:30`), `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:56` (= `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkContext.kt:36`)

### 6. `actual` declarations copy the member KDoc of the `expect` declaration verbatim; the type KDoc keeps the first sentence and adds "Actual implementation for <platform> platform."
- Confidence: high
- Evidence: `core/src/jsMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:27`, `core/src/jvmMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:31`, `core/src/jvmMain/kotlin/ch/riesenacht/biotopium/core/time/DateUtils.kt:23`
- Counterexamples in wording: "Implementation for JS target." (`network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:32`), "…, implementation for JS platform." (`logging/src/jsMain/kotlin/ch/riesenacht/biotopium/logging/LoggingConfig.kt:25`)

### 7. Test classes have KDoc "Test class for [Subject]."; `@Test` functions never have KDoc.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3Test.kt:25`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/network/PeerAddressBookTest.kt:27`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/bus/EventBusTest.kt:27`
- Counterexamples: no KDoc on `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:48`; "Test class for [EncoderTest], testing core model serialization." names the wrong subject (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderCoreModelTest.kt:40`).

### 8. Enum constants have KDoc in some enums and none in others.
- Confidence: low (4 enums with, 7 without)
- Evidence with: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/EffectProfile.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/BlockchainSignal.kt:28`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/Operation.kt:28`
- Evidence without: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/ItemType.kt:29`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionType.kt:29`, `logging/src/commonMain/kotlin/ch/riesenacht/biotopium/logging/LoggingLevel.kt:26`

## `@author`

### 9. The KDoc of every top-level type ends with `@author Manuel Riesen`, separated from the rest by a blank ` *` line.
- Confidence: high (195 of 211 top-level type KDocs; 196 of 198 `@author` lines follow a blank line)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:37`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/EventBus.kt:31`
- Counterexamples (no `@author`): `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/IntroductionGift.kt:24`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/DefaultTile.kt:25`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:40`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:25`. A second author is added as another `@author` line (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:58`).

### 10. Top-level functions and properties never carry `@author`.
- Confidence: high (69 of 70)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt:28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:25`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/claimRealmContract.kt:26`
- Counterexample: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/EmptyRecordBook.kt:24`

## Length and layout

### 11. A KDoc description is one line (70 %), sometimes two (23 %), rarely three or more (8 %).
- Confidence: high (368 / 119 / 41 of 528 blocks)
- Evidence one line: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Owner.kt:24`; two lines: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Plot.kt:27`; long: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/Block.kt:32`

### 12. Each sentence starts on its own line; a long sentence is wrapped after a comma. Separate paragraphs are rare.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Signature.kt:25`–`30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:25`–`26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Hashable.kt:22`–`25`
- Only paragraph break found: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:25`

## Wording

### 13. Descriptions are full sentences: capital letter, final period, present tense, active voice.
- Confidence: high (≈ 95 % of description lines end with a period)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/Player.kt:28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:70`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timespan.kt:35`
- Counterexamples (fragment, no period): "The element's owner" (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Element.kt:30`), "The type of item" (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Item.kt:33`), "The toolbar's slots" (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:98`)

### 14. Types start with "Represents a/an/the …"; a second sentence defines the concept or a constraint.
- Confidence: high (115 of 528 blocks start with "Represents")
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Plot.kt:27`–`28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Item.kt:25`–`26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Tile.kt:24`–`26`
- Other openings for components: "The manager for actions." (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:35`), "Validator for actions." (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionValidator.kt:28`), "Implementation of [BasicObservable]." (`reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/BasicObservableImpl.kt:25`)

### 15. Functions start with a third-person verb ("Creates", "Adds", "Sends", "Registers", "Validates"); properties start with "The …"; Boolean properties start with "Whether …".
- Confidence: high
- Evidence function: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:70`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:102`
- Evidence property: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/AbstractBlock.kt:38`
- Evidence Boolean: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:45`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:41`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:60`

### 16. KDoc says what a declaration is or does; "why" appears as a short extra sentence for surprising choices, and "how" only as "Internally, … is used to achieve this."
- Confidence: medium
- Evidence why: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/applyEffect.kt:23`–`26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3.kt:26`–`27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/effect/DevBlocklordSourceInitEffect.kt:27`
- Evidence how: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/AbstractBlock.kt:57`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/AbstractBlockRecord.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RawBlockRecord.kt:59`
- External sources are plain URLs after "For more information see:" (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:29`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3.kt:28`).

## Tags and links

### 17. Constructor properties are documented with `@property` in the class KDoc, or linked as `[name]` in its description.
- Confidence: medium
- Evidence `@property`: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/KeyPair.kt:23`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/item/Harvest.kt:28`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/config/P2pConfiguration.kt:26`
- Evidence links: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Realm.kt:29`–`30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/Block.kt:33`–`39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:26`
- Counterexamples: `@param` for constructor properties (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/HarvestAction.kt:29`); `@property` and `@param` mixed (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:28`–`29`).

### 18. Function parameters are mostly referenced as `[name]` links inside the sentence instead of `@param` tags.
- Confidence: medium (≈ 100 function KDocs with inline links; `@param` in 63 lines, mainly crypto, JS externals and GUI)
- Evidence inline: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/Player.kt:50`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:109`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:32`
- Evidence `@param`: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:47`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:32`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:90`

### 19. Tag text (`@param`, `@property`, `@return`, `@throws`) is a lowercase fragment without a final period.
- Confidence: high
- Evidence: `@return the action envelope` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:61`), `@param message message to sign` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:47`), `@throws NegativeCoordinateException operation resulted in a negative coordinate value` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:43`)
- Counterexamples: `@return serialized value.` (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:49`), `@param countFirst whether the first event is counted as well.` (`reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/EventRecorder.kt:43`)

### 20. `@return` of a Boolean function states the condition ("whether …" or "block is valid").
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/BlockRule.kt:35`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:33`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:85`

### 21. `@see` lists related types of the same hierarchy, one per line, between the tags and `@author`.
- Confidence: high (19 `@see`)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/Block.kt:41`–`43`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/HashedBlock.kt:33`–`35`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/ObservableList.kt:27`–`28`

### 22. In type KDoc, the tag groups are separated by blank lines (description, tags, `@see`, `@author`); in function KDoc `@return`/`@throws` often follow the description directly.
- Confidence: medium (`@throws` 6 of 6 directly after the text; `@return` 10 directly, 7 after a blank line)
- Evidence type: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ClaimRealmAction.kt:26`–`33`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/HashedBlockRecord.kt:26`–`34`
- Evidence function: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:70`–`71`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:42`–`43`
- Counterexamples: `@property` directly after the text (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/KeyPair.kt:23`); `@return` after a blank line (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:39`).

### 23. Links use `[Name]` for types and parameters and `[text][target]` when the sentence needs other words; code values are in backticks.
- Confidence: high
- Evidence `[text][target]`: `[action type][actionType]` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:109`), `[author's signature][sign]` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/HashedBlock.kt:52`), `[coordinates][Coordinate]` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/NegativeCoordinateException.kt:23`)
- Evidence backticks: `` `true` `` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:121`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:41`, `:48`)

## Inline comments

### 24. Inline comments are rare (87 in ≈ 14 600 lines) and appear where a block holds several rules or several steps.
- Confidence: high
- Evidence: rule files `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/seedContract.kt:38`–`95`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:30`–`84`; step sequence `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:64`–`88`

### 25. An inline comment sits on its own line above the code it describes, starts with `// `, is a lowercase fragment of 2–12 words and has no final period.
- Confidence: medium
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/createPlotContract.kt:60`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:166`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:44`
- Counterexamples: capitalized sentences in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:30`–`84` (11 comments) and capitalized labels in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/WorldSerializersModuleRegistrar.kt:38`; no space after `//` in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:91`; trailing comment in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/blockchain/BlockchainMessage.kt:34`.

### 26. Inline comments are of three kinds: the condition a rule checks (declarative, present tense), the step the next statement performs (imperative), or a label for a group of registrations.
- Confidence: high
- Evidence condition: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/growContract.kt:37`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/harvestContract.kt:42`
- Evidence step: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:88`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/claimRealmContract.kt:56`
- Evidence label: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/BlockchainSerializersModuleRegistrar.kt:42`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainTestSerializersModuleEffect.kt:38`

### 27. A comment that does not fit one line continues on the next line with another `//`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/claimRealmContract.kt:40`–`41`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/seedContract.kt:75`–`76`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionSerializersModuleRegistrar.kt:57`–`58`

### 28. Long test classes are split into sections with a block comment `/* Tests for [Type]. */`.
- Confidence: medium (7 occurrences, all in one file)
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:107`, `:124`, `:203`

## Markers

### 29. TODOs are written `//TODO <lowercase text>`: no space after `//`, no colon, no name, no ticket; a reference follows on the next line as `// see: <url>`.
- Confidence: high (6 TODOs, 0 FIXME/HACK/XXX)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:52`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/BiotopiumModule.kt:57`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionSerializersModuleRegistrar.kt:57`–`58`

### 30. Every statement-level `@Suppress("UNCHECKED_CAST")` is preceded by `//UNCHECKED cast in order to <purpose>` or a TODO.
- Confidence: high (6 of 6)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionValidator.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContractManager.kt:64`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:125`
- Other marker: `//NOTE: do not cast explicitly to Stream` (`network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:97`, single occurrence).

## Never commented

- `@Test` functions (0 of 154).
- `init` blocks, `override val type = …` tags, logger properties, companion objects, local functions.
- Arrange/act/assert sections in tests.
- Algorithms or "how it works" inside function bodies.
- Commented-out code is rare but present: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:151`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:52`–`55`.

## Representative KDoc excerpts

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:24`–`32`
```kotlin
/**
 * Represents the output of a hash function,
 * formatted as hexadecimal string.
 *
 * @author Manuel Riesen
 */
@Serializable
@JvmInline
value class Hash(val hex: String)
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ClaimRealmAction.kt:26`–`33`
```kotlin
/**
 * The action of claiming a realm.
 *
 * @property produce the claimed realm
 * @property consume the consumed claim paper
 *
 * @author Manuel Riesen
 */
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:118`–`123`
```kotlin
    /**
     * Registers an action [listener] for an [actionType].
     * The predicate [removeOn] sets a boundary to the listener's lifetime.
     * If [removeOn] is `true`, the listener is removed.
     */
    fun registerActionListener(actionType: ActionType, removeOn: (ActionRecord<out Action>) -> Boolean, listener: ActionListener) {
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:53`–`61`
```kotlin
    /**
     * Verifies a signature of a message.
     *
     * @param signature signature
     * @param message signed message
     * @param publicKey public key
     * @return the signature's validity
     */
    fun verify(signature: Signature, message: String, publicKey: PublicKey): Boolean
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/applyEffect.kt:21`–`28`
```kotlin
/**
 * Applies an [effect].
 * In order to trigger the init block of an object (singleton), it has to be used.
 * While mentioning the object in the code is sufficient for triggering initialization,
 * it is not very expressive.
 * It is strongly recommended using this function for applying effects.
 */
fun applyEffect(effect: ModuleEffect, profile: EffectProfile = EffectProfile.MAIN) {
```

## Representative inline comment excerpts

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/seedContract.kt:75`–`82`
```kotlin
        // the owner of the seed equals the author of the action,
        // the player owns the seed
        rule { action, origin, world ->
            val seed = action.consume

            seed.owner == origin.author
                    && world.players[seed.owner]?.items?.contains(seed) ?: false
        }
```

`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:73`–`83`
```kotlin
                    // create new block
                    val block = createBlock(actionPool)

                    // add the new block to the blockchain
                    val added = blockchainManager.add(block)

                    if(added) {
                        logger.debug { "new block added: $block" }

                        // publish the new block
                        OutgoingBlockBus.onNext(block)
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionSerializersModuleRegistrar.kt:56`–`61`
```kotlin
    polymorphic(BlockRecord::class) {
        //TODO replace workaround for supporting serialization of a type with generics
        // see: https://github.com/Kotlin/kotlinx.serialization/issues/944
        @Suppress("UNCHECKED_CAST")
        subclass(ActionRecord::class, ActionRecord.serializer( PolymorphicSerializer(Action::class)) as KSerializer<ActionRecord<*>>)
    }
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContractManager.kt:64`–`66`
```kotlin
        //UNCHECKED cast in order to retrieve the contract type
        @Suppress("UNCHECKED_CAST")
        val contract: ActionContract<T> = contracts[action.content.type] as ActionContract<T>
```

`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:75`–`79` (capitalized variant)
```kotlin
    // The block's previous hash is equal to the hash of the previous block
    rule { block: Block, prev: Block -> block.prevHash == prev.hash }

    // The block's height equals the increment of the previous block's height
    rule { block: Block, prev: Block -> block.height == prev.height + 1u }
```
