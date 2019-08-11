package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

class OpMapImmutableArrElems extends OpMapElems<JsArray>
{
    OpMapImmutableArrElems(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, ? extends JsElem> fn,
                            final Predicate<? super JsPair> predicate,
                            final JsPath startingPath
                           )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapImmutableArrElems(tail).map(fn,
                                                                                                                                    predicate,
                                                                                                                                    headPath
                                                                                                                                   ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).map(it -> it.prepend(headJson)),
                                                      headElem ->
                                                      {
                                                          JsElem headMapped = JsPair.of(headPath,
                                                                                        headElem
                                                                                       )
                                                                                    .ifElse(predicate,
                                                                                            fn::apply,
                                                                                            p -> p.elem
                                                                                           );
                                                          return more(() -> tailCall).map(tailResult -> tailResult.prepend(headMapped));
                                                      }
                                                     ).apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> map_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate,
                             final JsPath startingPath
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapImmutableArrElems(tail).map_(fn,
                                                                                                                                     predicate,
                                                                                                                                     headPath
                                                                                                                                    ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableObjElems(headObj).map_(fn,
                                                                                                                                                     predicate,
                                                                                                                                                     headPath
                                                                                                                                                    )
                                                                                                                                               .map(tailResult::prepend)
                                                                                             ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableArrElems(headArr).map_(fn,
                                                                                                                                                     predicate,
                                                                                                                                                     headPath.index(-1)
                                                                                                                                                    )
                                                                                                                                               .map(tailResult::prepend)
                                                                                             ),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(p))),
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                                               )
                                                     )
                                    .apply(head);


                                }
                               );
    }
}

