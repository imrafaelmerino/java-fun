package fun.gen;

import fun.tuple.Pair;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of pairs.
 */
public final class PairGen<A, B> implements Gen<Pair<A, B>> {

    private final Gen<A> _1;
    private final Gen<B> _2;

    private final SplitGen splitGen;

    private PairGen(final Gen<A> _1,
                    final Gen<B> _2) {
        this(_1,
             _2,
             SplitGen.DEFAULT);
    }

    private PairGen(final Gen<A> _1,
                    final Gen<B> _2,
                    final SplitGen splitGen) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this.splitGen = requireNonNull(splitGen);
    }

    public static <A, B> Gen<Pair<A, B>> of(final Gen<A> _1,
                                            final Gen<B> _2) {
        return new PairGen<>(_1,
                             _2);
    }

    @Override
    public Supplier<Pair<A, B>> apply(RandomGenerator seed) {
        Objects.requireNonNull(seed);
        final Supplier<A> a = _1.apply(splitGen.apply(seed));
        final Supplier<B> b = _2.apply(splitGen.apply(seed));
        return () -> new Pair<>(a.get(),
                                b.get());
    }
}
