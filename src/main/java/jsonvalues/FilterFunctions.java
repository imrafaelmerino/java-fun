package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Functions.ifJsonElse;
import static jsonvalues.Trampoline.done;
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

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsObj, Trampoline<JsObj>> filterJsObjs_(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                            final JsPath path
                                                           )
    {


        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterJsObjs_(predicate,
                                                                                                                 path
                                                                                                                ).apply(tail));
                                          return ifJsonElse(headJson -> JsPair.of(headPath,
                                                                                  headJson
                                                                                 )
                                                                              .ifElse(p -> predicate.test(p.path,
                                                                                                          headJson
                                                                                                         ),
                                                                                      p -> more(() -> tailCall).flatMap(tailResult -> filterJsObjs_(predicate,
                                                                                                                                                    headPath
                                                                                                                                                   ).apply(headJson)
                                                                                                                                                    .map(headFiltered ->
                                                                                                                                                         tailResult.put(head.getKey(),
                                                                                                                                                                        headFiltered
                                                                                                                                                                       )
                                                                                                                                                        )
                                                                                                                       ),
                                                                                      p -> tailCall
                                                                                     ),
                                                            headArray -> more(() -> tailCall).flatMap(tailResult -> filterArrJsObjs_(predicate,
                                                                                                                                     headPath.index(-1)
                                                                                                                                    ).apply(headArray)
                                                                                                                                     .map(headFiltered ->
                                                                                                                                          tailResult.put(head.getKey(),
                                                                                                                                                         headFiltered
                                                                                                                                                        )
                                                                                                                                         )
                                                                                                     ),
                                                            headElem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                              headElem
                                                                                                             ))
                                                           )
                                          .apply(head.getValue());
                                      }

                                     );

    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> filterArrJsObjs_(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                                   final JsPath path
                                                                  )
    {


        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.inc();

                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrJsObjs_(predicate,
                                                                                                                      headPath
                                                                                                                     ).apply(tail));
                                          return ifJsonElse(json -> JsPair.of(headPath,
                                                                              json
                                                                             )
                                                                          .ifElse(p -> predicate.test(p.path,
                                                                                                      json
                                                                                                     ),
                                                                                  p -> more(() -> tailCall).flatMap(tailResult -> filterJsObjs_(predicate,
                                                                                                                                                headPath
                                                                                                                                               ).apply(json)
                                                                                                                                                .map(tailResult::prepend)),
                                                                                  p -> tailCall

                                                                                 ),
                                                            array -> more(() -> tailCall).flatMap(json -> filterArrJsObjs_(predicate,
                                                                                                                           headPath.index(-1)
                                                                                                                          ).apply(array)
                                                                                                                           .map(json::prepend)),
                                                            value -> more(() -> tailCall).map(it -> it.prepend(value))
                                                           )
                                          .apply(head);
                                      }

                                     );


    }


    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsObj, Trampoline<JsObj>> filterKeys_(final Predicate<? super JsPair> predicate,
                                                          final JsPath path
                                                         )
    {
        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.key(head.getKey());
                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterKeys_(predicate,
                                                                                                               path
                                                                                                              ).apply(tail));
                                          return JsPair.of(headPath,
                                                           head.getValue()
                                                          )
                                                       .ifElse(predicate,
                                                               () -> ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonKeys_(predicate,
                                                                                                                                                       headPath
                                                                                                                                                      ).apply(headJson)
                                                                                                                                                       .map(headMapped ->
                                                                                                                                                            tailResult.put(head.getKey(),
                                                                                                                                                                           headMapped
                                                                                                                                                                          )
                                                                                                                                                           )
                                                                                                                        ),
                                                                                headElem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                                  headElem
                                                                                                                                 ))

                                                                               )
                                                               .apply(head.getValue()),
                                                               () -> tailCall
                                                              );
                                      }
                                     );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> filterArrKeys_(final Predicate<? super JsPair> predicate,
                                                                 final JsPath path
                                                                )
    {


        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.inc();
                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrKeys_(predicate,
                                                                                                                    headPath
                                                                                                                   ).apply(tail));
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonKeys_(predicate,
                                                                                                                                   headPath
                                                                                                                                  ).apply(headJson)
                                                                                                                                   .map(tailResult::prepend)),
                                                            headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                           )
                                          .apply(head);
                                      }
                                     );


    }

    private static Function<Json<?>, Trampoline<? extends Json<?>>> filterJsonKeys_(final Predicate<? super JsPair> predicate,
                                                                                    final JsPath startingPath
                                                                                   )
    {

        return json -> json.isObj() ? filterKeys_(predicate,
                                                  startingPath
                                                 ).apply(json.asJsObj()) : filterArrKeys_(predicate,
                                                                                          startingPath.index(-1)
                                                                                         ).apply(json.asJsArray());


    }




}
