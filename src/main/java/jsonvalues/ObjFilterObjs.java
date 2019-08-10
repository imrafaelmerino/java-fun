package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.MatchFns.ifObjElse;
import static jsonvalues.Trampoline.more;

public class ObjFilterObjs extends FilterObj<JsObj>
{


    public ObjFilterObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    public Trampoline<JsObj> filter(final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate

                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new ObjFilterObjs(tail).filter(startingPath,
                                                                                                                            predicate
                                                                                                                           ));
                                    return ifObjElse(json -> JsPair.of(headPath,
                                                                       json
                                                                      )
                                                                   .ifElse(p -> predicate.test(p.path,
                                                                                               json
                                                                                              ),
                                                                           p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                      json
                                                                                                                                     )),
                                                                           p -> tailCall
                                                                          ),
                                                     value -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                    value
                                                                                                                   ))
                                                    )
                                    .apply(head.getValue());
                                }

                               );
    }

    @Override
    public Trampoline<JsObj> filter_(final JsPath startingPath,
                              final BiPredicate<? super JsPath, ? super JsObj> predicate
                             )
    {


        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new ObjFilterObjs(tail).filter_(startingPath,
                                                                                                                             predicate
                                                                                                                            ));
                                    return ifJsonElse(headJson -> JsPair.of(headPath,
                                                                            headJson
                                                                           )
                                                                        .ifElse(p -> predicate.test(p.path,
                                                                                                    headJson
                                                                                                   ),
                                                                                p -> more(() -> tailCall).flatMap(tailResult -> filterJson_(headJson,
                                                                                                                                            startingPath,
                                                                                                                                            predicate
                                                                                                                                           )
                                                                                                                  .map(headFiltered ->
                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                      headFiltered
                                                                                                                                     )
                                                                                                                      )
                                                                                                                 ),
                                                                                p -> tailCall
                                                                               ),
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new ArrFilterObjs(headArray).filter_(headPath.index(-1),
                                                                                                                                                   predicate
                                                                                                                                                  )
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


}
