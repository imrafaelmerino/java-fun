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
// sample: ["aK9", "x2Pq7", "ZZ81m"]
Gen<Integer> ages = IntGen.biased(0, 120);
// sample: [0, 120, 37, 18, 65]

System.out.println(usernames.sample(5).toList());
System.out.println(ages.sample(5).toList());
System.out.println(Combinators.oneOf("A", "B", "C").collect(10_000));
```

Domain object generation:

```java
import fun.gen.*;

record User(String login, String name, Integer age) {}

Gen<User> users = MyRecordGen.builder()
        .field("login", StrGen.alphanumeric(3, 20))
        .field("name", StrGen.alphabetic(1, 40))
        .field("age", IntGen.arbitrary(18, 99))
        .build()
        .map(r -> new User(
        r.getString("login"),
        r.getString("name"),
        r.getInt("age")
));
// sample: [User[login=a1Z, name=Ana, age=33], User[login=q9kP, name=Leo, age=21]]

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
// sample: [42, 7, 90, 13, 58]

Gen<String> mapped = base.map(Object::toString);
// sample: ["42", "7", "90"]
Gen<Integer> distinct = base.distinct();
// sample: [3, 81, 47, 10]
Gen<Integer> distinctWithLimit = base.distinct(500);
// sample: [22, 11, 66, 4]
Gen<Integer> filtered = base.filter(n -> n % 2 == 0);
// sample: [84, 2, 56, 100]
Gen<Integer> filteredWithLimit = base.filter(n -> n > 90, 2000);
// sample: [91, 99, 94]
Gen<String> chained = base.flatMap(n -> StrGen.alphanumeric(1, Math.max(1, n % 10)));
// sample: ["A", "m9", "x7Q2"]
Gen<Integer> withSideEffect = base.peek(n -> System.out.println("generated=" + n));
// sample: [15, 73, 0]

System.out.println(base.sample().get());
System.out.println(base.sample(5).toList());
System.out.println(base.sample(5, 42L).toList()); // deterministic
System.out.println(base.collect(1000));
System.out.println(base.collect(1000, 42L));      // deterministic
Map<String, Long> parity = base.collect(1000, n -> n % 2 == 0 ? "even" : "odd");
System.out.println(parity);
```

Idiomatic naming note:
- Prefer `Gen.constant(...)`, `flatMap(...)`, and `filter(...)`.
- Legacy names `cons(...)`, `then(...)`, and `suchThat(...)` were removed.

Failure semantics:
- `distinct(...)`, `SetGen`, and `MapGen` throw `GenerationExhaustedException` if uniqueness goals cannot be reached in the configured tries.
- `filter(...)` throws `UnsatisfiableConstraintException` when the predicate cannot be satisfied in the configured tries.

## Generators Cookbook (Method by Method)

### IntGen

```java
import fun.gen.*;

Gen<Integer> g1 = IntGen.arbitrary();
// sample: [2147483647, -113, 0]
Gen<Integer> g2 = IntGen.arbitrary(10);
// sample: [10, 459, 2147483647]
Gen<Integer> g3 = IntGen.arbitrary(-20, 20);
// sample: [-3, 0, 19, -20, 20]
Gen<Integer> g4 = IntGen.biased();
// sample: [0, -128, 32767, 42]
Gen<Integer> g5 = IntGen.biased(10);
// sample: [10, 127, 32767, 9999]
Gen<Integer> g6 = IntGen.biased(-20, 20);
// sample: [-20, 0, 20, 7]
```

### LongGen

```java
import fun.gen.*;

Gen<Long> g1 = LongGen.arbitrary();
// sample: [9223372036854775807, -19, 0]
Gen<Long> g2 = LongGen.arbitrary(10L);
// sample: [10, 482, 99999999]
Gen<Long> g3 = LongGen.arbitrary(-100L, 100L);
// sample: [-91, 0, 74, 100]
Gen<Long> g4 = LongGen.biased();
// sample: [0, -128, 2147483647, 15]
Gen<Long> g5 = LongGen.biased(10L);
// sample: [10, 127, 32767, 4000]
Gen<Long> g6 = LongGen.biased(-100L, 100L);
// sample: [-100, 0, 100, 31]
```

### DoubleGen

```java
import fun.gen.*;

