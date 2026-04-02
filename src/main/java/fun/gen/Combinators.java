package fun.gen;

import fun.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Static combinators and helper constructors for {@link Gen}.
 * <p>
 * Methods in this class let you build generators from constant collections, choose among
 * generators with uniform or weighted probability, generate subsets/combinations, and derive
 * nullable values.
 * <p>
 * When a method accepts externally mutable inputs (lists, sets, or varargs arrays), the default
 * overload usually takes a defensive snapshot at generator creation time. Methods suffixed with
 * {@code View} keep live references to avoid that one-time copy cost.
 * <p>
 * API contract:
 * <ul>
 *   <li>Default overloads are mutation-stable: external changes after construction do not affect generation.</li>
 *   <li>{@code *View} overloads are live views: external changes are observable by subsequent generations.</li>
 *   <li>Use {@code *View} only when you intentionally want that live behavior and can control concurrent mutation.</li>
 * </ul>
 */
public final class Combinators {
    private static final int SHUFFLE_THRESHOLD = 5;

    private Combinators() {
    }

    /**
     * Creates a generator that selects one value from a provided list of values with equal probability.
     *
     * @param <T>    The type of values to choose from.
     * @param value  The first value to include in the selection.
     * @param others Additional values to include in the selection.
     * @return A generator that produces values randomly selected from the provided list of values.
     * @throws NullPointerException If the {@code others} array is {@code null}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Gen<T> oneOf(final T value,
                                   final T... others) {
        requireNonNull(others);
        final T[] snapshot = Arrays.copyOf(others,
                                           others.length);
        return r -> () ->
        {
            int n = r.nextInt(snapshot.length + 1);
            return n == 0 ?
                   value :
                   snapshot[n - 1];
        };
    }

    /**
     * Creates a generator that selects one value from a provided varargs array with equal probability,
     * without taking a defensive copy of {@code others}.
     * <p>
     * This avoids one-time allocation cost at generator construction time, but external mutations to the
     * backing array are reflected in subsequent generated values.
     *
     * @param <T>    The type of values to choose from.
     * @param value  The first value to include in the selection.
     * @param others Additional values to include in the selection (live reference).
     * @return A generator that produces values randomly selected from the provided values.
     * @throws NullPointerException If the {@code others} array is {@code null}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Gen<T> oneOfView(final T value,
                                       final T... others) {
        requireNonNull(others);
        return r -> () ->
        {
            int n = r.nextInt(others.length + 1);
            return n == 0 ?
                   value :
                   others[n - 1];
        };
    }

    /**
     * Creates a generator that selects one value from a provided list of values with equal probability.
     * This overload snapshots {@code values} at generator creation time.
     *
     * @param <T>    The type of values to choose from.
     * @param values A list of values to choose from.
     * @return A generator that produces values randomly selected from the provided list of values.
     * @throws NullPointerException     If the provided {@code values} list is {@code null}.
     * @throws IllegalArgumentException If the provided {@code values} list is empty.
     */
    public static <T> Gen<T> oneOf(final List<T> values) {
        final List<T> snapshot = new ArrayList<>(requireNonNull(values));
        if (snapshot.isEmpty())
            throw new IllegalArgumentException("list empty. No value can be generated");
        return r -> () -> snapshot.get(requireNonNull(r).nextInt(snapshot.size()));
    }

    /**
     * Creates a generator that selects one value from a provided set of values with equal probability.
     * This overload snapshots {@code values} at generator creation time.
     *
     * @param <T>    The type of values to choose from.
     * @param values A set of values to choose from.
     * @return A generator that produces values randomly selected from the provided set of values.
     * @throws NullPointerException     If the provided {@code values} set is {@code null}.
     * @throws IllegalArgumentException If the provided {@code values} set is empty.
     */
    public static <T> Gen<T> oneOf(final Set<T> values) {
        final List<T> snapshot = new ArrayList<>(requireNonNull(values));
        if (snapshot.isEmpty())
            throw new IllegalArgumentException("set empty. No value can be generated");
        return r -> () -> snapshot.get(requireNonNull(r).nextInt(snapshot.size()));
    }

