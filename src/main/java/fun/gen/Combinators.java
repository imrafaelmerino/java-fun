package fun.gen;

import fun.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class Combinators {
    private Combinators() {
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Gen<T> oneOf(final T value,
                                   final T... others) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(others);
        return r -> () ->
        {
            int n = r.nextInt(others.length + 1);
            return n == 0 ?
                   value :
                   others[n - 1];
        };
    }

    public static <T> Gen<T> oneOf(final List<T> values) {
        if (requireNonNull(values).isEmpty())
            throw new RuntimeException("list empty. No value can be generated");
        return r -> () -> values.get(requireNonNull(r).nextInt(values.size()));
    }

    @SafeVarargs
    @SuppressWarnings({"varargs", "overloads"})
    public static <A> Gen<A> oneOf(final Gen<? extends A> gen,
                                   final Gen<? extends A>... others) {

        requireNonNull(gen);
        requireNonNull(others);
        return r -> {
            final List<Supplier<? extends A>> suppliers = new ArrayList<>();
            suppliers.add(requireNonNull(gen.apply(SplitGen.DEFAULT.apply(r))));
            suppliers.addAll(Arrays.stream(others)
                                   .map(it -> it.apply(SplitGen.DEFAULT.apply(r)))
                                   .toList());
            final int bound = 1 + requireNonNull(others).length;
            return () -> suppliers.get(r.nextInt(bound))
                                  .get();
        };
    }

    /**
     * Creates a generator that chooses a generator from `pairs` based on the
     * provided likelihoods. The likelihood of a given generator being chosen is
     * its likelihood divided by the sum of all likelihoods. Shrinks toward
     * choosing an earlier generator, as well as shrinking the value generated
     * by the chosen generator.
     *
     * @param freq   a frequency pair
     * @param others the rests of pairs
     * @return a json generator
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <A> Gen<A> freq(final Pair<Integer, Gen<? extends A>> freq,
                                  final Pair<Integer, Gen<? extends A>>... others
    ) {
        List<Pair<Integer, Gen<? extends A>>> list =
                Arrays.stream(others).collect(Collectors.toList());
        list.add(freq);
        return freqList(list);

    }

    static <A> Gen<A> freqList(final List<Pair<Integer, Gen<? extends A>>> freqs) {

        return seed -> {
            List<Pair<Integer, Gen<? extends A>>> filtered =
                    requireNonNull(freqs).stream()
                                         .filter(it -> it.first() > 0)
                                         .collect(Collectors.toList());
            if (filtered.isEmpty()) throw new IllegalArgumentException("no items with positive weights");
            return freqSupplier(SplitGen.DEFAULT,
                                seed,
                                filtered);
        };
    }

    private static <A> Supplier<A> freqSupplier(SplitGen split,
                                                RandomGenerator seed,
                                                List<Pair<Integer, Gen<? extends A>>> filtered) {
        int total = 0;
        TreeMap<Integer, Supplier<? extends A>> treeMap = new TreeMap<>();
        for (var t : filtered) {
            total += t.first();
            treeMap.put(total,
                        t.second()
                         .apply(split.apply(seed)));
        }
        Supplier<Integer> choose =
                IntGen.arbitrary(1,
                                 total + 1)
                      .apply(split.apply(seed));
        return () -> treeMap.ceilingEntry(choose.get())
                            .getValue().get();
    }

    public static <O> Gen<O> nullable(final Gen<O> gen) {
        return nullable(gen,
                        5);
    }

    public static <O> Gen<O> nullable(final Gen<O> gen,
                                      int prob) {
        requireNonNull(gen);
        if (prob < 0) throw new IllegalArgumentException("prob < 0");
        if (prob > 100) throw new IllegalArgumentException("prob > 100");
        return IntGen.arbitrary(0,
                                101).then(n -> {
            if (n <= prob) return Gen.cons(null);
            else return gen;
        });
    }

    private static <I> void combinations(final List<I> input,
                                         final int i,
                                         final int k,
                                         final List<List<I>> combination,
                                         final List<I> combinations) {
        if (input.size() == 0) return;

        if (k == 0) {
            combination.add(new ArrayList<>(combinations));
            return;
        }

        if (i == input.size()) return;

        combinations.add(input.get(i));
        combinations(input,
                     i + 1,
                     k - 1,
                     combination,
                     combinations);

        combinations.remove(combinations.size() - 1);
        combinations(input,
                     i + 1,
                     k,
                     combination,
                     combinations);
    }

    private static <I> List<List<I>> combinations(final List<I> input,
                                                  final int k) {
        List<List<I>> result = new ArrayList<>();
        combinations(input,
                     0,
                     k,
                     result,
                     new ArrayList<>());
        return result;
    }


    public static <I> Gen<List<I>> combinations(final int k,
                                                final List<I> input) {
        requireNonNull(input);
        if (k < 0) throw new IllegalArgumentException("k < 0");
        return seed -> {
            if (input.isEmpty()) return List::of;
            List<List<I>> combinations = combinations(input,
                                                      k);
            var indexGen = IntGen.arbitrary(0,
                                            combinations.size()).apply(seed);
            return () -> combinations.get(indexGen.get());
        };
    }

    public static <I> Gen<List<I>> permutations(final List<I> input) {
        requireNonNull(input);
        return seed -> {
            if (input.isEmpty()) return List::of;
            List<List<I>> permutations = new ArrayList<>();
            permutations.add(input);
            for (int k = 1; k < input.size(); k++)
                permutations.addAll(combinations(input,
                                                 k));
            var indexGen = IntGen.arbitrary(0,
                                            permutations.size()).apply(seed);
            return () -> permutations.get(indexGen.get());
        };
    }


}
