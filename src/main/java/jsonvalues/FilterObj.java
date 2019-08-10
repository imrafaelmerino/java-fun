package jsonvalues;

import java.util.function.BiPredicate;

 abstract class FilterObj<T>
{

    T json;

    FilterObj(final T json)
    {
        this.json = json;
    }

     abstract Trampoline<T> filter(final JsPath startingPath,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 );

     abstract Trampoline<T> filter_(final JsPath startingPath,
                                   final BiPredicate<? super JsPath, ? super JsObj> predicate
                                  );


     Trampoline<? extends Json<?>> filterJson_(final Json<?> json,
                                              final JsPath startingPath,
                                              final BiPredicate<? super JsPath, ? super JsObj> predicate
                                             )
    {

        return json.isObj() ? new ObjFilterObjs(json.asJsObj()).filter_(startingPath,
                                                                        predicate
                                                                       ) : new ArrFilterObjs(json.asJsArray()).filter_(startingPath.index(-1),
                                                                                                                       predicate
                                                                                                                      );

    }


}
