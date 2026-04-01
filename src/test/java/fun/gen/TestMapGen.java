package fun.gen;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestMapGen {

    @Test
    public void testNotAbleToGenerateMapOfSize() {
        MapGen<String, Integer> gen =
                MapGen.of(StrGen.letters(1,
                                         1),
                          IntGen.arbitrary(0,
                                           10),

                          100000).withMaxTries(100001);

        Assertions.assertThrows(RuntimeException.class,
                                () -> gen.sample(10).peek(System.out::println).count());
    }

    @Test
    public void test() {
        int SIZE = 100;
        MapGen<String, Integer> gen =
                MapGen.of(StrGen.letters(1,
                                         2),
                          IntGen.arbitrary(0,
                                           10),
                          SIZE);

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.size() == SIZE));

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.values()
                                                   .stream()
                                                   .allMatch(n -> n <= 10))
        );


    }

    @Test
    public void arbitraryShouldGenerateSizesWithinRange() {
        int minSize = 2;
        int maxSize = 8;
        var gen = MapGen.arbitrary(StrGen.alphanumeric(1,
                                                       5),
                                   IntGen.arbitrary(0,
                                                    10),
                                   minSize,
                                   maxSize);
        Assertions.assertTrue(gen.sample(1000)
                                 .allMatch(map -> map.size() >= minSize && map.size() <= maxSize));
    }

    @Test
    public void biasedShouldHitRangeBoundaries() {
        int minSize = 1;
        int maxSize = 5;
        var sampled = MapGen.biased(StrGen.alphanumeric(1,
                                                        8),
                                    IntGen.arbitrary(0,
                                                     100),
                                    minSize,
                                    maxSize)
                            .sample(2000)
                            .map(Map::size)
                            .toList();

        Assertions.assertTrue(sampled.contains(minSize));
        Assertions.assertTrue(sampled.contains(maxSize));
    }

    @Test
    public void arbitraryShouldRejectInvalidRange() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MapGen.arbitrary(StrGen.alphanumeric(1,
                                                                           2),
                                                       IntGen.arbitrary(0,
                                                                        1),
                                                       -1,
                                                       2));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MapGen.arbitrary(StrGen.alphanumeric(1,
                                                                           2),
                                                       IntGen.arbitrary(0,
                                                                        1),
                                                       3,
                                                       2));
    }

    @Test
    public void ofNShouldBeAliasOfOf() {
        int size = 3;
        var gen = MapGen.ofN(StrGen.alphanumeric(1,
                                                 20),
                             IntGen.arbitrary(0,
                                              100),
                             size);
        Assertions.assertTrue(gen.sample(500).allMatch(map -> map.size() == size));
    }

    @Test
    public void ofWithZeroSizeShouldGenerateEmptyMaps() {
        var gen = MapGen.of(StrGen.alphanumeric(1,
                                                5),
                            IntGen.arbitrary(0,
                                             10),
                            0);
        Assertions.assertTrue(gen.sample(200).allMatch(Map::isEmpty));
    }

    @Test
    public void arbitraryRangeWithZeroOnlyShouldGenerateEmptyMaps() {
        var gen = MapGen.arbitrary(StrGen.alphanumeric(1,
                                                       5),
                                   IntGen.arbitrary(0,
                                                    10),
                                   0,
                                   0);
        Assertions.assertTrue(gen.sample(200).allMatch(Map::isEmpty));
    }
}
