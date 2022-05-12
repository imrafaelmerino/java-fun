package fun.gen;

import java.util.Objects;
import java.util.SplittableRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Gen<O> extends Function<RandomGenerator, Supplier<O>> {

    static <O> Gen<O> cons(final O value) {
        return seed -> () -> value;
    }

    static <O> Gen<O> cons(final Supplier<O> supplier) {
        return seed -> supplier;
    }

    default <P> Gen<P> map(final Function<O, P> fn) {
        Objects.requireNonNull(fn);
        return seed -> () -> fn.apply(this.apply(seed).get());
    }

    default Gen<O> peek(final Consumer<O> consumer) {
        return this.map(it -> {
            consumer.accept(it);
            return it;
        });
    }

    default <P> Gen<P> then(final Function<O, ? extends Gen<P>> fn) {
        Objects.requireNonNull(fn);
        return gen -> fn.apply(this.apply(SplitGen.DEFAULT.apply(gen))
                                   .get())
                        .apply(SplitGen.DEFAULT.apply(gen));
    }

    /**
     * Creates a generator that generates values from this generator that satisfy the given predicate.
     * Care is needed to ensure there is a high chance this generator will satisfy
     * the predicate. By default, `suchThat` will try 100 times to generate a value that
     * satisfies the predicate. If no value passes this predicate after this number
     * of iterations, a runtime exception will be thrown.
     *
     * @param predicate the predicate satisfied by every generated value
     * @return a generator
     */
    default Gen<O> suchThat(final Predicate<O> predicate) {
        return suchThat(requireNonNull(predicate),
                        1000
        );
    }

    /**
     * Creates a generator that generates values from this generator that satisfy the given predicate.
     * Care is needed to ensure there is a high chance this generator will satisfy
     * the predicate. By default, `suchThat` will try specified number of times to generate a value that
     * satisfies the predicate. If no value passes this predicate after this number
     * of iterations, a runtime exception will be thrown.
     *
     * @param predicate the predicate satisfied by every generated value
     * @param tries     the number of tries
     * @return a generator
     */
    default Gen<O> suchThat(final Predicate<O> predicate,
                            final int tries
    ) {
        requireNonNull(predicate);
        if (tries < 0) throw new IllegalArgumentException("tries < 0");
        return r -> () ->
        {
            requireNonNull(r);
            for (int i = 0; i < tries; i++) {
                O value = apply(r).get();
                if (predicate.test(value))
                    return value;
            }
            throw new RuntimeException(String.format("Couldn't satisfy such-that predicate after %s tries",
                                                     tries
            ));
        };
    }

    /**
     * Return a supplier of realized values from this generator
     *
     * @return a supplier of values
     */
    default Supplier<O> sample() {
        return apply(new SplittableRandom());
    }

    default Stream<O> sample(int n) {
        return Stream.generate(apply(new SplittableRandom())).limit(n);
    }

    /**
     * Return a supplier of realized values from this generator and the given seed
     *
     * @param random the seed of the generator
     * @return a supplier of values
     */
    default Supplier<O> sample(final RandomGenerator random) {
        return apply(requireNonNull(random));
    }


}
