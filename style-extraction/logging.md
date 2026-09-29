# Logging

### 1. A class or object that logs declares `private val logger = Logging.logger { }` among its properties.
- Confidence: high
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:43`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:54`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:40`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:51`
- Counterexamples: `protected val logger` shared with subclasses (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:49`); public logger with an explicit name `Logging.logger("NetworkNode")` (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:39`).

### 2. Only the `debug` level is used.
- Confidence: high (19 `debug`, 0 `trace`/`info`/`warn`/`error`)
- Evidence: `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:116`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:88`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:70`

### 3. Log messages are passed as a lambda: `logger.debug { "…" }`.
- Confidence: high (19 of 19)
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:96`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:62`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:46`

### 4. A log message starts lowercase, has no final period, and puts the logged value after a colon.
- Confidence: medium
- Evidence: `"dropping invalid block: $block"` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:88`), `"added block: ${message.block}"` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:61`), `"received message: $message"` (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:116`), `"found action candidate: ${it.candidate}"` (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:62`)
- Counterexamples: capitalized, no colon: `"P2P Node started with peer ID …"` (`network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:120`); no colon: `"sending message $message to all"` (`network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:60`), `"answered chain request with $fwdMessage"` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:79`).

### 5. Whole objects are logged through string templates (their `toString()`), not by picking fields.
- Confidence: high
- Evidence: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:80`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:96`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:84`
- Counterexample: selected fields in `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:46`.

### 6. Messages describe events, not states, and name what happened to what (`dropping invalid block`, `action added to pool`, `received chain request`).
- Confidence: medium
- Evidence: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:69`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:88`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:70`
- Note: tense varies between past (`added`, `received`), progressive (`sending`, `dropping`) and negated present (`not creating`, `could not answer`).
