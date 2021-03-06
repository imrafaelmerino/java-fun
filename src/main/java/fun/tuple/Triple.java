package fun.tuple;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

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

    public static <A, B, C> Triple<A, B, C> of(final A first,
                                               final B second,
                                               final C third) {
        return new Triple<>(requireNonNull(first),
                            requireNonNull(second),
                            requireNonNull(third));
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
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}