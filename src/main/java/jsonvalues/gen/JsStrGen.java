package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.StrGen;
import jsonvalues.JsStr;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsStrGen implements Gen<JsStr> {

    /**
     * Generator tha produces a digit from 0 to 9
     */
    public static final Gen<JsStr> digit = JsStrGen.of(StrGen.digit);
    /**
     * Generator tha produces a letter from a to z
     */
    public static final Gen<JsStr> letter = JsStrGen.of(StrGen.letter);

    /**
     * Generator tha produces an alphabetic character
     */
    public static final Gen<JsStr> alphabetic = JsStrGen.of(StrGen.alphabetic);

    /**
     * Generator tha produces an alphanumeric character
     */
    public static final Gen<JsStr> alphanumeric = JsStrGen.of(StrGen.alphanumeric);


    private final Gen<String> gen;

    private JsStrGen(Gen<String> gen) {
        this.gen = gen;
    }

    /**
     * Generates a seq of digits
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> digits(final int minLength,
                                    final int maxLength) {
        return JsStrGen.of(StrGen.digits(minLength,
                                         maxLength));
    }

    /**
     * Generates a seq of letters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> letters(final int minLength,
                                     final int maxLength) {
        return JsStrGen.of(StrGen.letters(minLength,
                                          maxLength));
    }

    /**
     * Generates a seq of alphabetic characters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> alphabetic(final int minLength,
                                        final int maxLength) {
        return JsStrGen.of(StrGen.alphabetic(minLength,
                                             maxLength));
    }

    /**
     * Generates a seq of alphanumeric characters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> alphanumeric(final int minLength,
                                          final int maxLength) {
        return JsStrGen.of(StrGen.alphanumeric(minLength,
                                               maxLength));
    }

    /**
     * Generates a seq of arbitrary characters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> arbitrary(final int minLength,
                                       final int maxLength) {
        return JsStrGen.of(StrGen.arbitrary(minLength,
                                            maxLength));
    }

    /**
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> biased(final int minLength,
                                    final int maxLength) {
        return JsStrGen.of(StrGen.biased(minLength,
                                         maxLength));
    }

    public static Gen<JsStr> of(final Gen<String> gen) {
        return new JsStrGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsStr> apply(Random seed) {
        return gen.map(JsStr::of)
                  .apply(seed);
    }
}
