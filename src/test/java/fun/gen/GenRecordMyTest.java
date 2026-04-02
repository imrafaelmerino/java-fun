package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GenRecordMyTest {

    private static final int SMALL_SAMPLE_SIZE = 100;
    private static final int REQUIRED_KEYS_SAMPLE_SIZE = 2_000;
    private static final int OPTIONAL_FIELDS_SAMPLE_SIZE = 1_000_000;
    private static final int OPTIONAL_AND_NULLABLE_SAMPLE_SIZE = 5_000_000;
    private static final double UNIFORM_GENERATOR_ERROR_MARGIN = 0.1;
    private static final double ALL_KEYS_SHARE_MIN = 49.5;
    private static final double ALL_KEYS_SHARE_MAX = 50.5;
    private static final double PARTIAL_KEYS_SHARE_MIN = 6.8;
    private static final double PARTIAL_KEYS_SHARE_MAX = 7.2;

    private static final List<String> ABC_KEYS = List.of("a", "b", "c");

    private static final List<Map.Entry<String, Gen<?>>> COMPACT_OF_FIELDS = List.of(
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

    @Nested
    class ConstructionTests {

        @ParameterizedTest
        @ValueSource(ints = {
                1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14, 15,
                16, 17, 18, 19, 20
        })
        void shouldGenerateRecordsWithExpectedArityWhenUsingCompactOfFactory(int fieldCount) {
            MyRecordGen gen = compactOfFirstNFields(fieldCount);
            assertAllSamplesHaveArity(gen, SMALL_SAMPLE_SIZE, fieldCount);
        }

        @Test
        void shouldGenerateRecordsWhenUsingConfiguredFieldGenerators() {
            MyRecordGen gen = MyRecordGen.of("a",
                                             IntGen.arbitrary(0, 10),
                                             "b",
                                             StrGen.letters(1, 1),
                                             "c",
                                             BoolGen.arbitrary());

            Map<MyRecord, Long> counts = TestFun.generate(5_000_000, gen);
            TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                             counts.keySet(),
                                                             UNIFORM_GENERATOR_ERROR_MARGIN);
        }
    }

    @Nested
    class OptionalAndNullableTests {

        @Test
        void shouldGenerateOptionalFieldsWhenConfiguringOptionalFields() {
            MyRecordGen gen = MyRecordGen.of("a",
                                             IntGen.arbitrary(0, 40_000),
                                             "b",
                                             IntGen.arbitrary(0, 40_000),
                                             "c",
                                             IntGen.arbitrary(0, 40_000))
                                   .withOptKeys("a", "b");

            Map<List<String>, Long> generated =
                    Stream.generate(gen.sample(new Random(0L)))
                          .limit(OPTIONAL_FIELDS_SAMPLE_SIZE)
                          .map(record -> presentOptionalIntKeys(record, ABC_KEYS))
                          .collect(Collectors.groupingBy(Function.identity(),
                                                         Collectors.counting()));

            assertShareWithinRange(generated,
                                   keys -> keys.containsAll(ABC_KEYS),
                                   OPTIONAL_FIELDS_SAMPLE_SIZE,
                                   49.8,
                                   50.2);
        }

        @Test
        void shouldKeepThoseKeysAlwaysPresentWhenUsingWithReqKeysCollection() {
            MyRecordGen gen = MyRecordGen.of("a",
                                             IntGen.arbitrary(0, 10),
                                             "b",
                                             IntGen.arbitrary(0, 10),
                                             "c",
                                             IntGen.arbitrary(0, 10))
                                   .withAllOptKeys()
                                   .withReqKeys(List.of("a", "b"));

            assertAllSamplesContainKeys(gen, REQUIRED_KEYS_SAMPLE_SIZE, "a", "b");
        }

        @Test
        void shouldGenerateNullableAndOptionalFieldsWhenCombiningBoth() {
            MyRecordGen gen = MyRecordGen.of("a",
                                             IntGen.arbitrary(0, 40_000),
                                             "b",
                                             StrGen.letters(1, 1),
                                             "c",
                                             IntGen.arbitrary(0, 1_000));

            assertAllAndPartialKeyShares(gen.withAllNullValues(),
                                         record -> presentNonNullKeys(record, ABC_KEYS));
            assertAllAndPartialKeyShares(gen.withNullValues("a", "b", "c"),
                                         record -> presentNonNullKeys(record, ABC_KEYS));
            assertAllAndPartialKeyShares(gen.withAllOptKeys(),
                                         record -> presentMapKeys(record, ABC_KEYS));
            assertAllAndPartialKeyShares(gen.withOptKeys("a", "b", "c"),
                                         record -> presentMapKeys(record, ABC_KEYS));
        }
    }

    @Nested
    class TypedGetterTests {

        @Test
        void shouldExposeGeneratedRecordDataWhenReadingWithTypedGetters() {
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", IntGen.biased()),
                                           record -> record.getOptionalInt("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", LongGen.biased()),
                                           record -> record.getOptionalLong("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a",
                                                          BigIntGen.arbitrary(new BigInteger("1000000000"),
                                                                              new BigInteger("2000000000"))),
                                           record -> record.getOptionalBigInteger("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", BigDecGen.arbitrary()),
                                           record -> record.getOptionalDecimal("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", DoubleGen.arbitrary()),
                                           record -> record.getOptionalDouble("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", StrGen.biased(1, 100)),
                                           record -> record.getOptionalString("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", CharGen.alphabetic()),
                                           record -> record.getOptionalChar("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a",
                                                          ListGen.biased(IntGen.arbitrary(), 1, 100)),
                                           record -> record.getOptionalListView("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", InstantGen.arbitrary()),
                                           record -> record.getOptionalInstant("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", BoolGen.arbitrary()),
                                           record -> record.getOptionalBoolean("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", BytesGen.arbitrary(1, 10)),
                                           record -> record.getOptionalBytes("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a", SetGen.ofN(IntGen.arbitrary(), 10)),
                                           record -> record.getOptionalSetView("a").isPresent());
            assertTypedGetterAlwaysPresent(MyRecordGen.of("a",
                                                          MapGen.of(StrGen.alphanumeric(1, 10),
                                                                    IntGen.arbitrary(),
                                                                    100)),
                                           record -> {
                                               Optional<Map<String, Integer>> value = record.getOptionalMapView("a");
                                               return value.isPresent();
                                           });
        }
    }

    private static void assertAllAndPartialKeyShares(MyRecordGen gen,
                                                     Function<MyRecord, List<String>> classifier) {
        Map<List<String>, Long> counts = gen.collect(OPTIONAL_AND_NULLABLE_SAMPLE_SIZE, classifier);
        assertShareWithinRange(counts,
                               keys -> keys.containsAll(ABC_KEYS),
                               OPTIONAL_AND_NULLABLE_SAMPLE_SIZE,
                               ALL_KEYS_SHARE_MIN,
                               ALL_KEYS_SHARE_MAX);
        assertShareWithinRange(counts,
                               keys -> !keys.containsAll(ABC_KEYS),
                               OPTIONAL_AND_NULLABLE_SAMPLE_SIZE,
                               PARTIAL_KEYS_SHARE_MIN,
                               PARTIAL_KEYS_SHARE_MAX);
    }

    private static void assertTypedGetterAlwaysPresent(MyRecordGen gen,
                                                       Predicate<MyRecord> predicate) {
        Assertions.assertTrue(gen.sample(SMALL_SAMPLE_SIZE).allMatch(predicate));
    }

    private static void assertAllSamplesContainKeys(MyRecordGen gen,
                                                    int sampleSize,
                                                    String... requiredKeys) {
        Assertions.assertTrue(gen.sample(sampleSize)
                                 .allMatch(record -> {
                                     Map<String, ?> map = record.asMap();
                                     for (String requiredKey : requiredKeys) {
                                         if (!map.containsKey(requiredKey)) {
                                             return false;
                                         }
                                     }
                                     return true;
                                 }));
    }

    private static void assertAllSamplesHaveArity(MyRecordGen gen,
                                                  int sampleSize,
                                                  int expectedSize) {
        Assertions.assertTrue(gen.sample(sampleSize)
                                 .allMatch(record -> record.asMap().size() == expectedSize));
    }

    private static <K> void assertShareWithinRange(Map<K, Long> counts,
                                                   Predicate<K> selector,
                                                   int totalSamples,
                                                   double minInclusive,
                                                   double maxInclusive) {
        Assertions.assertTrue(counts.entrySet()
                                    .stream()
                                    .filter(entry -> selector.test(entry.getKey()))
                                    .mapToDouble(entry -> ((double) entry.getValue() * 100) / totalSamples)
                                    .allMatch(percent -> percent >= minInclusive && percent <= maxInclusive));
    }

    private static List<String> presentMapKeys(MyRecord record,
                                               List<String> orderedKeys) {
        return orderedKeys.stream()
                          .filter(record.asMap()::containsKey)
                          .toList();
    }

    private static List<String> presentNonNullKeys(MyRecord record,
                                                   List<String> orderedKeys) {
        return orderedKeys.stream()
                          .filter(key -> record.asMap().containsKey(key) && record.asMap().get(key) != null)
                          .toList();
    }

    private static List<String> presentOptionalIntKeys(MyRecord record,
                                                       List<String> orderedKeys) {
        return orderedKeys.stream()
                          .filter(key -> record.getOptionalInt(key).isPresent())
                          .toList();
    }

    private static MyRecordGen compactOfFirstNFields(int fieldCount) {
        List<Map.Entry<String, Gen<?>>> fields = COMPACT_OF_FIELDS.subList(0, fieldCount);
        Map.Entry<String, Gen<?>> first = fields.getFirst();
        Object[] additional = new Object[(fields.size() - 1) * 2];
        int offset = 0;
        for (int i = 1; i < fields.size(); i++) {
            Map.Entry<String, Gen<?>> field = fields.get(i);
            additional[offset++] = field.getKey();
            additional[offset++] = field.getValue();
        }
        return MyRecordGen.of(first.getKey(), first.getValue(), additional);
    }
}
