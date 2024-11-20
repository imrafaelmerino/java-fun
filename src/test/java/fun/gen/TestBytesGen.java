package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestBytesGen {

    @Test
    public void testArbitrary() {

        Assertions.assertTrue(BytesGen.arbitrary(0,
                                                 10).sample(10000).allMatch(it -> it.length <= 10));


        Map<Integer, Long> count =
                TestFun.generate(1000000,
                                 BytesGen.arbitrary(0,
                                                    10).map(it -> it.length));


        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list(0,
                                                                      1,
                                                                      2,
                                                                      3,
                                                                      4,
                                                                      5,
                                                                      6,
                                                                      7,
                                                                      8,
                                                                      9,
                                                                      10),
                                                         0.05);

    }

    @Test
    public void testBiased() {
        int times = 1000000;
        int max = 3;
        Gen<byte[]> gen = BytesGen.biased(0,
                                          max);

        Assertions.assertTrue(gen.sample(times)
                                 .allMatch(it -> it.length <= max));


    }
}
