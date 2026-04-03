package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class GenCoreTest {

    private static final int SMALL_SAMPLE_SIZE = 100;
    private static final int DISTINCT_SAMPLE_SIZE = 100_000;
    private static final long SAMPLE_SEED = 42L;
    private static final long COLLECT_SEED = 123L;

    @Nested
    class FilterAndDistinctTests {

        @Test
        void shouldGenerateSamplesThatSatisfyPredicateWhenApplyingFilter() {
            MyRecordGen gen =
                    MyRecordGen.of("a", StrGen.alphanumeric(1, 1),
                                   "b", StrGen.alphanumeric(1, 1),
                                   "c", StrGen.alphanumeric(1, 1))
                               .withAllOptKeys();

            Assertions.assertTrue(gen.filter(record -> containsAllKeys(record.asMap(), "a", "b", "c"))
                                     .sample(SMALL_SAMPLE_SIZE)
                                     .findAny()
                                     .isPresent());

            Assertions.assertTrue(gen.filter(record -> containsNoKeys(record.asMap(), "a", "b", "c"))
                                     .sample(SMALL_SAMPLE_SIZE)
                                     .findAny()
                                     .isPresent());
        }

        @Test
        void shouldGenerateDistinctValuesWhenUsingDistinct() {
            List<Integer> ints = IntGen.arbitrary().distinct().sample(DISTINCT_SAMPLE_SIZE).toList();
            Assertions.assertEquals(ints.size(), new HashSet<>(ints).size());

            List<String> strings = StrGen.alphabetic(1, 10).distinct().sample(DISTINCT_SAMPLE_SIZE).toList();
            Assertions.assertEquals(strings.size(), new HashSet<>(strings).size());
        }

        @Test
        void shouldRejectNegativeTriesWhenUsingDistinct() {
            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> Gen.constant(1)
                                             .distinct(-1)
                                             .sample()
                                             .get());
        }

        @Test
        void shouldThrowTypedExhaustedExceptionWhenUsingDistinct() {
            var supplier = Gen.constant(1).distinct(3).sample();
            int first = supplier.get();
            Assertions.assertEquals(1, first);
            Assertions.assertThrows(GenerationExhaustedException.class, supplier::get);
        }

        @Test
        void shouldThrowTypedUnsatisfiableExceptionWhenUsingFilter() {
            Assertions.assertThrows(UnsatisfiableConstraintException.class,
                                    () -> Gen.constant(1)
                                             .filter(n -> n > 1, 5)
                                             .sample()
                                             .get());
        }
    }

    @Nested
    class DeterminismAndUtilitiesTests {

        @Test
        void shouldBeDeterministicWhenUsingSampleWithSeed() {
            List<Integer> left = IntGen.arbitrary(0, 1_000).sample(50, SAMPLE_SEED).toList();
            List<Integer> right = IntGen.arbitrary(0, 1_000).sample(50, SAMPLE_SEED).toList();
            Assertions.assertEquals(left, right);
        }

        @Test
        void shouldBeDeterministicWhenUsingCollectWithSeed() {
            Map<Integer, Long> left = IntGen.arbitrary(0, 10).collect(10_000, COLLECT_SEED);
            Map<Integer, Long> right = IntGen.arbitrary(0, 10).collect(10_000, COLLECT_SEED);
            Assertions.assertEquals(left, right);
        }

        @Test
        void shouldClassifySamplesIntoExpectedBucketsWhenClassifyingSamples() {
            Map<String, Predicate<Integer>> classifier = new HashMap<>();
            classifier.put("2", n -> n == 2);
            classifier.put("> 5", n -> n > 5);
            classifier.put("= 10", n -> n == 10);

            Map<String, Long> buckets = IntGen.arbitrary(0, 10)
                                             .classify(10_000, classifier, "Others");

            Assertions.assertFalse(buckets.isEmpty());
            Assertions.assertTrue(buckets.containsKey("Others"));
        }

        @Test
        void shouldGenerateSequentialValuesWhenUsingSeq() {
            Assertions.assertEquals(List.of(1, 2, 3, 4, 5),
                                    Gen.seq(n -> n)
                                       .sample(5)
                                       .collect(Collectors.toList()));
        }
    }

    private static boolean containsAllKeys(Map<String, ?> map,
                                           String... keys) {
        for (String key : keys) {
            if (!map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsNoKeys(Map<String, ?> map,
                                          String... keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
