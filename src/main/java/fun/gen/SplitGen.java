package fun.gen;


import java.util.SplittableRandom;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;

@FunctionalInterface
public interface SplitGen extends UnaryOperator<RandomGenerator> {
    SplitGen DEFAULT = rg -> new SplittableRandom(rg.nextLong());
}
