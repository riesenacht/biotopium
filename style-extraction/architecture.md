# Architecture decisions

Architectural decisions visible in the code at commit `8cb9f7b`. This is separate from the style files, which leave architecture out.
Each decision lists the files that show it. Only what the code shows is described; nothing is recommended.

## Overview

```
                     ┌──────────── client (root project, Korge GUI, JVM + JS) ───────────┐
                     │                                                                   │
blocklord (JVM app) ─┼──► core ──► network ──► logging                                   │
                     │      │         └──(jvm)──► network:gop2p (Go, c-shared, JNR-FFI)   │
                     │      ├──► reactive ──► Reaktive                                   │
                     │      └──► logging ──► kotlin-logging                              │
                     └───────────────────────────────────────────────────────────────────┘
```

Runtime flow of one player action:

```
client  ActionManager.createAction ─ validate locally ─► OutgoingActionBus
        Biotopium ─► ActionReqMessage ─► random blocklord (direct stream)
blocklord  ActionCandidateBus ─► BlockSmith: validate, pool, build + sign block
           BlockchainManager.add ─► OutgoingBlockBus ─► BlockAddMessage (pubsub broadcast)
every node BlockCandidateBus ─► BlockchainManager: validate block, append
           ─► IncomingActionBus ─► ActionManager listeners ─► WorldStateManager: execute contract
           ─► observable world maps ─► GUI subscribers redraw
```

## Platform and build

### 1. The code is Kotlin Multiplatform with two targets: JVM and JS (browser, IR backend).
- Evidence: `build.gradle.kts:28`–`29`, `core/build.gradle.kts:23`, `core/build.gradle.kts:31`, `network/build.gradle.kts:23`, `network/build.gradle.kts:31`

### 2. The project is split into Gradle modules by technical concern: `logging`, `reactive`, `network`, `core`, `blocklord` and the client in the root project.
- Evidence: `settings.gradle.kts:34`–`40`, `core/build.gradle.kts:43`–`45`, `network/build.gradle.kts:42`, `build.gradle.kts:54`–`57`, `blocklord/build.gradle.kts:35`–`38`
- Dependency direction: `logging` has no internal dependency; `network` uses `logging`; `core` uses `logging`, `reactive`, `network`; client and `blocklord` use all four.

### 3. There are two applications: the blocklord (JVM only, `application` plugin with `main`) and the game client (Korge, JVM and JS).
- Evidence: `blocklord/build.gradle.kts:23`, `blocklord/build.gradle.kts:67`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:103`, `build.gradle.kts:25`–`29`, `src/commonMain/kotlin/main.kt:23`

### 4. Platform differences sit behind `expect`/`actual` declarations; platform code lives only in `jvmMain`/`jsMain` source sets.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/crypto/Sha3.kt:32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/time/DateUtils.kt:28`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:28`, `logging/src/commonMain/kotlin/ch/riesenacht/biotopium/logging/LoggingConfig.kt:26`

### 5. Each third-party library is wrapped by a small own type, so other modules depend on the wrapper.
- Evidence: logging `logging/src/commonMain/kotlin/ch/riesenacht/biotopium/logging/Logging.kt:28`; reactive streams `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/EventBus.kt:38`; JSON `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:40`; crypto `core/src/jvmMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:41`, `core/src/jsMain/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519.kt:81`; P2P `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:81`
- Note: the wrappers expose the library in places: `reactive` exports Reaktive with `api(...)` (`reactive/build.gradle.kts:46`) and the GUI uses `BehaviorSubject` directly (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/slot.kt:57`).

### 6. On the JVM, peer-to-peer networking uses go-libp2p compiled as a C shared library and called through JNR-FFI; on JS it uses js-libp2p.
- Evidence: `network/gop2p/build.gradle.kts:30`, `network/gop2p/build.gradle.kts:47`–`52`, `network/gop2p/src/jvmMain/java/ch/riesenacht/biotopium/network/go2p/GoP2p.java:30`, `network/src/jvmMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:40`, `network/build.gradle.kts:54`

### 7. Packages are shared across modules: the package does not tell which module a type lives in.
- Evidence: `ch.riesenacht.biotopium.bus` in `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/EventBus.kt:19`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/IncomingActionBus.kt:19` and `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/ActionCandidateBus.kt:19`; `ch.riesenacht.biotopium.network` in `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:19` and `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:19`; `core.blockchain` in the blocklord module `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:19`

## Composition and lifecycle

### 8. Services are global singletons (`object`s) that reach each other by name; there is no dependency injection outside the GUI.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:39`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:35`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:36`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:45`–`49`
- GUI exception: Korge's `AsyncInjector` builds the scenes (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/BiotopiumModule.kt:61`–`66`).

