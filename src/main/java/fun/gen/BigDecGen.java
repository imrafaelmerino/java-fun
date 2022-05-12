package fun.gen;


import fun.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Represents a generator of big decimals.
 */
public final class BigDecGen implements Gen<BigDecimal> {

    public static final Gen<BigDecimal> arbitrary = new BigDecGen();
    public static final Gen<BigDecimal> biased = biased();

    private BigDecGen() {
    }

    public static Gen<BigDecimal> biased(final BigDecimal min,
                                         final BigDecimal max) {
        Objects.requireNonNull(min);
        Objects.requireNonNull(max);
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("max <= min");
        var gens = new ArrayList<Pair<Integer, Gen<? extends BigDecimal>>>();
        if (max.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Long.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Long.MIN_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Integer.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Integer.MIN_VALUE))));


        if (max.compareTo(BigDecimal.valueOf(Short.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Short.MAX_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Short.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Short.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Short.MIN_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Short.MIN_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Byte.MAX_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Byte.MAX_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Byte.MAX_VALUE))));

        if (max.compareTo(BigDecimal.valueOf(Byte.MIN_VALUE)) > 0
                && min.compareTo(BigDecimal.valueOf(Byte.MIN_VALUE)) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.valueOf(Byte.MIN_VALUE))));

        if (max.compareTo(BigDecimal.ZERO) > 0
                && min.compareTo(BigDecimal.ZERO) <= 0)
            gens.add(new Pair<>(1,
                                Gen.cons(BigDecimal.ZERO)));

        gens.add(new Pair<>(1,
                            Gen.cons(min)));

        gens.add(new Pair<>(1,
                            Gen.cons(max)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);
    }

    public static Gen<BigDecimal> arbitrary(final BigDecimal min,
                                            final BigDecimal max) {

        Objects.requireNonNull(min);
        Objects.requireNonNull(max);
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("max <= min");
        return seed -> () -> {
            BigDecimal random = min.add(BigDecimal.valueOf(seed.nextDouble()).multiply(max.subtract(min)));
            return random.setScale(2,
                                   RoundingMode.HALF_UP);
        };
    }

    private static Gen<BigDecimal> biased() {
        var gens = new ArrayList<Pair<Integer, Gen<? extends BigDecimal>>>();
        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Long.MAX_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Long.MIN_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Integer.MAX_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Integer.MIN_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Short.MAX_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Short.MAX_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Short.MIN_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Byte.MAX_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(Byte.MIN_VALUE))));

        gens.add(new Pair<>(1,
                            Gen.cons(BigDecimal.valueOf(0))));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<BigDecimal> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return () -> BigDecimal.valueOf(gen.nextDouble());
    }

}
