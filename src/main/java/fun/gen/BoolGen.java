package fun.gen;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of {@code Boolean} values.
 * This class implements the {@link Gen} interface to generate {@code Boolean} values, primarily for true or false values.
 *
 * @see Gen
 */
public final class BoolGen implements Gen<Boolean> {
    private static final BoolGen arbitrary = new BoolGen();

    private BoolGen() {
    }

    /**
     * Returns an arbitrary Boolean generator.
     *
     * @return An arbitrary generator for {@code Boolean} values.
     */
    public static BoolGen arbitrary() {
        return arbitrary;
    }

    @Override
    public Supplier<Boolean> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        return gen::nextBoolean;
    }
}
