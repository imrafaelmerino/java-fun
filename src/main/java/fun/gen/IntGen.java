package fun.gen;


import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of integer numbers.
 */
public final class IntGen implements Gen<Integer> {

    public static final Gen<Integer> arbitrary = new IntGen();
    public static final Gen<Integer> biased = biased();


    private IntGen() {
    }

    public static Gen<Integer> biased(int min,
                                      int max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");
        var gens = new ArrayList<Pair<Integer, Gen<? extends Integer>>>();
        if (min == Integer.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons(Integer.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min <= Short.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((int) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min <= Short.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((int) Short.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min <= Byte.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((int) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min <= Byte.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((int) Byte.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(new Pair<>(1,
                                Gen.cons(0)));

        gens.add(new Pair<>(1,
                            Gen.cons(min)));

        gens.add(new Pair<>(1,
                            Gen.cons(max - 1)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);
    }

    public static Gen<Integer> arbitrary(int min,
                                         int max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");

        return seed -> () -> seed.nextInt(min,
                                          max);
    }


    private static Gen<Integer> biased() {
        var gens = new ArrayList<Pair<Integer, Gen<? extends Integer>>>();

        gens.add(new Pair<>(1,
                            Gen.cons(Integer.MAX_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons(Integer.MIN_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons((int) Short.MAX_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons((int) Short.MIN_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons((int) Byte.MAX_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons((int) Byte.MIN_VALUE)));

        gens.add(new Pair<>(1,
                            Gen.cons(0)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary));

        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Integer> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return requireNonNull(gen)::nextInt;
    }

}
