package fun.gen;

import fun.tuple.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestTripleGen {

    @Test
    public void testTriple() {

        Gen<Triple<Integer, Integer, Integer>> gen =
                TripleGen.of(IntGen.arbitrary(0,
                                              1),
                             IntGen.arbitrary(0,
                                              1),
                             IntGen.arbitrary(0,
                                              1)
                );

        Assertions.assertTrue(gen.sample(1000).allMatch(it -> it.first() == 0 || it.first() == 1 &&
                (it.second() == 0 || it.second() == 1) &&
                (it.third() == 0 || it.third() == 1)));

        Map<Triple<Integer, Integer, Integer>, Long> map = TestFun.generate(1000000,
                                                                            gen);

        TestFun.assertGeneratedValuesHaveSameProbability(map,
                                                         TestFun.list(Triple.of(0,
                                                                                0,
                                                                                0),
                                                                      Triple.of(0,
                                                                                0,
                                                                                1),
                                                                      Triple.of(0,
                                                                                1,
                                                                                1),
                                                                      Triple.of(1,
                                                                                1,
                                                                                1),
                                                                      Triple.of(1,
                                                                                0,
                                                                                0),
                                                                      Triple.of(1,
                                                                                1,
                                                                                0),
                                                                      Triple.of(1,
                                                                                0,
                                                                                1)

                                                         ),
                                                         0.05
        );
    }
}
