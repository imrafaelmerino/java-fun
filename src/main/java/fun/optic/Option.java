package fun.optic;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Optional optic over a source type {@code S} and an optional focus type {@code T}.
 * <p>
 * Conceptually, this optic combines:
 * <ul>
 *   <li>A partial getter: {@code S -> Optional<T>}</li>
 *   <li>A setter: {@code (T, S) -> S}</li>
 * </ul>
 * It can be viewed as a weaker {@link Lens} (focus may be absent) and a weaker {@link Prism}
 * (it can also update the enclosing source).
 *
 * @param <S> source type.
 * @param <T> focus type.
 */
public class Option<S, T> {
    /** Getter for the optional focus. */
    public final Function<S, Optional<T>> get;

    /**
     * Setter for the focus. The resulting function updates a source with the provided focus value.
     */
    public final Function<T, Function<S, S>> set;

    /**
     * Focus modifier. If no focus is present, it returns the original source unchanged.
     */
    public final Function<Function<T, T>, Function<S, S>> modify;

    /**
     * Creates an optional optic from a partial getter and a setter.
     *
     * @param get partial getter for the focus.
     * @param set setter for updating the focus in a source.
     * @throws NullPointerException if any parameter is null.
     */
    public Option(final Function<S, Optional<T>> get,
                  final Function<T, Function<S, S>> set) {
        this.get = Objects.requireNonNull(get);
        this.set = Objects.requireNonNull(set);

        this.modify = f -> json -> {
            final Optional<T> value = get.apply(json);
            if (value.isEmpty()) return json;
            return set.apply(f.apply(value.get())).apply(json);
        };
    }


    /**
     * Composes this optional with another optional.
     * <p>
     * Resulting optic focuses from {@code S} directly into {@code F}, preserving optional semantics.
     *
     * @param other optional from {@code T} to {@code F}.
     * @param <F>   composed focus type.
     * @return composed optional.
     * @throws NullPointerException if {@code other} is null.
     */
    public <F> Option<S, F> compose(final Option<T, F> other) {
        Objects.requireNonNull(other);
        return new Option<>(s -> {
            Optional<T> t = this.get.apply(s);
            if (t.isPresent()) return other.get.apply(t.get());
            else return Optional.empty();
        },
                            f -> s -> {
                                Optional<T> t = this.get.apply(s);
                                if (t.isPresent()) return this.set.apply(other.set.apply(f).apply(t.get())).apply(s);
                                else return s;
                            });
    }

}



