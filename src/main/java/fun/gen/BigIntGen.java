package fun.gen;

import fun.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Generators for {@link BigInteger} values.
 * <p>
 * The default {@link #arbitrary()} generator produces non-negative values up to 64 bits.
 * Range-based methods generate values uniformly within inclusive bounds.
 */
public final class BigIntGen implements Gen<BigInteger> {

    private static final Gen<BigInteger> arbitrary = new BigIntGen(64);
    private final int nBits;

    private BigIntGen(final int nBits) {
        if (nBits < 0) throw new IllegalArgumentException("nBits < 0");
        this.nBits = nBits;
    }

    /**
     * Returns the default arbitrary generator.
     * <p>
     * Values are non-negative and fit in at most 64 bits.
     *
     * @return default arbitrary big integer generator
     */
    public static Gen<BigInteger> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a biased generator that emphasizes selected boundary-like values and zero.
     *
     * @return biased big integer generator
     */
    public static Gen<BigInteger> biased() {
        List<Pair<Integer, Gen<? extends BigInteger>>> gens = new ArrayList<>();

        gens.add(Pair.of(1,
                         Gen.cons(BigInteger.valueOf(Long.MAX_VALUE)
                                            .add(BigInteger.ONE))));
        gens.add(Pair.of(1,
                         Gen.cons(BigInteger.valueOf(Long.MIN_VALUE)
                                            .subtract(BigInteger.ONE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigInteger.valueOf(Integer.MAX_VALUE)
                                            .add(BigInteger.ONE))));
        gens.add(Pair.of(1,
                         Gen.cons(BigInteger.valueOf(Integer.MIN_VALUE)
                                            .subtract(BigInteger.ONE))));

        gens.add(Pair.of(1,
                         Gen.cons(BigInteger.ZERO)));

        gens.add(Pair.of(gens.size(),
                         arbitrary));

        return Combinators.freqList(gens);
    }

    /**
     * Returns a uniform generator over the inclusive range [{@code min}, {@code max}].
     *
     * @param min lower inclusive bound
     * @param max upper inclusive bound
     * @return arbitrary generator constrained to the provided range
     * @throws NullPointerException if {@code min} or {@code max} is {@code null}
     * @throws IllegalArgumentException if {@code min > max}
     */
    public static Gen<BigInteger> arbitrary(final BigInteger min,
                                            final BigInteger max) {
        if (requireNonNull(min).compareTo(requireNonNull(max)) > 0) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }

        return r -> {
            BigInteger range = max.subtract(min).add(BigInteger.ONE);
            int bitLength = range.bitLength();
            Random random = new Random(r.nextLong());
            return () -> {
                BigInteger randomBigInteger;
                do {
                    randomBigInteger = new BigInteger(bitLength,
                                                      random);
                } while (randomBigInteger.compareTo(range) >= 0);

                return randomBigInteger.add(min);
            };
        };


    }

    /**
     * Returns a biased generator over the inclusive range [{@code min}, {@code max}].
     *
     * @param min lower inclusive bound
     * @param max upper inclusive bound
     * @return biased range generator
     * @throws NullPointerException if {@code min} or {@code max} is {@code null}
     * @throws IllegalArgumentException if {@code min > max}
     */
    public static Gen<BigInteger> biased(final BigInteger min,
                                         final BigInteger max) {

        if (requireNonNull(min).compareTo(requireNonNull(max)) > 0)
            throw new IllegalArgumentException("max < min");

        List<Pair<Integer, Gen<? extends BigInteger>>> gens = new ArrayList<>();

        addGenIfBetween(BigInteger.valueOf(Long.MAX_VALUE)
                                  .add(BigInteger.ONE),
                        gens,
                        min,
                        max);

        addGenIfBetween(BigInteger.valueOf(Long.MIN_VALUE)
                                  .subtract(BigInteger.ONE),
                        gens,
                        min,
                        max);

        addGenIfBetween(BigInteger.valueOf(Integer.MAX_VALUE)
                                  .add(BigInteger.ONE),
                        gens,
                        min,
                        max);

        addGenIfBetween(BigInteger.valueOf(Integer.MIN_VALUE)
                                  .subtract(BigInteger.ONE),
                        gens,
                        min,
                        max);

        addGenIfBetween(BigInteger.ZERO,
                        gens,
                        min,
                        max);

        gens.add(Pair.of(1,
                         Gen.cons(min)));

        if (min.compareTo(max) != 0) {
            gens.add(Pair.of(1,
                             Gen.cons(max)));
        }

        gens.add(Pair.of(gens.size(),
                         arbitrary(min,
                                   max)));

        return Combinators.freqList(gens);
    }

    private static void addGenIfBetween(BigInteger value,
                                        List<Pair<Integer, Gen<? extends BigInteger>>> gens,
                                        BigInteger min,
                                        BigInteger max) {
        if (max.compareTo(value) >= 0
                && min.compareTo(value) <= 0) {
            gens.add(Pair.of(1,
                             Gen.cons(value)));
        }
    }


    @Override
    @SuppressWarnings("java:S2142")
    public Supplier<BigInteger> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        @SuppressWarnings("serial") Random rnd = new Random() {

            @Override
            protected int next(int bits) {
                if (bits <= 0) return 0;
                return gen.nextInt() >>> (Integer.SIZE - bits);
            }
        };

        return () -> new BigInteger(nBits,
                                    rnd);
    }


}
