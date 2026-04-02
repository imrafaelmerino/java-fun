# Changelog

All notable changes to this project are documented in this file.

## 4.0.0

### Breaking

- `MyRecord` optional accessors were renamed from `getOptXxx(...)` to `getOptionalXxx(...)`.
- `MyRecord.getStr(...)`, `MyRecord.getBool(...)`, and `MyRecord.getBigInt(...)` were renamed to:
  - `getString(...)`
  - `getBoolean(...)`
  - `getBigInteger(...)`
- Removed legacy `Gen.cons(...)`, `Gen.then(...)`, and `Gen.suchThat(...)`; use `Gen.constant(...)`, `Gen.flatMap(...)`, and `Gen.filter(...)`.
- Replaced the giant `MyRecordGen.of(...)` overload family with a compact varargs form and made `builder()`/`ofEntries(...)` the primary typed construction APIs.
- Removed public field `MyRecord.map`.
  Use `asMap()`, `containsKey(String)`, `size()`, and `isEmpty()` instead.
- `MyRecord` constructor now deep-freezes nested container values (`List`/`Set`/`Map`) and `byte[]` values by default.
- Removed ambiguous `MyRecord` collection getters without suffix (`getOptionalList`, `getList`, `getOptionalSet`, `getSet`, `getOptionalMap`, `getMap`) in favor of explicit `*View`/`*Copy` methods.

### Fixed

- `CsvStream` now applies `headerMapper` exactly once.
- `CsvStream` correctly handles separators inside quoted values.
- `CsvStream` correctly handles escaped quotes (`""`) inside quoted values.
- `CsvStream` now propagates downstream consumer exceptions instead of treating them as malformed-row errors.
- `CsvStream` now closes the underlying reader when `withExpectedHeaders(...)` validation fails during stream initialization.
- `Combinators.combinations(k, input)` no longer relies on rejection sampling, avoiding failures for extreme cardinalities (`k=0`, `k=n`, near-edges) on large inputs.
- `Combinators.combinations(k, List)` now validates `k` against the number of distinct input values, preventing impossible requests when the list contains duplicates.
- `Combinators.oneOf(value, others...)` and `Combinators.oneOf(gen, others...)` now take defensive snapshots of varargs inputs, avoiding external-mutation aliasing.
- `Combinators.oneOfList(...)`, `Combinators.nOf(...)`, `Combinators.subsets(...)`, and `Combinators.shuffle(...)` now snapshot mutable inputs by default.
- `Combinators.combinations(...)` now has a coherent `combinationsView(...)` counterpart for live-reference semantics.
- `Combinators.freq(...)`/`freqList(...)` now reject non-positive weights explicitly instead of silently ignoring them.
- Exhaustion/constraint failures now throw typed generation exceptions instead of generic runtime exceptions.

### Documentation

- Updated Javadoc to clarify numeric precision semantics.
- Expanded `Combinators` Javadoc/README with explicit safe-vs-view input handling guidance and examples.
- Clarified `MyRecord` collection access semantics with explicit `*View` and `*Copy` APIs and deep-freeze constructor behavior.
- Added migration guidance for `Gen` idiomatic names and the simplified `MyRecordGen` construction API.
- Normalized test class and test method naming to idiomatic JUnit 5 conventions (`*Test`, `should...When...`) to improve readability and maintenance.

### Added

- Added `Combinators.oneOfView(...)` overloads for value varargs, generator varargs, list, and set inputs.
- Added `Combinators.oneOfListView(...)`.
- Added `Combinators.nOfView(...)` overloads for list and set inputs.
- Added `Combinators.combinationsView(...)` overloads for list and set inputs.
- Added `Combinators.subsetsView(...)` overloads for list and set inputs.
- Added `Combinators.shuffleView(...)`.
- Added deterministic seed helpers in `Gen`:
  - `sample(long seed)`, `sample(int n, long seed)`
  - `collect(int n, long seed)`, `collect(int n, long seed, Function)`
  - `classify(..., int n, long seed)` overloads
- Added typed generation exception hierarchy:
  - `GenerationException`
  - `GenerationExhaustedException`
  - `UnsatisfiableConstraintException`
- Added `Gen.constant(...)`, `Gen.flatMap(...)`, and `Gen.filter(...)` as canonical API names.
- Added `MyRecordGen.builder()`, `MyRecordGen.ofEntries(...)`, and compact `MyRecordGen.of(key, gen, ...)` construction paths.
- Added `MyRecord` explicit collection accessors:
  - `getOptionalListView(...)`, `getListView(...)`, `getOptionalListCopy(...)`, `getListCopy(...)`
  - `getOptionalSetView(...)`, `getSetView(...)`, `getOptionalSetCopy(...)`, `getSetCopy(...)`
  - `getOptionalMapView(...)`, `getMapView(...)`, `getOptionalMapCopy(...)`, `getMapCopy(...)`
- Added `MyRecord.wrap(Map<String, ?>)` as explicit performance-oriented construction mode without deep-freeze.
- Added `TestSuiteConventionsTest` to enforce naming/modifier conventions (`*Test`, `should...When...`, non-public JUnit 5 tests) across test sources.
- Refactored `GenRecordMyTest` arity coverage into a parameterized test to keep compact `MyRecordGen.of(...)` coverage with less duplication.

## 3.0.0

### Breaking

- Upgraded Java baseline from 17 to 21.
- Renamed `Record` to `MyRecord` ([reason](https://errorprone.info/bugpattern/TypeParameterUnusedInFormals)).
- Renamed `RecordGen` to `MyRecordGen`.

### Maintenance

- Upgraded plugins and library versions.
- Added Error Prone.
- Updated README.

## 2.2.0

### Breaking

- Removed `BigIntGen.arbitrary(int nBits)`.
- Removed `BigIntGen.biased(int nBits)`.

### Added

- Added `BigIntGen.arbitrary(BigInteger min, BigInteger max)`.
- Added `BigIntGen.biased(BigInteger min, BigInteger max)`.

## 2.1.0

### Breaking

- In `Record`, `getXXX(...)` methods may return `null` and no longer return `Optional`.
  Migrate optional-safe usage to `getOptXXX(...)` methods.

### Added

- Added `CSVStreamBuilder` to read CSV files into `Stream<Record>`.
- Added `getOptXXX(...)` methods in `Record` (for example `getOptStr`, `getOptInt`).

### Changed

- Improved numeric coercion in `Record` getters to prefer returning compatible values instead of throwing.
  For example:
  - `record.getLong("age")` can accept `Byte`, `Short`, and `Integer`.
  - `record.getDouble(...)` can accept integral values in addition to floating-point values.
- Upgraded Error Prone.

## 2.0.0

### Breaking

- This version requires Java 17.

### Changed

- Refactored `Gen` to use `RandomGenerator` instead of `Random`.
- Refactored `SplitGen` to use `RandomGenerator.getDefault()`.

### Added

- Added tuple classes: `Quadruple`, `Quintuple`, `Sextuple`.
- Added generators: `QuadrupleGen`, `QuintupleGen`, `SextupleGen`.

## 1.4.0

### Added

- Added named generators (`NamedGen`).
- Added README section about recursive generators.

### Changed

- Improved `JsObjGen` nullable and optional field generation.
