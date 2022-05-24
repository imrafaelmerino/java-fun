package fun.gen;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of booleans.
 */
public final class BoolGen implements Gen<Boolean> {
    private static final BoolGen arbitrary = new BoolGen();

    private BoolGen() {
    }

    public static BoolGen arbitrary() {
        return arbitrary;
    }

    @Override
    public Supplier<Boolean> apply(final Random gen) {
        requireNonNull(gen);
        return gen::nextBoolean;
    }
}
