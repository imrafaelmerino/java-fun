package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.Trampoline.more;

 class ArrFilterKey extends FilterKey<JsArray>
{


     ArrFilterKey(final JsArray json)
    {
        super(json);
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
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new ArrFilterKey(tail).filter_(headPath,
                                                                                                                              predicate
                                                                                                                             ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJson_(headJson,
                                                                                                                         headPath,
                                                                                                                         predicate
                                                                                                                        ).map(tailResult::prepend)),
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

    @Override
     JsArray _filter_(final Predicate<? super JsPair> predicate
                    )
    {
        return json;
    }

    @Override
     JsArray _filter__(JsPath startingPath,
                      final Predicate<? super JsPair> predicate
                     )
    {
        for (final JsElem elem : json)
        {
            startingPath = startingPath.inc();
            final JsPair pair = JsPair.of(startingPath,
                                          elem
                                         );
            if (pair.elem.isArray())
                new ArrFilterKey(pair.elem.asJsArray())._filter__(startingPath.index(-1),
                                                                  predicate
                                                                 );
            else if (pair.elem.isObj())
                new ObjFilterKey(pair.elem.asJsObj())._filter__(startingPath,
                                                                predicate
                                                               );

        }
        return json;
    }

    ;

}
