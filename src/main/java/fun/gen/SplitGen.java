package fun.gen;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * A functional interface representing a generator splitter that takes a source {@link Random} generator and produces a new one.
 * This interface extends {@link java.util.function.UnaryOperator} to facilitate the creation of new random generators.
 */
@FunctionalInterface
public interface SplitGen extends UnaryOperator<RandomGenerator> {
    /**
     * A default implementation of the {@code SplitGen} interface, which creates a new {@link RandomGenerator} generator with a
     * seed generated from the source generator's state.
     * This default implementation is commonly used to split a random generator into multiple independent generators.
     */
    SplitGen DEFAULT = rg -> {
        if (rg instanceof RandomGenerator.SplittableGenerator s) return s.split();
        if (rg instanceof SecureRandom r) return new SecureRandom(r.generateSeed(r.nextInt(64,
                                                                                           128)));
        if (rg instanceof Random r) return new Random(r.nextLong());
        return RandomGeneratorFactory.getDefault()
                                     .create(rg.nextLong());
    };
}
