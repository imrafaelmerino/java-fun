package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.ListGen;
import fun.gen.StrGen;
import jsonvalues.JsStr;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class JsStrGen implements Gen<JsStr> {

    /**
     * Generates a seq of digits of the given length
     *
     * @param length the length of the string
     * @return a string generator
     */
    public static Gen<JsStr> digits(int length) {
        return JsStrGen.of(StrGen.digits(length));
    }

    public static Gen<JsStr> letters(int length) {
        return JsStrGen.of(StrGen.letters(length));
    }

    public static Gen<JsStr> arbitrary(int min,int max) {
        return JsStrGen.of(StrGen.arbitrary(min,max));
    }

    public static Gen<JsStr> biased(int min,int max) {
        return JsStrGen.of(StrGen.biased(min,max));
    }

    public static final Gen<JsStr> digit = JsStrGen.of(StrGen.digit);

    /**
     * Generates a letter from a-z
     */
    public static final Gen<JsStr> letter = JsStrGen.of(StrGen.letter);


    /**
     * Generates an alphabetic string of the given length
     *
     * @param length the length of the string
     * @return a string generator
     */
    public static Gen<JsStr> alpha(final int length) {
        return JsStrGen.of(StrGen.alpha(length));

    }

    /**
     * Generates an alphanumeric string of the given length
     *
     * @param length the length of the string
     * @return a string generator
     */
    public static Gen<JsStr> alphanumeric(final int length) {
        return JsStrGen.of(StrGen.alphanumeric(length));
    }

    private final Gen<String> gen;

    private JsStrGen(Gen<String> gen) {
        this.gen = gen;
    }

    public static Gen<JsStr> of(final Gen<String> gen) {
        return new JsStrGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsStr> apply(RandomGenerator seed) {
        return gen.map(JsStr::of).apply(seed);
    }
}
