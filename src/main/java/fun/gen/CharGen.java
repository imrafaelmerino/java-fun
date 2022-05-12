package fun.gen;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

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
                             26).map(i -> ((char) ('a' + i)));
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


    public static final Gen<Character> alpha =
            IntGen.arbitrary(65,
                             123).map(i -> (char) i.intValue());

    private CharGen() {
    }

    @Override
    public Supplier<Character> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return () -> ((char) requireNonNull(gen).nextInt(256));
    }
}
