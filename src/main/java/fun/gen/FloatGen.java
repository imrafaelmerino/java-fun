package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Represents a generator of floats.
 */
public final class FloatGen implements Gen<Float> {
    public static final Gen<Float> arbitrary = new FloatGen();
    public static final Gen<Float> biased = biased();


    @Override
    public Supplier<Float> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return gen::nextFloat;
    }

    public static Gen<Float> biased(final float min,
                                    final float max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");
        var gens = new ArrayList<Pair<Integer, Gen<? extends Float>>>();
        if (min == Float.MIN_VALUE)
            gens.add(new Pair<>(1,
                    Gen.cons(Float.MIN_VALUE)));
        if (min == Integer.MIN_VALUE)
            gens.add(new Pair<>(1,
                    Gen.cons((float) Integer.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min <= Byte.MAX_VALUE)
            gens.add(new Pair<>(1,
                    Gen.cons((float) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min <= Byte.MIN_VALUE)
            gens.add(new Pair<>(1,
                    Gen.cons((float) Byte.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min <= Short.MAX_VALUE)
            gens.add(new Pair<>(1,
                    Gen.cons((float) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min <= Short.MIN_VALUE)
            gens.add(new Pair<>(1,
                    Gen.cons((float) Short.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(new Pair<>(1,
                    Gen.cons(0.0f)));

        gens.add(new Pair<>(1,
                Gen.cons(min)));

        gens.add(new Pair<>(1,
                Gen.cons(max - 1)));

        gens.add(new Pair<>(gens.size(), arbitrary));

        return Combinators.freqList(gens);

    }

    public static Gen<Float> arbitrary(final float min,
                                       final float max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return seed -> () -> seed.nextFloat(min,
                max);
    }

    private static Gen<Float> biased() {
        var gens = new ArrayList<Pair<Integer, Gen<? extends Float>>>();
        gens.add(new Pair<>(1,
                Gen.cons((float) Integer.MIN_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons((float) Integer.MAX_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons(Float.MIN_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons(Float.MAX_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons((float) Byte.MAX_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons((float) Byte.MIN_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons((float) Short.MAX_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons((float) Short.MAX_VALUE)));
        gens.add(new Pair<>(1,
                Gen.cons(0.0f)));

        gens.add(new Pair<>(gens.size(),
                arbitrary));

        return Combinators.freqList(gens);

    }

    private FloatGen() {
    }


}

