package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of doubles.
 */
public final class DoubleGen implements Gen<Double> {
    private static final Gen<Double> arbitrary = new DoubleGen();

    private DoubleGen() {
    }

    public static Gen<Double> arbitrary() {
        return arbitrary;
    }

    public static Gen<Double> biased(final double min,
                                     final double max) {
        if (max < min) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends Double>>> gens = new ArrayList<>();
        if (max > Integer.MAX_VALUE && min < Integer.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((double) Integer.MAX_VALUE)));
        if (max > Integer.MIN_VALUE && min < Integer.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((double) Integer.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min < Short.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((double) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min < Short.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((double) Short.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min < Byte.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((double) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min < Byte.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((double) Byte.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(Pair.of(1,
                             Gen.cons(0.0)));

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

    public static Gen<Double> arbitrary(final double min,
                                        final double max) {
        if (max < min) throw new IllegalArgumentException("max < min");

        return seed -> () -> {
            double r = seed.nextDouble();
            r = r * (max - min) + min;
            if (r > max)  // may need to correct a rounding problem
                r = Double.longBitsToDouble(Double.doubleToLongBits(max) - 1);
            return r;
        };
    }


    public static Gen<Double> biased() {
        List<Pair<Integer, Gen<? extends Double>>> gens = new ArrayList<>();
        gens.add(Pair.of(1,
                         Gen.cons(Double.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons(Double.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Long.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Long.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Integer.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Integer.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Short.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Short.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Byte.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((double) Byte.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons(0.0)));
        gens.add(Pair.of(gens.size(),
                         arbitrary));
        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Double> apply(final Random gen) {
        requireNonNull(gen);
        return gen::nextDouble;
    }

}
