package fun.gen;

import fun.tuple.Pair;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * A generator for pairs of values of types A and B. This generator combines two provided generators to generate pairs containing values of the specified types.
 *
 * @param <A> The type of the first element in the pair.
 * @param <B> The type of the second element in the pair.
 */
public final class PairGen<A, B> implements Gen<Pair<A, B>> {

    private final Gen<A> _1;
    private final Gen<B> _2;
    private final SplitGen splitGen;


    private PairGen(final Gen<A> _1,
                    final Gen<B> _2) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this.splitGen = SplitGen.DEFAULT;
    }

    /**
     * Creates a new PairGen instance with the specified generators for the first and second elements of the pair.
     *
     * @param <A> The type of the first element in the pair.
     * @param <B> The type of the second element in the pair.
     * @param _1  The generator for the first element (type A) of the pair.
     * @param _2  The generator for the second element (type B) of the pair.
     * @return A new PairGen instance for generating pairs of values.
     */
    public static <A, B> Gen<Pair<A, B>> of(final Gen<A> _1,
                                            final Gen<B> _2) {
        return new PairGen<>(_1,
                             _2);
    }

    @Override
    public Supplier<Pair<A, B>> apply(final RandomGenerator seed) {
        requireNonNull(seed);
        final Supplier<A> a = _1.apply(splitGen.apply(seed));
        final Supplier<B> b = _2.apply(splitGen.apply(seed));
        return () -> Pair.of(a.get(),
                             b.get()
        );
    }
}
