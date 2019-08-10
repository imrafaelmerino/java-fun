package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.Trampoline.more;

public class ObjFilterElem extends FilterElem<JsObj>
{


    ObjFilterElem(final JsObj a
                 )
    {
        super(a
             );
    }

    @Override
    public Trampoline<JsObj> filter_(final JsPath startingPath,
                              final Predicate<? super JsPair> predicate
                             )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new ObjFilterElem(tail).filter_(startingPath,
                                                                                                                             predicate
                                                                                                                            )
                                                                                      );
                                    return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJson_(headJson,
                                                                                                                         headPath,
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
                                                                                                                                          )),
                                                                                p -> tailCall
                                                                               )

                                                     )
                                    .apply(head.getValue());

                                }
                               );
    }

    @Override
    public Trampoline<JsObj> filter(final JsPath startingPath,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new ObjFilterElem(tail).filter(startingPath,
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
