package fun.gen;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SetGenTest {

    @Test
    void shouldThrowWhenSetSizeCannotBeSatisfied() {
        SetGen<String> gen =
                SetGen.ofN(StrGen.letters(1,
                                          1),
                           5000)
                      .withMaxTries(5001);

        Assertions.assertThrows(RuntimeException.class,
                                () -> gen.sample(10)
                                         .count());
    }

    @Test
    void shouldNotOverflowDefaultMaxTriesWithLargeSizeWhenUsingOfN() {
        Assertions.assertDoesNotThrow(() -> SetGen.ofN(Gen.constant("x"),
                                                       Integer.MAX_VALUE));
    }

    @Test
    void shouldSucceedWhenValuesAreUniqueWhenUsingWithMaxTriesEqualToRequestedSize() {
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
