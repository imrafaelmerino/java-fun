package fun.gen;

import java.util.function.UnaryOperator;
import java.util.Random;

/**
 * A functional interface representing a generator splitter that takes a source {@link Random} generator and produces a new one.
 * This interface extends {@link java.util.function.UnaryOperator} to facilitate the creation of new random generators.
 */
@FunctionalInterface
public interface SplitGen extends UnaryOperator<Random> {
    /**
     * A default implementation of the {@code SplitGen} interface, which creates a new {@link Random} generator with a seed generated from the source generator's state.
     * This default implementation is commonly used to split a random generator into multiple independent generators.
     */
    SplitGen DEFAULT = rg -> new Random(rg.nextLong());
}