    /**
     * Creates a generator that selects one value from a provided list of values with equal probability,
     * without taking a defensive copy of the list.
     * <p>
     * External mutations to the list are reflected in subsequent generated values.
     *
     * @param <T>    The type of values to choose from.
     * @param values A live list of values to choose from.
     * @return A generator that produces values randomly selected from the provided list of values.
     * @throws NullPointerException     If {@code values} is {@code null}.
     * @throws IllegalArgumentException If {@code values} is empty.
     */
    public static <T> Gen<T> oneOfView(final List<T> values) {
        requireNonNull(values);
        if (values.isEmpty())
            throw new IllegalArgumentException("list empty. No value can be generated");
        return r -> () -> values.get(requireNonNull(r).nextInt(values.size()));
    }

    /**
     * Creates a generator that selects one value from a provided set of values with equal probability,
     * without taking a defensive copy of the set.
     * <p>
     * External mutations to the set are reflected in subsequent generated values.
     *
     * @param <T>    The type of values to choose from.
     * @param values A live set of values to choose from.
     * @return A generator that produces values randomly selected from the provided set of values.
     * @throws NullPointerException     If {@code values} is {@code null}.
     * @throws IllegalArgumentException If {@code values} is empty.
     */
    public static <T> Gen<T> oneOfView(final Set<T> values) {
        requireNonNull(values);
        if (values.isEmpty())
            throw new IllegalArgumentException("set empty. No value can be generated");
        return r -> () -> pickAtIndex(values,
                                      requireNonNull(r).nextInt(values.size()));
    }

    /**
     * Creates a generator that produces a list of random values (indexes not repeated) by selecting and removing elements from a provided list.
     * This overload snapshots {@code values} at generator creation time.
     *
     * @param <T>    The type of values to choose from.
     * @param values A list of values to choose from.
     * @param n      The number of values to generate in the list.
     * @return A generator that produces a list of {@code n} random values from the provided list.
     * @throws IllegalArgumentException If {@code n} is negative or greater than the size of the {@code values} list.
     */
    public static <T> Gen<List<T>> nOf(final List<T> values,
                                       int n) {
        requireNonNull(values);
        List<T> snapshot = new ArrayList<>(values);
        if (n < 0) throw new IllegalArgumentException("n < 0");
        if (n > snapshot.size()) throw new IllegalArgumentException("n > list.size=" + snapshot.size());
        return nOfView(snapshot,
                       n);
    }

    /**
     * Creates a generator that produces a list of random values (indexes not repeated) by selecting and
     * removing elements from a live list reference.
     * <p>
     * This method avoids the one-time defensive copy performed by {@link #nOf(List, int)}, but if the source
     * list is externally mutated after generator construction, generated results change accordingly and the
     * generator may fail when {@code n} becomes greater than current list size.
     *
     * @param <T>    The type of values to choose from.
     * @param values A live list of values to choose from.
     * @param n      The number of values to generate in the list.
     * @return A generator that produces a list of {@code n} random values from the provided list.
     * @throws NullPointerException     If {@code values} is {@code null}.
     * @throws IllegalArgumentException If {@code n} is negative or greater than {@code values.size()} at creation time.
     * @throws IllegalStateException    If the live list later shrinks below {@code n}.
     */
    public static <T> Gen<List<T>> nOfView(final List<T> values,
                                           int n) {
        requireNonNull(values);
        if (n < 0) throw new IllegalArgumentException("n < 0");
        if (n > values.size()) throw new IllegalArgumentException("n > list.size=" + values.size());
        return random -> () -> {
            if (n > values.size()) {
                throw new IllegalStateException("source list size (" + values.size() + ") is now smaller than n=" + n);
            }
            List<T> result = new ArrayList<>();
            List<T> copy = new ArrayList<>(values);
            generateCollection(n,
                               random,
                               result,
                               copy);
            return result;
        };
    }

    /**
     * Creates a generator that produces a set of random values (not repeated) by selecting and removing elements from a provided set.
     * This overload snapshots {@code values} at generator creation time.
     *
     * @param <T>    The type of values to choose from.
     * @param values A set of values to choose from.
     * @param n      The number of values to generate in the set.
     * @return A generator that produces a set of {@code n} random values from the provided set.
     * @throws IllegalArgumentException If {@code n} is negative or greater than the size of the {@code values} set.
     */
    public static <T> Gen<Set<T>> nOf(final Set<T> values,
                                      int n) {
        requireNonNull(values);
        Set<T> snapshot = new LinkedHashSet<>(values);
        if (n < 0) throw new IllegalArgumentException("n < 0");
        if (n > snapshot.size()) throw new IllegalArgumentException("n > set.size=" + snapshot.size());
        return nOfView(snapshot,
                       n);
    }

