package fun.gen;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestLongGen {


    @Test
    public void biasedLongInterval() {

        Map<Long, Long> counts = TestFun.generate(100000,
                                                  LongGen.biased(-100000000000000000L,
                                                                 100000000000000000L));

        List<Long> problematic = TestFun.list(
                100000000000000000L,
                -100000000000000000L,
                (long) Integer.MAX_VALUE,
                (long) Short.MAX_VALUE,
                (long) Byte.MAX_VALUE,
                (long) Integer.MIN_VALUE,
                (long) Short.MIN_VALUE,
                (long) Byte.MIN_VALUE,
                0L);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }


    @Test
    public void biasedLong() {

        Map<Long, Long> counts = TestFun.generate(100000,
                                                  LongGen.biased());

        List<Long> problematic = TestFun.list(Long.MAX_VALUE,
                                              Long.MAX_VALUE,
                                              (long) Integer.MAX_VALUE,
                                              (long) Integer.MIN_VALUE,
                                              (long) Short.MAX_VALUE,
                                              (long) Short.MIN_VALUE,
                                              (long) Byte.MAX_VALUE,
                                              (long) Byte.MIN_VALUE,
                                              0L);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

}
