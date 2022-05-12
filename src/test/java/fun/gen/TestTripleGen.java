package fun.gen;

import fun.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestTripleGen {


    @Test
    public void testPair() {

        Gen<Triple<Integer, Integer, Integer>> gen =
                TripleGen.of(IntGen.arbitrary(0,
                                              1),
                             IntGen.arbitrary(0,
                                              1),
                             IntGen.arbitrary(0,
                                              1)
                );

        Map<Triple<Integer, Integer, Integer>, Long> map = TestFun.generate(1000000,
                                                                            gen);

        TestFun.assertGeneratedValuesHaveSameProbability(map,
                                                         TestFun.list(new Triple<>(0,
                                                                                   0,
                                                                                   0),
                                                                      new Triple<>(0,
                                                                                   0,
                                                                                   1),
                                                                      new Triple<>(0,
                                                                                   1,
                                                                                   1),
                                                                      new Triple<>(1,
                                                                                   1,
                                                                                   1),
                                                                      new Triple<>(1,
                                                                                   0,
                                                                                   0),
                                                                      new Triple<>(1,
                                                                                   1,
                                                                                   0),
                                                                      new Triple<>(1,
                                                                                   0,
                                                                                   1)

                                                         ),
                                                         0.05
        );
    }
}
