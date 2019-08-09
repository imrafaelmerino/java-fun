package jsonvalues;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class ReduceFns
{

    static <T> BiFunction<JsPair, Optional<T>, Optional<T>> accumulateIf(final Predicate<? super JsPair> predicate,
                                                                         final Function<? super JsPair, T> map,
                                                                         final BinaryOperator<T> op
                                                                        )
    {

        return (pair, acc) ->
        {
            if (!predicate.test(pair)) return acc;
            final T mapped = map.apply(pair);

            final Optional<T> t = acc.map(it -> op.apply(it,
                                                         mapped
                                                        ));
            if (t.isPresent()) return t;
            return Optional.ofNullable(mapped);
        };
    }


    static <T> BiFunction<Json<?>, Optional<T>, Trampoline<Optional<T>>> reduceJson_(final BiFunction<JsPair, Optional<T>, Optional<T>> accumulator,
                                                                                     final JsPath startingPath,
                                                                                     final boolean isRecursive
                                                                                    )
    {

        return (json, acc) ->
        {
            if (json.isObj()) return reduceObj_(accumulator,
                                                startingPath,
                                                isRecursive
                                               ).apply(json.asJsObj(),
                                                       acc
                                                      );
            return reduceArr_(accumulator,
                              startingPath.index(-1),
                              isRecursive
                             ).apply(json.asJsArray(),
                                     acc
                                    );

        };
    }


    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static <T> BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>> reduceObj_(final BiFunction<JsPair, Optional<T>, Optional<T>> accumulator,
                                                                                  final JsPath startingPath,
                                                                                  final boolean isRecursive
                                                                                 )
    {

        return (obj, acc) -> obj.ifEmptyElse(done(acc),
                                             (head, tail) ->
                                             {
                                                 final JsPath headPath = startingPath.key(head.getKey());
                                                 return MatchFns.ifJsonElse(json -> isRecursive ? more(() -> reduceJson_(accumulator,
                                                                                                                         headPath,
                                                                                                                         isRecursive
                                                                                                                        ).apply(json,
                                                                                                                                acc
                                                                                                                               )
                                                                                                      ).flatMap(headAcc -> reduceObj_(accumulator,
                                                                                                                                      startingPath,
                                                                                                                                      isRecursive
                                                                                                                                     ).apply(tail,
                                                                                                                                             headAcc
                                                                                                                                            )) : more(() -> reduceObj_(accumulator,
                                                                                                                                                                       startingPath,
                                                                                                                                                                       isRecursive
                                                                                                                                                                      ).apply(tail,
                                                                                                                                                                              acc
                                                                                                                                                                             )
                                                                                                                                                     ),
                                                                            elem -> more(() -> reduceObj_(accumulator,
                                                                                                          startingPath,
                                                                                                          isRecursive
                                                                                                         ).apply(tail,
                                                                                                                 accumulator.apply(JsPair.of(headPath,
                                                                                                                                             elem
                                                                                                                                            ),
                                                                                                                                   acc
                                                                                                                                  )
                                                                                                                ))

                                                                           )
                                                                .apply(head.getValue());

                                             }
                                            );


    }


    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static <T> BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>> reduceArr_(final BiFunction<JsPair, Optional<T>, Optional<T>> accumulator,
                                                                                    final JsPath startingPath,
                                                                                    final boolean isRecursive
                                                                                   )
    {

        return (arr, acc) -> arr.ifEmptyElse(done(acc),
                                             (head, tail) ->
                                             {
                                                 final JsPath headPath = startingPath.inc();

                                                 return MatchFns.ifJsonElse(json -> isRecursive ? more(() -> reduceJson_(accumulator,
                                                                                                                         headPath,
                                                                                                                         isRecursive
                                                                                                                        ).apply(json,
                                                                                                                                acc
                                                                                                                               )
                                                                                                      ).flatMap(headAcc -> reduceArr_(accumulator,
                                                                                                                                      headPath,
                                                                                                                                      isRecursive
                                                                                                                                     ).apply(tail,
                                                                                                                                             headAcc
                                                                                                                                            )) : more(() -> reduceArr_(accumulator,
                                                                                                                                                                       headPath,
                                                                                                                                                                       isRecursive
                                                                                                                                                                      ).apply(tail,
                                                                                                                                                                              acc
                                                                                                                                                                             )
                                                                                                                                                     ),
                                                                            elem -> more(() -> reduceArr_(accumulator,
                                                                                                          headPath,
                                                                                                          isRecursive
                                                                                                         ).apply(tail,
                                                                                                                 accumulator.apply(JsPair.of(headPath,
                                                                                                                                             elem
                                                                                                                                            ),
                                                                                                                                   acc
                                                                                                                                  )
                                                                                                                ))

                                                                           )
                                                                .apply(head);
                                             }
                                            );
    }
}
