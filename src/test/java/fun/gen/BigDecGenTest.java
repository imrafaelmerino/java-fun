package fun.gen;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;

class BigDecGenTest {


    @Test
    @Tag("stats")
    void shouldBiasTowardBoundaryValuesWhenUsingBiasedInterval() {

        Map<BigDecimal, Long> counts =
                TestFun.generate(100000,
                                 BigDecGen.biased(BigDecimal.valueOf(Long.MIN_VALUE),
                                                  BigDecimal.valueOf(Long.MAX_VALUE)));


        List<BigDecimal> problematic = TestFun.list(
                BigDecimal.valueOf(Long.MAX_VALUE),
                BigDecimal.valueOf(Long.MIN_VALUE),
                BigDecimal.valueOf(Integer.MAX_VALUE),
                BigDecimal.valueOf(Short.MAX_VALUE),
                BigDecimal.valueOf(Byte.MAX_VALUE),
                BigDecimal.valueOf(Integer.MIN_VALUE),
                BigDecimal.valueOf(Short.MIN_VALUE),
                BigDecimal.valueOf(Byte.MIN_VALUE),
                BigDecimal.ZERO);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.1);
    }


    @Test
    @Tag("stats")
    void shouldBiasTowardBoundaryValuesWhenUsingBiasedDefault() {

        Map<BigDecimal, Long> counts = TestFun.generate(100000,
                                                        BigDecGen.biased());

        List<BigDecimal> problematic = TestFun.list(BigDecimal.valueOf(Long.MAX_VALUE),
                                                    BigDecimal.valueOf(Long.MIN_VALUE),
                                                    BigDecimal.valueOf(Integer.MAX_VALUE),
                                                    BigDecimal.valueOf(Integer.MIN_VALUE),
                                                    BigDecimal.valueOf(Short.MAX_VALUE),
                                                    BigDecimal.valueOf(Short.MIN_VALUE),
                                                    BigDecimal.valueOf(Byte.MAX_VALUE),
                                                    BigDecimal.valueOf(Byte.MIN_VALUE),
                                                    BigDecimal.ZERO);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

    @Test
    void shouldBiasTowardZeroWhenUpperBoundIsZero() {
        Map<BigDecimal, Long> counts = TestFun.generate(20000,
                                                        BigDecGen.biased(BigDecimal.valueOf(-0.1),
                                                                         BigDecimal.ZERO));

        long zeroCount = counts.getOrDefault(BigDecimal.ZERO,
                                             0L);

        // Regression check: max (=0) must be explicitly present in the biased set.
        // Without that bias, zero appears only from the arbitrary branch and is much rarer.
        org.junit.jupiter.api.Assertions.assertTrue(zeroCount > 3000,
                                                    "Expected strong bias towards zero, but got count=" + zeroCount);
    }

    @Test
    void shouldAlwaysRespectBoundsAfterRoundingWhenUsingArbitrary() {
        BigDecimal min = new BigDecimal("0.006");
        BigDecimal max = new BigDecimal("0.016");
        var supplier = BigDecGen.arbitrary(min,
                                           max)
                                .sample(new Random(0L));
        for (int i = 0; i < 20000; i++) {
            BigDecimal generated = supplier.get();
            org.junit.jupiter.api.Assertions.assertTrue(generated.compareTo(min) >= 0
                                                                && generated.compareTo(max) <= 0,
                                                        "Generated value out of bounds: " + generated);
        }
    }

}
