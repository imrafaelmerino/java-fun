package fun.gen;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestSetGen {

    @Test
    public void testNotAbleToGenerateSetOfSize() {
        SetGen<String> gen =
                SetGen.of(StrGen.letters(1,
                                         1),
                          5000)
                      .setMaxTries(5001);

        Assertions.assertThrows(RuntimeException.class,
                                () -> gen.sample(10).peek(System.out::println).count());
    }

}
