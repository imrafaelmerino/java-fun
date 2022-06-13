package fun.gen;


import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of integer numbers.
 */
public final class IntGen implements Gen<Integer> {

    private static final Gen<Integer> arbitrary = new IntGen();

    private IntGen() {
    }

    public static Gen<Integer> arbitrary() {
        return arbitrary;
    }

    public static Gen<Integer> biased(int min,
                                      int max) {
        if (max < min) throw new IllegalArgumentException("max < min");
        List<Pair<Integer, Gen<? extends Integer>>> gens = new ArrayList<>();

        if (max > Short.MAX_VALUE && min < Short.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min < Short.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Short.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min < Byte.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min < Byte.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((int) Byte.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(Pair.of(1,
                             Gen.cons(0)));

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

    public static Gen<Integer> arbitrary(int min,
                                         int max) {
        if (max < min) throw new IllegalArgumentException("max < min");

        return seed -> () -> {
            int r = seed.nextInt();
            // It's not case (1).
            final int n = max - min + 1;
            final int m = n - 1;
            if ((n & m) == 0) {
                // It is case (2): length of range is a power of 2.
                r = (r & m) + min;
            } else if (n > 0) {
                // It is case (3): need to reject over-represented candidates.
                for (int u = r >>> 1;
                     u + m - (r = u % n) < 0;
                     u = seed.nextInt() >>> 1)
                    ;
                r += min;
            } else {
                // It is case (4): length of range not representable as long.
                while (r < min || r > max) {
                    r = seed.nextInt();
                }
            }
            return r;
        };
    }


    public static Gen<Integer> biased() {
        List<Pair<Integer, Gen<? extends Integer>>> gens = new ArrayList<>();

        gens.add(Pair.of(1,
                         Gen.cons(Integer.MAX_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons(Integer.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Short.MAX_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Short.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Byte.MAX_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons((int) Byte.MIN_VALUE)));

        gens.add(Pair.of(1,
                         Gen.cons(0)));

        gens.add(Pair.of(gens.size(),
                         arbitrary));

        return Combinators.freqList(gens);
    }

    @Override
    public Supplier<Integer> apply(final Random gen) {
        Objects.requireNonNull(gen);
        return requireNonNull(gen)::nextInt;
    }


}
