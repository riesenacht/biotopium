# Style summary

A short version of the rules in this directory. Details, evidence and counterexamples are in the linked files.

## Files and naming
- One top-level declaration per file, named after it; files are small (median 15 lines). → [files-and-packages.md](files-and-packages.md)
- PascalCase file for a type, lowerCamelCase file for a top-level function or value; `<Type>Ext.kt` for extensions, `<thing>Dsl.kt` for DSL builders.
- Data types go into a `model` subpackage.
- Role suffixes on types (`Manager`, `Validator`, `Utils`, `Builder`, `Impl`). → [naming.md](naming.md)
- `const val` in lowerCamelCase; Booleans without `is` prefix; counts as `num…`; conversions `to…`; collection factories `…Of`.
- Acronyms written as words: `P2pNode`, `PeerId`, `JsonEncoder`.

## Modelling
- Immutable `data class`es with `val` only; one-property `@JvmInline value class`es for typed values. → [modelling.md](modelling.md)
- `sealed interface` for closed hierarchies (never `sealed class`), each subtype overriding a `type` enum tag.
- Small adjective interfaces (`Hashed`, `Signed`, `Producible`); read-only interface plus `Mutable…` counterpart.
- `typealias` for named collections; `fun interface` with `operator fun invoke` for named callbacks.
- Singletons as `object`; configured instances as body-less `object : Base(...)`.

## Functions and state
- Expression bodies for single expressions, often without return type. → [functions.md](functions.md)
- Named arguments for long constructor calls and for flags; defaults instead of optional overloads.
- Overloads for other argument shapes (`vararg`/`List`, element/collection).
- Extension properties for conversions (`1.coord`); operators on value types; DSLs as builder class + `init: Builder.() -> Unit`.
- `val` almost everywhere (1347 vs 35 `var`). → [nullability-and-state.md](nullability-and-state.md)
- Mutable state in `private val mutableX`, exposed as read-only `val x get() = mutableX`.
- `!!` after lookups that must succeed; `?: false` to close Boolean chains; early-return guards.

## Idioms and control flow
- Exhaustive `when` over enums without `else`, used as an expression; `if … else` expressions. → [control-flow-and-idioms.md](control-flow-and-idioms.md)
- Only `let` of the scope functions; `forEach`/`map`/`all`/`find` instead of loops.
- `it` in short lambdas; `val name = this` to name the receiver in nested builders.

## Classes, errors, logging
- Constructor parameters are `private val` if used later, plain otherwise. → [classes-and-dependencies.md](classes-and-dependencies.md)
- Wiring happens in `init`; member order is properties → `init` → functions → companion object.
- Validation returns `Boolean`; rejected input is logged at debug level and dropped. → [error-handling.md](error-handling.md)
- Exceptions only for invalid arguments, with `@Throws` plus KDoc `@throws`; no `require`/`check`/`Result`.
- `private val logger = Logging.logger { }`; only `debug`, lazy lambda messages, lowercase `"event: $value"`. → [logging.md](logging.md)

## Tests
- `kotlin.test`; class `<Subject>Test` in the subject's package; functions `test<Operation><Subject>_positive|negative_<detail>`. → [tests.md](tests.md)
- `@BeforeTest fun init()`.
- Arrange/act/assert without comments; golden `expected` strings.
- Fixtures via `create…`/`generate…` helpers and `copy(...)`; hand-written `Test…` fakes, no mocks; `try`/`catch` flag for expected exceptions.

## Comments
- KDoc on ≈ 90–95 % of public declarations; none on `@Test` functions and rarely on overrides. → [comments.md](comments.md)
- Types: "Represents …"; functions: third-person verb; properties: "The …"; Booleans: "Whether …".
- Full sentences, one per line, mostly one line long.
- Every type KDoc ends with a blank line and `@author Manuel Riesen`.
- Parameters referenced as `[name]` links more than `@param`; tag text is a lowercase fragment without period.
- Inline comments are rare, lowercase, no period, above the code; they state a rule's condition or label the next step.
- Markers are `//TODO text` and `//UNCHECKED cast in order to …`.

## Open decisions
31 patterns have competing variants; the list is in [README.md](README.md#2-conflicts). The largest ones:
- backing property name (`mutableX` vs `_x`)
- position of `positive`/`negative` in test names
- `@param` vs inline links
- inline comment casing
- return types on expression bodies

Note: `gradle.properties:1` sets `kotlin.code.style=official`, but the formatting differs from it in places (e.g. `if(` without a space); formatting is left out of these rules.
