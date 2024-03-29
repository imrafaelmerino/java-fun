package fun.gen;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The {@code Gen} interface represents a functional generator that produces values of type {@code O} based on a source of randomness.
 * Generators are a fundamental concept in data generation and can be used to create and manipulate random data for various purposes.
 * <p>
 * Implementations of this interface are expected to provide a means of generating values based on a random source, allowing for the
 * creation of custom generators for specific data generation needs.
 * <p>
 * Generators created by implementing this interface can be composed, transformed, and combined to generate complex data structures
 * and perform controlled random sampling.
 * Note: Implementations of this interface should ensure thread-safety when used in a multithreaded environment.
 *
 * @param <O> The type of values generated by the generator.
 */
@FunctionalInterface
public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> {

    /**
     * Creates a generator that produces values based on the provided function, where the integer input to the function
     * represents the call sequence (starting from 1 for the first call).
     *
     * @param fn  The function to generate values based on the call sequence.
     * @param <O> The type of value to generate.
     * @return A generator that produces values according to the provided function and call sequence.
     * @throws NullPointerException if the provided function is null.
     */
    static <O> Gen<O> seq(final Function<Integer, O> fn) {
        Objects.requireNonNull(fn);
        return random -> {
            AtomicInteger ai = new AtomicInteger(1);
            return () -> {
                int n = ai.getAndIncrement();
                return fn.apply(n);
            };
        };
    }

    /**
     * Creates a generator that always produces a constant value {@code value}.
     *
     * @param <O>   The type of value to generate.
     * @param value The constant value to be generated by the returned generator.
     * @return A generator that always produces the specified constant value.
     */
    static <O> Gen<O> cons(final O value) {
        return seed -> () -> value;
    }


    /**
     * Creates a generator that ensures distinct values are generated from the underlying generator,
     * with a default maximum number of tries set to 1000.
     *
     * @return A generator that produces distinct values with a maximum of 1000 tries.
     * @see #distinct(int)
     */
    default Gen<O> distinct() {
        return distinct(1000);
    }

    /**
     * Creates a generator that ensures distinct values are generated from the underlying generator.
     *
     * @param tries The maximum number of tries to generate a distinct value before throwing an exception.
     * @return A generator that produces distinct values based on the given number of tries.
     */
    default Gen<O> distinct(int tries) {
        return seed -> {
            Set<O> generated = new HashSet<>();
            Supplier<O> gen = this.apply(seed);
            return () -> {
                int i = 0;
                while (i < tries) {
                    O value = gen.get();
                    if (!generated.contains(value)) {
                        generated.add(value);
                        return value;
                    }
                    i++;
                }
                throw new RuntimeException("Max tries reached trying to generate a distinct value");
            };
        };
    }

    /**
     * Creates a new generator by applying a function to the values generated by this generator.
     *
     * @param <P> The type of values produced by the new generator.
     * @param fn  The function to apply to the values generated by this generator.
     * @return A generator that produces values of type {@code P} by applying the given function to the values
     * generated by this generator.
     * @throws NullPointerException If the provided function {@code fn} is {@code null}.
     */
    default <P> Gen<P> map(final Function<O, P> fn) {
        Objects.requireNonNull(fn);
        return seed -> {
            Supplier<O> gen = this.apply(seed);
            return () -> fn.apply(gen.get());
        };
    }

    /**
     * Creates a new generator that applies a consumer to each value generated by this generator without modifying the values.
     *
     * @param consumer The consumer function to apply to each generated value.
     * @return A generator that produces the same values as this generator but also invokes the given consumer function on each value.
     * @throws NullPointerException If the provided consumer {@code consumer} is {@code null}.
     */
    default Gen<O> peek(final Consumer<O> consumer) {
        return this.map(it -> {
            consumer.accept(it);
            return it;
        });
    }

    /**
     * Creates a new generator by chaining the generation of values from this generator with another generator.
     *
     * @param <P> The type of values produced by the new generator.
     * @param fn  The function that takes a value generated by this generator and returns another generator.
     * @return A generator that produces values of type {@code P} by first generating a value using this generator,
     * and then applying the provided function to generate values of type {@code P}.
     * @throws NullPointerException If the provided function {@code fn} is {@code null}.
     */
    default <P> Gen<P> then(final Function<O, ? extends Gen<P>> fn) {
        Objects.requireNonNull(fn);
        return seed -> fn.apply(this.apply(seed).get())
                         .apply(SplitGen.DEFAULT.apply(seed));
    }

    /**
     * Creates a generator that produces values satisfying a given predicate within a maximum of 1000 tries.
     * An alternative method is available to specify a custom maximum number of tries using {@link #suchThat(Predicate, int)}.
     *
     * @param predicate The predicate that values generated by this generator must satisfy.
     * @return A generator that produces values satisfying the specified predicate within a maximum of 1000 tries.
     * @throws NullPointerException If the provided predicate {@code predicate} is {@code null}.
     * @see #suchThat(Predicate, int)
     */
    default Gen<O> suchThat(final Predicate<O> predicate) {
        return suchThat(requireNonNull(predicate),
                        1000
        );
    }

    /**
     * Creates a generator that produces values satisfying a given predicate within a specified number of tries.
     *
     * @param predicate The predicate that values generated by this generator must satisfy.
     * @param tries     The maximum number of tries to generate a value satisfying the predicate before throwing an exception.
     * @return A generator that produces values satisfying the specified predicate within the given number of tries.
     * @throws NullPointerException     If the provided predicate {@code predicate} is {@code null}.
     * @throws IllegalArgumentException If {@code tries} is less than 0.
     * @throws RuntimeException         If the generator cannot produce a value satisfying the predicate within the specified number of tries.
     */
    default Gen<O> suchThat(final Predicate<O> predicate,
                            final int tries
    ) {
        requireNonNull(predicate);
        if (tries < 0) throw new IllegalArgumentException("tries < 0");
        return r -> {
            Supplier<O> supplier = apply(r);
            return () ->
            {
                requireNonNull(r);
                for (int i = 0; i < tries; i++) {
                    O value = supplier.get();
                    if (predicate.test(value))
                        return value;
                }
                throw new RuntimeException(String.format("Couldn't satisfy such-that predicate after %s tries",
                                                         tries
                ));
            };
        };
    }

    /**
     * Creates a supplier that generates a sample value using this generator with a new random seed.
     * An alternative method is available to specify a custom random seed using {@link #sample(RandomGenerator)}.
     *
     * @return A supplier that produces a sample value using this generator with a newly generated random seed.
     * @see #sample(RandomGenerator)
     */
    default Supplier<O> sample() {
        return apply(RandomGenerator.getDefault());
    }

    /**
     * Creates a stream of sample values by repeatedly generating values using this generator with a new random seed.
     *
     * @param n The number of sample values to generate in the stream.
     * @return A stream containing {@code n} sample values generated using this generator with newly generated random seeds.
     * @throws IllegalArgumentException If {@code n} is less than 0.
     */
    default Stream<O> sample(final int n) {
        return Stream.generate(apply(RandomGenerator.getDefault())).limit(n);
    }

    /**
     * Creates a supplier that generates a sample value using this generator with the provided random seed.
     *
     * @param random The random seed to use for generating the sample value.
     * @return A supplier that produces a sample value using this generator with the provided random seed.
     * @throws NullPointerException If the provided {@code random} parameter is {@code null}.
     */
    default Supplier<O> sample(final RandomGenerator random) {
        return apply(requireNonNull(random));
    }

    /**
     * Generates a map of sampled values and their counts by repeatedly generating values using this generator
     * with a new random seed and counting the occurrences of each value.
     *
     * @param n The number of samples to generate for collecting values and counts.
     * @return A map containing sampled values as keys and their respective counts as values.
     * @throws IllegalArgumentException If {@code n} is less than or equal to 0.
     */
    default Map<O, Long> collect(final int n) {
        if (n <= 0) throw new IllegalArgumentException("n <= 0");

        return sample(n)
                .collect(Collectors.groupingBy(
                        value -> value,
                        Collectors.counting()));
    }

    /**
     * Generates a map of sampled values mapped to another type and their counts by repeatedly generating values
     * using this generator with a new random seed, applying the provided mapping function, and counting the occurrences
     * of each mapped value.
     *
     * @param <I> The type to which values will be mapped.
     * @param n   The number of samples to generate for collecting values and counts.
     * @param map The mapping function that converts values to the target type {@code I}.
     * @return A map containing sampled values as keys (after mapping) and their respective counts as values.
     * @throws IllegalArgumentException If {@code n} is less than or equal to 0.
     * @throws NullPointerException     If the provided mapping function {@code map} is {@code null}.
     */
    default <I> Map<I, Long> collect(final int n,
                                     final Function<O, I> map) {
        if (n <= 0) throw new IllegalArgumentException("n <= 0");
        return map(requireNonNull(map)).collect(n);
    }

    /**
     * Classifies sampled values into two categories based on a given condition, counts the occurrences in each category,
     * and labels them accordingly.
     *
     * @param condition  The predicate condition used to classify values into two categories.
     * @param trueLabel  The label to assign to values that satisfy the condition.
     * @param falseLabel The label to assign to values that do not satisfy the condition.
     * @param n          The number of samples to generate for classification and counting.
     * @return A map containing the specified labels as keys and their respective counts as values.
     */
    default Map<String, Long> classify(final Predicate<O> condition,
                                       final String trueLabel,
                                       final String falseLabel,
                                       final int n) {

        return map(val -> condition.test(val) ?
                          trueLabel :
                          falseLabel).collect(n);

    }

    /**
     * Classifies sampled values into multiple categories based on a set of label-predicate pairs,
     * counts the occurrences in each category, and assigns a default label to values that do not match any predicate.
     *
     * @param n            The number of samples to generate for classification and counting.
     * @param classifier   A map of label-predicate pairs used to classify values into different categories.
     * @param defaultLabel The default label assigned to values that do not match any predicate.
     * @return A map containing the labels as keys and their respective counts as values.
     */
    default Map<String, Long> classify(final int n,
                                       final Map<String, Predicate<O>> classifier,
                                       final String defaultLabel) {

        Predicate<O> defaultClassifier =
                o -> classifier.values().stream()
                               .noneMatch(cla -> cla.test(o));

        Map<String, Predicate<O>> xs = new HashMap<>(classifier);
        xs.put(requireNonNull(defaultLabel),
               defaultClassifier);

        return collect(n,
                       o -> xs.keySet().stream()
                              .filter(key -> xs.get(key).test(o))
                              .collect(Collectors.joining(","))
        );

    }

}
