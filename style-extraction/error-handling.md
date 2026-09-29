# Error handling and validation

How invalid input, failures and unchecked casts are handled.

### 1. Validation functions return `Boolean`; invalid input is not an exception.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionValidator.kt:37`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:41`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:70`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:78`

### 2. An operation that can be rejected returns `Boolean` (added / published or not) instead of `Unit`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:73`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:97`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:87`

### 3. Rejected input is logged at debug level and dropped; the caller gets `false` or nothing.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:88`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:96`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:93`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:57`

```kotlin
if(isValid(envelope)) {
    OutgoingActionBus.onNext(envelope)
    return true
}

logger.debug { "not creating invalid action: $action" }

return false
```

### 4. Validation is a list of predicate lambdas combined with `all { }`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/ActionRuleSet.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:46`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:64`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/introductionContract.kt:43`

### 5. Preconditions are plain `if` checks with an early `return`; `require`, `check`, `error()`, `runCatching` and `Result` do not occur.
- Confidence: high (0 occurrences of the listed functions)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:56`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:45`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:42`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/actionRuleSetDsl.kt:67`

### 6. Exceptions are thrown only for invalid arguments or unreachable targets, as a dedicated `…Exception` class, and the throwing function carries both `@Throws(X::class)` and a KDoc `@throws`.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt:43`–`48`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:23`–`27`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt:37`–`41`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:52`–`58`
- Counterexample: `throw Exception("item cannot be added to full toolbar")` without annotation (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:230`).

```kotlin
/**
 * Adds a [delta] to the [coordinate].
 * @throws NegativeCoordinateException operation resulted in a negative coordinate value
 */
@Throws(NegativeCoordinateException::class)
operator fun plus(delta: CoordinateUnit) = if(coordinate.toInt() + delta.value >= 0)
        Coordinate((coordinate.toInt() + delta.value).toUInt())
    else throw NegativeCoordinateException()
```

### 7. Production code has no `try`/`catch`; exceptions are only caught in tests.
- Confidence: high (3 `catch`, all in tests)
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:53`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:102`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:131`

### 8. `@Suppress("UNCHECKED_CAST")` is put on the single statement that needs it, never on a function or file.
- Confidence: high (6 of 6)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionValidator.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContractManager.kt:65`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:126`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionSerializersModuleRegistrar.kt:59`
