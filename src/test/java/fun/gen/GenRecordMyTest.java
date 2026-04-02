package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GenRecordMyTest {

    private static final List<Entry<String, Gen<?>>> COMPACT_OF_FIELDS = List.of(
            Map.entry("a", IntGen.arbitrary()),
            Map.entry("b", StrGen.letters(1, 1)),
            Map.entry("c", BoolGen.arbitrary()),
            Map.entry("d", LongGen.arbitrary()),
            Map.entry("e", DoubleGen.arbitrary()),
            Map.entry("f", DoubleGen.arbitrary()),
            Map.entry("g", BytesGen.arbitrary(0, 1024)),
            Map.entry("h", ListGen.arbitrary(BigDecGen.arbitrary(), 0, 10)),
            Map.entry("i", BoolGen.arbitrary()),
            Map.entry("j", IntGen.biased()),
            Map.entry("k", IntGen.biased()),
            Map.entry("l", LongGen.biased()),
            Map.entry("m", BigIntGen.arbitrary(BigInteger.ONE, BigInteger.TEN)),
            Map.entry("n", BigDecGen.biased()),
            Map.entry("o", BigDecGen.arbitrary()),
            Map.entry("p", CharGen.arbitrary()),
            Map.entry("q", DoubleGen.biased()),
            Map.entry("r", DoubleGen.arbitrary()),
            Map.entry("s", InstantGen.biased()),
            Map.entry("t", InstantGen.arbitrary(1_000_000, 1_000_000_000))
    );

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void shouldGenerateRecordsWithExpectedArityWhenUsingCompactOfFactory(int fieldCount) {
        MyRecordGen gen = compactOfFirstNFields(fieldCount);
        Assertions.assertTrue(gen.sample(100).allMatch(it -> it.asMap().size() == fieldCount));
    }


    @Test
    void shouldGenerateRecordsWhenUsingConfiguredFieldGenerators() {

        MyRecordGen gen = MyRecordGen.of("a",
                                         IntGen.arbitrary(0,
                                                      10),
                                         "b",
                                         StrGen.letters(1,
                                                    1),
                                         "c",
                                         BoolGen.arbitrary());


        Map<MyRecord, Long> counts = TestFun.generate(5000000,
                                                      gen);


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         counts.keySet(),
                                                         0.1);

    }

    @Test
    void shouldGenerateOptionalFieldsWhenConfiguringOptionalFields() {

        MyRecordGen gen = MyRecordGen.of("a",
                                         IntGen.arbitrary(0,
                                                      40000),
                                         "b",
                                         IntGen.arbitrary(0,
                                                      40000),
                                         "c",
                                         IntGen.arbitrary(0,
                                                      40000)
        ).withOptKeys("a",
                      "b");


        int times = 1000000;
        Map<List<?>, Long> generated =
                Stream.generate(gen.sample(new Random(0L)))
                      .limit(times)
                      .map(record -> {
                          if (record.getOptionalInt("a").isPresent()
                                  && record.getOptionalInt("b").isPresent()
                                  && record.getOptionalInt("c").isPresent())
                              return Arrays.asList("a",
                                                   "b",
                                                   "c");
                          if (record.getOptionalInt("a").isPresent()
                                  && record.getOptionalInt("b").isPresent()
                          )
                              return Arrays.asList("a",
                                                   "b");
                          if (record.getOptionalInt("a").isPresent()
                                  && record.getOptionalInt("c").isPresent()
                          )
                              return Arrays.asList("a",
                                                   "c");
                          if (record.getOptionalInt("b").isPresent()
                                  && record.getOptionalInt("c").isPresent()
                          )
                              return Arrays.asList("b",
                                                   "c");
                          if (record.getOptionalInt("a").isPresent())
                              return List.of("a");
                          if (record.getOptionalInt("b").isPresent())
                              return List.of("b");
                          if (record.getOptionalInt("c").isPresent())
                              return List.of("c");
                          return new ArrayList<>();
                      })
                      .collect(Collectors.groupingBy(Function.identity(),
                                                     Collectors.counting()));

        Function<Long, Double> toPer =
                e -> ((double) (e * 100)) / times;

        Predicate<Map.Entry<List<?>, Long>> containsAll =
                it -> it.getKey().containsAll(Arrays.asList("a",
                                                            "b",
                                                            "c"));
        Assertions.assertTrue(generated
                                      .entrySet()
                                      .stream()
                                      .filter(containsAll)
                                      .map(Entry::getValue)
                                      .map(toPer)
                                      .allMatch(it -> it <= 50.2 && it >= 49.8));

    }

    @Test
    void shouldKeepThoseKeysAlwaysPresentWhenUsingWithReqKeysCollection() {
        MyRecordGen gen = MyRecordGen.of("a",
                                         IntGen.arbitrary(0,
                                                          10),
                                         "b",
                                         IntGen.arbitrary(0,
                                                          10),
                                         "c",
                                         IntGen.arbitrary(0,
                                                          10))
                                   .withAllOptKeys()
                                   .withReqKeys(List.of("a",
                                                        "b"));

        Assertions.assertTrue(gen.sample(2000)
                                 .allMatch(record -> record.asMap().containsKey("a")
                                         && record.asMap().containsKey("b")));
    }


    @Test
    void shouldGenerateNullableAndOptionalFieldsWhenCombiningBoth() {

        MyRecordGen gen = MyRecordGen.of("a",
                                         IntGen.arbitrary(0,
                                                      40000),
                                         "b",
                                         StrGen.letters(1,
                                                    1),
                                         "c",
                                         IntGen.arbitrary(0,
                                                      1000)
        );

        int times = 5_000_000;
        Function<MyRecord, List<String>> nonNullKeys = r -> {
            if (r.asMap().get("a") != null
                    && r.asMap().get("b") != null
                    && r.asMap().get("c") != null)
                return Arrays.asList("a",
                                     "b",
                                     "c");
            if (r.asMap().get("a") != null
                    && r.asMap().get("b") != null
                    && r.asMap().get("c") == null)
                return Arrays.asList("a",
                                     "b");
            if (r.asMap().get("a") != null
                    && r.asMap().get("b") == null
                    && r.asMap().get("c") != null)
                return Arrays.asList("a",
                                     "c");
            if (r.asMap().get("a") == null
                    && r.asMap().get("b") != null
                    && r.asMap().get("c") != null)
                return Arrays.asList("b",
                                     "c");
            if (r.asMap().get("a") == null
                    && r.asMap().get("c") == null
                    && r.asMap().get("b") != null)
                return List.of("b");
            if (r.asMap().get("a") == null
                    && r.asMap().get("c") != null
                    && r.asMap().get("b") == null)
                return List.of("c");
            if (r.asMap().get("a") != null
                    && r.asMap().get("c") == null
                    && r.asMap().get("b") == null)
                return List.of("a");

            return List.of();
        };

        Function<MyRecord, List<String>> presentKeys = r -> {
            if (r.asMap().containsKey("a")
                    && r.asMap().containsKey("b")
                    && r.asMap().containsKey("c"))
                return Arrays.asList("a",
                                     "b",
                                     "c");
            if (r.asMap().containsKey("a")
                    && r.asMap().containsKey("b")
                    && !r.asMap().containsKey("c"))
                return Arrays.asList("a",
                                     "b");
            if (r.asMap().containsKey("a")
                    && !r.asMap().containsKey("b")
                    && r.asMap().containsKey("c"))
                return Arrays.asList("a",
                                     "c");
            if (!r.asMap().containsKey("a")
                    && r.asMap().containsKey("b")
                    && r.asMap().containsKey("c"))
                return Arrays.asList("b",
                                     "c");
            if (!r.asMap().containsKey("a")
                    && !r.asMap().containsKey("c")
                    && r.asMap().containsKey("b"))
                return List.of("b");
            if (!r.asMap().containsKey("a")
                    && r.asMap().containsKey("c")
                    && !r.asMap().containsKey("b"))
                return List.of("c");
            if (r.asMap().containsKey("a")
                    && !r.asMap().containsKey("c")
                    && !r.asMap().containsKey("b"))
                return List.of("a");

            return List.of();
        };

        Predicate<Map.Entry<List<String>, Long>> containsAll =
                it -> it.getKey().containsAll(Arrays.asList("a",
                                                            "b",
                                                            "c"));
        Function<Map.Entry<List<String>, Long>, Double> toPer =
                e -> ((double) (e.getValue() * 100)) / times;

        Assertions.assertTrue(gen.withAllNullValues()
                                 .collect(times,
                                          nonNullKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll)
                                 .map(toPer)
                                 .allMatch(it -> it <= 50.5 && it >= 49.5));

        Assertions.assertTrue(gen.withAllOptKeys()
                                 .collect(times,
                                          presentKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll)
                                 .map(toPer)
                                 .allMatch(it -> it <= 50.5 && it >= 49.5));


        Assertions.assertTrue(
                gen
                        .withAllNullValues()
                        .collect(times,
                                 nonNullKeys)
                        .entrySet()
                        .stream()
                        .filter(containsAll.negate())
                        .map(toPer)
                        .allMatch(it -> it <= 7.2 && it >= 6.8));

        Assertions.assertTrue(
                gen
                        .withNullValues("a",
                                        "b",
                                        "c")
                        .collect(times,
                                 nonNullKeys)
                        .entrySet()
                        .stream()
                        .filter(containsAll.negate())
                        .map(toPer)
                        .allMatch(it -> it <= 7.2 && it >= 6.8));

        Assertions.assertTrue(gen.withAllOptKeys()
                                 .collect(times,
                                          presentKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll.negate())
                                 .map(toPer)
                                 .allMatch(it -> it <= 7.2 && it >= 6.8));

        Assertions.assertTrue(gen.withOptKeys("a",
                                              "b",
                                              "c")
                                 .collect(times,
                                          presentKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll.negate())
                                 .map(toPer)
                                 .allMatch(it -> it <= 7.2 && it >= 6.8));
    }


    @Test
    void shouldExposeGeneratedRecordDataWhenReadingWithTypedGetters() {

        Assertions.assertTrue(MyRecordGen.of("a",
                                             IntGen.biased())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalInt("a").isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             LongGen.biased())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalLong("a").isPresent()));


        Assertions.assertTrue(MyRecordGen.of("a",
                                             BigIntGen.arbitrary(new BigInteger("1000000000"),
                                                               new BigInteger("2000000000")))
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalBigInteger("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             BigDecGen.arbitrary())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalDecimal("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             DoubleGen.arbitrary())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalDouble("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             StrGen.biased(1,
                                                         100))
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalString("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             CharGen.alphabetic())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalChar("a")
                                                         .isPresent()));


        Assertions.assertTrue(MyRecordGen.of("a",
                                             ListGen.biased(IntGen.arbitrary(),
                                                          1,
                                                          100))
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalListView("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             InstantGen.arbitrary())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalInstant("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             BoolGen.arbitrary())
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalBoolean("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             BytesGen.arbitrary(1,
                                                              10))
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalBytes("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             SetGen.ofN(IntGen.arbitrary(),
                                                      10))
                                         .sample(100)
                                         .allMatch(it -> it.getOptionalSetView("a")
                                                         .isPresent()));

        Assertions.assertTrue(MyRecordGen.of("a",
                                             MapGen.of(StrGen.alphanumeric(1,
                                                                         10),
                                                     IntGen.arbitrary(),
                                                     100))
                                         .sample(100)
                                         .allMatch(it -> {
                                           Optional<Map<String, Integer>> a = it.getOptionalMapView("a");
                                           return a.isPresent();
                                       }));
    }

    private static MyRecordGen compactOfFirstNFields(int fieldCount) {
        List<Entry<String, Gen<?>>> fields = COMPACT_OF_FIELDS.subList(0, fieldCount);
        Entry<String, Gen<?>> first = fields.getFirst();
        Object[] additional = new Object[(fields.size() - 1) * 2];
        int offset = 0;
        for (int i = 1; i < fields.size(); i++) {
            Entry<String, Gen<?>> field = fields.get(i);
            additional[offset++] = field.getKey();
            additional[offset++] = field.getValue();
        }
        return MyRecordGen.of(first.getKey(), first.getValue(), additional);
    }


}
