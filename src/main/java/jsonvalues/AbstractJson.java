package jsonvalues;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;

abstract class AbstractJson
{

    protected <T> Optional<T> reduceElem(final JsPair p,
                                         final BinaryOperator<T> op,
                                         final Function<? super JsPair, T> fn,
                                         final Optional<T> result
                                        )
    {
        final T mapped = fn.apply(p);

        final Optional<T> t = result.map(it -> op.apply(it,
                                                        mapped
                                                       ));
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }

    @SuppressWarnings({"ReturnValueIgnored", "squid:S00100"})//  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    Json<?> filterObjs_(final Json<?> json,
                        final BiPredicate<? super JsPath, ? super JsObj> predicate,
                        final JsPath path
                       )
    {
        return Functions.ifObjElse(it -> filterObjs_(it,
                                                     predicate,
                                                     path
                                                    ),
                                   it -> filterObjs_(it.asJsArray(),
                                                     predicate,
                                                     path.index(-1)

                                                    )
                                  )
                        .apply(json);
    }

    abstract JsObj filterObjs_(final JsObj json,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate,
                               final JsPath path
                              );

    abstract JsArray filterObjs_(final JsArray json,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                 final JsPath path
                                );
}
