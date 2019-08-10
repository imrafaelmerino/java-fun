package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.Trampoline.more;

public class ObjFilterKey extends FilterKey<JsObj>
{

    ObjFilterKey(final JsObj json)
    {
        super(json);
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
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new ObjFilterKey(tail).filter_(startingPath,
                                                                                                                            predicate
                                                                                                                           ));
                                    return JsPair.of(headPath,
                                                     head.getValue()
                                                    )
                                                 .ifElse(predicate,
                                                         p -> ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJson_(headJson,
                                                                                                                                            headPath,
                                                                                                                                            predicate

                                                                                                                                           )
                                                                                                                  .map(headMapped ->
                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                      headMapped
                                                                                                                                     )
                                                                                                                      )
                                                                                                                 ),
                                                                         headElem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                           headElem
                                                                                                                          ))

                                                                        ).apply(head.getValue()),
                                                         p -> tailCall
                                                        );
                                }
                               );
    }

    @Override
    public Trampoline<JsObj> filter(final Predicate<? super JsPair> predicate
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = JsPath.empty()
                                                                  .key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new ObjFilterKey(tail).filter(predicate));
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

    @Override
    public JsObj _filter_(final Predicate<? super JsPair> predicate)
    {
        JsPath path = JsPath.empty();
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (predicate.negate()
                         .test(pair))
                iterator.remove();
        }
        return json;
    }

    @Override
    public JsObj _filter__(final JsPath startingPath,
                    final Predicate<? super JsPair> predicate
                   )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(startingPath.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (predicate.negate()
                         .test(pair))
                iterator.remove();
            else if (pair.elem.isObj())
                new ObjFilterKey(pair.elem.asJsObj())._filter__(pair.path,
                                                                predicate
                                                               );
            else if (pair.elem.isArray())
                new ArrFilterKey(pair.elem.asJsArray())._filter__(pair.path.index(-1),
                                                                  predicate
                                                                 );
        }

        return json;


    }
}
