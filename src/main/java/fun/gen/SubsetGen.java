package fun.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

class SubsetGen<O> implements Gen<Set<O>> {


    private final static int MAX_N_FIELDS_TO_CALCULATE_ALL_COMBINATIONS = 22;
    private final List<O> inputs;
    private final Map<Integer, List<Set<O>>> memoizationMap;

    SubsetGen(List<O> inputs) {
        this.inputs = Objects.requireNonNull(inputs);
        this.memoizationMap = new HashMap<>();
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

    private List<Set<O>> subsets(List<O> elements) {
        int hashCode = elements.hashCode();
        if (memoizationMap.containsKey(hashCode)) {
            return memoizationMap.get(hashCode);
        }

        List<Set<O>> combinations = new ArrayList<>();
        subsets(elements,
                0,
                new HashSet<>(),
                combinations);
        memoizationMap.put(hashCode,
                           combinations);

        return combinations;
    }


    @Override
    public Supplier<Set<O>> apply(RandomGenerator random) {

        if (inputs.size() < MAX_N_FIELDS_TO_CALCULATE_ALL_COMBINATIONS) {
            List<Set<O>> combinations = subsets(inputs);
            Gen<Integer> gen = IntGen.arbitrary(0,
                                                combinations.size() - 1);
            Supplier<Integer> indexSupplier = gen.apply(random);
            return () -> combinations.get(indexSupplier.get());
        } else {
            Gen<Integer> gen = IntGen.arbitrary(0,
                                                inputs.size());
            Supplier<Integer> lengthSupplier = gen.apply(SplitGen.DEFAULT.apply(random));

            return () -> {
                List<O> xs = new ArrayList<>(inputs);
                Combinators.shuffle(xs,
                                    random); // Shuffle the elements randomly
                return new HashSet<>(xs.subList(0,
                                                lengthSupplier.get()));
            };
        }
    }
}
