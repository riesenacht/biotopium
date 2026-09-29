# Kotlin style extraction

Code style extracted from this repository at commit `8cb9f7b`.
All paths are relative to the repository root; line numbers refer to that commit.
Each rule is backed by at least 3 occurrences. Counterexamples are listed, not judged.

## 1. Files

| File | Covers | Rules |
|------|--------|-------|
| [naming.md](naming.md) | Type, function, property, constant and parameter names | 13 |
| [files-and-packages.md](files-and-packages.md) | One declaration per file, file names, `Ext`/`Dsl` files, file size, packages | 11 |
| [modelling.md](modelling.md) | Data, value and sealed types, enums, interfaces, type aliases, objects, delegation | 15 |
| [functions.md](functions.md) | Expression bodies, named and default arguments, overloads, extensions, operators, DSL builders | 12 |
| [nullability-and-state.md](nullability-and-state.md) | `val`/`var`, private mutable backing properties, `!!`, `?: false`, guards, `lateinit`/`lazy` | 10 |
| [control-flow-and-idioms.md](control-flow-and-idioms.md) | `when`, `if` expressions, scope functions, collection operations, lambdas, `this` | 12 |
| [classes-and-dependencies.md](classes-and-dependencies.md) | Constructor parameters, reaching singletons, `init` blocks, member order, nested helpers | 5 |
| [error-handling.md](error-handling.md) | Boolean validation, reject-and-log, exceptions with `@Throws`, `@Suppress` placement | 8 |
| [logging.md](logging.md) | Logger declaration, levels, message format | 6 |
| [tests.md](tests.md) | Framework, test names, structure, fixtures, test doubles, assertions | 16 |
| [comments.md](comments.md) | KDoc coverage, `@author`, wording, tags, links, inline comments, markers | 30 |

## 2. Conflicts

Patterns with two or more competing variants. Each needs a decision.

| # | Topic | Variants | File |
|---|-------|----------|------|
| 1 | Private backing property name | `mutableTiles` (8×) vs `_trusted`, `_selected` (2×) | [nullability-and-state.md](nullability-and-state.md) #2 |
| 2 | `positive`/`negative` in test names | after the operation (62), at the end (39), missing (53) | [tests.md](tests.md) #4 |
| 3 | `assertEquals` argument order | expected first vs actual first | [tests.md](tests.md) #7 |
| 4 | Single-expression functions | expression body (46) vs block with one `return` (9) | [functions.md](functions.md) #1 |
| 5 | Return type on expression bodies | omitted (33) vs written (13) | [functions.md](functions.md) #2 |
| 6 | Constructor calls with many arguments | named vs positional | [functions.md](functions.md) #4 |
| 7 | Verb for building objects | `create…` vs `generate…` | [naming.md](naming.md) #10 |
| 8 | Compound word casing | `RuleSet` vs `Ruleset` | [naming.md](naming.md) #12 |
| 9 | Acronym casing | `P2p`, `Id`, `Json` vs `UI`, `2D` | [naming.md](naming.md) #3 |
| 10 | Names of non-`const` values | lowerCamelCase vs `SCREAMING_CASE` vs PascalCase | [naming.md](naming.md) #4 |
| 11 | Callback parameter names | `on<Event>` vs `callback`, `doAfter`, `applier` | [naming.md](naming.md) #11 |
| 12 | Value class property name | encoding/unit (`hex`, `seconds`) vs type name (`coordinate`, `index`) | [naming.md](naming.md) #13 |
| 13 | Calling function-typed values | `.invoke(...)` vs direct call | [control-flow-and-idioms.md](control-flow-and-idioms.md) #11 |
| 14 | `this.` for members | written without need vs omitted | [control-flow-and-idioms.md](control-flow-and-idioms.md) #12 |
| 15 | Lambda parameter | `it` vs named, not tied to length | [control-flow-and-idioms.md](control-flow-and-idioms.md) #7 |
| 16 | `map { }.toList()` | with vs without the extra `.toList()` | [control-flow-and-idioms.md](control-flow-and-idioms.md) #6 |
| 17 | Public collection types | read-only vs public `MutableList` | [nullability-and-state.md](nullability-and-state.md) #5 |
| 18 | Empty collection typing | type on property vs type arguments on factory (tests) | [nullability-and-state.md](nullability-and-state.md) #4 |
| 19 | Singleton dependencies | private alias property vs direct object reference | [classes-and-dependencies.md](classes-and-dependencies.md) #2 |
| 20 | Placement of private functions and companion objects | first / middle / last | [classes-and-dependencies.md](classes-and-dependencies.md) #4 |
| 21 | Logger declaration | `private … { }` vs `protected`, vs public with explicit name | [logging.md](logging.md) #1 |
| 22 | Log message format | `"event: $value"` vs free text, capitalized | [logging.md](logging.md) #4 |
| 23 | Test-only type names | `Test` prefix vs none (`StringRecord`, `EventRecorder`) | [tests.md](tests.md) #13 |
| 24 | Shared test fixtures | abstract base class vs private copies per class | [tests.md](tests.md) #10, #12 |
| 25 | Inline comment casing | lowercase fragment vs capitalized sentence/label | [comments.md](comments.md) #25 |
| 26 | Documenting parameters | inline `[name]` links vs `@param` | [comments.md](comments.md) #18 |
| 27 | Documenting constructor properties | `@property` vs `[name]` links vs `@param` | [comments.md](comments.md) #17 |
| 28 | Blank line before tags in function KDoc | with vs without | [comments.md](comments.md) #22 |
| 29 | KDoc on enum constants | 4 enums with, 7 without | [comments.md](comments.md) #8 |
| 30 | `@author` | present on 195 type KDocs, missing on 16 (mostly GUI) | [comments.md](comments.md) #9 |
| 31 | Wording for `actual` type KDoc | "Actual implementation for JS platform." vs "Implementation for JS target." vs ", implementation for JS platform." | [comments.md](comments.md) #6 |

