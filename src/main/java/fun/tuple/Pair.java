package fun.tuple;


import java.util.Objects;

public final class Pair<A,B> {

    private final A first;
    private final B second;

    public Pair(final A first,
                final B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return first;
    }

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