package fun.gen;

import java.util.function.Supplier;
import java.util.Random;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of booleans.
 */
public final class BoolGen implements Gen<Boolean> {
    public static final BoolGen arbitrary = new BoolGen();

    private BoolGen() {
    }

    @Override
    public Supplier<Boolean> apply(final Random gen) {
        requireNonNull(gen);
        return gen::nextBoolean;
    }
}
