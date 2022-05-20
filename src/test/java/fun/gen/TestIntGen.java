package fun.gen;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestIntGen {

    @Test
    public void biasedInt() {

        Map<Integer, Long> counts = TestFun.generate(100000,
                                                     IntGen.biased());

        List<Integer> problematic = TestFun.list(Integer.MAX_VALUE,
                                                 Integer.MIN_VALUE,
                                                 (int) Short.MAX_VALUE,
                                                 (int) Short.MIN_VALUE,
                                                 (int) Byte.MAX_VALUE,
                                                 (int) Byte.MIN_VALUE,
                                                 0);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

    @Test
    public void arbitraryInt() {

        Map<Integer, Long> counts = TestFun.generate(10000000,
                                                     IntGen.arbitrary(Integer.MAX_VALUE - 10,
                                                                      Integer.MAX_VALUE));

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(Integer.MAX_VALUE - 10,
                                                                      Integer.MAX_VALUE - 9,
                                                                      Integer.MAX_VALUE - 8,
                                                                      Integer.MAX_VALUE - 7,
                                                                      Integer.MAX_VALUE - 6,
                                                                      Integer.MAX_VALUE - 5,
                                                                      Integer.MAX_VALUE - 4,
                                                                      Integer.MAX_VALUE - 3,
                                                                      Integer.MAX_VALUE - 2,
                                                                      Integer.MAX_VALUE - 1,
                                                                      Integer.MAX_VALUE),
                                                         0.05);


    }


    @Test
    public void biasedIntInterval() {

        Map<Integer, Long> counts = TestFun.generate(100000,
                                                     IntGen.biased(-1000000000,
                                                                   100000000));

        List<Integer> problematic = TestFun.list(100000000,
                                                 -1000000000,
                                                 (int) Short.MAX_VALUE,
                                                 (int) Byte.MAX_VALUE,
                                                 (int) Short.MIN_VALUE,
                                                 (int) Byte.MIN_VALUE,
                                                 0);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);
    }
}
