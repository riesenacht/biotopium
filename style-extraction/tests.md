# Tests

Test framework, naming, structure, fixtures and assertions.
KDoc on test classes is in [comments.md](comments.md).

### 1. Tests use `kotlin.test` only (`@Test`, `@BeforeTest`, `assert…` functions); there is no mocking library.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3Test.kt:31`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:118`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableListTest.kt:54`

### 2. A test class is named `<Subject>Test` and lives in the package of its subject.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:19`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/network/PeerAddressBookTest.kt:19`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/bus/EventBusTest.kt:19`
- Note: one subject split over several classes gets a topic suffix: `JsonEncoderCoreModelTest`, `JsonEncoderNetworkMessageTest` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderCoreModelTest.kt:44`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderNetworkMessageTest.kt:43`).

### 3. Test functions start with `test`, followed by the operation and subject in camelCase, with underscore-separated qualifiers.
- Confidence: high (154 of 154 start with `test`)
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:88`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:169`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519Test.kt:45`

### 4. Most test names carry `positive` or `negative` as a qualifier, but its position varies and some names omit it.
- Confidence: low (101 of 154 have it: 62 directly after the operation, 39 at the end; 53 without)
- Evidence after operation: `testValidateNew_negative_genesisBlock_invalidHash` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:130`), `testVerify_positive_withKeyPairFromJs` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519Test.kt:74`), `testAllOnNext_positive_multipleValues` (`reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/bus/EventBusTest.kt:64`)
- Evidence at the end: `testCreateZeroCoordinate_usingConstructor_positive` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:31`), `testValidateChunkGenesisAction_positive` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:112`), `testValidateNew_genesisBlock_positive` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:124`)
- Evidence without: `testEncodeChunkGenesisAction` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt:32`), `testObserveListAdd` (`reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableListTest.kt:41`), `testSha256Hashing` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3Test.kt:40`)

### 5. The `@BeforeTest` function is named `init`.
- Confidence: high (6 of 6)
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:51`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt:27`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:119`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:103`

### 6. A test body is arrange / act / assert with local `val`s, separated by blank lines, without section comments.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/CreatePlotContractTest.kt:41`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/network/PeerAddressBookTest.kt:34`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderNetworkMessageTest.kt:51`

### 7. `assertEquals` takes the expected value first.
- Confidence: medium
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3Test.kt:33`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt:47`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/CreatePlotContractTest.kt:63`
- Counterexamples: actual first in `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:33`, `:39`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableMapTest.kt:56`.

### 8. Serialization tests hard-code the golden value in a local `expected` (or `encoded`) and compare it with a local `encoded` (or `decoded`); encode and decode tests come in pairs.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt:43`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderNetworkMessageTest.kt:51` / `:68`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoderCoreModelTest.kt:52` / `:71`

### 9. An expected exception is asserted with a `try`/`catch` that sets a flag, not with `assertFailsWith`.
- Confidence: high (3 of 3)
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateTest.kt:50`, `:99`, `:128`

```kotlin
var exceptionThrown = false
try {
    x + d
} catch(e: NegativeCoordinateException) {
    exceptionThrown = true
}
assertTrue(exceptionThrown)
```

### 10. Fixtures are helper functions named `create…` or `generate…` with default arguments, at the top of the test class or in an abstract base test class.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:59`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:81`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:94`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/EncoderTest.kt:100`

### 11. Shared fixture values are properties with getters (`defaultOwner`, `zeroTimestamp`, `currentTimestamp`).
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:81`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/EncoderTest.kt:53`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:53`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:61`

### 12. Fixture helpers are copied into each test class or base class; there is no shared test-utility file.
- Confidence: high
- Evidence `createActionRecord`: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:88`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/EncoderTest.kt:69`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:81`
- Evidence `authorKeyPair`: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:76`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/EncoderTest.kt:45`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:48`

### 13. Test doubles are hand-written classes prefixed `Test` that implement the production interface or extend the production base class.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:55`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:92`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/bus/EventBusTest.kt:33`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/TestCoreModuleEffect.kt:31`
- Counterexamples: test-only types without the prefix: `StringRecord` (`core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/model/record/StringRecord.kt:38`), `EventRecorder` (`reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/EventRecorder.kt:28`).

### 14. Negative cases are built by `copy(...)` of a valid fixture with one field changed.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:133`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:157`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/HarvestContractTest.kt:61`

### 15. Each case is its own test function; there are no parameterized or table-driven tests.
- Confidence: high
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/world/model/CoordinateUnitTest.kt:31`–`77`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidatorTest.kt:123`–`339`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/ActionValidatorTest.kt:111`–`1009`

### 16. Placeholder test data uses a small fixed set of literals: `"Hello world!"`, `"name"`, `"test"`, `"A"`/`"B"`/`"C"`.
- Confidence: medium
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3Test.kt:43`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:96`, `reactive/src/commonTest/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableListTest.kt:66`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/EncoderTest.kt:90`
