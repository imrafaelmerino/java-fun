package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

/**
 * A generator for strings of various types and lengths.
 */
public final class StrGen implements Gen<String> {

    private static final Gen<String> ascii = CharGen.ascii().map(String::valueOf);

    private static final Gen<String> digit = CharGen.digit().map(String::valueOf);

    private static final Gen<String> letter = CharGen.letter().map(String::valueOf);
    private static final Gen<String> alphabetic = CharGen.alphabetic().map(String::valueOf);
    private static final Gen<String> alphanumeric = Combinators.oneOf(CharGen.alphabetic(),
                                                                      CharGen.digit())
                                                               .map(String::valueOf);
    private final int length;

    private StrGen(final int length) {
        this.length = length;
    }


    /**
     * Generates a biased string with a minimum and maximum length.
     *
     * @param minLength The minimum length of the generated string.
     * @param maxLength The maximum length of the generated string.
     * @return A string generator biased towards the following values:
     * - Empty string ("") with a frequency of 1 if {@code minLength} is 0.
     * - A string with spaces (blank) of length {@code minLength}.
     * - A string with spaces (blank) of length {@code maxLength}.
     * - A string with random characters within the specified length range.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> biased(final int minLength,
                                     final int maxLength) {
        if (minLength < 0)
            throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength)
            throw new IllegalArgumentException("maxLength < minLength");
        List<Pair<Integer, Gen<? extends String>>> gens = new ArrayList<>();
        if (minLength == maxLength && minLength == 0) return Gen.cons("");

        if (minLength == 0) {
            gens.add(Pair.of(1,
                             Gen.cons("")));
        } else {
            gens.add(Pair.of(1,
                             new StrGen(minLength)));

            gens.add(Pair.of(1,
                             Gen.cons(blank(minLength))));
        }


        if (minLength != maxLength) {

            gens.add(Pair.of(1,
                             Gen.cons(blank(maxLength))));

            gens.add(Pair.of(1,
                             new StrGen(maxLength)));
        }

        gens.add(Pair.of(gens.size(),
                         arbitrary(minLength,
                                   maxLength)));

        return Combinators.freqList(gens);
    }

    /**
     * Generates a string with a minimum and maximum length.
     *
     * @param minLength The minimum length of the generated string.
     * @param maxLength The maximum length of the generated string.
     * @return A string generator producing values with lengths between {@code minLength} and {@code maxLength}.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> arbitrary(final int minLength,
                                        final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return seed -> genStr(SplitGen.DEFAULT.apply(seed),
                              IntGen.arbitrary(minLength,
                                               maxLength)
                                    .apply(SplitGen.DEFAULT.apply(seed)));
    }

    private static Supplier<String> genStr(RandomGenerator seed,
                                           Supplier<Integer> lengthGen) {

        return () -> {
            int l = lengthGen.get();
            List<Character> arbitrary =
                    ListGen.arbitrary(CharGen.arbitrary(),
                                      l,
                                      l)
                           .apply(seed).get();
            return chars2String(arbitrary);
        };


    }

    private static Supplier<String> genStr(RandomGenerator seed,
                                           Integer length) {
        Supplier<List<Character>> chars =
                ListGen.arbitrary(CharGen.arbitrary(),
                                  length,
                                  length)
                       .apply(seed);
        return () -> chars2String(chars.get());

    }


    private static String chars2String(List<Character> arbitrary) {
        return arbitrary
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    /**
     * Generates a sequence of digits as a string with a length between the specified interval.
     *
     * @param minLength The minimum length of the generated digit sequence.
     * @param maxLength The maximum length of the generated digit sequence.
     * @return A string generator producing digit sequences between {@code minLength} and {@code maxLength} in length.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> digits(final int minLength,
                                     final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return ListGen.arbitrary(digit,
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }

    /**
     * Generates a sequence of ASCII characters as a string with a length between the specified interval.
     *
     * @param minLength The minimum length of the generated ASCII sequence.
     * @param maxLength The maximum length of the generated ASCII sequence.
     * @return A string generator producing ASCII sequences between {@code minLength} and {@code maxLength} in length.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> ascii(final int minLength,
                                    final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return ListGen.arbitrary(ascii,
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }


    /**
     * Generates a sequence of letters as a string with a length between the specified interval.
     *
     * @param minLength The minimum length of the generated letter sequence.
     * @param maxLength The maximum length of the generated letter sequence.
     * @return A string generator producing letter sequences between {@code minLength} and {@code maxLength} in length.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> letters(final int minLength,
                                      final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return ListGen.arbitrary(letter,
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }

    /**
     * Generates a sequence of alphabetic characters as a string with a length between the specified interval.
     *
     * @param minLength The minimum length of the generated alphabetic sequence.
     * @param maxLength The maximum length of the generated alphabetic sequence.
     * @return A string generator producing alphabetic sequences between {@code minLength} and {@code maxLength} in length.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> alphabetic(final int minLength,
                                         final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");

        return ListGen.arbitrary(alphabetic,
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }

    /**
     * Generates a sequence of alphanumeric characters as a string with a length between the specified interval.
     *
     * @param minLength The minimum length of the generated alphanumeric sequence.
     * @param maxLength The maximum length of the generated alphanumeric sequence.
     * @return A string generator producing alphanumeric sequences between {@code minLength} and {@code maxLength} in length.
     * @throws IllegalArgumentException If {@code minLength} is negative or {@code maxLength} is less than {@code minLength}.
     */
    public static Gen<String> alphanumeric(final int minLength,
                                           final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");

        return ListGen.arbitrary(alphanumeric,
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));

    }

    private static String blank(int length) {
        if (length <= 0) throw new IllegalArgumentException("length <= 0");
        return String.join("",
                           Collections.nCopies(length,
                                               " "));

    }

    @Override
    public Supplier<String> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return genStr(gen,
                      length);
    }
}

