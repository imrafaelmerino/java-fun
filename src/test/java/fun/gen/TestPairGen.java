package fun.gen;

import fun.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestPairGen {

    @Test
    public void testPair() {

        Gen<Pair<Integer, Integer>> gen = PairGen.of(IntGen.arbitrary(0,
                                                                      2),
                                                     IntGen.arbitrary(0,
                                                                      2));

        Map<Pair<Integer, Integer>, Long> map = TestFun.generate(1000000,
                                                                 gen);

        TestFun.assertGeneratedValuesHaveSameProbability(map,
                                                         TestFun.list(new Pair<>(0,
                                                                                 0),
                                                                      new Pair<>(0,
                                                                                 1),
                                                                      new Pair<>(1,
                                                                                 0),
                                                                      new Pair<>(1,
                                                                                 1),
                                                                      new Pair<>(0,
                                                                                 2),
                                                                      new Pair<>(2,
                                                                                 0),
                                                                      new Pair<>(1,
                                                                                 2),
                                                                      new Pair<>(2,
                                                                                 1),
                                                                      new Pair<>(2,
                                                                                 2)

                                                         ),
                                                         0.05
        );
    }
}
