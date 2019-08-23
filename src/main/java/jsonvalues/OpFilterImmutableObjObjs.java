package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

class OpFilterImmutableObjObjs extends OpFilterObjs<JsObj>
{


    OpFilterImmutableObjObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate

                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterImmutableObjObjs(tail).filter(startingPath,
                                                                                                                                       predicate
                                                                                                                                      ));
                                    return ifObjElse(json -> JsPair.of(headPath,
                                                                       json
                                                                      )
                                                                   .ifElse(p -> predicate.test(p.path,
                                                                                               json
                                                                                              ),
                                                                           p -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                      json
                                                                                                                                     )),
                                                                           p -> tailCall
                                                                          ),
                                                     value -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                    value
                                                                                                                   ))
                                                    )
                                    .apply(head.getValue());
                                }

                               );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> filter_(final JsPath startingPath,
                              final BiPredicate<? super JsPath, ? super JsObj> predicate
                             )
    {


        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterImmutableObjObjs(tail).filter_(startingPath,
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
                                                                                                                                                                    .map(headFiltered ->
                                                                                                                                                                         tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                                                                        headFiltered
                                                                                                                                                                                       )
                                                                                                                                                                        )
                                                                                                                ),
                                                                               p -> tailCall
                                                                              ),
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableArrObjs(headArray).filter_(headPath.index(-1),
                                                                                                                                                              predicate
                                                                                                                                                             )
                                                                                                                                                     .map(headFiltered ->
                                                                                                                                                          tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                                                         headFiltered
                                                                                                                                                                        )
                                                                                                                                                         )
                                                                                               ),
                                                      headElem -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                                                                                        headElem
                                                                                                       ))
                                                     )
                                    .apply(head.getValue());
                                }
                               );

    }


}
