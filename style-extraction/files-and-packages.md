# Files and packages

What goes into a file, how files are named, file size, and package layout.
Order of members inside a class is in [classes-and-dependencies.md](classes-and-dependencies.md).

### 1. A file holds one top-level declaration and is named after it, even when the declaration is one line.
- Confidence: high (185 of 211 main files have exactly one top-level declaration)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Hashable.kt:29`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Owner.kt:28`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/SerializedMessage.kt:26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:32`
- Counterexamples: see rules 5–7; `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/item.kt:29` and `:38` are two classes in one file.

### 2. A file whose only declaration is a top-level function or property is named after it in lowerCamelCase.
- Confidence: high
- Evidence: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/actionPoolSize.kt:24`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/blocklordConfig.kt:26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/applyEffect.kt:28`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/chunkGenesisContract.kt:29`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/tutorial/startTutorial.kt:26`

### 3. A file that groups several top-level values or helpers on one theme gets a lowerCamelCase theme name.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:28`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/colors.kt:26`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:31`

### 4. A file that holds a DSL builder is named `<thing>Dsl.kt`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/actionContractDsl.kt:30`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/exec/actionExecDsl.kt:27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/blockRulesetDsl.kt:26`

### 5. Extensions on a type defined elsewhere go into `<Type>Ext.kt`.
- Confidence: medium
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:25`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/UIntExt.kt:25`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/TextExt.kt:27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/record/HashedBlockRecordExt.kt:29`
- Counterexamples: `Promise<T>.await()` in `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:67`; `Container.*` builders in the files of rule 7.

### 6. A factory function for a class is placed in the class's file, directly after the class.
- Confidence: high
- Evidence: `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt:134`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMapImpl.kt:102`, `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMap2D.kt:41`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RecordBook.kt:34`

### 7. A lowercase file may pair a builder extension function with the class it instantiates; the function comes first.
- Confidence: medium
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:44` → `:66`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:39` → `:61`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:36` → `:47`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:58` → `:95`

```kotlin
fun Container.slot(i: Int, slotWidth: Double, slotHeight: Double): Slot {
    return Slot(i, slotWidth, slotHeight).addTo(this)
}

class Slot(
    i: Int,
    ...
```

### 8. Constants that belong to a class are declared after it: as a top-level value at the end of the file or in a companion object at the end of the class.
- Confidence: medium
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/map/Realm.kt:45`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/plant/GrowingPlant.kt:62`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:118`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:266`
- Counterexamples: companion object at the top of the class in `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:70`; standalone constant file `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/actionPoolSize.kt:24`.

### 9. Files are short: most have at most 30 non-blank lines after the license header, package and imports (KDoc included).
- Confidence: high (162 of 211 main files ≤ 30 lines; median 15 lines with KDoc, 6 lines without comments)
- Evidence (whole file from the declaration to the end): `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/BlockCandidate.kt:24`–`31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/GrowAction.kt:25`–`39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/KeyManager.kt:24`–`41`
- Counterexamples: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:33`–`273` (206 non-blank lines), `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/menu/mainMenu.kt:36`–`155`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:54`–`1009`

### 10. Data-only types live in a `model` subpackage of the feature package that uses them.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/model/Hash.kt:19`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/model/Timestamp.kt:19`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:19`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/PeerId.kt:19`

### 11. Package names are lowercase single words; there are no underscores or camelCase segments.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/exec/ActionExec.kt:19`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/RecordBook.kt:19`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:19`
