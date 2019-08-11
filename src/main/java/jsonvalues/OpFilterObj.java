package jsonvalues;

import java.util.function.BiPredicate;

abstract class OpFilterObj<T>
{

    T json;

    OpFilterObj(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> filter(final JsPath startingPath,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 );

    abstract Trampoline<T> filter_(final JsPath startingPath,
                                   final BiPredicate<? super JsPath, ? super JsObj> predicate
                                  );

    abstract T _filter_(final JsPath startingPath,
                        final BiPredicate<? super JsPath, ? super JsObj> predicate
                       );

    abstract T _filter__(final JsPath startingPath,
                         final BiPredicate<? super JsPath, ? super JsObj> predicate
                        );


    Trampoline<? extends Json<?>> filterJson_(final Json<?> json,
                                              final JsPath startingPath,
                                              final BiPredicate<? super JsPath, ? super JsObj> predicate
                                             )
    {

        return json.isObj() ? new OpObjFilterObjs(json.asJsObj()).filter_(startingPath,
                                                                          predicate
                                                                         ) : new OpArrFilterObjs(json.asJsArray()).filter_(startingPath.index(-1),
                                                                                                                         predicate
                                                                                                                        );

    }


}
