package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Generators for {@link Double} values.
 * <p>
 * Range-based methods only accept finite bounds. Biased variants emphasize
 * boundary/common values and mix them with arbitrary values from the same interval.
 */
public final class DoubleGen implements Gen<Double> {
    private static final Gen<Double> arbitrary = new DoubleGen();

    private DoubleGen() {
    }

    /**
     * Returns a generator for arbitrary double values. This generator produces random double values within the entire range of double precision.
     *
     * @return A generator for arbitrary double values.
     */
    public static Gen<Double> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a biased generator for finite double values within the range [{@code min}, {@code max}].
     * This generator is biased toward common boundary and primitive-like values that fall inside the interval.
     *
     * @param min The minimum double value (inclusive).
     * @param max The maximum double value (inclusive).
     * @return A biased generator for double values within the specified range.
     * @throws IllegalArgumentException If either bound is not finite, or if {@code max} is less than {@code min}.
     */
    public static Gen<Double> biased(final double min,
                                     final double max) {
        if (!Double.isFinite(min) || !Double.isFinite(max))
            throw new IllegalArgumentException("min and max must be finite");
        if (max < min) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends Double>>> gens = new ArrayList<>();
        if (max >= Integer.MAX_VALUE && min <= Integer.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.constant((double) Integer.MAX_VALUE)));
        if (max >= Integer.MIN_VALUE && min <= Integer.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.constant((double) Integer.MIN_VALUE)));
        if (max >= Short.MAX_VALUE && min <= Short.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.constant((double) Short.MAX_VALUE)));
        if (max >= Short.MIN_VALUE && min <= Short.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.constant((double) Short.MIN_VALUE)));
        if (max >= Byte.MAX_VALUE && min <= Byte.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.constant((double) Byte.MAX_VALUE)));
        if (max >= Byte.MIN_VALUE && min <= Byte.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.constant((double) Byte.MIN_VALUE)));
        if (max >= 0 && min <= 0)
            gens.add(Pair.of(1,
                             Gen.constant(0.0)));

        gens.add(Pair.of(1,
                         Gen.constant(min)));

        if (max != min)
            gens.add(Pair.of(1,
                             Gen.constant(max)));

        gens.add(Pair.of(gens.size(),
                         arbitrary(min,
                                   max)));

        return Combinators.freqList(gens);

    }

    /**
     * Returns a generator for finite double values within the range [{@code min}, {@code max}].
     * Values are generated from a uniform source over [0,1), then scaled to the target interval.
     *
     * @param min The minimum double value (inclusive).
     * @param max The maximum double value (inclusive).
     * @return A generator for arbitrary double values within the specified range.
     * @throws IllegalArgumentException If either bound is not finite, or if {@code max} is less than {@code min}.
     */
    public static Gen<Double> arbitrary(final double min,
                                        final double max) {
        if (!Double.isFinite(min) || !Double.isFinite(max))
            throw new IllegalArgumentException("min and max must be finite");
        if (max < min) throw new IllegalArgumentException("max < min");
        if (max == min) return Gen.constant(min);

        return seed -> () -> {
            double range = max - min;
            if (Double.isFinite(range)) {
                return nextInFiniteRange(seed,
                                         min,
                                         max);
            }
            // When max - min overflows (for very wide intervals), split around a safe midpoint.
            // This keeps each half finite and avoids collapsing samples near max.
            double mid = min / 2.0 + max / 2.0;
            return seed.nextBoolean()
                    ? nextInFiniteRange(seed,
                                        min,
                                        mid)
                    : nextInFiniteRange(seed,
                                        mid,
                                        max);
        };
    }

    private static double nextInFiniteRange(RandomGenerator seed,
                                            double min,
                                            double max) {
        double r = seed.nextDouble();
        r = r * (max - min) + min;
        if (r > max) {
            return Math.nextDown(max);
        }
        return r;
    }

    /**
     * Returns a biased generator for double values. This generator is biased towards generating common double values.
     *
     * @return A biased generator for double values.
     */
    public static Gen<Double> biased() {
        List<Pair<Integer, Gen<? extends Double>>> gens = new ArrayList<>();
        gens.add(Pair.of(1,
                         Gen.constant(Double.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant(Double.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Long.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Long.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Integer.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Integer.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Short.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Short.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Byte.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant((double) Byte.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.constant(0.0)));
        gens.add(Pair.of(gens.size(),
                         arbitrary));
        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Double> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        return gen::nextDouble;
    }

}
