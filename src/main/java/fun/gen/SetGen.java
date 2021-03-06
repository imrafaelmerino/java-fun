package fun.gen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * represent a generator of sets.
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


    public static <T> SetGen<T> of(final Gen<T> gen,
                                   final int size) {
        return new SetGen<>(gen,
                            size,
                            size * 10);
    }

    public SetGen<T> setMaxTries(final int tries) {
        return new SetGen<>(gen,
                            size,
                            tries);
    }


    @Override
    public Supplier<Set<T>> apply(final Random seed) {
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
