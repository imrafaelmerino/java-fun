package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MapGenTest {

    private static final int SMALL_SAMPLE_SIZE = 200;
    private static final int MEDIUM_SAMPLE_SIZE = 1_000;
    private static final int LARGE_SAMPLE_SIZE = 10_000;

    @Nested
    class ValidationTests {

        @Test
        void shouldThrowWhenMapSizeCannotBeSatisfied() {
            MapGen<String, Integer> gen =
                    MapGen.of(StrGen.letters(1, 1),
                              IntGen.arbitrary(0, 10),
                              100_000)
                          .withMaxTries(100_001);

            Assertions.assertThrows(RuntimeException.class,
                                    () -> gen.sample(10).count());
        }

        @Test
        void shouldRejectInvalidRangeWhenUsingArbitrary() {
            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> MapGen.arbitrary(StrGen.alphanumeric(1, 2),
                                                           IntGen.arbitrary(0, 1),
                                                           -1,
                                                           2));
            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> MapGen.arbitrary(StrGen.alphanumeric(1, 2),
                                                           IntGen.arbitrary(0, 1),
                                                           3,
                                                           2));
        }
    }

    @Nested
    class GenerationTests {

        @Test
        void shouldGenerateFixedSizeMapsWhenUsingFixedSizeArbitrary() {
            int size = 100;
            MapGen<String, Integer> gen =
                    MapGen.of(StrGen.letters(1, 2),
                              IntGen.arbitrary(0, 10),
                              size);

            Assertions.assertTrue(gen.sample(LARGE_SAMPLE_SIZE)
                                     .allMatch(it -> it.size() == size));
            Assertions.assertTrue(gen.sample(LARGE_SAMPLE_SIZE)
                                     .allMatch(it -> it.values().stream().allMatch(n -> n <= 10)));
        }

        @Test
        void shouldGenerateSizesWithinRangeWhenUsingArbitrary() {
            int minSize = 2;
            int maxSize = 8;
            Gen<Map<String, Integer>> gen =
                    MapGen.arbitrary(StrGen.alphanumeric(1, 5),
                                     IntGen.arbitrary(0, 10),
                                     minSize,
                                     maxSize);

            assertAllSizesWithinRange(gen, MEDIUM_SAMPLE_SIZE, minSize, maxSize);
        }

        @Test
        void shouldHitRangeBoundariesWhenUsingBiased() {
            int minSize = 1;
            int maxSize = 5;
            var sampledSizes = MapGen.biased(StrGen.alphanumeric(1, 8),
                                             IntGen.arbitrary(0, 100),
                                             minSize,
                                             maxSize)
                                     .sample(2_000)
                                     .map(Map::size)
                                     .toList();

            Assertions.assertTrue(sampledSizes.contains(minSize));
            Assertions.assertTrue(sampledSizes.contains(maxSize));
        }

        @Test
        void shouldBehaveLikeOfWhenUsingOfN() {
            int size = 3;
            MapGen<String, Integer> gen = MapGen.ofN(StrGen.alphanumeric(1, 20),
                                                     IntGen.arbitrary(0, 100),
                                                     size);

            Assertions.assertTrue(gen.sample(500).allMatch(map -> map.size() == size));
        }

        @Test
        void shouldGenerateEmptyMapsWhenUsingOfWithZeroSize() {
            MapGen<String, Integer> gen = MapGen.of(StrGen.alphanumeric(1, 5),
                                                    IntGen.arbitrary(0, 10),
                                                    0);
            Assertions.assertTrue(gen.sample(SMALL_SAMPLE_SIZE).allMatch(Map::isEmpty));
        }

        @Test
        void shouldGenerateEmptyMapsWhenUsingArbitraryRangeWithZeroOnly() {
            Gen<Map<String, Integer>> gen = MapGen.arbitrary(StrGen.alphanumeric(1, 5),
                                                             IntGen.arbitrary(0, 10),
                                                             0,
                                                             0);
            Assertions.assertTrue(gen.sample(SMALL_SAMPLE_SIZE).allMatch(Map::isEmpty));
        }
    }

    private static void assertAllSizesWithinRange(Gen<Map<String, Integer>> gen,
                                                  int sampleSize,
                                                  int minSize,
                                                  int maxSize) {
        Assertions.assertTrue(gen.sample(sampleSize)
                                 .allMatch(map -> map.size() >= minSize && map.size() <= maxSize));
    }
}
