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
    public static final Gen<Double> arbitrary = new DoubleGen();
    public static final Gen<Double> biased = biased();


    private DoubleGen() {
    }

    public static Gen<Double> biased(final double min,
                                     final double max) {
        if (max < min) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends Double>>> gens = new ArrayList<Pair<Integer, Gen<? extends Double>>>();

        if (max > Integer.MAX_VALUE && min < Integer.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((double) Integer.MAX_VALUE)));
        if (max > Integer.MIN_VALUE && min < Integer.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((double) Integer.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min < Short.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((double) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min < Short.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((double) Short.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min < Byte.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((double) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min < Byte.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((double) Byte.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(new Pair<>(1,
                                Gen.cons(0.0)));

        gens.add(new Pair<>(1,
                            Gen.cons(min)));

        gens.add(new Pair<>(1,
                            Gen.cons(max)));

        gens.add(new Pair<>(gens.size(),
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


    private static Gen<Double> biased() {
        List<Pair<Integer, Gen<? extends Double>>> gens = new ArrayList<Pair<Integer, Gen<? extends Double>>>();
        gens.add(new Pair<>(1,
                            Gen.cons(Double.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons(Double.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Long.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Long.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Integer.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Integer.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Short.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Short.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Byte.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((double) Byte.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons(0.0)));
        gens.add(new Pair<>(gens.size(),
                            arbitrary));
        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Double> apply(final Random gen) {
        requireNonNull(gen);
        return gen::nextDouble;
    }

}