    /**
     * Creates a generator that produces a set of random values (not repeated) by selecting and removing
     * elements from a live set reference.
     * <p>
     * This method avoids the one-time defensive copy performed by {@link #nOf(Set, int)}, but if the source
     * set is externally mutated after generator construction, generated results change accordingly and the
     * generator may fail when {@code n} becomes greater than current set size.
     *
     * @param <T>    The type of values to choose from.
     * @param values A live set of values to choose from.
     * @param n      The number of values to generate in the set.
     * @return A generator that produces a set of {@code n} random values from the provided set.
     * @throws NullPointerException     If {@code values} is {@code null}.
     * @throws IllegalArgumentException If {@code n} is negative or greater than {@code values.size()} at creation time.
     * @throws IllegalStateException    If the live set later shrinks below {@code n}.
     */
    public static <T> Gen<Set<T>> nOfView(final Set<T> values,
                                          int n) {
        requireNonNull(values);
        if (n < 0) throw new IllegalArgumentException("n < 0");
        if (n > values.size()) throw new IllegalArgumentException("n > set.size=" + values.size());
        return random -> () -> {
            if (n > values.size()) {
                throw new IllegalStateException("source set size (" + values.size() + ") is now smaller than n=" + n);
            }
            Set<T> result = new HashSet<>();
            Set<T> copy = new HashSet<>(values);
            generateCollection(n,
                               random,
                               result,
                               copy);
            return result;
        };
    }

    private static <T> void generateCollection(int n,
                                               RandomGenerator random,
                                               Collection<T> result,
                                               Collection<T> copy) {
        for (int i = 0; i < n; i++) {
            int counter = 0;
            int j = random.nextInt(copy.size());
            Iterator<T> iter = copy.iterator();
            while (iter.hasNext()) {
                T next = iter.next();
                if (j == counter) {
                    result.add(next);
                    iter.remove();
                    break;
                }
                counter += 1;

            }
        }
    }

    /**
     * Creates a generator that selects one generator from a provided list of generators with equal probability.
     *
     * @param <A>    The type of values generated by the generators.
     * @param gen    The first generator to include in the selection.
     * @param others Additional generators to include in the selection.
     * @return A generator that produces values generated by one of the provided generators with equal probability.
     * @throws NullPointerException If either the {@code gen} or {@code others} array is {@code null}.
     */
    @SafeVarargs
    @SuppressWarnings({"varargs", "overloads"})
    public static <A> Gen<A> oneOf(final Gen<? extends A> gen,
                                   final Gen<? extends A>... others) {

        requireNonNull(gen);
        requireNonNull(others);
        final Gen<? extends A>[] snapshot = Arrays.copyOf(others,
                                                          others.length);
        return r -> {
            final List<Supplier<? extends A>> suppliers = new ArrayList<>();
            suppliers.add(requireNonNull(gen.apply(SplitGen.DEFAULT.apply(r))));
            suppliers.addAll(Arrays.stream(snapshot)
                                   .map(it -> it.apply(SplitGen.DEFAULT.apply(r)))
                                   .toList());
            final int bound = 1 + snapshot.length;
            return () -> suppliers.get(r.nextInt(bound))
                                  .get();
        };
    }

    /**
     * Creates a generator that selects one generator from varargs with equal probability, without
     * taking a defensive copy of {@code others}.
     * <p>
     * This avoids one-time allocation cost at generator construction time, but external mutations to
     * the backing varargs array are reflected in generated values.
     *
     * @param <A>    The type of values generated by the generators.
     * @param gen    The first generator to include in the selection.
     * @param others Additional generators to include in the selection (live reference).
     * @return A generator that produces values generated by one of the provided generators.
     * @throws NullPointerException If either {@code gen} or {@code others} is {@code null}.
     */
    @SafeVarargs
    @SuppressWarnings({"varargs", "overloads"})
    public static <A> Gen<A> oneOfView(final Gen<? extends A> gen,
                                       final Gen<? extends A>... others) {
        requireNonNull(gen);
        requireNonNull(others);
        return r -> {
            final List<Supplier<? extends A>> suppliers = new ArrayList<>();
            suppliers.add(requireNonNull(gen.apply(SplitGen.DEFAULT.apply(r))));
            suppliers.addAll(Arrays.stream(others)
                                   .map(it -> it.apply(SplitGen.DEFAULT.apply(r)))
                                   .toList());
            final int bound = 1 + others.length;
            return () -> suppliers.get(r.nextInt(bound))
                                  .get();
        };
    }

