package fun.tuple;

import java.util.Objects;

/**
 * Represents an immutable sextuple of six elements, A, B, C, D, E, and F.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 * @param <C> The type of the third element.
 * @param <D> The type of the fourth element.
 * @param <E> The type of the fifth element.
 * @param <F> The type of the sixth element.
 */
public final class Sextuple<A, B, C, D, E, F> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;
    private final E fifth;
    private final F sixth;

    private Sextuple(final A first,
                     final B second,
                     final C third,
                     final D fourth,
                     final E fifth,
                     final F sixth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
        this.sixth = sixth;
    }

    /**
     * Creates a new Sextuple with the specified first, second, third, fourth, fifth, and sixth elements. Null is allowed.
     *
     * @param first  The first element of the sextuple.
     * @param second The second element of the sextuple.
     * @param third  The third element of the sextuple.
     * @param fourth The fourth element of the sextuple.
     * @param fifth  The fifth element of the sextuple.
     * @param sixth  The sixth element of the sextuple.
     * @param <A>    The type of the first element.
     * @param <B>    The type of the second element.
     * @param <C>    The type of the third element.
     * @param <D>    The type of the fourth element.
     * @param <E>    The type of the fifth element.
     * @param <F>    The type of the sixth element.
     * @return A new Sextuple instance.
     */
    public static <A, B, C, D, E, F> Sextuple<A, B, C, D, E, F> of(final A first,
                                                                   final B second,
                                                                   final C third,
                                                                   final D fourth,
                                                                   final E fifth,
                                                                   final F sixth) {
        return new Sextuple<>(first,
                              second,
                              third,
                              fourth,
                              fifth,
                              sixth);
    }

    /**
     * Returns the first element.
     *
     * @return first element.
     */
    public A first() {
        return first;
    }

    /**
     * Returns the second element.
     *
     * @return second element.
     */
    public B second() {
        return second;
    }

    /**
     * Returns the third element.
     *
     * @return third element.
     */
    public C third() {
        return third;
    }

    /**
     * Returns the fourth element.
     *
     * @return fourth element.
     */
    public D fourth() {
        return fourth;
    }

    /**
     * Returns the fifth element.
     *
     * @return fifth element.
     */
    public E fifth() {
        return fifth;
    }

    /**
     * Returns the sixth element.
     *
     * @return sixth element.
     */
    public F sixth() {
        return sixth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sextuple<?, ?, ?, ?, ?, ?> that = (Sextuple<?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(first,
                              that.first) &&
                Objects.equals(second,
                               that.second) &&
                Objects.equals(third,
                               that.third) &&
                Objects.equals(fourth,
                               that.fourth) &&
                Objects.equals(fifth,
                               that.fifth) &&
                Objects.equals(sixth,
                               that.sixth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first,
                            second,
                            third,
                            fourth,
                            fifth,
                            sixth);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ", " + fourth + ", " + fifth + ", " + sixth + ")";
    }
}
