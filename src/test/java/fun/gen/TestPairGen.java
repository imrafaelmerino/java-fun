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
                                                         TestFun.list(Pair.of(0,
                                                                              0),
                                                                      Pair.of(0,
                                                                              1),
                                                                      Pair.of(1,
                                                                              0),
                                                                      Pair.of(1,
                                                                              1),
                                                                      Pair.of(0,
                                                                              2),
                                                                      Pair.of(2,
                                                                              0),
                                                                      Pair.of(1,
                                                                              2),
                                                                      Pair.of(2,
                                                                              1),
                                                                      Pair.of(2,
                                                                              2)

                                                         ),
                                                         0.05
        );
    }
}
