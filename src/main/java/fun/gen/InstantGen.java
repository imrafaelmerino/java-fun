package fun.gen;


import fun.tuple.Pair;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of Instant values. This class provides methods to generate arbitrary and biased Instant values.
 * Use this class to create random Instant generators for various purposes.
 */
public final class InstantGen implements Gen<Instant> {

    private static final Gen<Instant> arbitrary = new InstantGen();
    private static final long MAX_SECONDS = Instant.MAX.getEpochSecond();
    private static final long MIN_SECONDS = Instant.MIN.getEpochSecond();

    private InstantGen() {
    }

    /**
     * Returns a generator for arbitrary Instant values. This generator produces random Instant values within the entire range of possible Instant values.
     *
     * @return A generator for arbitrary Instant values.
     */
    public static Gen<Instant> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a biased generator for Instant values. This generator is biased towards generating common Instant values.
     *
     * @return A biased generator for Instant values.
     */
    public static Gen<Instant> biased() {
        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<>();

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

    /**
     * Returns a biased generator for Instant values within the specified range [{@code min}, {@code max}] (inclusive).
     * This generator is biased towards generating common Instant values within the specified range.
     *
     * @param min The minimum Instant value (inclusive).
     * @param max The maximum Instant value (inclusive).
     * @return A biased generator for Instant values within the specified range.
     * @throws IllegalArgumentException If {@code max} is less than {@code min} or if the provided range exceeds the maximum and minimum Instant values.
     */
    public static Gen<Instant> biased(final long min,
                                      final long max) {
        if (max < min)
            throw new IllegalArgumentException(max + " is greater than " + min);
        if (max > MAX_SECONDS)
            throw new IllegalArgumentException(max + " is greater than MAX_SECONDS " + MAX_SECONDS);

        if (min < MIN_SECONDS)
            throw new IllegalArgumentException(max + " is lower than MIN_SECONDS " + MIN_SECONDS);

        List<Pair<Integer, Gen<? extends Long>>> gens = new ArrayList<>();

        if (min <= Integer.MAX_VALUE && max >= Integer.MAX_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Integer.MAX_VALUE)));
        if (min <= Integer.MIN_VALUE && max >= Integer.MIN_VALUE)
            gens.add(Pair.of(1,
                             Gen.cons((long) Integer.MIN_VALUE)));
        if (min <= 0L && max >= 0L)
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
     * Generates an Instant in UTC format formatted with the ISO instant formatter (e.g., '2011-12-03T10:15:30Z') between
     * an interval given by two date-times with a time-zone.
     *
     * @param min The origin of the interval (inclusive).
     * @param max The bound of the interval (inclusive).
     * @return An Instant generator.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
     */

    public static Gen<Instant> arbitrary(final ZonedDateTime min,
                                         final ZonedDateTime max) {
        if (requireNonNull(max).isBefore(requireNonNull(min)))
            throw new IllegalArgumentException(min.format(ISO_INSTANT) + " is greater than " + max.format(ISO_INSTANT));

        return arbitrary(min.toEpochSecond(),
                         max.toEpochSecond());

    }

    /**
     * Generates an Instant in UTC format formatted with the ISO instant formatter (e.g., '2011-12-03T10:15:30Z') between
     * an interval given by two date-times with a time-zone.
     *
     * @param min The origin of the interval (inclusive).
     * @param max The bound of the interval (inclusive).
     * @return An Instant generator.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max}.
     */

    public static Gen<Instant> biased(final ZonedDateTime min,
                                      final ZonedDateTime max) {
        if (requireNonNull(max).isBefore(requireNonNull(min)))
            throw new IllegalArgumentException(min.format(ISO_INSTANT) + " is greater than " + max.format(ISO_INSTANT));

        return biased(min.toEpochSecond(),
                      max.toEpochSecond());

    }


    /**
     * Generates an Instant in UTC format formatted with the ISO instant formatter (e.g., '2011-12-03T10:15:30Z') between
     * an interval given by two Instants converted to the number of seconds from the epoch of 1970-01-01T00:00:00Z.
     *
     * @param min The origin of the interval (inclusive).
     * @param max The bound of the interval (inclusive).
     * @return An Instant generator.
     * @throws IllegalArgumentException If {@code min} is greater than {@code max} or if the provided range exceeds the maximum and minimum Instant values.
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
    public Supplier<Instant> apply(final RandomGenerator gen) {
        requireNonNull(gen);

        return LongGen.arbitrary(MIN_SECONDS,
                                 MAX_SECONDS)
                      .map(Instant::ofEpochSecond).apply(gen);
    }

}


