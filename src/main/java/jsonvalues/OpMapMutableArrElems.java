package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

final class OpMapMutableArrElems extends OpMapElems<JsArray>
{
    OpMapMutableArrElems(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, ? extends JsElem> fn,
                            final Predicate<? super JsPair> predicate,
                            final JsPath startingPath
                           )
    {
        return map(json,
                   json,
                   fn,
                   predicate,
                   startingPath
                  );

    }

    private Trampoline<JsArray> map(final JsArray acc,
                                    final JsArray remaining,
                                    final Function<? super JsPair, ? extends JsElem> fn,
                                    final Predicate<? super JsPair> predicate,
                                    final JsPath path
                                   )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> map(acc,
                                                                                             tail,
                                                                                             fn,
                                                                                             predicate,
                                                                                             headPath
                                                                                            ));
                                         return ifJsonElse(elem -> more(() -> tailCall).map(it -> it.put(new JsPath(headPath.last()),
                                                                                                         elem
                                                                                                        )),
                                                           elem -> JsPair.of(headPath,
                                                                             elem
                                                                            )
                                                                         .ifElse(predicate,
                                                                                 p -> more(() -> tailCall).map(tailResult -> tailResult.put(new JsPath(headPath.last()),
                                                                                                                                            fn.apply(p)
                                                                                                                                           )),
                                                                                 p -> more(() -> tailCall).map(tailResult -> tailResult.put(new JsPath(headPath.last()),
                                                                                                                                            elem
                                                                                                                                           ))
                                                                                )
                                                          ).apply(head);


                                     }
                                    );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsArray> map_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate,
                             final JsPath startingPath
                            )
    {
        return map_(json,
                    json,
                    fn,
                    predicate,
                    startingPath
                   );
    }
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    private Trampoline<JsArray> map_(final JsArray acc,
                                     final JsArray remaining,
                                     final Function<? super JsPair, ? extends JsElem> fn,
                                     final Predicate<? super JsPair> predicate,
                                     final JsPath startingPath
                                    )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> map_(acc,
                                                                                              tail,
                                                                                              fn,
                                                                                              predicate,
                                                                                              headPath
                                                                                             )
                                                                                  );
                                         return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpMapMutableObjElems(headObj).map_(fn,
                                                                                                                                                        predicate,
                                                                                                                                                        headPath
                                                                                                                                                       )
                                                                                                                                                  .map(headMapped ->
                                                                                                                                                       tailResult.put(new JsPath(headPath.last()),
                                                                                                                                                                      headMapped
                                                                                                                                                                     ))),
                                                           headArr -> more(() -> tailCall).flatMap(tailResult -> map_(headArr,
                                                                                                                      headArr,
                                                                                                                      fn,
                                                                                                                      predicate,
                                                                                                                      headPath.index(-1)
                                                                                                                     ).map(headMapped ->
                                                                                                                           tailResult.put(new JsPath(headPath.last()),
                                                                                                                                          headMapped
                                                                                                                                         ))),
                                                           elem ->
                                                           {
                                                               final JsElem headMapped = JsPair.of(headPath,
                                                                                                   elem
                                                                                                  )
                                                                                               .ifElse(predicate,
                                                                                                       fn::apply,
                                                                                                       p -> elem
                                                                                                      );
                                                               return more(() -> tailCall).map(tailResult -> tailResult.put(new JsPath(headPath.last()),
                                                                                                                            headMapped
                                                                                                                           ));
                                                           }
                                                          )
                                         .apply(head);


                                     }
                                    );
    }
}
