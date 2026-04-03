package fun.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.util.List;
import java.util.Map;

@Tag("stats")
class BoolGenTest {


    @Test
    void shouldGenerateTrueAndFalseWithSimilarProbabilityWhenUsingArbitrary() {

        Map<Boolean, Long> counts = TestFun.generate(100000,
                                                     BoolGen.arbitrary());

        List<Boolean> values = TestFun.list(true,
                                            false);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         values,
                                                         0.05);

    }
}
