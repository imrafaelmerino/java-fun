package fun.gen;


import fun.tuple.Triple;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * represent a generator of triples.
 */
public final class TripleGen<A, B, C> implements Gen<Triple<A, B, C>> {

    private final Gen<A> _1;
    private final Gen<B> _2;

    private final Gen<C> _3;

    private final SplitGen splitGen;

    private TripleGen(final Gen<A> _1,
                      final Gen<B> _2,
                      final Gen<C> _3) {
        this(_1,
             _2,
             _3,
             SplitGen.DEFAULT);
    }

    private TripleGen(final Gen<A> _1,
                      final Gen<B> _2,
                      final Gen<C> _3,
                      final SplitGen splitGen) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this._3 = requireNonNull(_3);
        this.splitGen = requireNonNull(splitGen);
    }

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
        return () -> new Triple<>(a.get(),
                                  b.get(),
                                  c.get());
    }
}
