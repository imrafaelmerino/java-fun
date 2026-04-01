# Changelog

All notable changes to this project are documented in this file.

## 4.0.0

### Breaking

- `MyRecord` optional accessors were renamed from `getOptXxx(...)` to `getOptionalXxx(...)`.
- `MyRecord.getStr(...)`, `MyRecord.getBool(...)`, and `MyRecord.getBigInt(...)` were renamed to:
  - `getString(...)`
  - `getBoolean(...)`
  - `getBigInteger(...)`
- Removed public field `MyRecord.map`.
  Use `asMap()`, `containsKey(String)`, `size()`, and `isEmpty()` instead.
- `MyRecord` now defensively copies input maps and exposes an immutable map view.

### Fixed

- `CsvStream` now applies `headerMapper` exactly once.
- `CsvStream` correctly handles separators inside quoted values.
- `CsvStream` correctly handles escaped quotes (`""`) inside quoted values.
- `CsvStream` now propagates downstream consumer exceptions instead of treating them as malformed-row errors.
- `CsvStream` now closes the underlying reader when `withExpectedHeaders(...)` validation fails during stream initialization.
- `Combinators.combinations(k, input)` no longer relies on rejection sampling, avoiding failures for extreme cardinalities (`k=0`, `k=n`, near-edges) on large inputs.
- `Combinators.combinations(k, List)` now validates `k` against the number of distinct input values, preventing impossible requests when the list contains duplicates.

### Documentation

- Updated Javadoc to clarify numeric precision semantics.

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