### 9. Services wire themselves in their `init` block, and "module effects" force that initialization: an effect object references the services, effects nest, and each belongs to a profile (`MAIN`, `DEV`, `TEST`).
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt:33`–`50`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/EffectProfile.kt:28`–`48`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/CoreModuleEffect.kt:36`–`47`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/applyEffect.kt:21`–`30`
- Entry points apply the effects first: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:105`–`106`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/BiotopiumModule.kt:55`–`58`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/TestCoreModuleEffect.kt:31`
- Side effects on other objects are marked with the source-only annotation `@SideEffect(receiver = …)` (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/SideEffect.kt:29`–`31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/effect/BlocklordSourceInitEffect.kt:34`).

### 10. One abstract application root, `Biotopium`, holds the configuration and the network manager and registers the shared message handlers; client and blocklord are `object`s that extend it.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:40`–`87`, `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:39`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/Blocklord.kt:52`

### 11. Configuration is hard-coded as top-level values, one per file: ports, bootstrap addresses, peer IDs and the blocklord key pair.
- Evidence: `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/blocklordKeys.kt:28`–`31`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/blocklordP2pConfig.kt:27`–`32`, `src/commonMain/kotlin/ch/riesenacht/biotopium/clientP2pConfig.kt:28`–`36`, `src/commonMain/kotlin/ch/riesenacht/biotopium/blocklordPeerIds.kt:26`–`28`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/actionPoolSize.kt:24`

## Communication

### 12. Components inside one process communicate through typed event buses: one `object` per event kind and direction.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/IncomingActionBus.kt:33`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/OutgoingActionBus.kt:31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/BlockCandidateBus.kt:31`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/ActionCandidateBus.kt:30`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/OutgoingBlockBus.kt:31`
- The KDoc of each bus defines what "incoming", "outgoing" and "candidate" mean (verified or not yet verified), e.g. `core/src/commonMain/kotlin/ch/riesenacht/biotopium/bus/IncomingActionBus.kt:25`–`29`.

### 13. Nodes talk over a libp2p network with two channels: a pubsub topic for broadcasts and a direct protocol stream for messages to one peer; both names carry the protocol version.
- Evidence: `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/BiotopiumProtocol.kt:27`–`29`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:54`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:71`, `network/src/jvmMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:63`–`69`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:86`, `:95`

### 14. Every message is a serializable data class sent as JSON inside a `MessageEnvelope` that carries the sender's peer ID; receivers dispatch on the message's `KClass` to registered handlers.
- Evidence: `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/MessageEnvelope.kt:36`–`40`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:37`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:95`–`100`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkNode.kt:113`–`129`, `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/JsonEncoder.kt:35`–`45`

### 15. Serializers for polymorphic types are collected in one global registry; each module contributes its hierarchies through registrar effects.
- Evidence: `network/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/SerializersModuleRegistry.kt:30`–`48`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/SerializersModuleRegistrar.kt:33`–`38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/CoreSerializersModuleEffect.kt:33`–`40`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/WorldSerializersModuleRegistrar.kt:36`–`51`

### 16. Network peers (peer IDs) and wallets (addresses) are separate identities; peers announce their address and every node keeps a two-way address book.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt:30`–`70`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:42`–`48`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/NetworkManager.kt:80`–`82`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/PeerAddressInfoMessage.kt:34`–`37`

### 17. A client syncs the chain by pulling: it sends its current height to a random blocklord and receives the missing blocks.
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/BiotopiumClient.kt:44`–`52`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:67`–`83`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/model/message/blockchain/ChainReqMessage.kt:33`–`35`

## State and consensus

### 18. Game state is event-sourced: actions are events stored in blocks, and the world is changed only by applying the actions of accepted blocks.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/Action.kt:24`–`26`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:78`–`83`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:49`–`57`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:88`–`94`

### 19. Each action type has one contract made of validation rules and an execution function; rules get the read-only `World`, execution gets `MutableWorld`.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContract.kt:32`–`35`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/rule/ActionRule.kt:38`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/exec/ActionExec.kt:36`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/ActionContractManager.kt:41`–`51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/contract/seedContract.kt:33`–`99`
- `MutableWorld` is a private object inside `WorldStateManager`, handed only to contract execution (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:79`–`86`).

### 20. The same validation runs at three places: the client before sending, the blocklord before pooling, and every node before appending a block.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:91`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:65`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:75`
- Rejected input is dropped with a debug log; the sender gets no answer (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:88`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:93`).