## 3. Example file candidates

Small, self-contained files with little domain logic.

| File | Shows |
|------|-------|
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/Coordinate.kt` | Value class, operator functions, getter property, `@Throws` + KDoc `@throws`, `if` expression |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/world/model/IntExt.kt` | `<Type>Ext.kt` file, conversion extension properties, KDoc per declaration without `@author` |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/Book.kt` | `typealias`, `…Of` factory overloads (`vararg` / `List`), expression bodies |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/network/PeerAddressBook.kt` | Private `mutable…` backing properties with read-only views, overload with swapped arguments, property KDoc |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/model/block/RawBlock.kt` | Data class implementing several interfaces, `to…` conversion with named arguments, `@see` / `@author` layout |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/rule/blockRulesetDsl.kt` | DSL builder class plus `init: Builder.() -> Unit` entry function, `…Dsl.kt` file name |
| `reactive/src/commonMain/kotlin/ch/riesenacht/biotopium/reactive/collection/MutableObservableList.kt` | Interface delegation with `by`, `lateinit`, private helpers, factory function after the class |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/effect/ModuleEffect.kt` | Default arguments, guard clause with early `return`, inline step comment |
| `core/src/commonMain/kotlin/ch/riesenacht/biotopium/core/blockchain/BlockchainManager.kt` | `object` with backing property, `init` subscription, `Boolean` result, debug log on rejection |
| `core/src/commonTest/kotlin/ch/riesenacht/biotopium/network/PeerAddressBookTest.kt` | Test class KDoc, `test…_positive_…` names, arrange/act/assert, expected-first assertions |

## 4. Excluded

Left out because they belong to the architecture, a library, the domain, or a linter.

- **License header**: the GPL block comment at the top of every file (project boilerplate; missing only in `core/src/commonTest/kotlin/ch/riesenacht/biotopium/serialization/HashableStringEncoderTest.kt`).
- **Architecture**: Kotlin Multiplatform `expect`/`actual` split and source sets; module split (`core`, `network`, `reactive`, `logging`, `blocklord`, client) and packages spanning modules; module effects, `@SideEffect` and effect profiles; event buses and manager singletons as the wiring mechanism; `suspend` start/stop of network nodes.
- **Libraries**: kotlinx.serialization (`@Serializable`, `@SerialName("<ClassName>")`, `@Transient`, `@Polymorphic`, serializers-module registrars, `reified` encode/decode); Korge (scenes, `Container` builder DSL, `AsyncInjector`, UI skins); Reaktive (`BehaviorSubject`, `PublishSubject`, `Disposable`); kotlin-logging (`typealias Logging = KotlinLogging`, `LoggingConfig`); JS interop (`external`, `@JsModule`, `@JsNonModule`, `dynamic`; names in external declarations copy the JS names).
- **Domain**: blockchain model chain Raw → Hashed → Block, `Producible`/`Consumable` actions, contents of the action rules and contracts, unsigned coordinate types, golden hash and key strings in tests.
- **Formatting a linter catches**: no space in `if(`, `when(`, `for(`, `catch(`; `get()` aligned with the property instead of indented; blank lines after `{` and between members; trailing commas; parameter wrapping; line length; import order and wildcard imports; `{  }` spacing.
