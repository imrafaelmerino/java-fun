package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestFloatGen {

    @Test
    public void biasedDouble() {

        Map<Float, Long> counts = TestFun.generate(100000,
                                                   FloatGen.biased);

        List<Float> problematic = TestFun.list(
                (float) Integer.MAX_VALUE,
                (float) Integer.MIN_VALUE,
                Float.MAX_VALUE,
                Float.MIN_VALUE,
                (float) Short.MAX_VALUE,
                (float) Short.MIN_VALUE,
                (float) Byte.MAX_VALUE,
                (float) Byte.MIN_VALUE,
                0.0f);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

    @Test
    public void arbitraryInt() {

        Assertions.assertTrue(FloatGen.arbitrary(1,
                                                 2)
                                      .sample(100000)
                                      .allMatch(it -> it >= 1.0 && it <= 2.0));


    }


    @Test
    public void biasedFloatInterval() {

        Map<Float, Long> counts = TestFun.generate(100000,
                                                   FloatGen.biased(-1000000000,
                                                                   100000000));

        List<Float> problematic = TestFun.list(100000000.0f,
                                               -1000000000.0f,
                                               (float) Short.MAX_VALUE,
                                               (float) Byte.MAX_VALUE,
                                               (float) Short.MIN_VALUE,
                                               (float) Byte.MIN_VALUE,
                                               0.0f);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);
    }
}
