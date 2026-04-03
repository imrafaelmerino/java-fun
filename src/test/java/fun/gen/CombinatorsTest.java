package fun.gen;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CombinatorsTest {

    private static final int LARGE_SAMPLE_SIZE = 100_000;
    private static final int MEDIUM_SAMPLE_SIZE = 50_000;
    private static final int SMALL_SAMPLE_SIZE = 1_000;
    private static final double UNIFORM_ERROR_MARGIN = 0.05;

    @Test
    @Tag("stats")
    void shouldGenerateValuesAccordingToWeightsWhenUsingFreq() {

        Gen<Integer> gen = Combinators.freq(Pair.of(1, Gen.constant(1)),
                                            Pair.of(1, Gen.constant(2)),
                                            Pair.of(1, Gen.constant(3)),
                                            Pair.of(1, Gen.constant(4)),
                                            Pair.of(1, Gen.constant(5)));

        assertUniformDistribution(gen, TestFun.list(1, 2, 3, 4, 5));
    }

    @Test
    void shouldSupportLargeTotalWeightsWithoutOverflowWhenUsingFreq() {
        Gen<Integer> gen = Combinators.freq(Pair.of(Integer.MAX_VALUE,
                                                    Gen.constant(1)),
                                            Pair.of(Integer.MAX_VALUE,
                                                    Gen.constant(2)));

        Assertions.assertDoesNotThrow(() -> {
            Map<Integer, Long> counts = TestFun.generate(MEDIUM_SAMPLE_SIZE,
                                                         gen);
            assertContainsAllKeys(counts, Set.of(1, 2));
        });
    }

    @Test
    @Tag("stats")
    void shouldGenerateNullAndValueWithSameProbabilityWhenUsingNullable() {


        assertUniformDistribution(Combinators.nullable(Gen.constant("a")),
                                  TestFun.list("a", null));
    }

    @Test
    void shouldNeverGenerateNullWhenUsingNullableWithZeroProbability() {
        Map<String, Long> counts = TestFun.generate(LARGE_SAMPLE_SIZE,
                                                    Combinators.nullable(Gen.constant("a"),
                                                                         0));

        Assertions.assertFalse(counts.containsKey(null));
        Assertions.assertEquals(LARGE_SAMPLE_SIZE,
                                counts.get("a"));
    }

    @Test
    void shouldAlwaysGenerateNullWhenUsingNullableWithHundredProbability() {
        Map<String, Long> counts = TestFun.generate(LARGE_SAMPLE_SIZE,
                                                    Combinators.nullable(Gen.constant("a"),
                                                                         100));

        Assertions.assertEquals(1,
                                counts.size());
        Assertions.assertEquals(LARGE_SAMPLE_SIZE,
                                counts.get(null));
    }

    @Test
    @Tag("stats")
    void shouldGenerateAnyProvidedValueWhenUsingOneOfVarargs() {


        assertUniformDistribution(Combinators.oneOf("a", "b", "c"),
                                  TestFun.list("a", "b", "c"));
    }

    @Test
    @Tag("stats")
    void shouldGenerateAnyProvidedValueWhenUsingOneOfList() {


        assertUniformDistribution(Combinators.oneOf(Arrays.asList("a", "b", "c")),
                                  TestFun.list("a", "b", "c"));
    }

    @Test
    void shouldNotDependOnSubsequentListMutationsWhenUsingOneOfList() {
        List<String> values = new ArrayList<>(Arrays.asList("a",
                                                             "b",
                                                             "c"));
        Gen<String> gen = Combinators.oneOf(values);
        values.clear();

        Assertions.assertTrue(gen.sample(SMALL_SAMPLE_SIZE)
                                 .allMatch(v -> v.equals("a") || v.equals("b") || v.equals("c")));
    }

    @Test
    void shouldSupportNullElementsWhenUsingOneOfList() {
        List<String> values = new ArrayList<>();
        values.add("a");
        values.add(null);
        Gen<String> gen = Combinators.oneOf(values);

        Map<String, Long> counts = TestFun.generate(MEDIUM_SAMPLE_SIZE,
                                                    gen);

        assertContainsAllKeys(counts, TestFun.list("a", null));
    }

    @Test
    void shouldNotDependOnSubsequentArrayMutationsWhenUsingOneOfVarargs() {
        String[] others = new String[]{"b", "c"};
        Gen<String> gen = Combinators.oneOf("a",
                                            others);
        others[0] = "z";

        Map<String, Long> counts = TestFun.generate(MEDIUM_SAMPLE_SIZE,
                                                    gen);

        assertContainsAllKeys(counts, Set.of("a", "b", "c"));
        Assertions.assertFalse(counts.containsKey("z"));
    }

    @Test
    void shouldReflectSubsequentArrayMutationsWhenUsingOneOfViewVarargs() {
        String[] others = new String[]{"b", "c"};
        Gen<String> gen = Combinators.oneOfView("a",
                                                others);
        others[0] = "z";

        Map<String, Long> counts = TestFun.generate(MEDIUM_SAMPLE_SIZE,
                                                    gen);

        assertContainsAllKeys(counts, Set.of("a", "z", "c"));
        Assertions.assertFalse(counts.containsKey("b"));
    }

    @Test
    @Tag("stats")
    void shouldGenerateAnyProvidedValueWhenUsingOneOfSet() {


        assertUniformDistribution(
                Combinators.oneOf(new HashSet<>(Arrays.asList("a", "b", "c"))),
                TestFun.list("a", "b", "c"));
    }

    @Test
    void shouldNotDependOnSubsequentSetMutationsWhenUsingOneOfSet() {
        Set<String> values = new HashSet<>(Arrays.asList("a",
                                                         "b",
                                                         "c"));
        Gen<String> gen = Combinators.oneOf(values);
        values.clear();

        Assertions.assertTrue(gen.sample(SMALL_SAMPLE_SIZE)
                                 .allMatch(v -> v.equals("a") || v.equals("b") || v.equals("c")));
    }

    @Test
    void shouldSupportNullElementsWhenUsingOneOfSet() {
        Set<String> values = new HashSet<>();
        values.add("a");
        values.add(null);
        Gen<String> gen = Combinators.oneOf(values);

        Map<String, Long> counts = TestFun.generate(MEDIUM_SAMPLE_SIZE,
                                                    gen);

        assertContainsAllKeys(counts, TestFun.list("a", null));
    }

    @Test
    @Tag("stats")
    void shouldGenerateValuesFromAnyProvidedGeneratorWhenUsingOneOfGenerators() {


        HashSet<String> values = new HashSet<>(Arrays.asList("a",
                                                             "b",
                                                             "c"));
        var gen = Combinators.oneOf(values);

        HashSet<String> values1 = new HashSet<>(Arrays.asList("d",
                                                              "e",
                                                              "f"));
        var gen1 = Combinators.oneOf(values1);

        assertUniformDistribution(Combinators.oneOf(gen, gen1),
                                  TestFun.list("a", "b", "c", "d", "e", "f"));
    }

    @Test
    @Tag("stats")
    void shouldGenerateValuesFromAnyProvidedGeneratorWhenUsingOneOfListGenerators() {


        HashSet<String> values = new HashSet<>(Arrays.asList("a",
                                                             "b",
                                                             "c"));
        var gen = Combinators.oneOf(values);

        HashSet<String> values1 = new HashSet<>(Arrays.asList("d",
                                                              "e",
                                                              "f"));
        var gen1 = Combinators.oneOf(values1);

        assertUniformDistribution(Combinators.oneOfList(List.of(gen, gen1)),
                                  TestFun.list("a", "b", "c", "d", "e", "f"));
    }

    @Test
    void shouldNotDependOnSubsequentArrayMutationsWhenUsingOneOfGeneratorVarargs() {
        @SuppressWarnings("unchecked")
        Gen<? extends String>[] others = new Gen[]{Gen.constant("b"),
                                                   Gen.constant("c")};
        Gen<String> gen = Combinators.oneOf(Gen.constant("a"),
                                            others);
        others[0] = Gen.constant("z");

        Map<String, Long> counts = TestFun.generate(MEDIUM_SAMPLE_SIZE,
                                                    gen);

        assertContainsAllKeys(counts, Set.of("a", "b", "c"));
        Assertions.assertFalse(counts.containsKey("z"));
    }

    @Test
    void shouldReflectSubsequentArrayMutationsWhenUsingOneOfGeneratorVarargsView() {
        @SuppressWarnings("unchecked")
        Gen<? extends String>[] others = new Gen[]{Gen.constant("b"),
                                                   Gen.constant("c")};
        Gen<String> gen = Combinators.oneOfView(Gen.constant("a"),
                                                others);
        others[0] = Gen.constant("z");

        Map<String, Long> counts = TestFun.generate(50000,
                                                    gen);

        Assertions.assertTrue(counts.containsKey("a"));
        Assertions.assertTrue(counts.containsKey("z"));
        Assertions.assertTrue(counts.containsKey("c"));
        Assertions.assertFalse(counts.containsKey("b"));
    }

    @Test
    void shouldNotDependOnSubsequentListMutationsWhenUsingOneOfGeneratorList() {
        List<Gen<? extends String>> gens = new ArrayList<>();
        gens.add(Gen.constant("x"));
        gens.add(Gen.constant("y"));
        Gen<String> gen = Combinators.oneOfList(gens);
        gens.clear();

        Assertions.assertTrue(gen.sample(200)
                                 .allMatch(v -> v.equals("x") || v.equals("y")));
    }

    @Test
    void shouldFailIfSourceListBecomesEmptyWhenUsingOneOfGeneratorListView() {
        List<Gen<? extends String>> gens = new ArrayList<>();
        gens.add(Gen.constant("x"));
        gens.add(Gen.constant("y"));
        Gen<String> gen = Combinators.oneOfListView(gens);
        gens.clear();

        Assertions.assertThrows(IllegalStateException.class,
                                () -> gen.sample().get());
    }


    @Test
    @Tag("stats")
    void shouldGenerateAllTwoElementCombinationsWhenUsingSubsetWithSizeTwo() {

        Gen<Set<String>> gen = Combinators.combinations(2,
                                                        Arrays.asList("a",
                                                                      "b",
                                                                      "c"));


        Assertions.assertTrue(gen.sample(100).map(ArrayList::new)
                                 .noneMatch(it -> it.get(0).equals(it.get(1)))
        );

        Map<String, Long> count = TestFun.generate(100000,
                                                   gen.map(ArrayList::new)
                                                      .map(it -> it.get(0) + it.get(1)));


        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list("ab",
                                                                      "ac",
                                                                      "bc"),
                                                         0.05);

    }


    @Test
    @Tag("stats")
    void shouldGenerateAllNonEmptySubsetsWhenUsingSubsetWithoutSizeConstraint() {

        Gen<Set<String>> gen =
                Combinators.subsets(Arrays.asList("a",
                                                  "b",
                                                  "c"));


        Map<Set<String>, Long> count = TestFun.generate(100000,
                                                        gen);


        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list(new HashSet<>(List.of("a")),
                                                                      new HashSet<>(List.of("b")),
                                                                      new HashSet<>(List.of("c")),
                                                                      new HashSet<>(Arrays.asList("a",
                                                                                                  "b")),
                                                                      new HashSet<>(Arrays.asList("b",
                                                                                                  "c")),
                                                                      new HashSet<>(Arrays.asList("a",
                                                                                                  "c")),
                                                                      new HashSet<>(Arrays.asList("a",
                                                                                                  "b",
                                                                                                  "c"))),
                                                         0.05);

    }

    @Test
    void shouldNotDependOnSubsequentListMutationsWhenUsingSubsets() {
        List<String> values = new ArrayList<>(List.of("a",
                                                      "b",
                                                      "c"));
        Gen<Set<String>> gen = Combinators.subsets(values);
        values.clear();

        Assertions.assertTrue(gen.sample(200)
                                 .allMatch(subset -> subset.stream()
                                                           .allMatch(v -> v.equals("a")
                                                                   || v.equals("b")
                                                                   || v.equals("c"))));
    }

    @Test
    void shouldReflectSubsequentListMutationsWhenUsingSubsetsView() {
        List<String> values = new ArrayList<>(List.of("a",
                                                      "b",
                                                      "c"));
        Gen<Set<String>> gen = Combinators.subsetsView(values);
        values.clear();

        Assertions.assertTrue(gen.sample(50)
                                 .allMatch(Set::isEmpty));
    }

    @Test
    void shouldNotDependOnSubsequentSourceListMutationsWhenUsingShuffle() {
        List<Integer> values = new ArrayList<>(List.of(1,
                                                       2,
                                                       3,
                                                       4));
        Gen<List<Integer>> gen = Combinators.shuffle(values);
        values.clear();

        Assertions.assertTrue(gen.sample(200)
                                 .allMatch(sample -> sample.containsAll(List.of(1,
                                                                                2,
                                                                                3,
                                                                                4))
                                         && sample.size() == 4));
    }

    @Test
    void shouldReflectSubsequentSourceListMutationsWhenUsingShuffleView() {
        List<Integer> values = new ArrayList<>(List.of(1,
                                                       2,
                                                       3,
                                                       4));
        Gen<List<Integer>> gen = Combinators.shuffleView(values);
        values.clear();

        Assertions.assertTrue(gen.sample(20)
                                 .allMatch(List::isEmpty));
    }

    @Test
    void shouldGenerateUniqueValuesOfRequestedSizeWhenUsingNOf() {

        List<Integer> numbers = IntStream.range(0,
                                                4).boxed().collect(Collectors.toList());


        Combinators.nOf(numbers,
                        2
                   )
                   .sample(100)

                   .forEach(n -> Assertions.assertEquals(2,
                                                         new HashSet<>(n).size()));


        Combinators.nOf(new HashSet<>(numbers),
                        numbers.size()
                   )
                   .sample(100)
                   .forEach(n -> Assertions.assertEquals(numbers.size(),
                                                         new HashSet<>(n).size()));


    }

    @Test
    void shouldNotDependOnSubsequentMutationsWhenUsingNOfList() {
        List<Integer> numbers = new ArrayList<>(List.of(0, 1, 2, 3));
        Gen<List<Integer>> gen = Combinators.nOf(numbers,
                                                 3);
        numbers.clear();

        Assertions.assertDoesNotThrow(() -> gen.sample(20)
                                               .forEach(sample -> Assertions.assertEquals(3,
                                                                                           sample.size())));
    }

    @Test
    void shouldFailIfSourceListShrinksBelowNWhenUsingNOfViewList() {
        List<Integer> numbers = new ArrayList<>(List.of(0, 1, 2, 3));
        Gen<List<Integer>> gen = Combinators.nOfView(numbers,
                                                     3);
        numbers.clear();

        Assertions.assertThrows(IllegalStateException.class,
                                () -> gen.sample().get());
    }

    @Test
    void shouldNotDependOnSubsequentMutationsWhenUsingNOfSet() {
        Set<Integer> numbers = new LinkedHashSet<>(List.of(0, 1, 2, 3));
        Gen<Set<Integer>> gen = Combinators.nOf(numbers,
                                                3);
        numbers.clear();

        Assertions.assertDoesNotThrow(() -> gen.sample(20)
                                               .forEach(sample -> Assertions.assertEquals(3,
                                                                                           sample.size())));
    }

    @Test
    void shouldFailIfSourceSetShrinksBelowNWhenUsingNOfViewSet() {
        Set<Integer> numbers = new LinkedHashSet<>(List.of(0, 1, 2, 3));
        Gen<Set<Integer>> gen = Combinators.nOfView(numbers,
                                                    3);
        numbers.clear();

        Assertions.assertThrows(IllegalStateException.class,
                                () -> gen.sample().get());
    }

    @Test
    void shouldRejectEmptyCollectionsWhenUsingOneOf() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.oneOf(List.<String>of()));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.oneOf(Set.<String>of()));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.oneOfList(List.of()));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.oneOfListView(List.of()));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.oneOfView(List.<String>of()));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.oneOfView(Set.<String>of()));
    }

    @Test
    void shouldRejectNegativeNWhenUsingNOf() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.nOf(List.of(1,
                                                              2),
                                                      -1));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.nOf(Set.of(1,
                                                             2),
                                                      -1));
    }

    @Test
    void shouldValidateKBoundsWhenUsingCombinations() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.combinations(-1,
                                                               List.of("a",
                                                                       "b")));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.combinations(3,
                                                               List.of("a",
                                                                       "b")));
    }

    @Test
    void shouldGenerateExtremeSizesWithoutSuchThatFailuresWhenUsingCombinations() {
        List<Integer> input = IntStream.range(0,
                                              40)
                                     .boxed()
                                     .toList();

        Gen<Set<Integer>> empty = Combinators.combinations(0,
                                                            input);
        Gen<Set<Integer>> single = Combinators.combinations(1,
                                                             input);
        Gen<Set<Integer>> full = Combinators.combinations(input.size(),
                                                           input);

        Assertions.assertDoesNotThrow(() -> empty.sample(20)
                                                 .forEach(values -> Assertions.assertEquals(0,
                                                                                             values.size())));
        Assertions.assertDoesNotThrow(() -> single.sample(20)
                                                  .forEach(values -> Assertions.assertEquals(1,
                                                                                              values.size())));
        Assertions.assertDoesNotThrow(() -> full.sample(20)
                                                .forEach(values -> Assertions.assertEquals(input.size(),
                                                                                            values.size())));
    }

    @Test
    void shouldReflectSubsequentSourceMutationsWhenUsingCombinationsViewList() {
        List<Integer> input = new ArrayList<>(List.of(1,
                                                      2,
                                                      3,
                                                      4));
        Gen<Set<Integer>> gen = Combinators.combinationsView(3,
                                                              input);
        input.clear();
        input.addAll(List.of(9,
                             8,
                             7));

        Assertions.assertTrue(gen.sample(20)
                                 .allMatch(values -> values.equals(Set.of(7,
                                                                          8,
                                                                          9))));
    }

    @Test
    void shouldFailIfSourceShrinksBelowKWhenUsingCombinationsViewSet() {
        Set<Integer> input = new LinkedHashSet<>(List.of(1,
                                                         2,
                                                         3,
                                                         4));
        Gen<Set<Integer>> gen = Combinators.combinationsView(3,
                                                              input);
        input.clear();
        input.addAll(List.of(1,
                             2));

        Assertions.assertThrows(IllegalStateException.class,
                                () -> gen.sample().get());
    }

    @Test
    void shouldRejectNonPositiveWeightsWhenUsingFreq() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.freq(Pair.of(0,
                                                               Gen.constant(1)),
                                                       Pair.of(1,
                                                               Gen.constant(2)))
                                                .sample()
                                                .get());
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Combinators.freq(Pair.of(-1,
                                                               Gen.constant(1)),
                                                       Pair.of(1,
                                                               Gen.constant(2)))
                                                .sample()
                                                .get());
    }

    private static <T> void assertUniformDistribution(Gen<T> generator,
                                                      List<T> expectedValues) {
        Map<T, Long> counts = TestFun.generate(LARGE_SAMPLE_SIZE, generator);
        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         expectedValues,
                                                         UNIFORM_ERROR_MARGIN);
    }

    private static <T> void assertContainsAllKeys(Map<T, Long> counts,
                                                  Collection<T> expectedKeys) {
        expectedKeys.forEach(key -> Assertions.assertTrue(counts.containsKey(key)));
    }

}
