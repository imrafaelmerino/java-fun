package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.Trampoline.more;

 class ArrFilterElem extends FilterElem<JsArray>
{


    ArrFilterElem(final JsArray a)
    {
        super(a);
    }

    @Override
     Trampoline<JsArray> filter(final JsPath startingPath,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {

                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new ArrFilterElem(tail).filter(headPath,
                                                                                                                              predicate
                                                                                                                             ));
                                    return ifJsonElse(elem -> more(() -> tailCall).map(it -> it.prepend(elem)),
                                                      elem -> JsPair.of(headPath,
                                                                        elem
                                                                       )
                                                                    .ifElse(predicate,
                                                                            p -> more(() -> tailCall).map(it -> it.prepend(elem)),
                                                                            p -> tailCall
                                                                           )
                                                     )
                                    .apply(head);
                                }
                               );
    }


    @Override
     Trampoline<JsArray> filter_(final JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {

                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new ArrFilterElem(tail).filter_(headPath,
                                                                                                                               predicate
                                                                                                                              ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJson_(headJson,
                                                                                                                         headPath,
                                                                                                                         predicate
                                                                                                                        ).map(tailResult::prepend)),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem)),
                                                                                p -> tailCall
                                                                               )
                                                     )
                                    .apply(head);
                                }
                               );
    }
}
