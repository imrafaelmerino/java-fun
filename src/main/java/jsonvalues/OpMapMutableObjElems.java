package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpMapMutableObjElems extends OpMapElems<JsObj>
{
    OpMapMutableObjElems(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, ? extends JsElem> fn,
                          final Predicate<? super JsPair> predicate,
                          final JsPath path
                         )
    {
        return map(json.asJsObj(),
                   json.asJsObj(),
                   fn,
                   predicate,
                   path
                  );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> map_(final Function<? super JsPair, ? extends JsElem> fn,
                           final Predicate<? super JsPair> predicate,
                           final JsPath path
                          )
    {

        return map_(json.asJsObj(),
                    json.asJsObj(),
                    fn,
                    predicate,
                    path
                   );
    }

    private Trampoline<JsObj> map(final JsObj acc,
                                  final JsObj remaining,
                                  final Function<? super JsPair, ? extends JsElem> fn,
                                  final Predicate<? super JsPair> predicate,
                                  final JsPath path
                                 )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = path.key(head.getKey());

                                         final Trampoline<JsObj> tailCall = more(() -> map(acc,
                                                                                           tail,
                                                                                           fn,
                                                                                           predicate,
                                                                                           path
                                                                                          ));

                                         return ifJsonElse(elem -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                                                                                         elem
                                                                                                        )),
                                                           elem -> JsPair.of(headPath,
                                                                             elem
                                                                            )
                                                                         .ifElse(predicate,
                                                                                 p -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                                                                                                            fn.apply(p)
                                                                                                                           )),
                                                                                 p -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                                                                                                            elem
                                                                                                                           ))
                                                                                )

                                                          ).apply(head.getValue());

                                     }
                                    );
    }
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    private Trampoline<JsObj> map_(final JsObj acc,
                                   final JsObj remaining,
                                   final Function<? super JsPair, ? extends JsElem> fn,
                                   final Predicate<? super JsPair> predicate,
                                   final JsPath startingPath
                                  )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.key(head.getKey());
                                         final Trampoline<JsObj> tailCall = more(() -> map_(acc,
                                                                                            tail,
                                                                                            fn,
                                                                                            predicate,
                                                                                            startingPath
                                                                                           ));
                                         return ifJsonElse(headObj ->
                                                           more(() -> tailCall).flatMap(tailResult -> map_(headObj,
                                                                                                           headObj,
                                                                                                           fn,
                                                                                                           predicate,
                                                                                                           headPath
                                                                                                          )
                                                                                        .map(headMapped -> tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                          headMapped
                                                                                                                         )
                                                                                            )
                                                                                       ),
                                                           headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapMutableArrElems(headArr).map_(fn,
                                                                                                                                                        predicate,
                                                                                                                                                        headPath.index(-1)
                                                                                                                                                       )
                                                                                                                                                  .map(headMapped -> tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                                                                    headMapped
                                                                                                                                                                                   )
                                                                                                                                                      )
                                                                                                  ),
                                                           headElem -> more(() -> tailCall).map(tailResult ->
                                                                                                {
                                                                                                    final JsElem headMapped = JsPair.of(headPath,
                                                                                                                                        headElem
                                                                                                                                       )
                                                                                                                                    .ifElse(predicate,
                                                                                                                                            fn::apply,
                                                                                                                                            p -> headElem
                                                                                                                                           );
                                                                                                    return tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                          headMapped
                                                                                                                         );
                                                                                                })
                                                          ).apply(head.getValue());
                                     }
                                    );
    }
}
