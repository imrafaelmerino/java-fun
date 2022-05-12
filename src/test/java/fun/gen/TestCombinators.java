package fun.gen;

import fun.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestCombinators {


    @Test
    public void freqCombinator() {

        Gen<Integer> gen = Combinators.freq(new Pair<>(1,
                                                       Gen.cons(1)),
                                            new Pair<>(1,
                                                       Gen.cons(2)),
                                            new Pair<>(1,
                                                       Gen.cons(3)),
                                            new Pair<>(1,
                                                       Gen.cons(4)),
                                            new Pair<>(1,
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


}