Gen<Double> g1 = DoubleGen.arbitrary();
// sample: [0.7312, 0.1044, 0.9981]
Gen<Double> g2 = DoubleGen.arbitrary(-10.0, 10.0);
// sample: [-9.22, 0.01, 8.77]
Gen<Double> g3 = DoubleGen.biased();
// sample: [0.0, 127.0, -128.0, 0.443]
Gen<Double> g4 = DoubleGen.biased(-10.0, 10.0);
// sample: [-10.0, 0.0, 10.0, 4.56]
```

### BigIntGen

```java
import fun.gen.*;

import java.math.BigInteger;

Gen<BigInteger> g1 = BigIntGen.arbitrary();
// sample: [0, 18446744073709551615, 90234]
Gen<BigInteger> g2 = BigIntGen.arbitrary(BigInteger.valueOf(-1_000), BigInteger.valueOf(1_000));
// sample: [-1000, -2, 0, 999]
Gen<BigInteger> g3 = BigIntGen.biased();
// sample: [0, 2147483648, -9223372036854775809]
Gen<BigInteger> g4 = BigIntGen.biased(BigInteger.valueOf(-1_000), BigInteger.valueOf(1_000));
// sample: [-1000, 0, 1000, 321]
```

### BigDecGen

```java
import fun.gen.*;

import java.math.BigDecimal;

Gen<BigDecimal> g1 = BigDecGen.arbitrary();
// sample: [0.13, 0.91, 0.44]
Gen<BigDecimal> g2 = BigDecGen.arbitrary(new BigDecimal("-100.00"), new BigDecimal("100.00"));
// sample: [-99.75, 0.42, 100.00]
Gen<BigDecimal> g3 = BigDecGen.biased();
// sample: [0, 127, -128, 0.77]
Gen<BigDecimal> g4 = BigDecGen.biased(new BigDecimal("-100.00"), new BigDecimal("100.00"));
// sample: [-100.00, 0, 100.00, 18.22]
```

Note: bounded `BigDecGen.arbitrary(min, max)` is cent-scale (`scale=2`) by design.

### BoolGen

```java
import fun.gen.*;

Gen<Boolean> gb = BoolGen.arbitrary();
// sample: [true, false, true, true]
```

### CharGen

```java
import fun.gen.*;

Gen<Character> g1 = CharGen.arbitrary();
// sample: ['\u0001', 'A', 'z']
Gen<Character> g2 = CharGen.arbitrary('a', 'z');
// sample: ['a', 'm', 'z']
Gen<Character> g3 = CharGen.ascii();
// sample: ['#', 'A', '9']
Gen<Character> g4 = CharGen.letter();
// sample: ['a', 'B', 'z']
Gen<Character> g5 = CharGen.digit();
// sample: ['0', '7', '9']
Gen<Character> g6 = CharGen.alphabetic();
// sample: ['Ñ', 'k', 'Ж']
```

### StrGen

```java
import fun.gen.*;

Gen<String> g1 = StrGen.arbitrary(0, 40);
// sample: ["", "a7$Q", "lorem123"]
Gen<String> g2 = StrGen.biased(0, 40);
// sample: ["", "          ", "abc"]
Gen<String> g3 = StrGen.digits(1, 12);
// sample: ["7", "045", "998211"]
Gen<String> g4 = StrGen.ascii(0, 40);
// sample: ["", "A!7", "x_y"]
Gen<String> g5 = StrGen.letters(1, 30);
// sample: ["a", "bCd", "xYz"]
Gen<String> g6 = StrGen.alphabetic(1, 30);
// sample: ["á", "ñQw", "Жk"]
Gen<String> g7 = StrGen.alphanumeric(1, 30);
// sample: ["a1", "Z9k2", "m7"]
```

### BytesGen

```java
import fun.gen.*;

