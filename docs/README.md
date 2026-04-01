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

`java-fun` brings practical functional patterns to Java, focused on **property-based testing** and **developer ergonomics**.

Main capabilities:
- Composable pseudo-random generators with `Gen<T>`.
- Built-in generators for primitives, collections, tuples, records, and time values.
- Rich combinators (`Combinators`) to compose and bias data generation.
- `MyRecord` and `MyRecordGen` for typed map-backed records.
- `CsvStreamBuilder` for robust CSV ingestion into `MyRecord`.
- Optics (`Lens`, `Prism`, `Option`) for composable data access/update.

## Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [What Is New in 4.0](#what-is-new-in-40)
- [Mental Model](#mental-model)
- [Core `Gen` Operations](#core-gen-operations)
- [Generators Cookbook (Method by Method)](#generators-cookbook-method-by-method)
- [Combinators Cookbook (All Public Methods)](#combinators-cookbook-all-public-methods)
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

## Quick Start

```java
import fun.gen.*;

Gen<String> usernames = StrGen.alphanumeric(3, 12);
Gen<Integer> ages = IntGen.biased(0, 120);

System.out.println(usernames.sample(5).toList());
System.out.println(ages.sample(5).toList());
System.out.println(Combinators.oneOf("A", "B", "C").collect(10_000));
```

Domain object generation:

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

- `MyRecord` naming is now consistent:
  - `getOptXxx(...)` -> `getOptionalXxx(...)`
  - `getStr/getBool/getBigInt` -> `getString/getBoolean/getBigInteger`
- `MyRecord` is safer:
  - defensive copy in constructor
  - immutable map view via `asMap()`
- `CsvStreamBuilder` parsing is more robust:
  - quoted separators handled correctly
  - escaped quotes (`""`) unescaped correctly
  - header mapper applied exactly once
- Improved Javadoc around numeric conversion and precision semantics.

## Mental Model

`Gen<T>` is a function from a `RandomGenerator` to a stateful `Supplier<T>`:

```java
public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> {}
```

Why this works well:
- **Composable**: map/chain/filter generators naturally.
- **Deterministic**: provide a seed to reproduce failures.
- **Lazy**: values are generated only when requested.

## Core `Gen` Operations

```java
import fun.gen.*;

import java.util.Map;

Gen<Integer> base = IntGen.arbitrary(0, 100);

Gen<String> mapped = base.map(Object::toString);
Gen<Integer> distinct = base.distinct();
Gen<Integer> distinctWithLimit = base.distinct(500);
Gen<Integer> filtered = base.suchThat(n -> n % 2 == 0);
Gen<Integer> filteredWithLimit = base.suchThat(n -> n > 90, 2000);
Gen<String> chained = base.then(n -> StrGen.alphanumeric(1, Math.max(1, n % 10)));
Gen<Integer> withSideEffect = base.peek(n -> System.out.println("generated=" + n));

System.out.println(base.sample().get());
System.out.println(base.sample(5).toList());
System.out.println(base.collect(1000));
Map<String, Long> parity = base.collect(1000, n -> n % 2 == 0 ? "even" : "odd");
System.out.println(parity);
```

## Generators Cookbook (Method by Method)

### IntGen

```java
import fun.gen.*;

Gen<Integer> g1 = IntGen.arbitrary();
Gen<Integer> g2 = IntGen.arbitrary(10);
Gen<Integer> g3 = IntGen.arbitrary(-20, 20);
Gen<Integer> g4 = IntGen.biased();
Gen<Integer> g5 = IntGen.biased(10);
Gen<Integer> g6 = IntGen.biased(-20, 20);
```

### LongGen

```java
import fun.gen.*;

Gen<Long> g1 = LongGen.arbitrary();
Gen<Long> g2 = LongGen.arbitrary(10L);
Gen<Long> g3 = LongGen.arbitrary(-100L, 100L);
Gen<Long> g4 = LongGen.biased();
Gen<Long> g5 = LongGen.biased(10L);
Gen<Long> g6 = LongGen.biased(-100L, 100L);
```

### DoubleGen

```java
import fun.gen.*;

Gen<Double> g1 = DoubleGen.arbitrary();
Gen<Double> g2 = DoubleGen.arbitrary(-10.0, 10.0);
Gen<Double> g3 = DoubleGen.biased();
Gen<Double> g4 = DoubleGen.biased(-10.0, 10.0);
```

### BigIntGen

```java
import fun.gen.*;

import java.math.BigInteger;

Gen<BigInteger> g1 = BigIntGen.arbitrary();
Gen<BigInteger> g2 = BigIntGen.arbitrary(BigInteger.valueOf(-1_000), BigInteger.valueOf(1_000));
Gen<BigInteger> g3 = BigIntGen.biased();
Gen<BigInteger> g4 = BigIntGen.biased(BigInteger.valueOf(-1_000), BigInteger.valueOf(1_000));
```

### BigDecGen

```java
import fun.gen.*;

import java.math.BigDecimal;

Gen<BigDecimal> g1 = BigDecGen.arbitrary();
Gen<BigDecimal> g2 = BigDecGen.arbitrary(new BigDecimal("-100.00"), new BigDecimal("100.00"));
Gen<BigDecimal> g3 = BigDecGen.biased();
Gen<BigDecimal> g4 = BigDecGen.biased(new BigDecimal("-100.00"), new BigDecimal("100.00"));
```

Note: bounded `BigDecGen.arbitrary(min, max)` is cent-scale (`scale=2`) by design.

### BoolGen

```java
import fun.gen.*;

Gen<Boolean> gb = BoolGen.arbitrary();
```

### CharGen

```java
import fun.gen.*;

Gen<Character> g1 = CharGen.arbitrary();
Gen<Character> g2 = CharGen.arbitrary('a', 'z');
Gen<Character> g3 = CharGen.ascii();
Gen<Character> g4 = CharGen.letter();
Gen<Character> g5 = CharGen.digit();
Gen<Character> g6 = CharGen.alphabetic();
```

### StrGen

```java
import fun.gen.*;

Gen<String> g1 = StrGen.arbitrary(0, 40);
Gen<String> g2 = StrGen.biased(0, 40);
Gen<String> g3 = StrGen.digits(1, 12);
Gen<String> g4 = StrGen.ascii(0, 40);
Gen<String> g5 = StrGen.letters(1, 30);
Gen<String> g6 = StrGen.alphabetic(1, 30);
Gen<String> g7 = StrGen.alphanumeric(1, 30);
```

### BytesGen

```java
import fun.gen.*;

Gen<byte[]> g1 = BytesGen.arbitrary(0, 256);
Gen<byte[]> g2 = BytesGen.biased(0, 256);
```

### InstantGen

```java
import fun.gen.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

Gen<java.time.Instant> g1 = InstantGen.arbitrary();
Gen<java.time.Instant> g2 = InstantGen.biased();
Gen<java.time.Instant> g3 = InstantGen.arbitrary(0L, 4_102_444_800L); // [1970, 2100]
Gen<java.time.Instant> g4 = InstantGen.biased(0L, 4_102_444_800L);

ZonedDateTime min = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
ZonedDateTime max = ZonedDateTime.of(2030, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
Gen<java.time.Instant> g5 = InstantGen.arbitrary(min, max);
Gen<java.time.Instant> g6 = InstantGen.biased(min, max);
```

### ListGen

```java
import fun.gen.*;

Gen<java.util.List<Integer>> g1 = ListGen.ofN(IntGen.arbitrary(0, 10), 5);
Gen<java.util.List<Integer>> g2 = ListGen.arbitrary(IntGen.arbitrary(0, 10), 0, 20);
Gen<java.util.List<Integer>> g3 = ListGen.biased(IntGen.arbitrary(0, 10), 0, 20);
```

### SetGen

```java
import fun.gen.*;

SetGen<Integer> base = SetGen.ofN(IntGen.arbitrary(0, 1000), 10);
SetGen<Integer> tuned = base.withMaxTries(5_000);
```

### MapGen

```java
import fun.gen.*;

MapGen<String, Integer> fixed = MapGen.of(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 5);
MapGen<String, Integer> fixedWithAlias = MapGen.ofN(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 5);
MapGen<String, Integer> tuned = fixed.withMaxTries(10_000);

Gen<java.util.Map<String, Integer>> arbitraryMap =
        MapGen.arbitrary(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 0, 20);

Gen<java.util.Map<String, Integer>> biasedMap =
        MapGen.biased(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 0, 20);
```

### NamedGen

```java
import fun.gen.*;

Gen<Integer> deferred = NamedGen.of("counter");
Gen<Integer> resolved = NamedGen.of("counter", IntGen.arbitrary(0, 100));
```

### Pair/Triple/Quadruple/Quintuple/Sextuple generators

```java
import fun.gen.*;
import fun.tuple.*;

Gen<Pair<String, Integer>> p = PairGen.of(StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100));
Gen<Triple<String, Integer, Boolean>> t = TripleGen.of(StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary());
Gen<Quadruple<String, Integer, Boolean, Long>> q4 = QuadrupleGen.of(
        StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary(), LongGen.arbitrary(0L, 1000L)
);
Gen<Quintuple<String, Integer, Boolean, Long, Double>> q5 = QuintupleGen.of(
        StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary(), LongGen.arbitrary(0L, 1000L), DoubleGen.arbitrary(-1, 1)
);
Gen<Sextuple<String, Integer, Boolean, Long, Double, Character>> q6 = SextupleGen.of(
        StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary(), LongGen.arbitrary(0L, 1000L), DoubleGen.arbitrary(-1, 1), CharGen.letter()
);
```

## Combinators Cookbook (All Public Methods)

```java
import fun.gen.*;
import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// oneOf from constant values (varargs)
Gen<String> c1 = Combinators.oneOf("A", "B", "C");

// oneOf from List / Set
Gen<String> c2 = Combinators.oneOf(List.of("X", "Y", "Z"));
Gen<String> c3 = Combinators.oneOf(Set.of("red", "green", "blue"));

// nOf from List / Set
Gen<List<String>> c4 = Combinators.nOf(List.of("a", "b", "c", "d"), 2);
Gen<Set<String>> c5 = Combinators.nOf(Set.of("a", "b", "c", "d"), 2);

// oneOf from generators (varargs)
Gen<Integer> c6 = Combinators.oneOf(
        IntGen.arbitrary(0, 10),
        IntGen.arbitrary(100, 110),
        IntGen.arbitrary(1000, 1010)
);

// oneOf from generator list
Gen<Integer> c7 = Combinators.oneOfList(List.of(
        IntGen.arbitrary(0, 10),
        IntGen.arbitrary(100, 110)
));

// weighted choice
Gen<Integer> c8 = Combinators.freq(
        Pair.of(7, IntGen.arbitrary(0, 10)),
        Pair.of(2, IntGen.arbitrary(100, 110)),
        Pair.of(1, IntGen.arbitrary(1000, 1010))
);

// nullable default (50%) and custom probability
Gen<String> c9 = Combinators.nullable(StrGen.alphabetic(1, 8));
Gen<String> c10 = Combinators.nullable(StrGen.alphabetic(1, 8), 20);

// combinations from list or set
Gen<Set<Integer>> c11 = Combinators.combinations(2, List.of(1, 2, 3, 4));
Gen<Set<Integer>> c12 = Combinators.combinations(2, Set.of(1, 2, 3, 4));

// all subsets from list or set
Gen<Set<Integer>> c13 = Combinators.subsets(List.of(1, 2, 3));
Gen<Set<Integer>> c14 = Combinators.subsets(Set.of(1, 2, 3));

// shuffle
Gen<List<Integer>> c15 = Combinators.shuffle(List.of(1, 2, 3, 4, 5));

// swap utility (in-place)
List<String> xs = new ArrayList<>(List.of("a", "b", "c"));
Combinators.swap(xs, 0, 2);
System.out.println(xs); // [c, b, a]
```

Useful practical scenarios:
- Generate partially null DTOs with `nullable(...)` to harden null handling.
- Stress parser edge cases with weighted `freq(...)` (e.g., more malformed values).
- Exhaustively test small set behaviors with `subsets(...)` and `combinations(...)`.

## MyRecord and MyRecordGen

`MyRecord` access patterns:

```java
import fun.gen.MyRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

MyRecord rec = new MyRecord(Map.of(
        "name", "ana",
        "age", 32,
        "active", true,
        "salary", BigDecimal.valueOf(1000),
        "tags", List.of("dev", "java")
));

String name = rec.getString("name");
Integer age = rec.getInt("age");
Boolean active = rec.getBoolean("active");
BigDecimal salary = rec.getDecimal("salary");
List<String> tags = rec.getList("tags");

String nick = rec.getOptionalString("nick").orElse("n/a");

boolean hasName = rec.containsKey("name");
int size = rec.size();
Map<String, ?> raw = rec.asMap();
```

`MyRecordGen` setup patterns:

```java
import fun.gen.*;

// Using overloaded of(...)
MyRecordGen g1 = MyRecordGen.of("id", IntGen.arbitrary(1, 1_000));
MyRecordGen g2 = MyRecordGen.of(
        "id", IntGen.arbitrary(1, 1_000),
        "name", StrGen.alphabetic(1, 30)
);

// Dynamic builder style using of() + set(...)
MyRecordGen dynamic = MyRecordGen.of()
        .set("id", IntGen.arbitrary(1, 1_000))
        .set("name", StrGen.alphabetic(1, 30))
        .set("age", IntGen.arbitrary(18, 99));

// Optional/required/nullable controls
MyRecordGen tuned = dynamic
        .withOptKeys("age")
        .withReqKeys("id", "name")
        .withNullValues("name");

MyRecordGen allOptional = tuned.withAllOptKeys();
MyRecordGen allNullable = tuned.withAllNullValues();

Gen<MyRecord> users = tuned;
```

## CSV Ingestion

```java
import fun.gen.*;

import java.io.File;

try (var rows = CsvStreamBuilder.of(new File("users.csv"), ",")
        .withExpectedHeaders("id", "name", "age", "active")
        .withHeaderMapper(String::trim)
        .withValueMapper((header, value) -> value)
        .withTrimValues(true)
        .withStrictRowWidth()
        .withNullTokens("", "null", "N/A")
        .withNullTokenMatcher(token -> token.equalsIgnoreCase("none"))
        .withErrorCollector((row, ex) -> System.err.println("Row " + row + " error: " + ex.getMessage()))
        .withSkipMalformedRows()
        .get()) {

    rows.forEach(r -> {
        Integer id = r.getInt("id");
        String name = r.getString("name");
        Integer age = r.getInt("age");
        Boolean active = r.getBoolean("active");
        System.out.println(id + " " + name + " " + age + " " + active);
    });
}
```

If you need raw string values only:

```java
import fun.gen.*;

CsvStreamBuilder.of(new java.io.File("raw.csv"), ";")
        .withoutTypeConversion()
        .get();
```

## Recursive Generators

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

```java
import fun.optic.Lens;

record Address(String city) {}
record User(String name, Address address) {}

Lens<User, Address> userAddress = new Lens<>(
        User::address,
        address -> user -> new User(user.name(), address)
);

Lens<Address, String> addressCity = new Lens<>(
        Address::city,
        city -> address -> new Address(city)
);

Lens<User, String> userCity = userAddress.compose(addressCity);

User before = new User("Ana", new Address("Madrid"));
User after = userCity.set.apply("Valencia").apply(before);
System.out.println(after);
```

## Testing Tips

- Start with `biased()` generators to hit boundaries quickly.
- Use `collect(n)` to lock expected distribution behavior.
- Use `distinct(tries)` only where uniqueness is a requirement.
- Keep a fixed `RandomGenerator` seed in failing tests for reproducibility.
- Prefer small composable generators over one big opaque generator.

## Migration Guide (3.x -> 4.0)

Main breaking changes:

- `getOptXxx(...)` -> `getOptionalXxx(...)`
- `getStr(...)` -> `getString(...)`
- `getBool(...)` -> `getBoolean(...)`
- `getBigInt(...)` -> `getBigInteger(...)`
- `record.map` public field removed.

Migration example:

```java
// Before
// record.map.get("x")
// record.getOptStr("name")
// record.getBool("active")

// After
record.asMap().get("x");
record.getOptionalString("name");
record.getBoolean("active");
```

## Related Projects

- [json-values](https://github.com/imrafaelmerino/json-values)

---

If you use `java-fun` in production or research, feedback and PRs with real workloads are very welcome.
