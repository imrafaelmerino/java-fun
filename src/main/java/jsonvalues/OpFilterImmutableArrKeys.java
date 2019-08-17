package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

class OpFilterImmutableArrKeys extends OpFilterKeys<JsArray>
{


    OpFilterImmutableArrKeys(final JsArray json)
    {
        super(json);
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsArray> filter_(final JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterImmutableArrKeys(tail).filter_(headPath,
                                                                                                                                          predicate
                                                                                                                                         ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableObjKeys(headObj).filter_(headPath,
                                                                                                                                                            predicate
                                                                                                                                                           )
                                                                                                                                                   .map(tailResult::prepend)),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpFilterImmutableArrKeys(headArr).filter_(headPath.index(-1),
                                                                                                                                                          predicate
                                                                                                                                                         )),
                                                      headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                     )
                                    .apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> filter(final Predicate<? super JsPair> predicate
                              )
    {
        throw new UnsupportedOperationException("filter keys of array");
    }


}
