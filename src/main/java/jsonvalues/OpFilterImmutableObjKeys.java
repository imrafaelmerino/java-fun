package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

class OpFilterImmutableObjKeys extends OpFilterKeys<JsObj>
{

    OpFilterImmutableObjKeys(final JsObj json)
    {
        super(json);
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> filter_(final JsPath startingPath,
                              final Predicate<? super JsPair> predicate
                             )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterImmutableObjKeys(tail).filter_(startingPath,
                                                                                                                                        predicate
                                                                                                                                       ));
                                    return JsPair.of(headPath,
                                                     head.getValue()
                                                    )
                                                 .ifElse(predicate,
                                                         p -> ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableObjKeys(headObj).filter_(headPath,
                                                                                                                                                                             predicate
                                                                                                                                                                            )
                                                                                                                                                                    .map(headMapped ->
                                                                                                                                                                         tailResult.put(head.getKey(),
                                                                                                                                                                                        headMapped
                                                                                                                                                                                       )
                                                                                                                                                                        )),
                                                                         headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableArrKeys(headArray).filter_(headPath.index(-1),
                                                                                                                                                                                 predicate
                                                                                                                                                                                )
                                                                                                                                                                        .map(headMapped ->
                                                                                                                                                                             tailResult.put(head.getKey(),
                                                                                                                                                                                            headMapped
                                                                                                                                                                                           )
                                                                                                                                                                            )),
                                                                         headElem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                           headElem
                                                                                                                          ))
                                                                        )
                                                         .apply(head.getValue()),
                                                         p -> tailCall
                                                        );
                                }
                               );
    }

    @Override
    Trampoline<JsObj> filter(final Predicate<? super JsPair> predicate
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = JsPath.empty()
                                                                  .key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterImmutableObjKeys(tail).filter(predicate));
                                    return JsPair.of(headPath,
                                                     head.getValue()
                                                    )
                                                 .ifElse(predicate,
                                                         p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                    head.getValue()
                                                                                                                   )),


                                                         p -> tailCall
                                                        );
                                }
                               );
    }


}
