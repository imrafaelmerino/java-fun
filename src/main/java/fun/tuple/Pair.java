package fun.tuple;


import java.util.Objects;


/**
 * Represents an immutable pair of two elements, A and B.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 */
public final class Pair<A, B> {
    private final A first;
    private final B second;

    private Pair(final A first,
                 final B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new Pair with the specified first and second elements. Null is allowed
     *
     * @param first  The first element of the pair.
     * @param second The second element of the pair.
     * @param <A>    The type of the first element.
     * @param <B>    The type of the second element.
     * @return A new Pair instance.
     */
    public static <A, B> Pair<A, B> of(final A first,
                                       final B second) {
        return new Pair<>(first,
                          second);
    }

    /**
     * Gets the first element of the pair.
     *
     * @return The first element.
     */
    public A first() {
        return first;
    }

    /**
     * Gets the second element of the pair.
     *
     * @return The second element.
     */
    public B second() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first,
                              pair.first) && Objects.equals(second,
                                                            pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first,
                            second);
    }

    @Override
    public String toString() {
        return "(" +
                first +
                ", " + second +
                ')';
    }
}