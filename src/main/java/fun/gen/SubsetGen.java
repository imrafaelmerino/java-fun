package fun.gen;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

class SubsetGen<O> implements Gen<Set<O>> {

    private final List<O> inputs;

    SubsetGen(List<O> inputs) {
        this.inputs = Objects.requireNonNull(inputs);
    }

    @Override
    public Supplier<Set<O>> apply(RandomGenerator random) {
        Objects.requireNonNull(random);
        return () -> {
            Set<O> subset = new HashSet<>();
            for (O elem : inputs) {
                if (random.nextBoolean()) {
                    subset.add(elem);
                }
            }
            return subset;
        };
    }
}