Gen<byte[]> g1 = BytesGen.arbitrary(0, 256);
// sample: [[], [12, -4, 99], [0, 1, 2, 3]]
Gen<byte[]> g2 = BytesGen.biased(0, 256);
// sample: [[], [7, 8, 9], [...256 bytes...]]
```

### InstantGen

```java
import fun.gen.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

Gen<java.time.Instant> g1 = InstantGen.arbitrary();
// sample: [1970-01-01T00:00:00Z, 2038-01-19T03:14:07Z]
Gen<java.time.Instant> g2 = InstantGen.biased();
// sample: [1970-01-01T00:00:00Z, 1901-12-13T20:45:52Z]
Gen<java.time.Instant> g3 = InstantGen.arbitrary(0L, 4_102_444_800L); // [1970, 2100]
// sample: [1975-05-03T10:00:00Z, 2099-12-31T23:59:59Z]
Gen<java.time.Instant> g4 = InstantGen.biased(0L, 4_102_444_800L);
// sample: [1970-01-01T00:00:00Z, 2100-01-01T00:00:00Z]

ZonedDateTime min = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
ZonedDateTime max = ZonedDateTime.of(2030, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
Gen<java.time.Instant> g5 = InstantGen.arbitrary(min, max);
// sample: [2001-07-12T11:22:33Z, 2024-03-01T00:00:00Z]
Gen<java.time.Instant> g6 = InstantGen.biased(min, max);
// sample: [2000-01-01T00:00:00Z, 2030-01-01T00:00:00Z, 2015-10-20T08:00:00Z]
```

### ListGen

```java
import fun.gen.*;

Gen<java.util.List<Integer>> g1 = ListGen.ofN(IntGen.arbitrary(0, 10), 5);
// sample: [[1, 5, 9, 0, 3], [10, 10, 4, 2, 8]]
Gen<java.util.List<Integer>> g2 = ListGen.arbitrary(IntGen.arbitrary(0, 10), 0, 20);
// sample: [[], [3, 7], [1, 0, 2, 9]]
Gen<java.util.List<Integer>> g3 = ListGen.biased(IntGen.arbitrary(0, 10), 0, 20);
// sample: [[], [4, 1, 9, 8, 0, ...], [6]]
```

### SetGen

```java
import fun.gen.*;

SetGen<Integer> base = SetGen.ofN(IntGen.arbitrary(0, 1000), 10);
// sample: [{1, 7, 42, ...10 elems...}, {0, 15, 999, ...}]
SetGen<Integer> tuned = base.withMaxTries(5_000);
// sample: [{2, 13, 88, ...10 elems...}]
```

### MapGen

```java
import fun.gen.*;

MapGen<String, Integer> fixed = MapGen.of(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 5);
// sample: [{a1B=12, X9k=77, ...5 entries...}]
MapGen<String, Integer> fixedWithAlias = MapGen.ofN(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 5);
// sample: [{k3L=4, p8Q=90, ...5 entries...}]
MapGen<String, Integer> tuned = fixed.withMaxTries(10_000);
// sample: [{ab1=0, zz9=100, ...5 entries...}]

Gen<java.util.Map<String, Integer>> arbitraryMap =
        MapGen.arbitrary(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 0, 20);
// sample: [{}, {u7P=11}, {a1B=12, q9W=33, ...}]

Gen<java.util.Map<String, Integer>> biasedMap =
        MapGen.biased(StrGen.alphanumeric(3, 8), IntGen.arbitrary(0, 100), 0, 20);
// sample: [{}, {...20 entries...}, {x1=4, y2=9}]
```

### NamedGen

```java
import fun.gen.*;

Gen<Integer> deferred = NamedGen.of("counter");
// sample: [depends on later registration of "counter"]
Gen<Integer> resolved = NamedGen.of("counter", IntGen.arbitrary(0, 100));
// sample: [0, 57, 100, 22]
```

### Pair/Triple/Quadruple/Quintuple/Sextuple generators

```java
import fun.gen.*;
import fun.tuple.*;

Gen<Pair<String, Integer>> p = PairGen.of(StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100));
// sample: [(ana, 24), (leo, 99)]
Gen<Triple<String, Integer, Boolean>> t = TripleGen.of(StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary());
// sample: [(ana, 24, true), (leo, 99, false)]
Gen<Quadruple<String, Integer, Boolean, Long>> q4 = QuadrupleGen.of(
        StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary(), LongGen.arbitrary(0L, 1000L)
);
// sample: [(a, 1, true, 900), (b, 88, false, 12)]
Gen<Quintuple<String, Integer, Boolean, Long, Double>> q5 = QuintupleGen.of(
        StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary(), LongGen.arbitrary(0L, 1000L), DoubleGen.arbitrary(-1, 1)
);
// sample: [(a, 1, true, 900, 0.33), (b, 88, false, 12, -0.4)]
Gen<Sextuple<String, Integer, Boolean, Long, Double, Character>> q6 = SextupleGen.of(
        StrGen.alphabetic(1, 10), IntGen.arbitrary(0, 100), BoolGen.arbitrary(), LongGen.arbitrary(0L, 1000L), DoubleGen.arbitrary(-1, 1), CharGen.letter()
);
// sample: [(a, 1, true, 900, 0.33, 'x'), (b, 88, false, 12, -0.4, 'Q')]
```

## Combinators Cookbook (All Public Methods)

```java
import fun.gen.*;
import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// oneOf from constant values (varargs)
Gen<String> c1 = Combinators.oneOf("A", "B", "C");
// sample: ["A", "C", "B", "A"]
Gen<String> c1v = Combinators.oneOfView("A", "B", "C");
// sample: ["A", "B", "C", "A"]

