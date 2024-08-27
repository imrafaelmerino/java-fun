package fun.tuple;

import java.util.Objects;

/**
 * Represents an immutable quintuple of five elements, A, B, C, D, and E.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 * @param <C> The type of the third element.
 * @param <D> The type of the fourth element.
 * @param <E> The type of the fifth element.
 */
public final class Quintuple<A, B, C, D, E> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;
    private final E fifth;

    private Quintuple(final A first,
                      final B second,
                      final C third,
                      final D fourth,
                      final E fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
    }

    /**
     * Creates a new Quintuple with the specified first, second, third, fourth, and fifth elements. Null is allowed.
     *
     * @param first  The first element of the quintuple.
     * @param second The second element of the quintuple.
     * @param third  The third element of the quintuple.
     * @param fourth The fourth element of the quintuple.
     * @param fifth  The fifth element of the quintuple.
     * @param <A>    The type of the first element.
     * @param <B>    The type of the second element.
     * @param <C>    The type of the third element.
     * @param <D>    The type of the fourth element.
     * @param <E>    The type of the fifth element.
     * @return A new Quintuple instance.
     */
    public static <A, B, C, D, E> Quintuple<A, B, C, D, E> of(final A first,
                                                              final B second,
                                                              final C third,
                                                              final D fourth,
                                                              final E fifth) {
        return new Quintuple<>(first,
                               second,
                               third,
                               fourth,
                               fifth);
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    public D fourth() {
        return fourth;
    }

    public E fifth() {
        return fifth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quintuple<?, ?, ?, ?, ?> that = (Quintuple<?, ?, ?, ?, ?>) o;
        return Objects.equals(first,
                              that.first) &&
                Objects.equals(second,
                               that.second) &&
                Objects.equals(third,
                               that.third) &&
                Objects.equals(fourth,
                               that.fourth) &&
                Objects.equals(fifth,
                               that.fifth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first,
                            second,
                            third,
                            fourth,
                            fifth);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ", " + fourth + ", " + fifth + ")";
    }
}