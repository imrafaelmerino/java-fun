package fun.article;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.PairGen;
import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Tag("stats")
class ArticleExamplesTest {

    private static final int LARGE_SAMPLE_SIZE = 100_000_000;
    private static final int MEDIUM_SAMPLE_SIZE = 100_000;

    private static double ratio(long count,
                                double total) {
        return count / total;
    }

    @Test
    void shouldGenerateUniformIntegerDistributionWhenUsingArbitrary() {
        Map<Integer, Long> counts = IntGen.arbitrary(-5, 5).collect(LARGE_SAMPLE_SIZE);
        IntStream.rangeClosed(-5, 5).forEach(value -> Assertions.assertTrue(counts.containsKey(value)));
        Assertions.assertEquals(11, counts.size());
        double expected = 1.0 / 11;
        IntStream.rangeClosed(-5, 5).forEach(value -> Assertions.assertTrue(
                Math.abs(ratio(counts.get(value), LARGE_SAMPLE_SIZE) - expected) < 0.01
        ));

        Gen<Integer> gen = IntGen.arbitrary();
        Supplier<Integer> supplier = gen.apply(new Random(0L));
        Assertions.assertAll(
                () -> Assertions.assertEquals(-1155484576, supplier.get()),
                () -> Assertions.assertEquals(-723955400, supplier.get()),
                () -> Assertions.assertEquals(1033096058, supplier.get())
        );
    }

    @Test
    void shouldGenerateBoundaryBiasedIntegerDistributionWhenUsingBiased() {
        Map<Integer, Long> counts = IntGen.biased(-5, 5).collect(LARGE_SAMPLE_SIZE);
        Assertions.assertTrue(ratio(counts.get(-5), LARGE_SAMPLE_SIZE) > 0.15);
        Assertions.assertTrue(ratio(counts.get(0), LARGE_SAMPLE_SIZE) > 0.15);
        Assertions.assertTrue(ratio(counts.get(5), LARGE_SAMPLE_SIZE) > 0.15);
        Assertions.assertTrue(ratio(counts.get(-1), LARGE_SAMPLE_SIZE) < 0.08);
        Assertions.assertTrue(ratio(counts.get(1), LARGE_SAMPLE_SIZE) < 0.08);
    }

    @Test
    void shouldGenerateDependentPairsWhenComparingFilterAndFlatMapApproaches() {
        Gen<Pair<Integer, Integer>> filtered =
                PairGen.of(IntGen.arbitrary(0, 20), IntGen.arbitrary(0, 20))
                       .filter(pair -> pair.second() > 2 * pair.first());
        Map<Pair<Integer, Integer>, Long> filteredCounts = filtered.collect(MEDIUM_SAMPLE_SIZE);
        filteredCounts.keySet().forEach(pair ->
                Assertions.assertTrue(pair.second() > 2 * pair.first())
        );

        Gen<Pair<Integer, Integer>> flatMapped =
                IntGen.arbitrary(0, 20)
                      .flatMap(a -> IntGen.arbitrary(2 * a + 1).map(b -> Pair.of(a, b)));
        Map<Pair<Integer, Integer>, Long> flatMappedCounts = flatMapped.collect(MEDIUM_SAMPLE_SIZE);
        flatMappedCounts.keySet().forEach(pair ->
                Assertions.assertTrue(pair.second() > 2 * pair.first())
        );
    }

    @Test
    void shouldGenerateWeightedVowelsWhenUsingFreqCombinator() {
        Gen<Character> uniformVowelGen = Combinators.oneOf('A', 'E', 'I', 'O', 'U');
        Map<Character, Long> uniformCounts = uniformVowelGen.collect(MEDIUM_SAMPLE_SIZE);
        Assertions.assertEquals(5, uniformCounts.size());

        Gen<Character> weightedVowelGen = Combinators.freq(Pair.of(3, Gen.constant('A')),
                                                            Pair.of(4, Gen.constant('E')),
                                                            Pair.of(2, Gen.constant('I')),
                                                            Pair.of(3, Gen.constant('O')),
                                                            Pair.of(1, Gen.constant('U')));
        Map<Character, Long> weightedCounts = weightedVowelGen.collect(MEDIUM_SAMPLE_SIZE);
        double a = ratio(weightedCounts.get('A'), MEDIUM_SAMPLE_SIZE);
        double e = ratio(weightedCounts.get('E'), MEDIUM_SAMPLE_SIZE);
        double i = ratio(weightedCounts.get('I'), MEDIUM_SAMPLE_SIZE);
        double o = ratio(weightedCounts.get('O'), MEDIUM_SAMPLE_SIZE);
        double u = ratio(weightedCounts.get('U'), MEDIUM_SAMPLE_SIZE);
        Assertions.assertTrue(e > a);
        Assertions.assertTrue(a > i);
        Assertions.assertTrue(o > u);
    }

}
