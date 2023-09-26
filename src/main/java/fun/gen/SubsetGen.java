package fun.gen;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

class SubsetSupplier<O> implements Supplier<Set<O>> {

    private final Supplier<Stream<Set<O>>> s;

    private final List<O> inputs;
    private final Supplier<Long> gen;
    private final Map<Integer, List<Set<O>>> memoizationMap;

    SubsetSupplier(List<O> inputs,
                   Random random) {
        this.inputs = Objects.requireNonNull(inputs);
        this.s = () -> subsets(inputs);
        long i = (long) Math.pow(2,
                                 inputs.size()) - 1;
        this.gen = LongGen.arbitrary(0,
                                     i).apply(Objects.requireNonNull(random));
        this.memoizationMap = new HashMap<>();
    }

    public SubsetSupplier(List<O> inputs) {
        this(inputs,
             new Random());
    }

    private static <I> void subsets(List<I> elements,
                                    int currentIndex,
                                    Set<I> currentCombination,
                                    List<Set<I>> combinations) {
        if (currentIndex == elements.size()) {
            combinations.add(new HashSet<>(currentCombination));
            return;
        }

        currentCombination.add(elements.get(currentIndex));
        subsets(elements,
                currentIndex + 1,
                currentCombination,
                combinations);
        currentCombination.remove(elements.get(currentIndex));

        subsets(elements,
                currentIndex + 1,
                currentCombination,
                combinations);
    }

    private Stream<Set<O>> subsets(List<O> elements) {
        int hashCode = elements.hashCode();
        if (memoizationMap.containsKey(hashCode)) {
            return memoizationMap.get(hashCode).stream();
        }

        List<Set<O>> combinations = new ArrayList<>();
        subsets(elements,
                0,
                new HashSet<>(),
                combinations);
        Stream<Set<O>> resultStream = combinations.stream();
        memoizationMap.put(hashCode,
                           combinations);

        return resultStream;
    }

    Gen<Set<O>> gen() {
        return random -> new SubsetSupplier<>(SubsetSupplier.this.inputs,
                                              random
        );
    }

    @Override
    public Set<O> get() {
        return s.get().skip(gen.get()).findFirst().orElse(null);
    }


}
