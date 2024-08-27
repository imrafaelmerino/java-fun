package fun.gen;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCombinators {

    @Test
    public void freqCombinator() {

        Gen<Integer> gen = Combinators.freq(Pair.of(1,
                                                    Gen.cons(1)),
                                            Pair.of(1,
                                                    Gen.cons(2)),
                                            Pair.of(1,
                                                    Gen.cons(3)),
                                            Pair.of(1,
                                                    Gen.cons(4)),
                                            Pair.of(1,
                                                    Gen.cons(5)));

        Map<Integer, Long> counts = TestFun.generate(100000,
                                                     gen);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(1,
                                                                      2,
                                                                      3,
                                                                      4,
                                                                      5),
                                                         0.05);
    }

    @Test
    public void nullable() {


        Map<String, Long> counts =
                TestFun.generate(100000,
                                 Combinators.nullable(Gen.cons("a")));


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(
                                                                 "a",
                                                                 null),
                                                         0.05);
    }

    @Test
    public void testOneOfValues() {


        Map<String, Long> counts =
                TestFun.generate(100000,
                                 Combinators.oneOf("a",
                                                   "b",
                                                   "c"));


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(
                                                                 "a",
                                                                 "b",
                                                                 "c"),
                                                         0.05);
    }

    @Test
    public void testOneOfListOfValues() {


        Map<String, Long> counts =
                TestFun.generate(100000,
                                 Combinators.oneOf(Arrays.asList("a",
                                                                 "b",
                                                                 "c")));


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(
                                                                 "a",
                                                                 "b",
                                                                 "c"),
                                                         0.05);
    }

    @Test
    public void testOneOfSetOfValues() {


        Map<String, Long> counts =
                TestFun.generate(100000,
                                 Combinators.oneOf(new HashSet<>(Arrays.asList("a",
                                                                               "b",
                                                                               "c"))
                                 )
                );


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(
                                                                 "a",
                                                                 "b",
                                                                 "c"),
                                                         0.05);
    }

    @Test
    public void testOneOfGen() {


        HashSet<String> values = new HashSet<>(Arrays.asList("a",
                                                             "b",
                                                             "c"));
        var gen = Combinators.oneOf(values);

        HashSet<String> values1 = new HashSet<>(Arrays.asList("d",
                                                              "e",
                                                              "f"));
        var gen1 = Combinators.oneOf(values1);

        Map<String, Long> counts =
                TestFun.generate(100000,
                                 Combinators.oneOf(gen,
                                                   gen1)
                );


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(
                                                                 "a",
                                                                 "b",
                                                                 "c",
                                                                 "d",
                                                                 "e",
                                                                 "f"),
                                                         0.05);
    }

    @Test
    public void testOneOfListGen() {


        HashSet<String> values = new HashSet<>(Arrays.asList("a",
                                                             "b",
                                                             "c"));
        var gen = Combinators.oneOf(values);

        HashSet<String> values1 = new HashSet<>(Arrays.asList("d",
                                                              "e",
                                                              "f"));
        var gen1 = Combinators.oneOf(values1);

        Map<String, Long> counts =
                TestFun.generate(100000,
                                 Combinators.oneOfList(List.of(gen,
                                                               gen1))
                );


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(
                                                                 "a",
                                                                 "b",
                                                                 "c",
                                                                 "d",
                                                                 "e",
                                                                 "f"),
                                                         0.05);
    }


    @Test
    public void testCombinations() {

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
    public void testSubSets() {

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
    public void testNOf() {

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

}
