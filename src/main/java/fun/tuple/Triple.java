package fun.tuple;

import java.util.Objects;

/**
 * Represents an immutable triple of three elements, A, B, and C.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 * @param <C> The type of the third element.
 */
public final class Triple<A, B, C> {

    private final A first;
    private final B second;
    private final C third;

    private Triple(final A first,
                   final B second,
                   final C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Creates a new Triple with the specified first, second, and third elements. Null is allowed
     *
     * @param first  The first element of the triple.
     * @param second The second element of the triple.
     * @param third  The third element of the triple.
     * @param <A>    The type of the first element.
     * @param <B>    The type of the second element.
     * @param <C>    The type of the third element.
     * @return A new Triple instance.
     */
    public static <A, B, C> Triple<A, B, C> of(final A first,
                                               final B second,
                                               final C third) {
        return new Triple<>(first,
                            second,
                            third);
    }

    /**
     * Gets the first element of the triple.
     *
     * @return The first element.
     */
    public A first() {
        return first;
    }

    /**
     * Gets the second element of the triple.
     *
     * @return The second element.
     */
    public B second() {
        return second;
    }

    /**
     * Gets the third element of the triple.
     *
     * @return The third element.
     */
    public C third() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(first,
                              triple.first) &&
                Objects.equals(second,
                               triple.second) &&
                Objects.equals(third,
                               triple.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first,
                            second,
                            third);
    }

    @Override
    public String toString() {
        return "(" +
                first +
                ", " + second +
                ", " + third +
                ")";
    }
}