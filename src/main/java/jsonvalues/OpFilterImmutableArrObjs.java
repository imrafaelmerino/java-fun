package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

class OpFilterImmutableArrObjs extends OpFilterObjs<JsArray>
{


    OpFilterImmutableArrObjs(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> filter(final JsPath startingPath,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate

                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterImmutableArrObjs(tail).filter(headPath,
                                                                                                                                         predicate
                                                                                                                                        ));
                                    return ifObjElse(headJson -> JsPair.of(headPath,
                                                                           headJson
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   headJson
                                                                                                  ),
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headJson)),
                                                                               p -> tailCall
                                                                              )
                                    ,
                                                     headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                    )
                                    .apply(head);
                                }

                               );
    }

    @Override
    Trampoline<JsArray> filter_(final JsPath startingPath,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate

                               )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterImmutableArrObjs(tail).filter_(headPath,
                                                                                                                                          predicate
                                                                                                                                         ));
                                    return ifJsonElse(headObj -> JsPair.of(headPath,
                                                                           headObj
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   headObj
                                                                                                  ),
                                                                               p -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableObjObjs(headObj).filter_(headPath,
                                                                                                                                                                             predicate
                                                                                                                                                                            )
                                                                                                                                                                    .map(tailResult::prepend)),
                                                                               p -> tailCall

                                                                              ),
                                                      headArr -> more(() -> tailCall).flatMap(json -> new OpFilterImmutableArrObjs(headArr).filter_(headPath.index(-1),
                                                                                                                                                    predicate
                                                                                                                                                   )
                                                                                                                                           .map(json::prepend)),
                                                      headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                     )
                                    .apply(head);
                                }

                               );
    }


}