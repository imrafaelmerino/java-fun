package jsonvalues;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * A Lens is an optic that can be seen as a pair of functions:
 {@code
  - get: S      => O i.e. from an S, we can extract an O
  - set: (O, S) => S i.e. from an S and a O, we obtain a S. Unless a prism, to go back to S we need another S.
 }
 * Typically a Lens can be defined between a Product (e.g. record, tuple) and one of its component.
 * Given a lens there are essentially three things you might want to do:
 * -view the subpart
 * -modify the whole by changing the subpart
 * -combine this lens with another lens to look even deeper
 *
 * @param <S> the source of a lens
 * @param <O> the target of a lens
 */
public class Lens<S, O> {

  /**
   * function to view the part
   */
  public final Function<S, O> get;
  /**
   * function to modify the whole by setting the subpart
   */
  public final Function<O, Function<S, S>> set;

    /**
     find if the target satisfies the predicate
     */
  public final Function<Predicate<O>,Function<S, Optional<O>>> find;

    /**
     check if there is a target and it satisfies the predicate
     */
  public final Function<Predicate<O>,Predicate<S>> exists;
  /**
   * function to modify the whole by modifying the subpart with a function
   */
  public final Function<Function<O, O>, Function<S, S>> modify;

  Lens(final Function<S, O> get,
       final Function<O, Function<S, S>> set) {

    this.set = set;
    this.get = get;
    this.modify = f -> json -> set.apply(f.apply(get.apply(json))).apply(json);
    this.find = predicate -> s -> predicate.test(get.apply(s)) ?
                                     Optional.of((get.apply(s))) :
                                     Optional.empty();
    this.exists = predicate -> s -> predicate.test(get.apply(s));
  }


  public <T> Option<S, T> compose(final Prism<O, T> prism) {
        return new Option<>(json -> requireNonNull(prism).getOptional.apply(get.apply(json)),
                            value -> json -> set.apply(prism.reverseGet.apply(value))
                                                .apply(json)
        );


    }

}
