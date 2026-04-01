package fun.optic;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Partial optic that focuses from a source {@code S} into an optional target {@code T}.
 * <p>
 * A prism is defined by:
 * <ul>
 *   <li>{@code getOptional: S -> Optional<T>}</li>
 *   <li>{@code reverseGet: T -> S}</li>
 * </ul>
 * It is typically used with sum/coproduct-like domains (variants, unions).
 *
 * @param <S> source type.
 * @param <T> focus type.
 */
public class Prism<S, T> {
    /** Partial getter for the focus. */
    public final Function<S, Optional<T>> getOptional;

    /** Reverse constructor from a focus value back to source type. */
    public final Function<T, S> reverseGet;

    /** Predicate that is {@code true} when this prism does not match. */
    public final Predicate<S> isEmpty;

    /** Predicate that is {@code true} when this prism matches. */
    public final Predicate<S> nonEmpty;

    /** Modifier that returns the original source unchanged when this prism does not match. */
    public final Function<Function<T, T>, Function<S, S>> modify;

    /** Modifier that returns empty when this prism does not match. */
    public final Function<Function<T, T>, Function<S, Optional<S>>> modifyOpt;

    /** Filtered read of the focus based on a predicate. */
    public final Function<Predicate<T>, Function<S, Optional<T>>> find;

    /** Predicate-based existence check on the focus. */
    public final Function<Predicate<T>, Predicate<S>> exists;

    /** Universal check on optional focus: true when missing or when predicate matches. */
    public final Function<Predicate<T>, Predicate<S>> all;

    /**
     * Creates a prism from a partial getter and a reverse constructor.
     *
     * @param getOptional partial getter.
     * @param reverseGet  reverse constructor.
     * @throws NullPointerException if any parameter is null.
     */
    public Prism(final Function<S, Optional<T>> getOptional,
                 final Function<T, S> reverseGet
    ) {
        this.getOptional = Objects.requireNonNull(getOptional);
        this.reverseGet = Objects.requireNonNull(reverseGet);
        this.modify = f -> {
            Objects.requireNonNull(f);
            return v ->
            {
                final Optional<T> opt = getOptional.apply(v);
                if (opt.isPresent()) return reverseGet.apply(f.apply(opt.get()));
                else return v;
            };
        };
        this.modifyOpt = f -> v ->
        {
            final Optional<T> opt = getOptional.apply(v);
            return opt.map(t -> reverseGet.apply(f.apply(t)));
        };
        this.isEmpty = target -> getOptional.apply(target)
                                            .isEmpty();
        this.nonEmpty = target -> getOptional.apply(target)
                                             .isPresent();
        this.find = predicate -> v -> getOptional.apply(v)
                                                 .filter(predicate);

        this.exists = predicate -> v -> getOptional.apply(v)
                                                   .filter(predicate)
                                                   .isPresent();
        this.all = predicate -> v ->
        {
            final Optional<T> value = getOptional.apply(v);
            return value.map(predicate::test)
                        .orElse(true);
        };
    }

}
