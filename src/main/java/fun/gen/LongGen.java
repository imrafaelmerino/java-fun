package fun.gen;


import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of long numbers.
 */
public final class LongGen implements Gen<Long> {

    private static final Gen<Long> arbitrary = new LongGen();

    private LongGen() {
    }

    public static Gen<Long> arbitrary() {
        return arbitrary;
    }

    public static Gen<Long> biased(final long min,
                                   final long max) {
        if (max < min) throw new IllegalArgumentException("max < min");

        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<>();
        if (max > Integer.MAX_VALUE && min < Integer.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Integer.MAX_VALUE)));
        if (max > Integer.MIN_VALUE && min < Integer.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Integer.MIN_VALUE)));
        if (max > Short.MAX_VALUE && min < Short.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Short.MAX_VALUE)));
        if (max > Short.MIN_VALUE && min < Short.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Short.MIN_VALUE)));
        if (max > Byte.MAX_VALUE && min < Byte.MAX_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Byte.MAX_VALUE)));
        if (max > Byte.MIN_VALUE && min < Byte.MIN_VALUE)
            gens.add(new Pair<>(1,
                                Gen.cons((long) Byte.MIN_VALUE)));
        if (max > 0 && min < 0)
            gens.add(new Pair<>(1,
                                Gen.cons(0L)));

        gens.add(new Pair<>(1,
                            Gen.cons(min)));

        gens.add(new Pair<>(1,
                            Gen.cons(max)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary(min,
                                      max)));

        return Combinators.freqList(gens);
    }

    public static Gen<Long> arbitrary(final long min,
                                      final long max) {

        if (max < min) throw new IllegalArgumentException("max < min");

        return seed -> () -> {
            long r = seed.nextLong();
            // It's not case (1).
            final long n = max - min + 1;
            final long m = n - 1;
            if ((n & m) == 0L) {
                // It is case (2): length of range is a power of 2.
                r = (r & m) + min;
            } else if (n > 0L) {
                // It is case (3): need to reject over-represented candidates.
                /* This loop takes an unlovable form (but it works):
                   because the first candidate is already available,
                   we need a break-in-the-middle construction,
                   which is concisely but cryptically performed
                   within the while-condition of a body-less for loop. */
                for (long u = r >>> 1;            // ensure nonnegative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = seed.nextLong() >>> 1) // retry
                    ;
                r += min;
            } else {
                // It is case (4): length of range not representable as long.
                while (r < min || r > max)
                    r = seed.nextLong();
            }
            return r;
        };
    }


    public static Gen<Long> biased() {
        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<>();

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
    public Supplier<Long> apply(final Random gen) {
        requireNonNull(gen);
        return gen::nextLong;
    }

}