    /**
     * Creates a generator that selects one generator from a provided list of generators with equal probability.
     * This overload snapshots the input list at generator creation time.
     *
     * @param <A>  The type of values generated by the generators.
     * @param gens generators to include in the selection.
     * @return A generator that produces values generated by one of the provided generators with equal probability.
     * @throws NullPointerException     If {@code gens} is {@code null}.
     * @throws IllegalArgumentException If {@code gens} is empty.
     */
    @SuppressWarnings({"varargs", "overloads"})
    public static <A> Gen<A> oneOfList(final List<Gen<? extends A>> gens) {
        final List<Gen<? extends A>> snapshot = new ArrayList<>(requireNonNull(gens));
        if (snapshot.isEmpty()) {
            throw new IllegalArgumentException("list empty. No generator can be selected");
        }
        return r -> {
            List<Supplier<? extends A>> suppliers =
                    new ArrayList<>(snapshot.stream()
                                        .map(it -> it.apply(SplitGen.DEFAULT.apply(r)))
                                        .toList()
                    );
            int bound = snapshot.size();
            return () -> suppliers.get(r.nextInt(bound))
                                  .get();
        };
    }

    /**
     * Creates a generator that selects one generator from a live list with equal probability,
     * without taking a defensive copy of the list.
     * <p>
     * External mutations to {@code gens} before sampling are reflected in behavior.
     *
     * @param <A>  The type of values generated by the generators.
     * @param gens A live list of generators to include in the selection.
     * @return A generator that produces values generated by one of the provided generators.
     * @throws NullPointerException     If {@code gens} is {@code null}.
     * @throws IllegalArgumentException If {@code gens} is empty at creation time.
     * @throws IllegalStateException    If the live list is empty when sampling starts.
     */
    @SuppressWarnings({"varargs", "overloads"})
    public static <A> Gen<A> oneOfListView(final List<Gen<? extends A>> gens) {
        requireNonNull(gens);
        if (gens.isEmpty()) {
            throw new IllegalArgumentException("list empty. No generator can be selected");
        }
        return r -> {
            if (gens.isEmpty()) {
                throw new IllegalStateException("source generator list is now empty");
            }
            List<Supplier<? extends A>> suppliers =
                    new ArrayList<>(gens.stream()
                                        .map(it -> it.apply(SplitGen.DEFAULT.apply(r)))
                                        .toList()
                    );
            int bound = suppliers.size();
            return () -> suppliers.get(r.nextInt(bound))
                                  .get();
        };
    }

