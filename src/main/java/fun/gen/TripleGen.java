package fun.gen;


import fun.tuple.Triple;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * A generator for triples of elements of type A, B, and C.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 * @param <C> The type of the third element.
 */
public final class TripleGen<A, B, C> implements Gen<Triple<A, B, C>> {
    private final Gen<A> _1;
    private final Gen<B> _2;
    private final Gen<C> _3;

    private final SplitGen splitGen;


    private TripleGen(final Gen<A> _1,
                      final Gen<B> _2,
                      final Gen<C> _3) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this._3 = requireNonNull(_3);
        this.splitGen = SplitGen.DEFAULT;
    }

    /**
     * Creates a TripleGen with generators for the three elements.
     *
     * @param _1  The generator for the first element.
     * @param _2  The generator for the second element.
     * @param _3  The generator for the third element.
     * @param <A> The type of the first element.
     * @param <B> The type of the second element.
     * @param <C> The type of the third element.
     * @return A TripleGen instance for generating triples of elements.
     */
    public static <A, B, C> Gen<Triple<A, B, C>> of(final Gen<A> _1,
                                                    final Gen<B> _2,
                                                    final Gen<C> _3) {
        return new TripleGen<>(_1,
                               _2,
                               _3);
    }

    @Override
    public Supplier<Triple<A, B, C>> apply(final RandomGenerator seed) {
        Objects.requireNonNull(seed);
        final Supplier<A> a = _1.apply(splitGen.apply(seed));
        final Supplier<B> b = _2.apply(splitGen.apply(seed));
        final Supplier<C> c = _3.apply(splitGen.apply(seed));
        return () -> Triple.of(a.get(),
                               b.get(),
                               c.get());
    }
}
