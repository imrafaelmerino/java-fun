# DX and Javadoc Audit (Session)

## Scope

Focused review of public API behavior, argument validation, and Javadoc accuracy in `fun.gen`.

## Main Findings

1. Probability edge bug in `Combinators.nullable(gen, prob)`
- `prob=0` still produced `null` in ~1% of samples due to an off-by-one condition.

2. Missing/weak validation in generator constructors
- `BytesGen.arbitrary(min,max)` did not reject negative `minLength` early.
- `Combinators.nOf(...)` accepted negative `n`.
- `Combinators.combinations(k, ...)` lacked bounds checks for `k`.

3. Collection mutation hazard in `Combinators.oneOf(List/Set)`
- The generated supplier depended on the original mutable collection.
- Mutating the input collection after creating the generator could lead to unstable behavior.

4. Numeric entropy and boundary quality
- `BigIntGen` used an incorrect random-bit extraction path in `next(int bits)`.
- `BigIntGen.arbitrary(min,max)` seeded an internal `Random` from `nextInt()` instead of `nextLong()`.
- `DoubleGen.arbitrary/biased(min,max)` accepted non-finite bounds, enabling undefined `NaN`/infinite outcomes.

5. Javadoc mismatches and exception contract drift
- Exception clauses/documented ranges did not always match runtime behavior.
- A few docs referenced non-existent parameters or stale error conditions.

## Improvements Applied

- Fixed `nullable` off-by-one and added regression tests for `prob=0` and `prob=100`.
- Added early validation for invalid arguments (`BytesGen`, `nOf`, `combinations`, finite bounds in `DoubleGen`).
- Hardened `oneOf(List/Set)` by snapshotting input collections while preserving `null` elements.
- Corrected `BigIntGen` randomness bit extraction and improved seeding quality.
- Updated Javadocs for affected methods to reflect current behavior and thrown exceptions.
- Added regression tests for all above fixes.

## Verification

- Targeted suites executed multiple times:
  - `fun.gen.TestCombinators`
  - `fun.gen.TestDoubleGen`
  - `fun.gen.TestBigIntGen`
  - `fun.gen.TestBytesGen`
  - `fun.gen.TestBigDecGen`
  - `fun.gen.TestLongGen`
- Full suite executed:
  - `mvn -q test`

All runs passed.