### 21. Blocks are produced only by trusted blocklords (proof of authority); the list of trusted addresses is set once at startup through an effect.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:43`–`44`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlocklordSource.kt:55`–`70`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/effect/DevBlocklordSourceInitEffect.kt:31`–`37`, `blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockSmith.kt:71`–`90`

### 22. The chain is append-only with no fork handling: a block is accepted only if it extends the local last block.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockValidator.kt:41`–`47`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:76`–`82`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:73`–`91`

### 23. Integrity rests on SHA3-256 hashes and Ed25519 signatures for every block and every record; a wallet address is the public key itself.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockUtils.kt:42`–`53`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/blockRules.kt:28`–`52`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/Address.kt:27`

### 24. The hash input is a custom deterministic encoding: all non-null values flattened into one `;`-separated string, identical on JVM and JS.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoder.kt:26`–`34`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoder.kt:71`–`76`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt:43`

### 25. Blocks and records are built in typed stages (raw → hashed → signed); each stage is its own data class, and `toHashable()` returns the raw form that is hashed.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/RawBlock.kt:51`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/HashedBlock.kt:54`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/AbstractBlock.kt:59`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:63`–`73`

### 26. The data model is immutable; mutable state lives only in the state managers and is exposed as observable collections so the GUI can react to changes.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/WorldStateManager.kt:41`–`72`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/Player.kt:41`–`47`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/realm.kt:93`–`102`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/toolbar/toolbar.kt:193`–`201`

## Client

### 27. The GUI is a Korge module with three scenes in a fixed flow: main menu → bootstrap (start node, sync chain, create the player) → realm.
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/BiotopiumModule.kt:48`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/menu/mainMenu.kt:40`–`44`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/bootstrap/BootstrapScene.kt:56`–`81`

### 28. The client creates time-based actions itself: one coroutine timer per growing plant sends the next grow action.
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:48`–`68`, `src/commonMain/kotlin/ch/riesenacht/biotopium/GrowthScheduler.kt:74`–`88`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/realm/tile.kt:116`–`118`

### 29. A client has a new key pair on every start; keys are not stored and "Import Keys" is disabled.
- Evidence: `src/commonMain/kotlin/ch/riesenacht/biotopium/biotopiumClientConfig.kt:27`–`32`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:56`, `src/commonMain/kotlin/ch/riesenacht/biotopium/gui/menu/mainMenu.kt:139`

## Concurrency

### 30. Network start and stop are `suspend` functions; the JVM runs blocking native listen loops in background jobs, and JS wraps promises with a small `await` helper.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Biotopium.kt:92`–`101`, `network/src/jvmMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:71`–`89`, `network/src/jsMain/kotlin/ch/riesenacht/biotopium/network/utils/jsHelpers.kt:67`–`69`

### 31. Shared state is held in plain mutable collections without locks; on the JVM, received messages are handled on `Dispatchers.Default` threads.
- Evidence: `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:45`–`47`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt:46`, `network/src/jvmMain/kotlin/ch/riesenacht/biotopium/network/P2pNode.kt:71`–`82`

## Testing

### 32. Tests live in `commonTest` only, so they run on both targets; cross-platform agreement is checked with fixed test vectors from each platform.
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519Test.kt:45`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/crypto/Ed25519Test.kt:59`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt:43`
- Only `core` and `reactive` have tests; `network`, `logging`, `blocklord` and the client have none (no `*Test` source set in those modules).

### 33. Tests replace global setup through the `TEST` effect profile and replace state with hand-written in-memory worlds.
- Evidence: `core/src/commonTest/kotlin/ch/riesenacht/biotopium/TestCoreModuleEffect.kt:31`–`35`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/blockchain/TestBlocklordSourceInitEffect.kt:32`–`38`, `core/src/commonTest/kotlin/ch/riesenacht/biotopium/core/action/contract/ContractTest.kt:55`–`74`

## Limitations stated in the code

- The project is a proof of concept (`README.md:19`).
- Development key material and trusted addresses are committed in source and marked insecure (`blocklord/src/commonMain/kotlin/ch/riesenacht/biotopium/blocklord/blocklordKeys.kt:28`–`31`, `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/effect/DevBlocklordSourceInitEffect.kt:27`).
- Open TODOs: serialization of generic types uses a workaround (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/model/ActionSerializersModuleRegistrar.kt:57`), listener dispatch may be slow (`core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/action/ActionManager.kt:52`), client initialization is forced by a reference (`src/commonMain/kotlin/ch/riesenacht/biotopium/gui/BiotopiumModule.kt:57`).
