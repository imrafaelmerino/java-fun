package fun.gen;

import fun.tuple.Quintuple;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * A generator for quintuples of values of types A, B, C, D, and E. This generator combines five provided generators to generate quintuples containing values of the specified types.
 *
 * @param <A> The type of the first element in the quintuple.
 * @param <B> The type of the second element in the quintuple.
 * @param <C> The type of the third element in the quintuple.
 * @param <D> The type of the fourth element in the quintuple.
 * @param <E> The type of the fifth element in the quintuple.
 */
public final class QuintupleGen<A, B, C, D, E> implements Gen<Quintuple<A, B, C, D, E>> {

    private final Gen<A> _1;
    private final Gen<B> _2;
    private final Gen<C> _3;
    private final Gen<D> _4;
    private final Gen<E> _5;
    private final SplitGen splitGen;

    private QuintupleGen(final Gen<A> _1,
                         final Gen<B> _2,
                         final Gen<C> _3,
                         final Gen<D> _4,
                         final Gen<E> _5) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this._3 = requireNonNull(_3);
        this._4 = requireNonNull(_4);
        this._5 = requireNonNull(_5);
        this.splitGen = SplitGen.DEFAULT;
    }

    /**
     * Creates a new QuintupleGen instance with the specified generators for the first, second, third, fourth, and fifth elements of the quintuple.
     *
     * @param <A> The type of the first element in the quintuple.
     * @param <B> The type of the second element in the quintuple.
     * @param <C> The type of the third element in the quintuple.
     * @param <D> The type of the fourth element in the quintuple.
     * @param <E> The type of the fifth element in the quintuple.
     * @param _1  The generator for the first element (type A) of the quintuple.
     * @param _2  The generator for the second element (type B) of the quintuple.
     * @param _3  The generator for the third element (type C) of the quintuple.
     * @param _4  The generator for the fourth element (type D) of the quintuple.
     * @param _5  The generator for the fifth element (type E) of the quintuple.
     * @return A new QuintupleGen instance for generating quintuples of values.
     */
    public static <A, B, C, D, E> Gen<Quintuple<A, B, C, D, E>> of(final Gen<A> _1,
                                                                   final Gen<B> _2,
                                                                   final Gen<C> _3,
                                                                   final Gen<D> _4,
                                                                   final Gen<E> _5) {
        return new QuintupleGen<>(_1,
                                  _2,
                                  _3,
                                  _4,
                                  _5);
    }

    @Override
    public Supplier<Quintuple<A, B, C, D, E>> apply(final RandomGenerator seed) {
        requireNonNull(seed);
        final Supplier<A> a = _1.apply(splitGen.apply(seed));
        final Supplier<B> b = _2.apply(splitGen.apply(seed));
        final Supplier<C> c = _3.apply(splitGen.apply(seed));
        final Supplier<D> d = _4.apply(splitGen.apply(seed));
        final Supplier<E> e = _5.apply(splitGen.apply(seed));
        return () -> Quintuple.of(a.get(),
                                  b.get(),
                                  c.get(),
                                  d.get(),
                                  e.get());
    }
}