// oneOf from List / Set
Gen<String> c2 = Combinators.oneOf(List.of("X", "Y", "Z"));
// sample: ["Z", "X", "Y"]
Gen<String> c3 = Combinators.oneOf(Set.of("red", "green", "blue"));
// sample: ["green", "red", "blue"]
Gen<String> c2v = Combinators.oneOfView(new ArrayList<>(List.of("X", "Y", "Z")));
// sample: ["Y", "X", "Z"]
Gen<String> c3v = Combinators.oneOfView(new HashSet<>(Set.of("red", "green", "blue")));
// sample: ["blue", "green", "red"]

// nOf from List / Set
Gen<List<String>> c4 = Combinators.nOf(List.of("a", "b", "c", "d"), 2);
// sample: [["a", "d"], ["b", "c"]]
Gen<Set<String>> c5 = Combinators.nOf(Set.of("a", "b", "c", "d"), 2);
// sample: [{a, d}, {b, c}]
Gen<List<String>> c4v = Combinators.nOfView(new ArrayList<>(List.of("a", "b", "c", "d")), 2);
// sample: [["d", "a"], ["b", "c"]]
Gen<Set<String>> c5v = Combinators.nOfView(new HashSet<>(Set.of("a", "b", "c", "d")), 2);
// sample: [{b, d}, {a, c}]

// oneOf from generators (varargs)
Gen<Integer> c6 = Combinators.oneOf(
        IntGen.arbitrary(0, 10),
        IntGen.arbitrary(100, 110),
        IntGen.arbitrary(1000, 1010)
);
// sample: [5, 108, 1003, 1]
Gen<Integer> c6v = Combinators.oneOfView(
        IntGen.arbitrary(0, 10),
        IntGen.arbitrary(100, 110),
        IntGen.arbitrary(1000, 1010)
);
// sample: [1009, 4, 106, 2]

// oneOf from generator list
Gen<Integer> c7 = Combinators.oneOfList(List.of(
        IntGen.arbitrary(0, 10),
        IntGen.arbitrary(100, 110)
));
// sample: [3, 109, 101, 0]
Gen<Integer> c7v = Combinators.oneOfListView(new ArrayList<>(List.of(
        IntGen.arbitrary(0, 10),
        IntGen.arbitrary(100, 110)
)));
// sample: [8, 104, 1, 107]