    /**
     * Creates a generator that chooses a generator from `pairs` based on the provided likelihoods.
     * The likelihood of a given generator being chosen is its likelihood divided by the sum of all likelihoods.
     *
     * @param freq   a frequency pair
     * @param others the rest of the frequency pairs
     * @param <A>    the type of the values
     * @return A generator that selects a generator based on provided likelihoods.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <A> Gen<A> freq(final Pair<Integer, Gen<? extends A>> freq,
                                  final Pair<Integer, Gen<? extends A>>... others
    ) {
        List<Pair<Integer, Gen<? extends A>>> list =
                Arrays.stream(requireNonNull(others))
                      .collect(Collectors.toList());
        list.add(requireNonNull(freq));
        return freqList(list);

    }

    static <A> Gen<A> freqList(final List<Pair<Integer, Gen<? extends A>>> freqs) {
        requireNonNull(freqs);
        if (freqs.isEmpty()) throw new IllegalArgumentException("empty frequency list");
        return seed -> {
            return freqSupplier(SplitGen.DEFAULT,
                                seed,
                                validatedFrequencies(freqs));
        };
    }

    private static <A> Supplier<A> freqSupplier(SplitGen split,
                                                RandomGenerator seed,
                                                List<Pair<Integer, Gen<? extends A>>> filtered) {
        long total = 0L;
        TreeMap<Long, Supplier<? extends A>> treeMap = new TreeMap<>();
        for (Pair<Integer, Gen<? extends A>> t : filtered) {
            if (Long.MAX_VALUE - total < t.first()) {
                throw new IllegalArgumentException("total weight overflow");
            }
            total += t.first();
            treeMap.put(total,
                        t.second()
                         .apply(split.apply(seed)));
        }
        Supplier<Long> choose =
                LongGen.arbitrary(1L,
                                  total)
                       .apply(split.apply(seed));
        return () -> treeMap.ceilingEntry(choose.get())
                            .getValue().get();
    }

    private static <A> List<Pair<Integer, Gen<? extends A>>> validatedFrequencies(
            List<Pair<Integer, Gen<? extends A>>> freqs) {
        List<Pair<Integer, Gen<? extends A>>> result = new ArrayList<>(freqs.size());
        for (int i = 0; i < freqs.size(); i++) {
            Pair<Integer, Gen<? extends A>> pair = requireNonNull(freqs.get(i),
                                                                   "null frequency pair at index " + i);
            Integer weight = requireNonNull(pair.first(),
                                            "null weight at index " + i);
            if (weight <= 0) {
                throw new IllegalArgumentException("weight must be > 0 at index " + i + " but was " + weight);
            }
            requireNonNull(pair.second(),
                           "null generator at index " + i);
            result.add(pair);
        }
        return result;
    }

    /**
     * Creates a generator that generates nullable values with a specified probability of being null.
     *
     * @param <O> The type of values to generate.
     * @param gen The generator for non-null values.
     * @return A generator that produces nullable values with a 50% probability of being null.
     */
    public static <O> Gen<O> nullable(final Gen<O> gen) {
        return nullable(gen,
                        50);
    }

    /**
     * Creates a generator that generates nullable values with a specified probability of being null.
     *
     * @param <O>  The type of values to generate.
     * @param gen  The generator for non-null values.
     * @param prob The probability (0-100) of the value being null.
     * @return A generator that produces nullable values with the specified probability of being null.
     * @throws IllegalArgumentException If {@code prob} is not in the range 0-100.
     */
    public static <O> Gen<O> nullable(final Gen<O> gen,
                                      int prob) {
        requireNonNull(gen);
        if (prob < 0) throw new IllegalArgumentException("prob < 0");
        if (prob > 100) throw new IllegalArgumentException("prob > 100");
        return seed -> {
            Supplier<Integer> n =
                    IntGen.arbitrary(0,
                                     99)
                          .apply(SplitGen.DEFAULT.apply(seed));


            Supplier<O> supplier =
                    gen.apply(SplitGen.DEFAULT.apply(seed));

            return () -> n.get() < prob ?
                         null :
                         supplier.get();
        };

    }

    /**
     * Creates a generator that generates combinations of elements from a list of values.
     * This overload snapshots distinct input values at generator creation time.
     *
     * @param <I>   The type of elements in the list.
     * @param k     The size of the combinations to generate.
     * @param input A list of input values.
     * @return A generator that produces combinations of elements of size {@code k} from the input list.
     * @throws IllegalArgumentException If {@code k} is negative or greater than the number of distinct values in
     *                                  {@code input}.
     */
    public static <I> Gen<Set<I>> combinations(final int k,
                                               final List<I> input) {
        requireNonNull(input);
        List<I> distinctInput = new ArrayList<>(new LinkedHashSet<>(input));
        if (k < 0 || k > distinctInput.size()) {
            throw new IllegalArgumentException("k must be between 0 and number of distinct input values");
        }
        return random -> () -> {
            List<I> shuffled = new ArrayList<>(distinctInput);
            shuffle(shuffled,
                    random);
            return new HashSet<>(shuffled.subList(0,
                                                  k));
        };
    }

