package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class TestSubsetGenThresholdBehavior {

    @Test
    public void shouldIncludeEachElementWithApproxHalfProbabilityForLargeN() {
        int n = 22;
        int samples = 120_000;

        Gen<Set<Integer>> gen =
                Combinators.subsets(IntStream.range(0,
                                                    n)
                                             .boxed()
                                             .toList());

        long[] inclusionCounts = new long[n];
        gen.sample(samples)
           .forEach(set -> {
               for (Integer elem : set) {
                   inclusionCounts[elem] += 1;
               }
           });

        long expected = samples / 2L;
        for (int i = 0; i < n; i++) {
            long c = inclusionCounts[i];
            Assertions.assertTrue(Math.abs(c - expected) < expected * 0.08,
                                  "element " + i + " inclusion frequency out of range");
        }
    }

    @Test
    public void shouldNotShowDistributionJumpAtThreshold21And22() {
        int samples = 200_000;

        Gen<Set<Integer>> gen21 =
                Combinators.subsets(IntStream.range(0,
                                                    21)
                                             .boxed()
                                             .toList());
        Gen<Set<Integer>> gen22 =
                Combinators.subsets(IntStream.range(0,
                                                    22)
                                             .boxed()
                                             .toList());

        double mean21 = gen21.sample(samples)
                             .mapToInt(Set::size)
                             .average()
                             .orElseThrow();
        double mean22 = gen22.sample(samples)
                             .mapToInt(Set::size)
                             .average()
                             .orElseThrow();

        Assertions.assertTrue(Math.abs(mean21 - 10.5) < 0.2);
        Assertions.assertTrue(Math.abs(mean22 - 11.0) < 0.2);
    }

    @Test
    public void shouldHaveBinomialSizeShapeFor22Fields() {
        int n = 22;
        int samples = 200_000;

        Gen<Set<Integer>> gen =
                Combinators.subsets(IntStream.range(0,
                                                    n)
                                             .boxed()
                                             .toList());

        Map<Integer, Long> sizeCounts = TestFun.generate(samples,
                                                         gen.map(Set::size));

        long size1 = sizeCounts.getOrDefault(1,
                                             0L);
        long center = sizeCounts.getOrDefault(11,
                                              0L);

        Assertions.assertTrue(center > size1 * 100,
                              "size distribution should be concentrated near n/2");
    }
}
