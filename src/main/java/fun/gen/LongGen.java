package fun.gen;


import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Represents a generator of long numbers.
 */
public final class LongGen implements Gen<Long> {

    public static final Gen<Long> arbitrary = new LongGen();
    public static final Gen<Long> biased = biased();

    private LongGen() {
    }

    public static Gen<Long> biased(final long min,
                                   final long max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");

        var gens = new ArrayList<Pair<Integer, Gen<? extends Long>>>();

        if (min == Long.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons(Long.MIN_VALUE)));

        if (max > Integer.MAX_VALUE && min <= Integer.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Integer.MAX_VALUE)));
        if (max > Integer.MIN_VALUE && min <= Integer.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Integer.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min <= Short.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min <= Short.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Short.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min <= Byte.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min <= Byte.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Byte.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(new Pair<>(1,
                                Gen.cons(0L)));

        gens.add(new Pair<>(1,
                            Gen.cons(min)));

        gens.add(new Pair<>(1,
                            Gen.cons(max - 1)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);
    }

    public static Gen<Long> arbitrary(final long min,
                                      final long max) {

        if (max <= min) throw new IllegalArgumentException("max <= min");

        return seed -> () -> seed.nextLong(min,
                                           max);
    }


    private static Gen<Long> biased() {
        var gens = new ArrayList<Pair<Integer, Gen<? extends Long>>>();

        gens.add(new Pair<>(1,
                            Gen.cons(Long.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons(Long.MIN_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons((long) Integer.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((long) Integer.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((long) Short.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((long) Short.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((long) Byte.MAX_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons((long) Byte.MIN_VALUE)));
        gens.add(new Pair<>(1,
                            Gen.cons(0L)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Long> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return gen::nextLong;
    }

}
