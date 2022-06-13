package fun.gen;

import fun.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * represent a generator of strings.
 */
public final class StrGen implements Gen<String> {

    private static final Gen<String> ascii = CharGen.ascii.map(String::valueOf);

    private static final Gen<String> digit = CharGen.digit.map(String::valueOf);
    /**
     * Generates a letter from a-z
     */
    private static final Gen<String> letter = CharGen.letter.map(String::valueOf);
    private static final Gen<String> alphabetic = CharGen.alphabetic.map(String::valueOf);
    private static final Gen<String> alphanumeric = Combinators.oneOf(CharGen.alphabetic,
                                                                      CharGen.digit).map(String::valueOf);
    private final int length;

    private StrGen(final int length) {
        this.length = length;
    }

    public static Gen<String> digit() {
        return digit;
    }

    public static Gen<String> ascii() {
        return ascii;
    }

    public static Gen<String> letter() {
        return letter;
    }

    public static Gen<String> alphabetic() {
        return alphabetic;
    }

    public static Gen<String> alphanumeric() {
        return alphanumeric;
    }

    public static Gen<String> biased(final int minLength,
                                     final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
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

    public static Gen<String> arbitrary(final int minLength,
                                        final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return seed -> genStr(SplitGen.DEFAULT.apply(seed),
                              IntGen.arbitrary(minLength,
                                               maxLength)
                                    .apply(SplitGen.DEFAULT.apply(seed)));
    }

    private static Supplier<String> genStr(Random seed,
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
    private static Supplier<String> genStr(Random seed,
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
     * Generates a seq of digits of a length between the specified interval
     *
     * @param minLength min length of the string
     * @param maxLength max length of the string
     * @return a string generator
     */
    public static Gen<String> digits(final int minLength,
                                     final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return ListGen.arbitrary(CharGen.digit.map(String::valueOf),
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }

    /**
     * Generates a seq of digits of a length between the specified interval
     *
     * @param minLength min length of the string
     * @param maxLength max length of the string
     * @return a string generator
     */
    public static Gen<String> ascii(final int minLength,
                                    final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return ListGen.arbitrary(CharGen.ascii.map(String::valueOf),
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }


    /**
     * Generates a seq of letters of a length between the specified interval
     *
     * @param minLength min length of the string
     * @param maxLength max length of the string
     * @return a string generator
     */
    public static Gen<String> letters(final int minLength,
                                      final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return ListGen.arbitrary(CharGen.letter.map(String::valueOf),
                                 minLength,
                                 maxLength)
                      .map(it -> String.join("",
                                             it));
    }

    /**
     * Generates a seq of alphabetic characters of a length between the specified interval
     *
     * @param minLength min length of the string
     * @param maxLength max length of the string
     * @return a string generator
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
     * Generates a seq of alphanumeric characters of a length between the specified interval
     *
     * @param minLength min length of the string
     * @param maxLength max length of the string
     * @return a string generator
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
    public Supplier<String> apply(final Random gen) {
        Objects.requireNonNull(gen);
        return genStr(gen,
                      length);
    }
}

