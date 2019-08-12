package jsonvalues;

import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.Trampoline;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

abstract class OpMapObjs<T>
{

    T json;

    OpMapObjs(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> map(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate,
                               final JsPath startingPath
                              );

    abstract Trampoline<T> map_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                final JsPath startingPath
                               );
}
