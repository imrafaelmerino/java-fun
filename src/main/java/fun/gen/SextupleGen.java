package fun.gen;

import fun.tuple.Sextuple;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * A generator for sextuples of values of types A, B, C, D, E, and F. This generator combines six provided generators to generate sextuples containing values of the specified types.
 *
 * @param <A> The type of the first element in the sextuple.
 * @param <B> The type of the second element in the sextuple.
 * @param <C> The type of the third element in the sextuple.
 * @param <D> The type of the fourth element in the sextuple.
 * @param <E> The type of the fifth element in the sextuple.
 * @param <F> The type of the sixth element in the sextuple.
 */
public final class SextupleGen<A, B, C, D, E, F> implements Gen<Sextuple<A, B, C, D, E, F>> {

    private final Gen<A> _1;
    private final Gen<B> _2;
    private final Gen<C> _3;
    private final Gen<D> _4;
    private final Gen<E> _5;
    private final Gen<F> _6;
    private final SplitGen splitGen;

    private SextupleGen(final Gen<A> _1,
                        final Gen<B> _2,
                        final Gen<C> _3,
                        final Gen<D> _4,
                        final Gen<E> _5,
                        final Gen<F> _6) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this._3 = requireNonNull(_3);
        this._4 = requireNonNull(_4);
        this._5 = requireNonNull(_5);
        this._6 = requireNonNull(_6);
        this.splitGen = SplitGen.DEFAULT;
    }

    /**
     * Creates a new SextupleGen instance with the specified generators for the first, second, third, fourth, fifth, and sixth elements of the sextuple.
     *
     * @param <A> The type of the first element in the sextuple.
     * @param <B> The type of the second element in the sextuple.
     * @param <C> The type of the third element in the sextuple.
     * @param <D> The type of the fourth element in the sextuple.
     * @param <E> The type of the fifth element in the sextuple.
     * @param <F> The type of the sixth element in the sextuple.
     * @param _1  The generator for the first element (type A) of the sextuple.
     * @param _2  The generator for the second element (type B) of the sextuple.
     * @param _3  The generator for the third element (type C) of the sextuple.
     * @param _4  The generator for the fourth element (type D) of the sextuple.
     * @param _5  The generator for the fifth element (type E) of the sextuple.
     * @param _6  The generator for the sixth element (type F) of the sextuple.
     * @return A new SextupleGen instance for generating sextuples of values.
     */
    public static <A, B, C, D, E, F> Gen<Sextuple<A, B, C, D, E, F>> of(final Gen<A> _1,
                                                                        final Gen<B> _2,
                                                                        final Gen<C> _3,
                                                                        final Gen<D> _4,
                                                                        final Gen<E> _5,
                                                                        final Gen<F> _6) {
        return new SextupleGen<>(_1,
                                 _2,
                                 _3,
                                 _4,
                                 _5,
                                 _6);
    }

    @Override
    public Supplier<Sextuple<A, B, C, D, E, F>> apply(final RandomGenerator seed) {
        requireNonNull(seed);
        final Supplier<A> a = _1.apply(splitGen.apply(seed));
        final Supplier<B> b = _2.apply(splitGen.apply(seed));
        final Supplier<C> c = _3.apply(splitGen.apply(seed));
        final Supplier<D> d = _4.apply(splitGen.apply(seed));
        final Supplier<E> e = _5.apply(splitGen.apply(seed));
        final Supplier<F> f = _6.apply(splitGen.apply(seed));
        return () -> Sextuple.of(a.get(),
                                 b.get(),
                                 c.get(),
                                 d.get(),
                                 e.get(),
                                 f.get());
    }
}
