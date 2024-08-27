package fun.gen;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Represents a generator of characters.
 * This class implements the {@link Gen} interface to generate characters within various character ranges,
 * including ASCII characters, letters (a-z), digits (0-9), and arbitrary characters within specified ranges.
 * It provides methods to generate characters from these ranges and check for valid character codepoints.
 *
 * @see Gen
 * @see IntGen
 * @see Combinators
 */
public final class CharGen implements Gen<Character> {

    private static final Gen<Character> ascii =
            IntGen.arbitrary(('\u0000'),
                             ('\u007f'))
                  .map(it -> ((char) it.intValue()));
    private static final Gen<Character> letter =
            IntGen.arbitrary(0,
                             25)
                  .map(i -> ((char) ('a' + i)));
    private static final Gen<Character> digit =
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
    private static final Gen<Character> alphabetic =
            Combinators.oneOf(IntGen.arbitrary(65,
                                               90),
                              IntGen.arbitrary(97,
                                               122))
                       .map(i -> (char) i.intValue());
    private static final Gen<Character> arbitrary = new CharGen();

    private CharGen() {
    }

    /**
     * Generates ASCII characters within the range 0 to 127 (inclusive).
     *
     * @return An ASCII character generator.
     */
    public static Gen<Character> ascii() {
        return ascii;
    }

    /**
     * Generates lowercase letters (a-z).
     *
     * @return A letter (a-z) character generator.
     */
    public static Gen<Character> letter() {
        return letter;
    }

    /**
     * Generates digits (0-9).
     *
     * @return A digit (0-9) character generator.
     */
    public static Gen<Character> digit() {
        return digit;
    }

    /**
     * Generates alphabetic characters (A-Z, a-z).
     *
     * @return An alphabetic (A-Z, a-z) character generator.
     */
    public static Gen<Character> alphabetic() {
        return alphabetic;
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

    /**
     * Generates arbitrary characters within the specified range [{@code min}, {@code max}] (inclusive).
     *
     * @param min The minimum character value (inclusive).
     * @param max The maximum character value (inclusive).
     * @return An arbitrary character generator within the specified range.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
     */
    public static Gen<Character> arbitrary(char min,
                                           char max) {
        if (min > max) throw new IllegalArgumentException("min > max");
        return seed -> supplier(seed,
                                min,
                                max);
    }

    /**
     * Generates arbitrary characters within the entire Unicode character range (0 to 65535).
     *
     * @return An arbitrary character generator for the entire Unicode range.
     */
    public static Gen<Character> arbitrary() {
        return arbitrary;
    }

    static Supplier<Character> supplier(RandomGenerator gen,
                                        char min,
                                        char max) {
        Supplier<Integer> supplier = IntGen.arbitrary(min,
                                                      max)
                                           .suchThat(it -> !isNonCharacter(it) && !isPUC(it))
                                           .apply(gen);

        return () -> ((char) supplier.get().intValue());
    }

    @Override
    public Supplier<Character> apply(final RandomGenerator seed) {

        requireNonNull(seed);
        return supplier(seed,
                        '\u0000',
                        '\uffff');
    }
}
