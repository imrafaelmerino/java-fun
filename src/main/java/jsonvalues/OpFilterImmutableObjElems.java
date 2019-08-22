package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpFilterImmutableObjElems extends OpFilterElems<JsObj>
{
    OpFilterImmutableObjElems(final JsObj a)
    {
        super(a
             );
    }
    @Override
    Trampoline<JsObj> filter_(final JsPath startingPath,
                              final Predicate<? super JsPair> predicate
                             )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterImmutableObjElems(tail).filter_(startingPath,
                                                                                                                                         predicate
                                                                                                                                        )
                                                                                      );
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableObjElems(headObj).filter_(headPath,
                                                                                                                                                           predicate
                                                                                                                                                          )
                                                                                                                                                  .map(headFiltered ->
                                                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                                                      headFiltered
                                                                                                                                                                     )
                                                                                                                                                      )
                                                                                             ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableArrElems(headArr).filter_(headPath.index(-1),
                                                                                                                                                           predicate
                                                                                                                                                          )
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
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                           headElem
                                                                                                                                          )
                                                                                                             ),
                                                                                p -> tailCall
                                                                               )

                                                     )
                                    .apply(head.getValue());

                                }
                               );
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterImmutableObjElems(tail).filter(startingPath,
                                                                                                                                        predicate
                                                                                                                                       ));
                                    return ifJsonElse(headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                        headElem
                                                                                                                       )),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                           headElem
                                                                                                                                          )),
                                                                                p -> tailCall
                                                                               )
                                                     )
                                    .apply(head.getValue());

                                }
                               );
    }
}