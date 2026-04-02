package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SetGenTest {

    @Nested
    class ValidationTests {

        @Test
        void shouldThrowWhenSetSizeCannotBeSatisfied() {
            SetGen<String> gen = SetGen.ofN(StrGen.letters(1, 1), 5_000)
                                       .withMaxTries(5_001);

            Assertions.assertThrows(RuntimeException.class,
                                    () -> gen.sample(10).count());
        }

        @Test
        void shouldNotOverflowDefaultMaxTriesWithLargeSizeWhenUsingOfN() {
            Assertions.assertDoesNotThrow(() -> SetGen.ofN(Gen.constant("x"),
                                                           Integer.MAX_VALUE));
        }
    }

    @Nested
    class GenerationTests {

        @Test
        void shouldSucceedWhenValuesAreUniqueWhenUsingWithMaxTriesEqualToRequestedSize() {
            Set<Integer> values = SetGen.ofN(Gen.seq(n -> n), 3)
                                        .withMaxTries(3)
                                        .sample()
                                        .get();

            Assertions.assertEquals(3, values.size());
            Assertions.assertTrue(values.containsAll(Set.of(1, 2, 3)));
        }
    }
}
