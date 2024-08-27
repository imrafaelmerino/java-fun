package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestDoubleGen {

    @Test
    public void biasedDouble() {

        Map<Double, Long> counts = TestFun.generate(100000,
                                                    DoubleGen.biased());

        List<Double> problematic = TestFun.list((double) Integer.MAX_VALUE,
                                                (double) Integer.MIN_VALUE,
                                                (double) Short.MAX_VALUE,
                                                (double) Short.MIN_VALUE,
                                                (double) Byte.MAX_VALUE,
                                                (double) Byte.MIN_VALUE,
                                                0.0);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

    @Test
    public void arbitraryInt() {

        Assertions.assertTrue(DoubleGen.arbitrary(1,
                                                  2)
                                       .sample(100000)
                                       .allMatch(it -> it >= 1.0 && it <= 2.0));


    }


    @Test
    public void biasedIntInterval() {

        Map<Double, Long> counts = TestFun.generate(100000,
                                                    DoubleGen.biased(-1000000000,
                                                                     100000000));

        List<Double> problematic = TestFun.list(100000000.0,
                                                -1000000000.0,
                                                (double) Short.MAX_VALUE,
                                                (double) Byte.MAX_VALUE,
                                                (double) Short.MIN_VALUE,
                                                (double) Byte.MIN_VALUE,
                                                0.0);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);
    }
}
