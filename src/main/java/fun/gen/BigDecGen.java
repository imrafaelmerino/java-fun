package fun.gen;


import static java.util.Objects.requireNonNull;

import fun.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Generators for {@link BigDecimal} values.
 * <p>
 * Includes arbitrary generators and biased variants that emphasize common boundaries.
 * <p>
 * Precision note:
 * some generators in this class are backed by {@code double} sampling and therefore do not
 * enumerate all possible decimal values in a range.
 */
public final class BigDecGen implements Gen<BigDecimal> {

    private static final Gen<BigDecimal> arbitrary = new BigDecGen();

    private BigDecGen() {
    }

    /**
     * Returns an arbitrary generator in the range [0, 1).
     * <p>
     * Values are produced from {@link RandomGenerator#nextDouble()} and converted with
     * {@link BigDecimal#valueOf(double)}.
     *
     * @return arbitrary decimal generator
     */
    public static Gen<BigDecimal> arbitrary() {
        return arbitrary;
    }

    /**
     * Creates a biased {@code BigDecimal} generator that produces values within the specified range [{@code min}, {@code max}].
     * The generated values will be biased towards specific values based on the specified range.
     * This method aims to create generators with specific value distributions, emphasizing the specified boundaries and zero.
     * <p>
     * The generated values are biased toward the following values within the specified range:
     * - The minimum value {@code min} is always included as a possible generated value.
     * - The maximum value {@code max} is included as a possible generated value unless it is equal to {@code min}.
     * - Zero is included as a possible value within the specified range when it lies between {@code min} and {@code max}.
     *
     * @param min The minimum value (inclusive) of the generated {@code BigDecimal}.
     * @param max The maximum value (inclusive) of the generated {@code BigDecimal}.
     * @return A biased generator for {@code BigDecimal} values within the specified range, emphasizing specific values.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<BigDecimal> biased(final BigDecimal min,
                                         final BigDecimal max) {
        requireNonNull(min);
        requireNonNull(max);
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends BigDecimal>>> gens = new ArrayList<>();
        if (max.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) < 0
        )
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Long.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Long.MIN_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Integer.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Integer.MIN_VALUE))));


        if (max.compareTo(BigDecimal.valueOf(Short.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Short.MAX_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Short.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Short.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Short.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Short.MIN_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Byte.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Byte.MAX_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Byte.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Byte.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Byte.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.valueOf(Byte.MIN_VALUE))));

        if (max.compareTo(BigDecimal.ZERO) > 0
                && min.compareTo(BigDecimal.ZERO) < 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigDecimal.ZERO)));

        gens.add(Pair.of(1,
                         Gen.cons(min)));

        if (max.compareTo(min) != 0)
            gens.add(Pair.of(1,
                             Gen.cons(max)));


        gens.add(Pair.of(gens.size(),
                         arbitrary(min,
                                   max)));

        return Combinators.freqList(gens);
    }

    /**
     * Creates an arbitrary {@code BigDecimal} generator that produces values within the
     * specified range [{@code min}, {@code max}].
     * <p>
     * Generation algorithm:
     * - sample {@code u} from {@code nextDouble()} in [0, 1)
     * - compute {@code min + u * (max - min)}
     * - round to scale {@code 2} using {@link RoundingMode#HALF_UP}
     * - clamp to [{@code min}, {@code max}] after rounding
     * <p>
     * This is a cent-scale generator by design, not a full-precision decimal generator.
     *
     * @param min The minimum value (inclusive) of the generated {@code BigDecimal}.
     * @param max The maximum value (inclusive) of the generated {@code BigDecimal}.
     * @return An arbitrary generator for {@code BigDecimal} values within the specified range.
     * @throws IllegalArgumentException If {@code max} is less than {@code min}.
     */
    public static Gen<BigDecimal> arbitrary(final BigDecimal min,
                                            final BigDecimal max) {

        requireNonNull(min);
        requireNonNull(max);
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException("max < min");
        return seed -> () -> {
            BigDecimal random = min.add(BigDecimal.valueOf(seed.nextDouble())
                                                  .multiply(max.subtract(min)));
            BigDecimal rounded = random.setScale(2,
                                                 RoundingMode.HALF_UP);
            if (rounded.compareTo(min) < 0) return min;
            if (rounded.compareTo(max) > 0) return max;
            return rounded;
        };
    }

    /**
     * Creates a biased {@code BigDecimal} generator that produces values within a predefined range of common numeric types.
     * The generated values will be biased towards common integer and zero values within the predefined range.
     * This method is suitable for generating {@code BigDecimal} values with typical numeric distributions.
     * <p>
     * The generated values are biased toward the following values within the predefined range:
     * - The maximum possible {@code BigDecimal} value, equivalent to {@link Long#MAX_VALUE}.
     * - The minimum possible {@code BigDecimal} value, equivalent to {@link Long#MIN_VALUE}.
     * - The maximum possible {@code BigDecimal} value representable as an {@link Integer}.
     * - The minimum possible {@code BigDecimal} value representable as an {@link Integer}.
     * - The maximum possible {@code BigDecimal} value representable as a {@link Short}.
     * - The minimum possible {@code BigDecimal} value representable as a {@link Short}.
     * - The maximum possible {@code BigDecimal} value representable as a {@link Byte}.
     * - The minimum possible {@code BigDecimal} value representable as a {@link Byte}.
     * - Zero (0) is included as a possible value.
     * - Arbitrary values distributed uniformly within the predefined range.
     *
     * @return A biased generator for {@code BigDecimal} values within a predefined range, emphasizing common numeric values.
     */
    public static Gen<BigDecimal> biased() {
        List<Pair<Integer, Gen<? extends BigDecimal>>> gens = new ArrayList<>();
        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Long.MAX_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Long.MIN_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Integer.MAX_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Integer.MIN_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Short.MAX_VALUE))));


        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Short.MIN_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Byte.MAX_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(Byte.MIN_VALUE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigDecimal.valueOf(0))));

        gens.add(Pair.of(gens.size(),
                         arbitrary));

        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<BigDecimal> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        return () -> BigDecimal.valueOf(gen.nextDouble());
    }

}
