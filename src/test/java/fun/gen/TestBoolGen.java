package fun.gen;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestBoolGen {


    @Test
    public void testBooleanGen() {

        Map<Boolean, Long> counts = TestFun.generate(100000,
                                                     BoolGen.arbitrary());

        List<Boolean> values = TestFun.list(true,
                                            false);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         values,
                                                         0.05);

    }
}
