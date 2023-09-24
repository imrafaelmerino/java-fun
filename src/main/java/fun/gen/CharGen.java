package fun.gen;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of characters.
 * This class implements the {@link Gen} interface to generate characters within various character ranges,
 * including ASCII characters, letters (a-z), digits (0-9), and arbitrary characters within specified ranges.
 * It provides methods to generate characters from these ranges and check for valid character codepoints.
 * <p>
 * Example usage:
 * <pre>
 * // Create an ASCII character generator.
 * Gen&lt;Character&gt; asciiGenerator = CharGen.ascii();
 * Character asciiChar = asciiGenerator.sample(new Random()).get();
 *
 * // Create a letter (a-z) character generator.
 * Gen&lt;Character&gt; letterGenerator = CharGen.letter();
 * Character letterChar = letterGenerator.sample(new Random()).get();
 *
 * // Create a digit (0-9) character generator.
 * Gen&lt;Character&gt; digitGenerator = CharGen.digit();
 * Character digitChar = digitGenerator.sample(new Random()).get();
 *
 * // Create an arbitrary character generator within the range (min, max).
 * Gen&lt;Character&gt; arbitraryGenerator = CharGen.arbitrary('A', 'Z');
 * Character arbitraryChar = arbitraryGenerator.sample(new Random()).get();
 * </pre>
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

    /**
     * Generates ASCII characters within the range 0 to 127 (inclusive).
     *
     * @return An ASCII character generator.
     */
    public static  Gen<Character> ascii(){
        return ascii;
    }


    private static final Gen<Character> letter =
            IntGen.arbitrary(0,
                             25)
                  .map(i -> ((char) ('a' + i)));
    /**
     * Generates lowercase letters (a-z).
     *
     * @return A letter (a-z) character generator.
     */
    public static  Gen<Character> letter(){
        return letter;
    }


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
    /**
     * Generates digits (0-9).
     *
     * @return A digit (0-9) character generator.
     */
    public static  Gen<Character> digit(){
        return digit;
    }
    private static final Gen<Character> alphabetic =
            Combinators.oneOf(IntGen.arbitrary(65,
                                               90),
                              IntGen.arbitrary(97,
                                               122))
                       .map(i -> (char) i.intValue());

    /**
     * Generates alphabetic characters (A-Z, a-z).
     *
     * @return An alphabetic (A-Z, a-z) character generator.
     */
    public static  Gen<Character> alphabetic(){
        return alphabetic;
    }
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
