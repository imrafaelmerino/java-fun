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
    public void testBigInt() {
        int bits = 5;

        Map<BigInteger, Long> counts = TestFun.generate(100000,
                                                        BigIntGen.arbitrary(bits,
                                                                            bits + 1));

        List<BigInteger> values = IntStream.range(0,
                                                  1 << bits).mapToObj(BigInteger::valueOf).collect(Collectors.toList());


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         values,
                                                         0.1);


    }

    @Test
    public void arbitraryBigInt() {
        Map<BigInteger, Long> counts = TestFun.generate(100000,
                                                        BigIntGen.arbitrary(1,
                                                                            3));


        Assertions.assertTrue(IntStream.range(0,
                                              7)
                                       .allMatch(it -> counts.containsKey(BigInteger.valueOf(it))));
    }


}