// weighted choice
Gen<Integer> c8 = Combinators.freq(
        Pair.of(7, IntGen.arbitrary(0, 10)),
        Pair.of(2, IntGen.arbitrary(100, 110)),
        Pair.of(1, IntGen.arbitrary(1000, 1010))
);
// sample: [1, 7, 4, 103, 2, 0]
// weights must be strictly positive; 0 or negatives throw IllegalArgumentException

// nullable default (50%) and custom probability
Gen<String> c9 = Combinators.nullable(StrGen.alphabetic(1, 8));
// sample: [null, "ana", null, "leo"]
Gen<String> c10 = Combinators.nullable(StrGen.alphabetic(1, 8), 20);
// sample: ["ana", "leo", null, "marta"]

// combinations from list or set
Gen<Set<Integer>> c11 = Combinators.combinations(2, List.of(1, 2, 3, 4));
// sample: [{1, 2}, {1, 4}, {2, 3}]
Gen<Set<Integer>> c12 = Combinators.combinations(2, Set.of(1, 2, 3, 4));
// sample: [{1, 3}, {2, 4}, {1, 2}]
Gen<Set<Integer>> c11v = Combinators.combinationsView(2, new ArrayList<>(List.of(1, 2, 3, 4)));
// sample: [{1, 4}, {2, 3}, {1, 2}] (reflects live source changes)
Gen<Set<Integer>> c12v = Combinators.combinationsView(2, new LinkedHashSet<>(Set.of(1, 2, 3, 4)));
// sample: [{2, 4}, {1, 3}, {1, 2}]

// all subsets from list or set
Gen<Set<Integer>> c13 = Combinators.subsets(List.of(1, 2, 3));
// sample: [{}, {1}, {2, 3}, {1, 2, 3}]
Gen<Set<Integer>> c14 = Combinators.subsets(Set.of(1, 2, 3));
// sample: [{2}, {1, 3}, {1, 2, 3}]
Gen<Set<Integer>> c13v = Combinators.subsetsView(new ArrayList<>(List.of(1, 2, 3)));
// sample: [{}, {1, 2}, {3}]
Gen<Set<Integer>> c14v = Combinators.subsetsView(new HashSet<>(Set.of(1, 2, 3)));
// sample: [{1}, {2, 3}, {1, 2, 3}]

// shuffle
Gen<List<Integer>> c15 = Combinators.shuffle(List.of(1, 2, 3, 4, 5));
// sample: [[3, 1, 5, 4, 2], [2, 5, 1, 3, 4]]
Gen<List<Integer>> c15v = Combinators.shuffleView(new ArrayList<>(List.of(1, 2, 3, 4, 5)));
// sample: [[4, 2, 1, 5, 3], [5, 1, 4, 2, 3]]

// swap utility (in-place)
List<String> xs = new ArrayList<>(List.of("a", "b", "c"));
Combinators.swap(xs, 0, 2);
System.out.println(xs); // [c, b, a]
```

Useful practical scenarios:
- Generate partially null DTOs with `nullable(...)` to harden null handling.
- Stress parser edge cases with weighted `freq(...)` (e.g., more malformed values).
- Exhaustively test small set behaviors with `subsets(...)` and `combinations(...)`.

Safety model:
- Default methods (no `View` suffix) snapshot mutable inputs at construction time (including `combinations(...)`).
- `*View` methods avoid that copy and keep a live reference (faster setup, but caller is responsible for avoiding unsafe external mutations).

## MyRecord and MyRecordGen

`MyRecord` access patterns:

```java
import fun.gen.MyRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

MyRecord rec = new MyRecord(Map.of(
        "name", "ana",
        "age", 32,
        "active", true,
        "salary", BigDecimal.valueOf(1000),
        "tags", List.of("dev", "java"),
        "metadata", Map.of("source", "api")
));

