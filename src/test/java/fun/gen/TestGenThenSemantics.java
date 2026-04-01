package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestGenThenSemantics {

    @Test
    public void thenShouldEvaluateOuterGeneratorForEachSample() {
        List<Integer> values = Gen.seq(n -> n)
                                  .then(Gen::cons)
                                  .sample(5)
                                  .toList();

        Assertions.assertEquals(List.of(1,
                                        2,
                                        3,
                                        4,
                                        5),
                                values);
    }
}

