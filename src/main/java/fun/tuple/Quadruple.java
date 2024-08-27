package fun.tuple;

import java.util.Objects;

/**
 * Represents an immutable quadruple of four elements, A, B, C, and D.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 * @param <C> The type of the third element.
 * @param <D> The type of the fourth element.
 */
public final class Quadruple<A, B, C, D> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    private Quadruple(final A first,
                      final B second,
                      final C third,
                      final D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * Creates a new Quadruple with the specified first, second, third, and fourth elements. Null is allowed.
     *
     * @param first  The first element of the quadruple.
     * @param second The second element of the quadruple.
     * @param third  The third element of the quadruple.
     * @param fourth The fourth element of the quadruple.
     * @param <A>    The type of the first element.
     * @param <B>    The type of the second element.
     * @param <C>    The type of the third element.
     * @param <D>    The type of the fourth element.
     * @return A new Quadruple instance.
     */
    public static <A, B, C, D> Quadruple<A, B, C, D> of(final A first,
                                                        final B second,
                                                        final C third,
                                                        final D fourth) {
        return new Quadruple<>(first,
                               second,
                               third,
                               fourth);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruple<?, ?, ?, ?> that = (Quadruple<?, ?, ?, ?>) o;
        return Objects.equals(first,
                              that.first) &&
                Objects.equals(second,
                               that.second) &&
                Objects.equals(third,
                               that.third) &&
                Objects.equals(fourth,
                               that.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first,
                            second,
                            third,
                            fourth);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
    }
}