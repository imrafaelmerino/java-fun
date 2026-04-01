package fun.gen;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestSetGen {

    @Test
    public void testNotAbleToGenerateSetOfSize() {
        SetGen<String> gen =
                SetGen.ofN(StrGen.letters(1,
                                          1),
                           5000)
                      .withMaxTries(5001);

        Assertions.assertThrows(RuntimeException.class,
                                () -> gen.sample(10).peek(System.out::println).count());
    }

    @Test
    public void ofNShouldNotOverflowDefaultMaxTriesWithLargeSize() {
        Assertions.assertDoesNotThrow(() -> SetGen.ofN(Gen.cons("x"),
                                                       Integer.MAX_VALUE));
    }

    @Test
    public void withMaxTriesEqualToRequestedSizeShouldSucceedWhenValuesAreUnique() {
        Set<Integer> values = SetGen.ofN(Gen.seq(n -> n),
                                         3)
                                    .withMaxTries(3)
                                    .sample()
                                    .get();

        Assertions.assertEquals(3,
                                values.size());
        Assertions.assertTrue(values.containsAll(Set.of(1,
                                                        2,
                                                        3)));
    }

}
