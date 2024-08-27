package fun.gen;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * A generator for creating sets of elements of type T.
 *
 * @param <T> The type of elements in the generated set.
 */
public final class SetGen<T> implements Gen<Set<T>> {
    private final Gen<T> gen;
    private final int size;
    private final int maxTries;

    private SetGen(final Gen<T> gen,
                   final int size,
                   final int maxTries) {
        if (size < 0) throw new IllegalArgumentException("size < 0");
        if (maxTries < size) throw new IllegalArgumentException("maxTries < size");
        this.gen = requireNonNull(gen);
        this.size = size;
        this.maxTries = maxTries;
    }

    /**
     * Creates a new SetGen instance with the specified generator and size.
     * The maximum number of tries will be set to size * 10.
     *
     * @param gen  The generator for individual elements.
     * @param size The desired size of the generated set.
     * @param <T>  The type of elements in the set.
     * @return A SetGen instance.
     */
    public static <T> SetGen<T> ofN(final Gen<T> gen,
                                    final int size) {
        return new SetGen<>(gen,
                            size,
                            size * 10);
    }

    /**
     * Sets the maximum number of tries for generating the set to improve the generation process.
     * This method creates a new instance of SetGen with the updated maximum tries.
     *
     * @param tries The maximum number of attempts to generate the set. Setting this value higher may increase the chances
     *              of successfully generating a set of the desired size, but it can also increase the time it takes to
     *              generate the set.
     * @return A new SetGen instance with the updated maximum tries.
     * @throws IllegalArgumentException If the specified number of tries is negative.
     */
    public SetGen<T> withMaxTries(final int tries) {
        return new SetGen<>(gen,
                            size,
                            tries);
    }


    @Override
    public Supplier<Set<T>> apply(final RandomGenerator seed) {
        requireNonNull(seed);
        if (size < 0) throw new IllegalArgumentException("size < 0");
        if (maxTries < 0) throw new IllegalArgumentException("maxTries negative");

        final Supplier<T> supplier = gen.apply(seed);
        return () ->
        {
            int tries = 0;
            Set<T> set = new HashSet<>();
            while (set.size() != size) {
                set.add(supplier.get());
                tries += 1;
                if (tries >= maxTries)
                    throw new RuntimeException(String.format("Couldn't generate set of %s different elements after %s tries",
                                                             size,
                                                             maxTries));
            }
            return set;
        };
    }
}
