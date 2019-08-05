package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Functions.ifJsonElse;
import static jsonvalues.Trampoline.more;

class FilterFunctions
{
    @SuppressWarnings("squid:S00100")
    static Function<JsObj, Trampoline<JsObj>> filterElems_(final Predicate<? super JsPair> predicate,
                                                           final JsPath path
                                                          )
    {
        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterElems_(predicate,
                                                                                                                path
                                                                                                               ).apply(tail)
                                                                                            );
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonElems(predicate,
                                                                                                                                   headPath
                                                                                                                                  ).apply(headJson)
                                                                                                                                   .map(headFiltered ->
                                                                                                                                        tailResult.put(head.getKey(),
                                                                                                                                                       headFiltered
                                                                                                                                                      )
                                                                                                                                       )
                                                                                                    ),
                                                            headElem -> JsPair.of(headPath,
                                                                                  headElem
                                                                                 )
                                                                              .ifElse(predicate,
                                                                                      () -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                  headElem
                                                                                                                                                 )),
                                                                                      () -> tailCall
                                                                                     )

                                                           )
                                          .apply(head.getValue());

                                      }
                                     );

    }

    //squid:S00100_ naming convention: xx_ traverses the whole json
    @SuppressWarnings("squid:S00100")
    static Function<JsArray, Trampoline<JsArray>> filterArrElems_(final Predicate<? super JsPair> predicate,
                                                                  final JsPath path
                                                                 )
    {


        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {

                                          final JsPath headPath = path.inc();

                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrElems_(predicate,
                                                                                                                     headPath
                                                                                                                    ).apply(tail));
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonElems(predicate,
                                                                                                                                   headPath
                                                                                                                                  ).apply(headJson)
                                                                                                                                   .map(tailResult::prepend)),
                                                            headElem -> JsPair.of(headPath,
                                                                                  headElem
                                                                                 )
                                                                              .ifElse(predicate,
                                                                                      () -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem)),
                                                                                      () -> tailCall
                                                                                     )
                                                           )
                                          .apply(head);
                                      }
                                     );


    }

    private static Function<Json<?>, Trampoline<? extends Json<?>>> filterJsonElems(final Predicate<? super JsPair> predicate,
                                                                                    final JsPath startingPath
                                                                                   )
    {

        return json -> json.isObj() ? filterElems_(predicate,
                                                   startingPath
                                                  ).apply(json.asJsObj()) : filterArrElems_(predicate,
                                                                                            startingPath.index(-1)
                                                                                           ).apply(json.asJsArray());


    }

}
