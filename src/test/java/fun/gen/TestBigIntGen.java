package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestBigIntGen {


    @Test
    public void arbitraryBigInt() {
        Map<BigInteger, Long> counts = TestFun.generate(100000,
                                                        BigIntGen.arbitrary(3));


        Assertions.assertTrue(IntStream.range(0,
                                              7)
                                       .allMatch(it -> counts.containsKey(BigInteger.valueOf(it))));
    }


    @Test
    public void biasedBigIntInterval() {

        Map<BigInteger, Long> counts =
                TestFun.generate(1000000,
                                 BigIntGen.biased(1000));


        List<BigInteger> problematic = TestFun.list(
                BigInteger.valueOf(Long.MAX_VALUE),
                BigInteger.valueOf(Integer.MAX_VALUE),
                BigInteger.valueOf(Short.MAX_VALUE),
                BigInteger.valueOf(Byte.MAX_VALUE),
                BigInteger.ZERO);


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.1);
    }


}
