package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

abstract class OpMapKeys<T>
{

    T json;

    OpMapKeys(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> map(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate,
                               final JsPath startingPath
                              );
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    abstract Trampoline<T> map_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate,
                                final JsPath startingPath
                               );
}
