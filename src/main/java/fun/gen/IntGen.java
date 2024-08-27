package fun.gen;


import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of Integer values. This class provides methods to generate arbitrary and biased Integer values.
 * Use this class to create random Integer generators for various purposes.
 */
public final class IntGen implements Gen<Integer> {

    private static final Gen<Integer> arbitrary = new IntGen();

    private IntGen() {
    }

    /**
     * Returns a generator for arbitrary Integer values. This generator produces random Integer values within the entire range of possible Integer values.
     *
     * @return A generator for arbitrary Integer values.
     */
    public static Gen<Integer> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a biased generator for Integer values starting from {@code min} (inclusive).
     * This generator is biased towards producing common Integer values starting from the specified minimum value.
     *
     * @param min The minimum Integer value (inclusive) from which values will be generated.
     * @return A biased generator for Integer values starting from the specified minimum value.
     * @throws IllegalArgumentException If {@code min} is greater than {@link Integer#MAX_VALUE}.
     */
    public static Gen<Integer> biased(int min) {
        return biased(min,
                      Integer.MAX_VALUE);
    }

    /**
     * Returns a biased generator for Integer values within the specified range [{@code min}, {@code max}] (inclusive).
     * This generator is biased towards generating common Integer values within the specified range.
     *
     * @param min The minimum Integer value (inclusive).
     * @param max The maximum Integer value (inclusive).
     * @return A biased generator for Integer values within the specified range.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<Integer> biased(int min,
                                      int max) {
        if (max < min) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends Integer>>> gens = new ArrayList<>();

        if (max >= Short.MAX_VALUE && min <= Short.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Short.MAX_VALUE)));
        if (max >= Short.MIN_VALUE && min <= Short.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Short.MIN_VALUE)));
        if (max >= Byte.MAX_VALUE && min <= Byte.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Byte.MAX_VALUE)));
        if (max >= Byte.MIN_VALUE && min <= Byte.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Byte.MIN_VALUE)));
        if (max >= 0 && min <= 0)
            gens.add(Pair.of(1,
                             Gen.cons(0)));

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
     * Generates an Integer value within the specified range [{@code min}, {@code max}] (inclusive) using the provided Random generator.
     *
     * @param min The minimum Integer value (inclusive).
     * @param max The maximum Integer value (inclusive).
     * @return An Integer generator for the specified range.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<Integer> arbitrary(int min,
                                         int max) {
        if (max < min) throw new IllegalArgumentException("max < min");

        return seed -> () -> {
            int r = seed.nextInt();
            final int n = max - min + 1;
            final int m = n - 1;
            if ((n & m) == 0) {
                r = (r & m) + min;
            } else if (n > 0) {
                for (int u = r >>> 1;
                     u + m - (r = u % n) < 0;
                     u = seed.nextInt() >>> 1)
                    ;
                r += min;
            } else {
                while (r < min || r > max) {
                    r = seed.nextInt();
                }
            }
            return r;
        };
    }

    /**
     * Generates an Integer value starting from {@code min} (inclusive) up to {@link Integer#MAX_VALUE} (inclusive) using the provided Random generator.
     *
     * @param min The minimum Integer value (inclusive) from which values will be generated.
     * @return An Integer generator starting from the specified minimum value.
     * @throws IllegalArgumentException If {@code min} is greater than {@link Integer#MAX_VALUE}.
     */
    public static Gen<Integer> arbitrary(int min) {
        return arbitrary(min,
                         Integer.MAX_VALUE);
    }

    /**
     * Returns a biased generator for common Integer values. This generator is biased towards generating common Integer values.
     *
     * @return A biased generator for common Integer values.
     */
    public static Gen<Integer> biased() {
        List<Pair<Integer, Gen<? extends Integer>>> gens = new ArrayList<>();

        gens.add(Pair.of(1,
                         Gen.cons(Integer.MAX_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons(Integer.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Short.MAX_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Short.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Byte.MAX_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Byte.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons(0)));

        gens.add(Pair.of(gens.size(),
                         arbitrary));

        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Integer> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return requireNonNull(gen)::nextInt;
    }


}
