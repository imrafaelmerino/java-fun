package fun.gen;


import static java.util.Objects.requireNonNull;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Represents a generator for long values. This class provides methods to generate arbitrary and biased long values within specified ranges.
 */
public final class LongGen implements Gen<Long> {

    private static final Gen<Long> arbitrary = new LongGen();

    private LongGen() {
    }

    /**
     * Returns an arbitrary long generator.
     *
     * @return An arbitrary generator for long values.
     */
    public static Gen<Long> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a biased long generator that generates values within the specified range [{@code min}, {@code max}] (inclusive).
     *
     * @param min The minimum value to be generated.
     * @param max The maximum value to be generated.
     * @return A biased generator for long values within the specified range.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<Long> biased(final long min,
                                   final long max) {
        if (max < min) throw new IllegalArgumentException("max < min");

        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<>();
        if (max >= Integer.MAX_VALUE && min <= Integer.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Integer.MAX_VALUE)));

        if (max >= Integer.MIN_VALUE && min <= Integer.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Integer.MIN_VALUE)));
        if (max >= Short.MAX_VALUE && min <= Short.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Short.MAX_VALUE)));
        if (max >= Short.MIN_VALUE && min <= Short.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Short.MIN_VALUE)));
        if (max >= Byte.MAX_VALUE && min <= Byte.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Byte.MAX_VALUE)));
        if (max >= Byte.MIN_VALUE && min <= Byte.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Byte.MIN_VALUE)));
        if (max >= 0 && min <= 0)
            gens.add(Pair.of(1,
                             Gen.cons(0L)));

        gens.add(Pair.of(1,
                         Gen.cons(min)));
        if (max != min)
            gens.add(Pair.of(1,
                             Gen.cons(max)));

        gens.add(Pair.of(gens.size(),
                         arbitrary(min,
                                   max)));

        return Combinators.freqList(gens);
    }

    /**
     * Returns an arbitrary long generator that generates values within the specified range [{@code min}, {@code max}] (inclusive).
     *
     * @param min The minimum value to be generated.
     * @param max The maximum value to be generated.
     * @return An arbitrary generator for long values within the specified range.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<Long> arbitrary(final long min,
                                      final long max) {

        if (max < min) throw new IllegalArgumentException("max < min");

        return seed -> () -> {
            long r = seed.nextLong();
            final long n = max - min + 1;
            final long m = n - 1;
            if ((n & m) == 0L) {
                r = (r & m) + min;
            } else if (n > 0L) {
                for (long u = r >>> 1;            // ensure nonnegative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = seed.nextLong() >>> 1) // retry
                    ;
                r += min;
            } else {
                // It is case (4): length of range not representable as long.
                while (r < min || r > max)
                    r = seed.nextLong();
            }
            return r;
        };
    }

    /**
     * Returns a biased long generator that generates values biased towards common long values.
     *
     * @return A biased generator for long values.
     */
    public static Gen<Long> biased() {
        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<>();

        gens.add(Pair.of(1,
                         Gen.cons(Long.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons(Long.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((long) Integer.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Integer.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Short.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Short.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Byte.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Byte.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons(0L)));

        gens.add(Pair.of(gens.size(),
                         arbitrary));

        return Combinators.freqList(gens);
    }

    /**
     * Returns a biased generator for long values within the range [{@code min}, {@link Long#MAX_VALUE}] (inclusive).
     * This generator is biased towards generating common long values within the specified range.
     *
     * @param min The minimum long value (inclusive).
     * @return A biased long generator starting from the specified minimum value.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<Long> biased(long min) {
        return biased(min,
                      Long.MAX_VALUE);
    }

    /**
     * Generates a long value within the range [{@code min}, {@link Long#MAX_VALUE}] (inclusive) using the provided Random generator.
     *
     * @param min The minimum long value (inclusive).
     * @return A long generator starting from the specified minimum value.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<Long> arbitrary(long min) {
        return arbitrary(min,
                         Long.MAX_VALUE);
    }

    @Override
    public Supplier<Long> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        return gen::nextLong;
    }

}
