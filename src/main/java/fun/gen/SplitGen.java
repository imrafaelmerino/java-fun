package fun.gen;

import java.util.function.UnaryOperator;
import java.util.Random;

@FunctionalInterface
public interface SplitGen extends UnaryOperator<Random> {
    SplitGen DEFAULT = rg -> new Random(rg.nextLong());
}
