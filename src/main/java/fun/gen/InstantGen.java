package fun.gen;


import fun.tuple.Pair;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of instants.
 */
public final class InstantGen implements Gen<Instant> {

    private static final Gen<Instant> arbitrary = new InstantGen();
    private static final long MAX_SECONDS = Instant.MAX.getEpochSecond();
    private static final long MIN_SECONDS = Instant.MIN.getEpochSecond();

    private InstantGen() {
    }

    public static Gen<Instant> arbitrary() {
        return arbitrary;
    }

    public static Gen<Instant> biased() {
        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<Pair<Integer, Gen<? extends Long>>>();

        gens.add(Pair.of(1,
                         Gen.cons(MAX_SECONDS)));
        gens.add(Pair.of(1,
                         Gen.cons(MIN_SECONDS)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Integer.MAX_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons((long) Integer.MIN_VALUE)));
        gens.add(Pair.of(1,
                         Gen.cons(0L)));
        gens.add(Pair.of(gens.size(),
                         LongGen.arbitrary(MIN_SECONDS,
                                           MAX_SECONDS)));

        return Combinators.freqList(gens).map(Instant::ofEpochSecond);

    }

    public static Gen<Instant> biased(final long min,
                                      final long max) {
        if (max < min)
            throw new IllegalArgumentException(max + " is greater than " + min);
        if (max > MAX_SECONDS)
            throw new IllegalArgumentException(max + " is greater than MAX_SECONDS " + MAX_SECONDS);

        if (min < MIN_SECONDS)
            throw new IllegalArgumentException(max + " is lower than MIN_SECONDS " + MIN_SECONDS);

        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<Pair<Integer, Gen<? extends Long>>>();

        if (min <= Integer.MAX_VALUE && max >= Integer.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Integer.MAX_VALUE)));
        if (min <= Integer.MIN_VALUE && max >= Integer.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Integer.MIN_VALUE)));
        if (min < 0L && max > 0L)
            gens.add(Pair.of(1,
                             Gen.cons(0L)));
        gens.add(Pair.of(1,
                         Gen.cons(min)));
        if (max != min)

            gens.add(Pair.of(1,
                             Gen.cons(max)));
        gens.add(Pair.of(gens.size(),
                         LongGen.arbitrary(min,
                                           max)));

        return Combinators.freqList(gens).map(Instant::ofEpochSecond);

    }

    /**
     * generates an instant in UTC formatted with the ISO instant formatter (such as '2011-12-03T10:15:30Z'), between
     * an interval given by two date-time with a time-zone.
     *
     * @param min the origin of the interval (inclusive)
     * @param max the bound of the interval (inclusive)
     * @return an instant generator
     */

    public static Gen<Instant> arbitrary(final ZonedDateTime min,
                                         final ZonedDateTime max) {
        if (requireNonNull(max).isBefore(requireNonNull(min)))
            throw new IllegalArgumentException(min.format(ISO_INSTANT) + " is greater than " + max.format(ISO_INSTANT));

        return arbitrary(min.toEpochSecond(),
                         max.toEpochSecond());

    }


    /**
     * generates an instant in UTC formatted with the ISO instant formatter (such as '2011-12-03T10:15:30Z'), between
     * an interval given by two instants converted to the number of seconds from the epoch of 1970-01-01T00:00:00Z.
     *
     * @param min the origin of the interval (inclusive)
     * @param max the bound of the interval (inclusive)
     * @return an instant generator
     */
    public static Gen<Instant> arbitrary(final long min,
                                         final long max) {
        if (max < min)
            throw new IllegalArgumentException(max + " is greater than " + min);
        if (max > MAX_SECONDS)
            throw new IllegalArgumentException(max + " is greater than MAX_SECONDS " + MAX_SECONDS);

        if (min < MIN_SECONDS)
            throw new IllegalArgumentException(max + " is lower than MIN_SECONDS " + MIN_SECONDS);

        return LongGen.arbitrary(min,
                                 max).map(Instant::ofEpochSecond);
    }

    @Override
    public Supplier<Instant> apply(final Random gen) {
        requireNonNull(gen);

        return LongGen.arbitrary(MIN_SECONDS,
                                 MAX_SECONDS + 1)
                      .map(Instant::ofEpochSecond).apply(gen);
    }

}


