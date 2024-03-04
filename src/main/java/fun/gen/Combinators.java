package fun.gen;

import fun.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Utility class for generating values and working with generators. This class provides a set of static methods
 * for creating and manipulating generators that produce values of various types. Generators can be used for
 * generating random data, making selections, and performing other data generation tasks.
 * <p>
 * The class includes methods for generating values randomly from lists, sets, or arrays, creating generators
 * based on likelihoods, generating combinations and subsets of elements, and more. It offers a wide range of
 * utility functions for working with data generation in a flexible and controlled manner.
 * <p>
 * Users can leverage these methods to create custom generators for specific data generation needs and scenarios.
 * The methods are designed to be composable and flexible, allowing users to combine and modify generators
 * to suit their requirements.
 * <p>
 * Note: This class is intended for advanced usage and may require an understanding of functional programming
 * concepts.
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
     * @throws NullPointerException If either the {@code value} or {@code others} array is {@code null}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Gen<T> oneOf(final T value,
                                   final T... others) {
        requireNonNull(value);
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
     *
     * @param <T>    The type of values to choose from.
     * @param values A list of values to choose from.
     * @return A generator that produces values randomly selected from the provided list of values.
     * @throws NullPointerException If the provided {@code values} list is {@code null} or empty.
     */
    public static <T> Gen<T> oneOf(final List<T> values) {
        if (requireNonNull(values).isEmpty())
            throw new RuntimeException("list empty. No value can be generated");
        return r -> () -> values.get(requireNonNull(r).nextInt(values.size()));
    }

    /**
     * Creates a generator that selects one value from a provided set of values with equal probability.
     *
     * @param <T>    The type of values to choose from.
     * @param values A set of values to choose from.
     * @return A generator that produces values randomly selected from the provided set of values.
     * @throws NullPointerException If the provided {@code values} set is {@code null} or empty.
     */
    public static <T> Gen<T> oneOf(final Set<T> values) {
        if (requireNonNull(values).isEmpty())
            throw new RuntimeException("set empty. No value can be generated");
        return r -> {
            int size = values.size();
            return () -> {
                int counter = 0;
                Iterator<T> iterator = values.iterator();
                int j = r.nextInt(size);
                T value = null;
                while (iterator.hasNext()) {
                    T next = iterator.next();
                    if (j == counter) value = next;
                    counter += 1;
                }
                return value;

            };
        };
    }

    /**
     * Creates a generator that produces a list of random values (indexes not repeated) by selecting and removing elements from a provided list.
     *
     * @param <T>    The type of values to choose from.
     * @param values A list of values to choose from.
     * @param n      The number of values to generate in the list.
     * @return A generator that produces a list of {@code n} random values from the provided list.
     * @throws IllegalArgumentException If {@code n} is greater than the size of the {@code values} list.
     */
    public static <T> Gen<List<T>> nOf(final List<T> values,
                                       int n) {
        if (n > values.size()) throw new IllegalArgumentException("n > list.size=" + values.size());
        return random -> () -> {
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
     *
     * @param <T>    The type of values to choose from.
     * @param values A set of values to choose from.
     * @param n      The number of values to generate in the set.
     * @return A generator that produces a set of {@code n} random values from the provided set.
     * @throws IllegalArgumentException If {@code n} is greater than the size of the {@code values} set.
     */
    public static <T> Gen<Set<T>> nOf(final Set<T> values,
                                      int n) {
        if (n > values.size()) throw new IllegalArgumentException("n > set.size=" + values.size());
        return random -> () -> {
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
     * Creates a generator that selects one generator from a provided list of generators with equal probability.
     *
     * @param <A>  The type of values generated by the generators.
     * @param gens generators to include in the selection.
     * @return A generator that produces values generated by one of the provided generators with equal probability.
     * @throws NullPointerException If either the {@code gen} or {@code others} array is {@code null}.
     */
    @SuppressWarnings({"varargs", "overloads"})
    public static <A> Gen<A> oneOfList(final List<Gen<? extends A>> gens) {

        requireNonNull(gens);
        return r -> {
            List<Supplier<? extends A>> suppliers =
                    new ArrayList<>(gens.stream()
                                        .map(it -> it.apply(SplitGen.DEFAULT.apply(r)))
                                        .toList()
                    );
            int bound = gens.size();
            return () -> suppliers.get(r.nextInt(bound))
                                  .get();
        };
    }

    /**
     * Creates a generator that chooses a generator from `pairs` based on the provided likelihoods.
     * The likelihood of a given generator being chosen is its likelihood divided by the sum of all likelihoods.
     * Shrinks toward choosing an earlier generator, as well as shrinking the value generated by the chosen generator.
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
        for (Pair<Integer, Gen<? extends A>> t : filtered) {
            total += t.first();
            treeMap.put(total,
                        t.second()
                         .apply(split.apply(seed)));
        }
        Supplier<Integer> choose =
                IntGen.arbitrary(1,
                                 total)
                      .apply(split.apply(seed));
        return () -> treeMap.ceilingEntry(choose.get())
                            .getValue().get();
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
                                     100)
                          .apply(SplitGen.DEFAULT.apply(seed));


            Supplier<O> supplier =
                    gen.apply(SplitGen.DEFAULT.apply(seed));

            return () -> n.get() <= prob ?
                         null :
                         supplier.get();
        };

    }

    /**
     * Creates a generator that generates combinations of elements from a list of values.
     *
     * @param <I>   The type of elements in the list.
     * @param k     The size of the combinations to generate.
     * @param input A list of input values.
     * @return A generator that produces combinations of elements of size {@code k} from the input list.
     */
    public static <I> Gen<Set<I>> combinations(final int k,
                                               final List<I> input) {
        return subsets(input).suchThat(it -> it.size() == k);
    }

    /**
     * Creates a generator that generates combinations of elements from a set of values.
     *
     * @param <I>   The type of elements in the set.
     * @param k     The size of the combinations to generate.
     * @param input A set of input values.
     * @return A generator that produces combinations of elements of size {@code k} from the input set.
     */
    public static <I> Gen<Set<I>> combinations(final int k,
                                               final Set<I> input) {
        return combinations(k,
                            new ArrayList<>(input));
    }

    /**
     * Creates a generator that generates subsets of elements from a list of values.
     *
     * @param <I>      The type of elements in the list.
     * @param elements A list of input values.
     * @return A generator that produces subsets of input elements.
     */
    public static <I> Gen<Set<I>> subsets(List<I> elements) {
        return new SubsetGen<>(elements);
    }

    /**
     * Creates a generator that generates subsets of elements from a set of values.
     *
     * @param <I>      The type of elements in the set.
     * @param elements A set of input values.
     * @return A generator that produces subsets of input elements.
     */
    public static <I> Gen<Set<I>> subsets(Set<I> elements) {
        return subsets(new ArrayList<>(elements));
    }

    /**
     * Generates a new list by shuffling the elements of the given list using a random generator.
     * The original list {@code xs} remains unaffected.
     *
     * @param xs  the list of elements to shuffle
     * @param <I> the type of elements in the list
     * @return a generator that produces shuffled lists
     */
    public static <I> Gen<List<I>> shuffle(List<I> xs) {
        return random -> () -> {
            List<I> ys = new ArrayList<>(xs);
            shuffle(ys,
                    random);
            return ys;
        };
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
