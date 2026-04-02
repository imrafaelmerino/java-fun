package fun.article;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.PairGen;
import fun.tuple.Pair;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ArticleExamplesTest {

    private static <I> Map<I, String> toPercentages(Map<I, Long> counters,
                                                    double totalGenerated) {
        return counters.entrySet()
                       .stream()
                       .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(),
                                                               (e.getValue() / totalGenerated) * 100 + " %"))
                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                 Map.Entry::getValue));
    }

    @Test
    void shouldGenerateUniformIntegerDistributionWhenUsingArbitrary() {
        requireStatsProfile();

        Map<Integer, Long> counts = IntGen.arbitrary(-5, 5).collect(100_000_000);
        Map<Integer, String> percentages = toPercentages(counts, 100_000_000);
        System.out.println(percentages);

        Gen<Integer> gen = IntGen.arbitrary();
        Supplier<Integer> supplier = gen.apply(new Random(0L));
        System.out.println(supplier.get());
        System.out.println(supplier.get());
        System.out.println(supplier.get());
    }

    @Test
    void shouldGenerateBoundaryBiasedIntegerDistributionWhenUsingBiased() {
        requireStatsProfile();

        Map<Integer, Long> counts = IntGen.biased(-5, 5).collect(100_000_000);
        Map<Integer, String> percentages = toPercentages(counts, 100_000_000);
        System.out.println(percentages);
    }

    @Test
    void shouldGenerateDependentPairsWhenComparingFilterAndFlatMapApproaches() {
        requireStatsProfile();

        Gen<Pair<Integer, Integer>> filtered =
                PairGen.of(IntGen.arbitrary(0, 20), IntGen.arbitrary(0, 20))
                       .filter(pair -> pair.second() > 2 * pair.first());
        System.out.println(filtered.collect(100_000));

        Gen<Pair<Integer, Integer>> flatMapped =
                IntGen.arbitrary(0, 20)
                      .flatMap(a -> IntGen.arbitrary(2 * a + 1).map(b -> Pair.of(a, b)));
        System.out.println(flatMapped.collect(100_000));
    }

    @Test
    void shouldGenerateWeightedVowelsWhenUsingFreqCombinator() {
        requireStatsProfile();

        Gen<Character> uniformVowelGen = Combinators.oneOf('A', 'E', 'I', 'O', 'U');
        System.out.println(uniformVowelGen.collect(100_000));

        Gen<Character> weightedVowelGen = Combinators.freq(Pair.of(3, Gen.constant('A')),
                                                            Pair.of(4, Gen.constant('E')),
                                                            Pair.of(2, Gen.constant('I')),
                                                            Pair.of(3, Gen.constant('O')),
                                                            Pair.of(1, Gen.constant('U')));
        System.out.println(toPercentages(weightedVowelGen.collect(100_000), 100_000));
    }

    private static void requireStatsProfile() {
        Assumptions.assumeTrue(Boolean.getBoolean("javafun.stats"),
                               "Article/statistical tests are disabled by default. Run with -Pstats.");
    }
}
