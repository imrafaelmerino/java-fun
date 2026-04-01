<p align="center">
  <img src="./logo/package_twitter_if9bsyj4/base/full/coverphoto/base_logo_white_background.png" alt="java-fun logo"/>
</p>

<p align="center">
  <a href="https://search.maven.org/artifact/com.github.imrafaelmerino/java-fun">
    <img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.github.imrafaelmerino/java-fun"/>
  </a>
  <a href="https://github.com/imrafaelmerino/java-fun/blob/main/LICENSE">
    <img alt="License" src="https://img.shields.io/badge/license-Apache%202.0-blue"/>
  </a>
  <a href="https://www.buymeacoffee.com/imrafaelmerino">
    <img alt="Buy Me a Coffee" src="https://img.shields.io/badge/Buy%20Me%20a%20Coffee-%E2%98%95-yellow"/>
  </a>
</p>

# java-fun

`java-fun` brings practical functional patterns to Java with a strong focus on **property-based testing**.

The library is centered around:
- `Gen<T>` for composable pseudo-random data generation.
- Powerful built-in generators for primitives, collections, tuples, and records.
- `MyRecord`/`MyRecordGen` for typed, map-backed object generation.
- `CsvStreamBuilder` for robust CSV ingestion into typed records.
- `Lens`, `Prism`, and `Option` optics for composable data access/update.

## Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [What Is New in 4.0](#what-is-new-in-40)
- [Mental Model](#mental-model)
- [Core Gen Operations](#core-gen-operations)
- [Built-in Generators](#built-in-generators)
- [Combinators](#combinators)
- [MyRecord and MyRecordGen](#myrecord-and-myrecordgen)
- [CSV Ingestion](#csv-ingestion)
- [Recursive Generators](#recursive-generators)
- [Optics](#optics)
- [Testing Tips](#testing-tips)
- [Migration Guide (3.x -> 4.x)](#migration-guide-3x---40)
- [Related Projects](#related-projects)

## Installation

```xml
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>java-fun</artifactId>
  <version>${java-fun.version}</version>
</dependency>
```

Project coordinates:
- Group: `com.github.imrafaelmerino`
- Artifact: `java-fun`

## Quick Start

```java
import fun.gen.*;

Gen<String> usernames = StrGen.alphanumeric(3, 12);
Gen<Integer> ages = IntGen.biased(0, 120);

// sample values
System.out.println(usernames.sample(5).toList());
System.out.println(ages.sample(5).toList());

// inspect distribution
System.out.println(Combinators.oneOf("A", "B", "C").collect(10_000));
```

Generating domain objects:

```java
import fun.gen.*;

record User(String login, String name, Integer age) {}

Gen<User> users = MyRecordGen.of(
        "login", StrGen.alphanumeric(3, 20),
        "name", StrGen.alphabetic(1, 40),
        "age", IntGen.arbitrary(18, 99)
).map(r -> new User(
        r.getString("login"),
        r.getString("name"),
        r.getInt("age")
));

System.out.println(users.sample(3).toList());
```

## What Is New in 4.0

- `MyRecord` API naming is now consistent:
  `getOptXxx` -> `getOptionalXxx`, `getStr/getBool/getBigInt` ->
  `getString/getBoolean/getBigInteger`.
- `MyRecord` is safer by default:
  defensive copy on construction and immutable `asMap()` view.
- `CsvStreamBuilder` parsing is more robust:
  quoted separators and escaped quotes are handled correctly.
- Header normalization in CSV ingestion is coherent:
  header mapping is applied exactly once.
- Javadoc was clarified around numeric conversion and precision semantics.

## Mental Model

`Gen<T>` is a function from a `RandomGenerator` to a stateful `Supplier<T>`:

```java
public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> {}
```

Why this shape is useful:
- **Composable**: transform, chain, and filter generators.
- **Deterministic** with a seed when needed.
- **Lazy**: values are produced only when sampled.

## Core Gen Operations

Most workflows use these operations:

```java
Gen<Integer> base = IntGen.arbitrary(0, 100);

Gen<String> mapped = base.map(Object::toString);

Gen<Integer> positiveEven = base.suchThat(n -> n > 0 && n % 2 == 0);

Gen<Integer> distinct = base.distinct(500);

Gen<String> chained = base.then(n -> StrGen.alphanumeric(1, Math.max(1, n % 10)));
```

Sampling helpers:
- `sample()` -> `Supplier<T>`
- `sample(int n)` -> `Stream<T>`
- `collect(int n)` -> frequency map `Map<T, Long>`
- `collect(int n, Function<T, K> mapper)` -> grouped frequency by projection

## Built-in Generators

### Primitive-like generators

- `IntGen`: `arbitrary`, `biased`, bounded and unbounded variants.
- `LongGen`: same pattern as `IntGen`.
- `DoubleGen`: bounded/unbounded arbitrary and biased variants.
- `BigIntGen`: bounded/unbounded arbitrary and biased variants.
- `BigDecGen`: bounded/unbounded arbitrary and biased variants.
- `BoolGen.arbitrary()`.
- `CharGen`: `ascii`, `letter`, `digit`, `alphabetic`, bounded/unbounded arbitrary.
- `StrGen`: `arbitrary`, `biased`, `digits`, `ascii`, `letters`, `alphabetic`, `alphanumeric`.
- `InstantGen`: bounded/unbounded `arbitrary` and `biased`.
- `BytesGen`: bounded `arbitrary` / `biased` byte arrays.

### Collections

- `ListGen.ofN(gen, size)`
- `ListGen.arbitrary(gen, minLength, maxLength)`
- `ListGen.biased(gen, minLength, maxLength)`
- `SetGen.ofN(gen, size)` and `withMaxTries(tries)`
- `MapGen.of(keyGen, valueGen, size)` and `withMaxTries(tries)`

### Tuples

- Value types: `Pair`, `Triple`, `Quadruple`, `Quintuple`, `Sextuple`
- Generators: `PairGen`, `TripleGen`, `QuadrupleGen`, `QuintupleGen`, `SextupleGen`

Example:

```java
Gen<fun.tuple.Pair<String, Integer>> pairGen = PairGen.of(
        StrGen.alphanumeric(1, 8),
        IntGen.arbitrary(0, 100)
);
```

## Combinators

`Combinators` helps combine values or generators declaratively.

Common ones:

```java
Gen<String> one = Combinators.oneOf("A", "B", "C");
Gen<Integer> weighted = Combinators.freq(
        fun.tuple.Pair.of(8, IntGen.arbitrary(0, 10)),
        fun.tuple.Pair.of(2, IntGen.biased(0, 10))
);
Gen<String> nullable = Combinators.nullable(StrGen.alphanumeric(1, 5), 20);
Gen<Set<Integer>> comb = Combinators.combinations(2, List.of(1, 2, 3, 4));
Gen<Set<Integer>> subsets = Combinators.subsets(Set.of(1, 2, 3));
Gen<List<Integer>> shuffled = Combinators.shuffle(List.of(1, 2, 3, 4));
```

Notes:
- `freq` ignores non-positive weights and validates that at least one positive weight exists.
- `nullable(gen, prob)` uses `prob` in range `[0, 100]`.

## MyRecord and MyRecordGen

`MyRecordGen` creates map-backed records with typed accessors.

### Why use it

- Keep generation setup declarative.
- Build domain objects with `.map(...)` at the edges.
- Handle optional keys and nullable values explicitly.

### MyRecord highlights

`MyRecord` is now immutable-friendly:
- Backing map is defensively copied.
- `asMap()` returns an immutable view.
- Convenience methods: `containsKey`, `size`, `isEmpty`.

Typed accessors follow a consistent pattern:
- Optional accessors: `getOptionalXxx(key)`.
- Value-or-default accessors: `getXxx(key, supplier)`.
- Nullable boxed shortcuts: `getXxx(key)` for boxed/reference types.

Example:

```java
Gen<MyRecord> person = MyRecordGen.of(
        "name", StrGen.alphabetic(1, 20),
        "age", IntGen.arbitrary(0, 120),
        "active", BoolGen.arbitrary()
).withOptKeys("active")
 .withNullValues("name");

MyRecord rec = person.sample().get();

String name = rec.getOptionalString("name").orElse("unknown");
Integer age = rec.getInt("age");
Boolean active = rec.getBoolean("active");
```

## CSV Ingestion

`CsvStreamBuilder` reads a CSV file into `Stream<MyRecord>` with configurable normalization,
validation, and error handling.

Key features:
- Header mapping (`withHeaderMapper`).
- Value mapping (`withValueMapper`).
- Optional trim policy (`withTrimValues`).
- Optional type conversion (`withoutTypeConversion` to disable).
- Expected-header validation (`withExpectedHeaders`).
- Strict row width (`withStrictRowWidth`).
- Null token mapping (`withNullTokens`, `withNullTokenMatcher`).
- Malformed-row handling (`withRowErrorHandler`, `withSkipMalformedRows`, `withErrorCollector`).

```java
import fun.gen.*;

import java.io.File;

try (var rows = CsvStreamBuilder.of(new File("users.csv"), ",")
        .withExpectedHeaders("id", "name", "age", "active")
        .withStrictRowWidth()
        .withNullTokens("", "null", "N/A")
        .withErrorCollector((row, ex) ->
                System.err.println("Row " + row + " ignored: " + ex.getMessage()))
        .withSkipMalformedRows()
        .get()) {

    rows.forEach(r -> {
        Integer id = r.getInt("id");
        String name = r.getString("name");
        Integer age = r.getInt("age");
        Boolean active = r.getBoolean("active");
        // process...
    });
}
```

Behavior details:
- Header mapping is applied exactly once before records are built.
- Quoted separators are handled correctly.
- Escaped quotes (`""`) inside quoted fields are unescaped.

## Recursive Generators

Use `NamedGen` to declare recursion safely and clearly.

```java
import fun.gen.*;

Gen<MyRecord> person = NamedGen.of(
        "person",
        MyRecordGen.of(
                "name", StrGen.alphabetic(1, 20),
                "age", IntGen.arbitrary(0, 100),
                "parent", NamedGen.of("person")
        ).withOptKeys("parent")
);
```

## Optics

`java-fun` includes three optics:
- `Lens<S, A>`: mandatory focus.
- `Prism<S, A>`: partial focus.
- `Option<S, A>`: optional focus with set/modify.

Mini example with `Lens` composition:

```java
import fun.optic.Lens;

record Address(String city) {}
record User(String name, Address address) {}

Lens<User, Address> userAddress = new Lens<>(
        User::address,
        addr -> user -> new User(user.name(), addr)
);

Lens<Address, String> addressCity = new Lens<>(
        Address::city,
        city -> addr -> new Address(city)
);

Lens<User, String> userCity = userAddress.compose(addressCity);

User u1 = new User("Ana", new Address("Madrid"));
User u2 = userCity.set.apply("Valencia").apply(u1);
```

## Testing Tips

- Use `biased()` generators early to hit known boundary cases faster.
- Add `collect(n)` assertions in generator tests to guard distribution regressions.
- Use `distinct(tries)` only when uniqueness is truly required.
- For deterministic debugging, keep a single `RandomGenerator` seed and reuse it.
- Prefer composing small generators rather than embedding complex logic in one lambda.

## Migration Guide (3.x -> 4.0)

Main breaking changes in the `MyRecord` API:

- `getOptXxx(...)` -> `getOptionalXxx(...)`
- `getStr(...)` -> `getString(...)`
- `getBool(...)` -> `getBoolean(...)`
- `getBigInt(...)` -> `getBigInteger(...)`
- `record.map` (public field) removed.

New recommended access pattern:

```java
// before
// record.map.get("x")

// now
record.asMap().get("x");
record.containsKey("x");
record.getOptionalString("x");
```

Also improved:
- CSV parsing around quoted separators and escaped quotes.
- CSV header mapping consistency.
- Numeric-conversion Javadoc clarity (especially decimal precision semantics).

## Related Projects

- [json-values](https://github.com/imrafaelmerino/json-values)

---

If you are using `java-fun` in production or research, opening issues/PRs with real-world usage
patterns is extremely valuable for improving generator ergonomics and API DX.
