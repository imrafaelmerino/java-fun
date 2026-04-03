package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

class SplitGenTest {

    @Test
    void shouldSplitWhenSourceIsSplittableGenerator() {
        SplittableRandom source = new SplittableRandom(1L);

        RandomGenerator split = SplitGen.DEFAULT.apply(source);

        Assertions.assertNotNull(split);
        Assertions.assertNotSame(source,
                                 split);
    }

    @Test
    void shouldSplitWhenSourceIsRandom() {
        Random source = new Random(1L);

        RandomGenerator split = SplitGen.DEFAULT.apply(source);

        Assertions.assertNotNull(split);
        Assertions.assertNotSame(source,
                                 split);
        Assertions.assertTrue(split instanceof Random);
    }

    @Test
    void shouldSplitWhenSourceIsSecureRandom() {
        SecureRandom source = new SecureRandom(new byte[]{1,
                                                          2,
                                                          3,
                                                          4});

        RandomGenerator split = SplitGen.DEFAULT.apply(source);

        Assertions.assertNotNull(split);
        Assertions.assertNotSame(source,
                                 split);
        Assertions.assertTrue(split instanceof SecureRandom);
    }

    @Test
    void shouldFallbackWhenSourceIsGenericRandom() {
        StubRandomGenerator source = new StubRandomGenerator();

        RandomGenerator split = SplitGen.DEFAULT.apply(source);

        Assertions.assertNotNull(split);
        Assertions.assertNotSame(source,
                                 split);
        Assertions.assertNotEquals(StubRandomGenerator.class,
                                   split.getClass());
    }

    private static final class StubRandomGenerator implements RandomGenerator {

        private long state = 1L;

        @Override
        public int nextInt() {
            return (int) nextLong();
        }

        @Override
        public long nextLong() {
            state = (state * 1664525L) + 1013904223L;
            return state;
        }
    }
}
