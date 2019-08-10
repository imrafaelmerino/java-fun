package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.MatchFns.ifObjElse;
import static jsonvalues.Trampoline.more;

class ArrFilterObjs extends FilterObj<JsArray>
{


    ArrFilterObjs(final JsArray json)
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

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new ArrFilterObjs(tail).filter(headPath,
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

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new ArrFilterObjs(tail).filter_(headPath,
                                                                                                                               predicate
                                                                                                                              ));
                                    return ifJsonElse(json -> JsPair.of(headPath,
                                                                        json
                                                                       )
                                                                    .ifElse(p -> predicate.test(p.path,
                                                                                                json
                                                                                               ),
                                                                            p -> more(() -> tailCall).flatMap(tailResult -> new ObjFilterObjs(json).filter_(headPath,
                                                                                                                                                            predicate
                                                                                                                                                           )
                                                                                                                                                   .map(tailResult::prepend)),
                                                                            p -> tailCall

                                                                           ),
                                                      array -> more(() -> tailCall).flatMap(json -> new ArrFilterObjs(array).filter_(headPath.index(-1),
                                                                                                                                     predicate
                                                                                                                                    )
                                                                                                                            .map(json::prepend)),
                                                      value -> more(() -> tailCall).map(it -> it.prepend(value))
                                                     )
                                    .apply(head);
                                }

                               );
    }
}