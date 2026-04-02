package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GenFlatMapSemanticsTest {

    @Test
    void shouldEvaluateOuterGeneratorForEachSampleWhenUsingFlatMap() {
        List<Integer> values = Gen.seq(n -> n)
                                  .flatMap(Gen::constant)
                                  .sample(5)
                                  .toList();

        Assertions.assertEquals(List.of(1,
                                        2,
                                        3,
                                        4,
                                        5),
                                values);
    }

    @Test
    void shouldUseOuterValueToChooseInnerGeneratorWhenUsingFlatMap() {
        List<Integer> values = Gen.seq(n -> n)
                                  .flatMap(n -> Gen.constant(n * 10))
                                  .sample(4)
                                  .toList();

        Assertions.assertEquals(List.of(10,
                                        20,
                                        30,
                                        40),
                                values);
    }
}
