package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of floats.
 */
public final class FloatGen implements Gen<Float> {
    private static final Gen<Float> arbitrary = new FloatGen();
    public static Gen<Float> arbitrary() {
        return arbitrary;
    }

    private FloatGen() {
    }

    public static Gen<Float> biased(final float min,
                                    final float max) {
        if (max < min) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends Float>>> gens = new ArrayList<>();

        if (max > Integer.MAX_VALUE && min < Integer.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((float) Integer.MAX_VALUE)));

        if (max > Integer.MIN_VALUE && min < Integer.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((float) Integer.MIN_VALUE)));

        if (max > Byte.MAX_VALUE && min < Byte.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((float) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min < Byte.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((float) Byte.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min < Short.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((float) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min < Short.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((float) Short.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(new Pair<>(1,
                                Gen.cons(0.0f)));

        gens.add(new Pair<>(1,
                            Gen.cons(min)));

        gens.add(new Pair<>(1,
                            Gen.cons(max)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary(min,
                                      max)));

        return Combinators.freqList(gens);

    }

    public static Gen<Float> arbitrary(final float min,
                                       final float max) {
        if (max < min) throw new IllegalArgumentException("max < min");
        return seed -> () -> {
            float r = seed.nextFloat();
            r = r * (max - min) + min;
            if (r > max) // may need to correct a rounding problem
                r = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
            return r;
        };
    }

    public static Gen<Float> biased() {
        List<Pair<Integer, Gen<? extends Float>>> gens = new ArrayList<>();
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
                            Gen.cons((float) Short.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons(0.0f)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);

    }

    @Override
    public Supplier<Float> apply(final Random gen) {
        requireNonNull(gen);
        return gen::nextFloat;
    }


}