    /**
     * Creates a generator that generates combinations of elements from a live list reference,
     * without taking a defensive snapshot at construction time.
     * <p>
     * The list is de-duplicated per sample to preserve set-combination semantics. If the live source
     * later contains fewer distinct values than {@code k}, sampling fails with {@link IllegalStateException}.
     *
     * @param <I>   The type of elements in the list.
     * @param k     The size of the combinations to generate.
     * @param input A live list of input values.
     * @return A generator that produces combinations of elements of size {@code k} from the current list state.
     * @throws NullPointerException     If {@code input} is {@code null}.
     * @throws IllegalArgumentException If {@code k} is negative or greater than the number of distinct input values at creation time.
     * @throws IllegalStateException    If the live input later contains fewer than {@code k} distinct values.
     */
    public static <I> Gen<Set<I>> combinationsView(final int k,
                                                   final List<I> input) {
        requireNonNull(input);
        final int initialDistinct = new LinkedHashSet<>(input).size();
        if (k < 0 || k > initialDistinct) {
            throw new IllegalArgumentException("k must be between 0 and number of distinct input values");
        }
        return random -> () -> {
            List<I> distinctInput = new ArrayList<>(new LinkedHashSet<>(input));
            if (k > distinctInput.size()) {
                throw new IllegalStateException(
                        "source distinct size (" + distinctInput.size() + ") is now smaller than k=" + k
                );
            }
            List<I> shuffled = new ArrayList<>(distinctInput);
            shuffle(shuffled,
                    random);
            return new LinkedHashSet<>(shuffled.subList(0,
                                                        k));
        };
    }

    /**
     * Creates a generator that generates combinations of elements from a set of values.
     * This overload snapshots input values at generator creation time.
     *
     * @param <I>   The type of elements in the set.
     * @param k     The size of the combinations to generate.
     * @param input A set of input values.
     * @return A generator that produces combinations of elements of size {@code k} from the input set.
     * @throws IllegalArgumentException If {@code k} is negative or greater than {@code input.size()}.
     */
    public static <I> Gen<Set<I>> combinations(final int k,
                                               final Set<I> input) {
        return combinations(k,
                            new ArrayList<>(requireNonNull(input)));
    }

    /**
     * Creates a generator that generates combinations of elements from a live set reference,
     * without taking a defensive snapshot at construction time.
     * <p>
     * If the live source later contains fewer values than {@code k}, sampling fails with
     * {@link IllegalStateException}.
     *
     * @param <I>   The type of elements in the set.
     * @param k     The size of the combinations to generate.
     * @param input A live set of input values.
     * @return A generator that produces combinations of elements of size {@code k} from the current set state.
     * @throws NullPointerException     If {@code input} is {@code null}.
     * @throws IllegalArgumentException If {@code k} is negative or greater than {@code input.size()} at creation time.
     * @throws IllegalStateException    If the live source later shrinks below {@code k}.
     */
    public static <I> Gen<Set<I>> combinationsView(final int k,
                                                   final Set<I> input) {
        requireNonNull(input);
        if (k < 0 || k > input.size()) {
            throw new IllegalArgumentException("k must be between 0 and number of distinct input values");
        }
        return random -> () -> {
            if (k > input.size()) {
                throw new IllegalStateException("source set size (" + input.size() + ") is now smaller than k=" + k);
            }
            List<I> shuffled = new ArrayList<>(input);
            shuffle(shuffled,
                    random);
            return new LinkedHashSet<>(shuffled.subList(0,
                                                        k));
        };
    }

    /**
     * Creates a generator that generates subsets of elements from a list of values.
     * This overload snapshots distinct input values at generator creation time.
     *
     * @param <I>      The type of elements in the list.
     * @param elements A list of input values.
     * @return A generator that produces subsets of input elements.
     */
    public static <I> Gen<Set<I>> subsets(List<I> elements) {
        requireNonNull(elements);
        return subsetsView(distinctSnapshot(elements));
    }

    /**
     * Creates a generator that generates subsets of elements from a set of values.
     * This overload snapshots input values at generator creation time.
     *
     * @param <I>      The type of elements in the set.
     * @param elements A set of input values.
     * @return A generator that produces subsets of input elements.
     */
    public static <I> Gen<Set<I>> subsets(Set<I> elements) {
        return subsetsView(new LinkedHashSet<>(requireNonNull(elements)));
    }

