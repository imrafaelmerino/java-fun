package fun.optic;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * A Prism is an optic that can be seen as a pair of functions:
 * - {@code getOptional: S -> Optional<T>} i.e., get the target of a Prism or nothing if there is no target.
 * - {@code reverseGet: T -> S} i.e., get the modified source of a Prism.
 * Typically, a Prism encodes the relation between a Sum or CoProduct type and one of its elements.
 *
 * @param <S> the source of a prism.
 * @param <T> the target of a prism.
 */
public class Prism<S, T> {
    /**
     * The function to get the target of a Prism or nothing if there is no target.
     */
    public final Function<S, Optional<T>> getOptional;

    /**
     * The function to get the modified source of a Prism.
     */
    public final Function<T, S> reverseGet;

    /**
     * A predicate to check if there is no target.
     */
    public final Predicate<S> isEmpty;

    /**
     * A predicate to check if there is a target.
     */
    public final Predicate<S> nonEmpty;

    /**
     * A function to modify the target of a Prism with a function, returning the same source if the prism is not matching.
     * Basically, it means we don't care about the success of the operation.
     */
    public final Function<Function<T, T>, Function<S, S>> modify;

    /**
     * A function to modify the target of a Prism with a function, returning empty if the prism is not matching.
     * Unless modifyOpt, we need to know the success of the operation.
     */
    public final Function<Function<T, T>, Function<S, Optional<S>>> modifyOpt;

    /**
     * A function to find if the target satisfies the predicate.
     */
    public final Function<Predicate<T>, Function<S, Optional<T>>> find;

    /**
     * A function to check if there is a target, and it satisfies the predicate.
     */
    public final Function<Predicate<T>, Predicate<S>> exists;

    /**
     * A function to check if there is no target or the target satisfies the predicate.
     */
    public final Function<Predicate<T>, Predicate<S>> all;

    /**
     * Creates a new Prism with the given functions for getting and setting the target.
     *
     * @param getOptional The function to get the target of a Prism or nothing if there is no target.
     * @param reverseGet  The function to get the modified source of a Prism.
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