String name = rec.getString("name");
Integer age = rec.getInt("age");
Boolean active = rec.getBoolean("active");
BigDecimal salary = rec.getDecimal("salary");
List<String> tagsView = rec.getListView("tags");
List<String> tagsCopy = rec.getListCopy("tags");
Map<String, Object> metadataView = rec.getMapView("metadata");
Map<String, Object> metadataCopy = rec.getMapCopy("metadata");

String nick = rec.getOptionalString("nick").orElse("n/a");

boolean hasName = rec.containsKey("name");
int size = rec.size();
Map<String, ?> raw = rec.asMap();

Map<String, Object> source = new LinkedHashMap<>();
source.put("name", "ana");
source.put("tags", new ArrayList<>(List.of("dev")));
MyRecord fast = MyRecord.wrap(source); // no deep freeze, live source view
```

Collection access semantics in `MyRecord`:
- `*View` methods return a typed view over the stored frozen value (no extra copy at read time).
- `*Copy` methods return an immutable defensive copy.
- Collection access now uses explicit `*View` and `*Copy` methods only.
- `new MyRecord(map)` performs deep freeze on nested `List`/`Set`/`Map` and `byte[]`.
- `MyRecord.wrap(map)` skips deep-freeze for performance and keeps a live wrapped reference.

`MyRecordGen` setup patterns:

```java
import fun.gen.*;
import fun.tuple.Pair;

// Preferred fluent builder
MyRecordGen g1 = MyRecordGen.builder()
        .field("id", IntGen.arbitrary(1, 1_000))
        .build();
// sample: [{id=731}, {id=12}]
MyRecordGen g2 = MyRecordGen.builder()
        .field("id", IntGen.arbitrary(1, 1_000))
        .field("name", StrGen.alphabetic(1, 30))
        .build();
// sample: [{id=731, name=Ana}, {id=12, name=Leo}]

// Compact declarative entries
MyRecordGen dynamic = MyRecordGen.ofEntries(
        Pair.of("id", IntGen.arbitrary(1, 1_000)),
        Pair.of("name", StrGen.alphabetic(1, 30)),
        Pair.of("age", IntGen.arbitrary(18, 99))
);
// sample: [{id=44, name=Eva, age=31}, {id=901, name=Tom, age=22}]

// Compact call-site style (alternating key/generator pairs)
MyRecordGen compact = MyRecordGen.of(
        "id", IntGen.arbitrary(1, 1_000),
        "name", StrGen.alphabetic(1, 30),
        "age", IntGen.arbitrary(18, 99)
);
// sample: [{id=44, name=Eva, age=31}, {id=901, name=Tom, age=22}]

// Optional/required/nullable controls
MyRecordGen tuned = dynamic
        .withOptKeys("age")
        .withReqKeys("id", "name")
        .withNullValues("name");
// sample: [{id=44, name=null, age=31}, {id=901, name=Tom}]

MyRecordGen allOptional = tuned.withAllOptKeys();
// sample: [{}, {id=3}, {name=Ana, age=29}]
MyRecordGen allNullable = tuned.withAllNullValues();
// sample: [{id=null, name=null, age=null}, {id=7, name=Eva, age=null}]

Gen<MyRecord> users = tuned;
// sample: [{id=44, name=Ana, age=31}, {id=901, name=null}]
```

Migration note:
- The previous giant `MyRecordGen.of(...)` overload family was replaced by one compact varargs form.
- Prefer `MyRecordGen.builder()` or `MyRecordGen.ofEntries(...)` for strongest type-safety and readability.
- Use compact `MyRecordGen.of(...)` when you prioritize concise call sites.

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
        MyRecordGen.builder()
                .field("name", StrGen.alphabetic(1, 20))
                .field("age", IntGen.arbitrary(0, 100))
                .field("parent", NamedGen.of("person"))
                .build()
                .withOptKeys("parent")
);
// sample: [{name=Ana, age=30}, {name=Leo, age=5, parent={name=Ana, age=30}}]
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
- Run fast CI-equivalent checks locally with `mvn -q test jacoco:report jacoco:check`.
- Run statistical/distribution checks explicitly with `mvn -q -Pstats test`.

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
