package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

public class OpObjFilterObjs extends OpFilterObj<JsObj>
{


    public OpObjFilterObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    public Trampoline<JsObj> filter(final JsPath startingPath,
                                    final BiPredicate<? super JsPath, ? super JsObj> predicate

                                   )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpObjFilterObjs(tail).filter(startingPath,
                                                                                                                              predicate
                                                                                                                             ));
                                    return ifObjElse(json -> JsPair.of(headPath,
                                                                       json
                                                                      )
                                                                   .ifElse(p -> predicate.test(p.path,
                                                                                               json
                                                                                              ),
                                                                           p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                      json
                                                                                                                                     )),
                                                                           p -> tailCall
                                                                          ),
                                                     value -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                    value
                                                                                                                   ))
                                                    )
                                    .apply(head.getValue());
                                }

                               );
    }

    @Override
    public Trampoline<JsObj> filter_(final JsPath startingPath,
                                     final BiPredicate<? super JsPath, ? super JsObj> predicate
                                    )
    {


        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head.getKey());

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpObjFilterObjs(tail).filter_(startingPath,
                                                                                                                               predicate
                                                                                                                              ));
                                    return ifJsonElse(headJson -> JsPair.of(headPath,
                                                                            headJson
                                                                           )
                                                                        .ifElse(p -> predicate.test(p.path,
                                                                                                    headJson
                                                                                                   ),
                                                                                p -> more(() -> tailCall).flatMap(tailResult -> filterJson_(headJson,
                                                                                                                                            startingPath,
                                                                                                                                            predicate
                                                                                                                                           )
                                                                                                                  .map(headFiltered ->
                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                      headFiltered
                                                                                                                                     )
                                                                                                                      )
                                                                                                                 ),
                                                                                p -> tailCall
                                                                               ),
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpArrFilterObjs(headArray).filter_(headPath.index(-1),
                                                                                                                                                     predicate
                                                                                                                                                    )
                                                                                                                                            .map(headFiltered ->
                                                                                                                                               tailResult.put(head.getKey(),
                                                                                                                                                              headFiltered
                                                                                                                                                             )
                                                                                                                                              )
                                                                                               ),
                                                      headElem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                        headElem
                                                                                                       ))
                                                     )
                                    .apply(head.getValue());
                                }

                               );

    }

    @Override
    JsObj _filter_(final JsPath startingPath,
                   final BiPredicate<? super JsPath, ? super JsObj> predicate
                  )
    {
        JsPath path = JsPath.empty();
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsElem value = entry.getValue();
            if (value.isObj() && predicate.negate()
                                          .test(path.key(entry.getKey()),
                                                value.asJsObj()
                                               )
            ) iterator.remove();
        }
        return json;
    }

    @Override
    JsObj _filter__(final JsPath startingPath,
                    final BiPredicate<? super JsPath, ? super JsObj> predicate
                   )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(startingPath.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (pair.elem.isJson())
            {
                if (pair.elem.isObj() && predicate.negate()
                                                  .test(pair.path,
                                                        pair.elem.asJsObj()
                                                       )
                ) iterator.remove();
                else if (pair.elem.isObj()) new OpObjFilterObjs(pair.elem.asJsObj())._filter__(pair.path,
                                                                                               predicate
                                                                                              );
                else if (pair.elem.isArray()) new OpArrFilterObjs(pair.elem.asJsArray())._filter__(pair.path.index(-1),
                                                                                                   predicate
                                                                                                  );

            }

        }
        return json;

    }


}
