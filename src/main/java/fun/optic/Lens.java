package fun.optic;


import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Total optic that focuses from a source {@code S} into a mandatory part {@code O}.
 * <p>
 * A lens is defined by:
 * <ul>
 *   <li>{@code get: S -> O}</li>
 *   <li>{@code set: (O, S) -> S}</li>
 * </ul>
 * Typical usage is focusing into product-like structures (records, tuples, objects).
 *
 * @param <S> source type.
 * @param <O> focus type.
 */
public class Lens<S, O> {

    /** Getter for the focused part. */
    public final Function<S, O> get;
    /**
     * Setter for replacing the focused part in the source.
     */
    public final Function<O, Function<S, S>> set;

    /**
     * Finds the focus when it satisfies a predicate.
     */
    public final Function<Predicate<O>, Function<S, Optional<O>>> find;

    /**
     * Checks whether the focused value satisfies a predicate.
     */
    public final Function<Predicate<O>, Predicate<S>> exists;
    /**
     * Modifier for transforming the focus and rebuilding the source.
     */
    public final Function<Function<O, O>, Function<S, S>> modify;

    /**
     * Creates a lens from a getter and setter.
     *
     * @param get focus getter.
     * @param set focus setter.
     * @throws NullPointerException if any parameter is null.
     */
    public Lens(final Function<S, O> get,
                final Function<O, Function<S, S>> set) {

        this.set = requireNonNull(set);
        this.get = requireNonNull(get);
        this.modify = f -> json -> set.apply(f.apply(get.apply(json)))
                                      .apply(json);
        this.find = predicate -> s -> predicate.test(get.apply(s)) ?
                                      Optional.of(get.apply(s)) :
                                      Optional.empty();
        this.exists = predicate -> s -> predicate.test(get.apply(s));
    }


    /**
     * Composes this lens with a prism, producing an {@link Option}.
     *
     * @param prism prism from current focus type to a nested optional focus.
     * @param <T>   new focus type.
     * @return composed optional optic.
     * @throws NullPointerException if {@code prism} is null.
     */
    public <T> Option<S, T> compose(final Prism<O, T> prism) {
        Objects.requireNonNull(prism);
        return new Option<>(
                json -> requireNonNull(prism).getOptional.apply(get.apply(json)),
                value -> json -> set.apply(prism.reverseGet.apply(requireNonNull(value)))
                                    .apply(requireNonNull(json))
        );


    }

    /**
     * Composes this lens with another lens.
     *
     * @param other lens from current focus to nested focus.
     * @param <B>   new focus type.
     * @return composed lens.
     * @throws NullPointerException if {@code other} is null.
     */
    public <B> Lens<S, B> compose(final Lens<O, B> other) {
        Objects.requireNonNull(other);
        return new Lens<>(this.get.andThen(other.get),
                          b -> s -> {
                              O o = this.get.apply(requireNonNull(s));
                              if (o == null) return s;
                              O newO = other.set.apply(requireNonNull(b)).apply(o);
                              return this.set.apply(newO).apply(s);
                          }
        );
    }

    /**
     * Composes this lens with an option.
     *
     * @param option optional optic from current focus to nested optional focus.
     * @param <B>    new focus type.
     * @return composed optional optic.
     * @throws NullPointerException if {@code option} is null.
     */
    public <B> Option<S, B> compose(final Option<O, B> option) {
        Objects.requireNonNull(option);
        return new Option<>(s -> option.get.apply(get.apply(s)),
                            b -> s -> {
                                O c = get.apply(s);
                                if (c == null) return s;
                                O d = option.set.apply(b).apply(c);
                                return set.apply(d).apply(s);
                            });
    }


}
