package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Trampoline.more;

class OpMapImmutableObjKeys extends OpMapKeys<JsObj>
{
    OpMapImmutableObjKeys(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, String> fn,
                          final Predicate<? super JsPair> predicate,
                          final JsPath startingPath
                         )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapImmutableObjKeys(tail).map(fn,
                                                                                                                                 predicate,
                                                                                                                                 startingPath
                                                                                                                                )
                                                                                      );
                                    return Trampoline.more(() -> tailCall)
                                                     .map(tailResult ->
                                                          {
                                                              final String keyMapped = JsPair.of(headPath,
                                                                                                 head.getValue()
                                                                                                )
                                                                                             .ifElse(predicate,
                                                                                                     fn,
                                                                                                     p -> head.getKey()
                                                                                                    );
                                                              return tailResult.put(keyMapped,
                                                                                    head.getValue()
                                                                                   );
                                                          });
                                }
                               );
    }

    @Override
    Trampoline<JsObj> map_(final Function<? super JsPair, String> fn,
                           final Predicate<? super JsPair> predicate,
                           final JsPath startingPath
                          )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapImmutableObjKeys(tail).map_(fn,
                                                                                                                                  predicate,
                                                                                                                                  startingPath
                                                                                                                                 )
                                                                                      );
                                    JsPair pair = JsPair.of(headPath,
                                                            head.getValue()
                                                           );

                                    return pair.ifElse(predicate,
                                                       headTrue -> headTrue.ifJsonElse((path, headObj) ->
                                                                                       more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableObjKeys(headObj).map_(fn,
                                                                                                                                                                          predicate,
                                                                                                                                                                          headPath
                                                                                                                                                                         )
                                                                                                                                                                    .map(headObjResult ->
                                                                                                                                                                         tailResult.put(fn.apply(pair),
                                                                                                                                                                                        headObjResult
                                                                                                                                                                                       )
                                                                                                                                                                        )
                                                                                                                   ),
                                                                                       (path, headArr) ->
                                                                                       more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableArrKeys(headArr).map_(fn,
                                                                                                                                                                          predicate,
                                                                                                                                                                          headPath.index(-1)
                                                                                                                                                                         )
                                                                                                                                                                    .map(headArrResult ->
                                                                                                                                                                         tailResult.put(fn.apply(pair),
                                                                                                                                                                                        headArrResult
                                                                                                                                                                                       )
                                                                                                                                                                        )
                                                                                                                   ),
                                                                                       (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.put(fn.apply(pair),
                                                                                                                                                                 headElem
                                                                                                                                                                ))
                                                                                      ),
                                                       headFalse -> headFalse.ifJsonElse((path, headObj) ->
                                                                                         more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableObjKeys(headObj).map_(fn,
                                                                                                                                                                            predicate,
                                                                                                                                                                            headPath
                                                                                                                                                                           )
                                                                                                                                                                      .map(headObjResult ->
                                                                                                                                                                           tailResult.put(head.getKey(),
                                                                                                                                                                                          headObjResult
                                                                                                                                                                                         )
                                                                                                                                                                          )
                                                                                                                     ),
                                                                                         (path, headArr) ->
                                                                                         more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableArrKeys(headArr).map_(fn,
                                                                                                                                                                            predicate,
                                                                                                                                                                            headPath.index(-1)
                                                                                                                                                                           )
                                                                                                                                                                      .map(headArrResult ->
                                                                                                                                                                           tailResult.put(head.getKey(),
                                                                                                                                                                                          headArrResult
                                                                                                                                                                                         )
                                                                                                                                                                          )
                                                                                                                     ),
                                                                                         (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                                   headElem
                                                                                                                                                                  ))
                                                                                        )
                                                      );
                                }
                               );
    }
}
