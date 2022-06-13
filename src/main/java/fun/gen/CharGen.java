package fun.gen;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of characters.
 */
public final class CharGen implements Gen<Character> {

    public static final Gen<Character> ascii =
            IntGen.arbitrary(('\u0000'),
                             ('\u007f'))
                  .map(it -> ((char) it.intValue()));


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
    private static final Gen<Character> arbitrary = new CharGen();

    private CharGen() {
    }

    static boolean isNonCharacter(int codepoint) {
        if (codepoint >= 55296 && codepoint <= 57343)
            return true;
        if (codepoint >= 64976 && codepoint <= 65007)
            return true;
        return codepoint == 65534 || codepoint == 65535;

    }

    static boolean isPUC(int codepoint) {
        return codepoint >= 57344 && codepoint <= 63743;
    }

    public static Gen<Character> arbitrary(char min,
                                           char max) {
        if (min > max) throw new IllegalArgumentException("min > max");
        return seed -> supplier(seed,
                                min,
                                max);
    }

    public static Gen<Character> arbitrary() {
        return arbitrary;
    }

     static Supplier<Character> supplier(Random gen,
                                                char min,
                                                char max) {
        Supplier<Integer> supplier = IntGen.arbitrary(min,
                                                      max)
                                           .suchThat(it -> !isNonCharacter(it) && !isPUC(it))
                                           .apply(gen);

        return () -> ((char) supplier.get().intValue());
    }

    @Override
    public Supplier<Character> apply(final Random seed) {

        requireNonNull(seed);
        return supplier(seed,
                        '\u0000',
                        '\uffff');
    }
}
