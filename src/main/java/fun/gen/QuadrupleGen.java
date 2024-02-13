package fun.gen;

import fun.tuple.Quadruple;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * A generator for quadruples of values of types A, B, C, and D. This generator combines four provided generators to generate quadruples containing values of the specified types.
 *
 * @param <A> The type of the first element in the quadruple.
 * @param <B> The type of the second element in the quadruple.
 * @param <C> The type of the third element in the quadruple.
 * @param <D> The type of the fourth element in the quadruple.
 */
public final class QuadrupleGen<A, B, C, D> implements Gen<Quadruple<A, B, C, D>> {

    private final Gen<A> _1;
    private final Gen<B> _2;
    private final Gen<C> _3;
    private final Gen<D> _4;
    private final SplitGen splitGen;


    private QuadrupleGen(final Gen<A> _1,
                         final Gen<B> _2,
                         final Gen<C> _3,
                         final Gen<D> _4) {
        this._1 = requireNonNull(_1);
        this._2 = requireNonNull(_2);
        this._3 = requireNonNull(_3);
        this._4 = requireNonNull(_4);
        this.splitGen = SplitGen.DEFAULT;
    }

    /**
     * Creates a new QuadrupleGen instance with the specified generators for the first, second, third, and fourth elements of the quadruple.
     *
     * @param <A> The type of the first element in the quadruple.
     * @param <B> The type of the second element in the quadruple.
     * @param <C> The type of the third element in the quadruple.
     * @param <D> The type of the fourth element in the quadruple.
     * @param _1  The generator for the first element (type A) of the quadruple.
     * @param _2  The generator for the second element (type B) of the quadruple.
     * @param _3  The generator for the third element (type C) of the quadruple.
     * @param _4  The generator for the fourth element (type D) of the quadruple.
     * @return A new QuadrupleGen instance for generating quadruples of values.
     */
    public static <A, B, C, D> Gen<Quadruple<A, B, C, D>> of(final Gen<A> _1,
                                                             final Gen<B> _2,
                                                             final Gen<C> _3,
                                                             final Gen<D> _4) {
        return new QuadrupleGen<>(_1,
                                  _2,
                                  _3,
                                  _4);
    }

    @Override
    public Supplier<Quadruple<A, B, C, D>> apply(final RandomGenerator seed) {
        requireNonNull(seed);
        final Supplier<A> a = _1.apply(splitGen.apply(seed));
        final Supplier<B> b = _2.apply(splitGen.apply(seed));
        final Supplier<C> c = _3.apply(splitGen.apply(seed));
        final Supplier<D> d = _4.apply(splitGen.apply(seed));
        return () -> Quadruple.of(a.get(),
                                  b.get(),
                                  c.get(),
                                  d.get());
    }
}