    /**
     * Creates a generator that generates subsets from a live list reference without taking a defensive copy.
     * <p>
     * External mutations to the source list are reflected in subsequent generated values.
     * If the list contains duplicates, they behave as repeated Bernoulli trials but collapse in the resulting set.
     *
     * @param <I>      The type of elements in the list.
     * @param elements A live list of input values.
     * @return A generator that produces subsets of input elements.
     * @throws NullPointerException If {@code elements} is {@code null}.
     */
    public static <I> Gen<Set<I>> subsetsView(List<I> elements) {
        return new SubsetGen<>(requireNonNull(elements));
    }

    /**
     * Creates a generator that generates subsets from a live set reference without taking a defensive copy.
     * <p>
     * External mutations to the source set are reflected in subsequent generated values.
     *
     * @param <I>      The type of elements in the set.
     * @param elements A live set of input values.
     * @return A generator that produces subsets of input elements.
     * @throws NullPointerException If {@code elements} is {@code null}.
     */
    public static <I> Gen<Set<I>> subsetsView(Set<I> elements) {
        requireNonNull(elements);
        return random -> () -> {
            Set<I> subset = new HashSet<>();
            for (I elem : elements) {
                if (random.nextBoolean()) {
                    subset.add(elem);
                }
            }
            return subset;
        };
    }

    /**
     * Generates a new list by shuffling the elements of the given list using a random generator.
     * The original list {@code xs} remains unaffected because this overload snapshots it at creation time.
     *
     * @param xs  the list of elements to shuffle
     * @param <I> the type of elements in the list
     * @return a generator that produces shuffled lists
     */
    public static <I> Gen<List<I>> shuffle(List<I> xs) {
        List<I> snapshot = new ArrayList<>(requireNonNull(xs));
        return random -> () -> {
            List<I> ys = new ArrayList<>(snapshot);
            shuffle(ys,
                    random);
            return ys;
        };
    }

    /**
     * Generates shuffled lists from a live source list without taking a defensive copy at construction time.
     * <p>
     * External mutations to {@code xs} before sampling are reflected in generated values.
     *
     * @param xs  a live list of elements to shuffle
     * @param <I> the type of elements in the list
     * @return a generator that produces shuffled lists
     * @throws NullPointerException If {@code xs} is {@code null}.
     */
    public static <I> Gen<List<I>> shuffleView(List<I> xs) {
        requireNonNull(xs);
        return random -> () -> {
            List<I> ys = new ArrayList<>(xs);
            shuffle(ys,
                    random);
            return ys;
        };
    }

    private static <I> List<I> distinctSnapshot(List<I> elements) {
        return new ArrayList<>(new LinkedHashSet<>(elements));
    }

    private static <T> T pickAtIndex(Collection<T> values,
                                     int index) {
        int i = 0;
        for (T value : values) {
            if (i == index) {
                return value;
            }
            i++;
        }
        throw new IllegalStateException("collection size changed during selection");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static void shuffle(List<?> list,
                        RandomGenerator rnd) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--)
                swap(list,
                     i - 1,
                     rnd.nextInt(i));
        } else {
            Object[] arr = list.toArray();

            for (int i = size; i > 1; i--)
                swap(arr,
                     i - 1,
                     rnd.nextInt(i));

            ListIterator it = list.listIterator();
            for (Object e : arr) {
                it.next();
                it.set(e);
            }
        }
    }

    /**
     * Swaps the elements at the specified positions in the specified list.
     * (If the specified positions are equal, invoking this method leaves
     * the list unchanged.)
     *
     * @param list The list in which to swap elements.
     * @param i    the index of one element to be swapped.
     * @param j    the index of the other element to be swapped.
     * @throws IndexOutOfBoundsException if either {@code i} or {@code j}
     *                                   is out of range (i &lt; 0 || i &gt;= list.size()
     *                                   || j &lt; 0 || j &gt;= list.size()).
     * @since 1.4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void swap(List<?> list,
                            int i,
                            int j) {
        // instead of using a raw type here, it's possible to capture
        // the wildcard, but it will require a call to a supplementary
        // private method
        ((List) list).set(i,
                          ((List) list).set(j,
                                            ((List) list).get(i)));
    }

    /**
     * Swaps the two specified elements in the specified array.
     */
    private static void swap(Object[] arr,
                             int i,
                             int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
