package fun.gen;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TestBigDecGen {


    @Test
    public void biasedBigDecimalInterval() {

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
    public void biasedBigDecimal() {

        Map<BigDecimal, Long> counts = TestFun.generate(100000,
                                                        BigDecGen.biased());

        List<BigDecimal> problematic = TestFun.list(BigDecimal.valueOf(Long.MAX_VALUE),
                                                    BigDecimal.valueOf(Long.MAX_VALUE),
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

}
