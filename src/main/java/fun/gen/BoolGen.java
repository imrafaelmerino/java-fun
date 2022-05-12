package fun.gen;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Represents a generator of booleans.
 */
public final class BoolGen implements Gen<Boolean> {
    public static final BoolGen arbitrary = new BoolGen();

    private BoolGen() {
    }

    @Override
    public Supplier<Boolean> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return gen::nextBoolean;
    }
}
