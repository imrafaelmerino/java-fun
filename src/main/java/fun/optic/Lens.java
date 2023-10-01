package fun.optic;


import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * A Lens is an optic that can be seen as a pair of functions:
 * <pre>{@code
 * - get: S      => O i.e. from an S, we can extract an O
 * - set: (O, S) => S i.e. from an S and an O, we obtain an S. Unless it's a prism,
 *   to go back to S, we need another S.
 * }</pre>
 * Typically, a Lens can be defined between a Product (e.g., record, tuple) and one of its components.
 * Given a lens, there are essentially three things you might want to do:
 * <ul>
 *     <li>View the subpart.</li>
 *     <li>Modify the whole by changing the subpart.</li>
 *     <li>Combine this lens with another lens to look even deeper.</li>
 * </ul>
 *
 * @param <S> the source of a lens
 * @param <O> the target of a lens
 */
public class Lens<S, O> {

    /**
     * Function to view the part.
     */
    public final Function<S, O> get;
    /**
     * function to modify the whole by setting the subpart
     */
    public final Function<O, Function<S, S>> set;

    /**
     * find if the target satisfies the predicate
     */
    public final Function<Predicate<O>, Function<S, Optional<O>>> find;

    /**
     * check if there is a target, and it satisfies the predicate
     */
    public final Function<Predicate<O>, Predicate<S>> exists;
    /**
     * function to modify the whole by modifying the subpart with a function
     */
    public final Function<Function<O, O>, Function<S, S>> modify;

    /**
     * Creates a new Lens instance with the specified get and set functions.
     *
     * @param get The function to view the part.
     * @param set The function to modify the whole by setting the subpart.
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
     * Composing a Lens and a Prism returns and Optional
     *
     * @param prism A Prism from the focus of the lens to the new focus of the Optional
     * @param <T>   the type of the new focus of the Optional
     * @return an Optional
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
     * Compose this lens with another one
     *
     * @param other the other lens
     * @param <B>   the type of the focus on the new lens
     * @return a new Lens
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
     * Composes this lens with an Option.
     *
     * @param option The Option to compose with.
     * @param <B>    The type of the focus on the new Option.
     * @return A new Option.
     */
    public <B> Option<S, B> compose(final Option<O, B> option) {

        return new Option<>(s -> option.get.apply(get.apply(s)),
                            b -> s -> {
                                O c = get.apply(s);
                                if (c == null) return s;
                                O d = option.set.apply(b).apply(c);
                                return set.apply(d).apply(s);
                            });
    }


}
