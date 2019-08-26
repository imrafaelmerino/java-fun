package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapImmutableArrKeys extends OpMapKeys<JsArray>
{
    OpMapImmutableArrKeys(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, String> fn,
                            final Predicate<? super JsPair> predicate,
                            final JsPath startingPath
                           )
    {
        throw InternalError.opNotSupportedForArrays();
    }

    @Override
    Trampoline<JsArray> map_(final Function<? super JsPair, String> fn,
                             final Predicate<? super JsPair> predicate,
                             final JsPath startingPath
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapImmutableArrKeys(tail).map_(fn,
                                                                                                                                    predicate,
                                                                                                                                    headPath
                                                                                                                                   ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableObjKeys(headObj).map_(fn,
                                                                                                                                                    predicate,
                                                                                                                                                    headPath
                                                                                                                                                   )
                                                                                                                                              .map(tailResult::prepend)),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapImmutableArrKeys(headArr).map_(fn,
                                                                                                                                                    predicate,
                                                                                                                                                    headPath.index(-1)
                                                                                                                                                   )
                                                                                                                                              .map(tailResult::prepend)),
                                                      headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                     )
                                    .apply(head);
                                }
                               );
    }
}
