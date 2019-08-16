package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpMapMutableArrKeys extends OpMapKeys<JsArray>
{
    OpMapMutableArrKeys(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, String> fn,
                            final Predicate<? super JsPair> predicate,
                            final JsPath startingPath
                           )
    {
        throw new UnsupportedOperationException("map keys of array");

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsArray> map_(final Function<? super JsPair, String> fn,
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
                                     final Function<? super JsPair, String> fn,
                                     final Predicate<? super JsPair> predicate,
                                     final JsPath startingPath
                                    )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {

                                         final JsPath headPath = startingPath.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> map_(acc,
                                                                                              remaining.tail(),
                                                                                              fn,
                                                                                              predicate,
                                                                                              headPath
                                                                                             ));

                                         return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> new OpMapMutableObjKeys(headJson).map_(fn,
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
                                                           headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(new JsPath(headPath.last()),
                                                                                                                             headElem
                                                                                                                            ))
                                                          )
                                         .apply(head);

                                     }
                                    );
    }
}
