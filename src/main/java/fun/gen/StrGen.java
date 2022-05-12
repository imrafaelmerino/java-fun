package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * represent a generator of strings.
 */
public final class StrGen implements Gen<String> {

    public static final Gen<String> digit =
            CharGen.digit.map(String::valueOf);
    /**
     * Generates a letter from a-z
     */
    public static final Gen<String> letter =
            CharGen.letter.map(String::valueOf);
    private final int length;

    private StrGen(final int length) {
        this.length = length;
    }

    public static Gen<String> biased(final int min,
                                     final int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");
        var gens = new ArrayList<Pair<Integer, Gen<? extends String>>>();

        if (min == 0)
            gens.add(new Pair<>(1,
                                Gen.cons("")));

        gens.add(new Pair<>(1,
                            new StrGen(min)));

        gens.add(new Pair<>(1,
                            new StrGen(max - 1)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary(min,
                                      max)));

        return Combinators.freqList(gens);
    }

    public static Gen<String> arbitrary(final int min,
                                        final int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return seed -> genStr(seed,
                              IntGen.arbitrary(min,
                                               max).apply(seed));
    }

    private static Supplier<String> genStr(RandomGenerator seed,
                                           Supplier<Integer> lengthGen) {
        return () -> {
            int length = lengthGen.get();
            if (length == 0) return "";
            byte[] array = new byte[seed.nextInt(length)];
            seed.nextBytes(array);
            return new String(array,
                              UTF_8);
        };
    }

    /**
     * Generates a seq of digits of the given length
     *
     * @param length the length of the string
     * @return a string generator
     */
    public static Gen<String> digits(final int length) {
        if (length <= 0) throw new IllegalArgumentException("length not greater than zero");
        return ListGen.<String>arbitrary(length)
                      .apply(digit)
                      .map(arr -> String.join("",
                                              arr));
    }

    public static Gen<String> letters(final int length) {
        if (length <= 0) throw new IllegalArgumentException("length not greater than zero");
        return ListGen.<String>arbitrary(length)
                      .apply(letter)
                      .map(arr -> String.join("",
                                              arr));
    }

    /**
     * Generates an alphabetic string of the given length
     *
     * @param length the length of the string
     * @return a string generator
     */
    public static Gen<String> alpha(final int length) {
        if (length < 0) throw new IllegalArgumentException("length lower than zero");
        return ListGen.<String>arbitrary(length)
                      .apply(CharGen.alpha.map(String::valueOf))
                      .map(it -> String.join("",
                                             it));

    }

    /**
     * Generates an alphanumeric string of the given length
     *
     * @param length the length of the string
     * @return a string generator
     */
    public static Gen<String> alphanumeric(final int length) {
        if (length < 0) throw new IllegalArgumentException("length lower than zero");

        return ListGen.arbitrary(length)
                      .apply(Combinators.oneOf(CharGen.alpha,
                                               CharGen.digit))
                      .map(it -> it.stream()
                                   .map(String::valueOf)
                                   .collect(Collectors.joining(""))
                      );
    }

    @Override
    public Supplier<String> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return genStr(gen,
                      () -> length);
    }
}

