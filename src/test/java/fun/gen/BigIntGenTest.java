package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.random.RandomGenerator;

class BigIntGenTest {


    @Test
    void shouldGenerateValuesWithinBoundsWhenUsingArbitrary() {


        BigInteger min = new BigInteger("1000000000");
        BigInteger max = new BigInteger("2000000000");
        Gen<BigInteger> arbitraryBigIntGen =
                BigIntGen.arbitrary(min,
                                    max
                );

        Assertions.assertTrue(arbitraryBigIntGen
                                      .sample(100000)
                                      .allMatch(
                                              bi -> bi.compareTo(min) >= 0 && bi.compareTo(max) <= 0));
    }

    @Test
    void shouldReachHighBitLengthsWhenUsingArbitraryDefault() {
        var supplier = BigIntGen.arbitrary()
                                .sample(RandomGenerator.getDefault());
        int maxBitLength = 0;
        for (int i = 0; i < 500; i++) {
            maxBitLength = Math.max(maxBitLength,
                                    supplier.get()
                                            .bitLength());
        }

        Assertions.assertTrue(maxBitLength > 40,
                              "Expected arbitrary 64-bit generator to produce values with bitLength > 40, got " + maxBitLength);
    }


    @Test
    void shouldBiasTowardBoundaryValuesWhenUsingBiasedInterval() {
        BigInteger min = new BigInteger("-10000000000000000000000");
        BigInteger max = new BigInteger("100000000000000000000000");
        var gen =
                BigIntGen.biased(min,
                                 max
                );


        List<BigInteger> problematic =
                TestFun.list(min,
                             max,
                             BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE),
                             BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE),
                             BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
                             BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE),
                             BigInteger.ZERO);

        TestFun.assertGeneratedValuesHaveSameProbability(gen.collect(10000000),
                                                         problematic,
                                                         0.05);


    }

    @Test
    void shouldBiasTowardBoundaryValuesWhenUsingBiasedDefault() {
        var gen = BigIntGen.biased();


        List<BigInteger> problematic =
                TestFun.list(BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE),
                             BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE),
                             BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
                             BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE),
                             BigInteger.ZERO);

        TestFun.assertGeneratedValuesHaveSameProbability(gen.collect(10000000),
                                                         problematic,
                                                         0.05);


    }


}
