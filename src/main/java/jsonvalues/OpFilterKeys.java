package jsonvalues;

import java.util.function.Predicate;

 abstract class OpFilterKeys<T>
{

    final T json;

     OpFilterKeys(final T json
                 )
    {
        this.json = json;
    }


     Trampoline<? extends Json<?>> filterJson_(final Json<?> json,
                                              final JsPath startingPath,
                                              final Predicate<? super JsPair> predicate
                                             )
    {

        return json.isObj() ? new OpFilterImmutableObjKeys(json.asJsObj()).filter_(startingPath,
                                                                                   predicate
                                                                                  ) : new OpFilterImmutableArrKeys(json.asJsArray()).filter_(startingPath.index(-1),
                                                                                                                                             predicate
                                                                                                                                            );

    }

     abstract Trampoline<T> filter_(final JsPath startingPath,
                                   final Predicate<? super JsPair> predicate
                                  );

     abstract Trampoline<T> filter(final Predicate<? super JsPair> predicate);


}
