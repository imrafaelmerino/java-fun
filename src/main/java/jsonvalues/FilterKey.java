package jsonvalues;

import java.util.function.Predicate;

 abstract class FilterKey<T>
{

    final T json;

     FilterKey(final T json
             )
    {
        this.json = json;
    }


     Trampoline<? extends Json<?>> filterJson_(final Json<?> json,
                                              final JsPath startingPath,
                                              final Predicate<? super JsPair> predicate
                                             )
    {

        return json.isObj() ? new ObjFilterKey(json.asJsObj()).filter_(startingPath,
                                                                       predicate
                                                                      ) : new ArrFilterKey(json.asJsArray()).filter_(startingPath.index(-1),
                                                                                                                     predicate
                                                                                                                    );

    }

     abstract Trampoline<T> filter_(final JsPath startingPath,
                                   final Predicate<? super JsPair> predicate
                                  );

     abstract Trampoline<T> filter(final Predicate<? super JsPair> predicate);

     abstract T _filter_(final Predicate<? super JsPair> predicate);

     abstract T _filter__(final JsPath startingPath,
                         final Predicate<? super JsPair> predicate
                        );

}
