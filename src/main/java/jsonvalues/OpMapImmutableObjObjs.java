package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

class OpMapImmutableObjObjs extends OpMapObjs<JsObj>
{
    OpMapImmutableObjObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                          final BiPredicate<? super JsPath, ? super JsObj> predicate,
                          final JsPath startingPath
                         )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapImmutableObjObjs(tail).map(fn,
                                                                                                                                 predicate,
                                                                                                                                 startingPath
                                                                                                                                ));
                                    return ifObjElse(headObj -> more(() -> tailCall).map(tailResult ->
                                                                                         {
                                                                                             final JsElem headMapped = JsPair.of(headPath,
                                                                                                                                 headObj
                                                                                                                                )
                                                                                                                             .ifElse(p -> predicate.test(p.path,
                                                                                                                                                         headObj
                                                                                                                                                        ),
                                                                                                                                     p -> fn.apply(p.path,
                                                                                                                                                   headObj
                                                                                                                                                  ),
                                                                                                                                     p -> p.elem
                                                                                                                                    );
                                                                                             return tailResult.put(head.getKey(),
                                                                                                                   headMapped
                                                                                                                  );
                                                                                         }),
                                                     headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                       headElem
                                                                                                                      ))
                                                    )
                                    .apply(head.getValue());
                                }
                               );
    }

    @Override
    Trampoline<JsObj> map_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                           final BiPredicate<? super JsPath, ? super JsObj> predicate,
                           final JsPath startingPath
                          )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapImmutableObjObjs(tail).map_(fn,
                                                                                                                                  predicate,
                                                                                                                                  startingPath
                                                                                                                                 )
                                                                                      );
                                    return ifJsonElse(headObj ->
                                                      {
                                                          JsObj headMapped = JsPair.of(headPath,
                                                                                       headObj
                                                                                      )
                                                                                   .ifElse(p -> predicate.test(headPath,
                                                                                                               headObj
                                                                                                              ),
                                                                                           p -> fn.apply(headPath,
                                                                                                         headObj
                                                                                                        ),
                                                                                           p -> headObj
                                                                                          );
                                                          return more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableObjObjs(headMapped).map_(fn,
                                                                                                                                                       predicate,
                                                                                                                                                       headPath
                                                                                                                                                      )
                                                                                                                                                 .map(headMappedResult ->
                                                                                                                                                      tailResult.put(head.getKey(),
                                                                                                                                                                     headMappedResult
                                                                                                                                                                    )
                                                                                                                                                     )
                                                                                             );
                                                      },
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableArrObjs(headArray).map_(fn,
                                                                                                                                                        predicate,
                                                                                                                                                        headPath.index(-1)
                                                                                                                                                       )
                                                                                                                                                  .map(headResult ->
                                                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                                                      headResult
                                                                                                                                                                     )
                                                                                                                                                      )
                                                                                               ),
                                                      headElement -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                           headElement
                                                                                                                          ))
                                                     )
                                    .apply(head.getValue());
                                }
                               );
    }
}
