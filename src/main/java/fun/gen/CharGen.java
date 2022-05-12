package fun.gen;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of characters.
 */
public final class CharGen implements Gen<Character> {
    public static final Gen<Character> arbitrary = new CharGen();

    /**
     * Generates a letter from a-z
     */
    public static final Gen<Character> letter =
            IntGen.arbitrary(0,
                             25)
                  .map(i -> ((char) ('a' + i)));
    /**
     * Generates character from 65-122
     */
    public static final Gen<Character> digit =
            Combinators.oneOf(
                    '0',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9');


    public static final Gen<Character> alphabetic =
            Combinators.oneOf(IntGen.arbitrary(65,
                                               90),
                              IntGen.arbitrary(97,
                                               122))
                       .map(i -> (char) i.intValue());

    private CharGen() {
    }

    @Override
    public Supplier<Character> apply(final Random gen) {
        requireNonNull(gen);
        return () -> ((char) requireNonNull(gen).nextInt(256));
    }
}
