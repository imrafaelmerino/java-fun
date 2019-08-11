package jsonvalues;

import jsonvalues.JsPair;
import jsonvalues.JsPath;
import jsonvalues.Trampoline;

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

    abstract Trampoline<T> map_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate,
                                final JsPath startingPath
                               );
}
