package fun.gen;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public void nullableFreq() {

    }

    @Test
    public void testCombinations() {

        Gen<List<String>> gen = Combinators.combinations(2,
                                                         Arrays.asList("a",
                                                                       "b",
                                                                       "c"));
        Assertions.assertTrue(gen.sample()
                                 .limit(1000)
                                 .noneMatch(it -> it.get(0).endsWith(it.get(1))));

        Map<String, Long> count = TestFun.generate(100000,
                                                   gen.map(it -> it.get(0) + it.get(1)));


        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list("ab","ac","bc"),
                                                         0.05);

    }
}
