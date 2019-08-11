package jsonvalues;

import java.util.Iterator;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

class OpArrFilterObjs extends OpFilterObj<JsArray>
{


    OpArrFilterObjs(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> filter(final JsPath startingPath,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate

                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpArrFilterObjs(tail).filter(headPath,
                                                                                                                                predicate
                                                                                                                               ));
                                    return ifObjElse(headJson -> JsPair.of(headPath,
                                                                           headJson
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   headJson
                                                                                                  ),
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headJson)),
                                                                               p -> tailCall
                                                                              )
                                    ,
                                                     headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                    )
                                    .apply(head);
                                }

                               );
    }

    @Override
    Trampoline<JsArray> filter_(final JsPath startingPath,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate

                               )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpArrFilterObjs(tail).filter_(headPath,
                                                                                                                                 predicate
                                                                                                                                ));
                                    return ifJsonElse(json -> JsPair.of(headPath,
                                                                        json
                                                                       )
                                                                    .ifElse(p -> predicate.test(p.path,
                                                                                                json
                                                                                               ),
                                                                            p -> more(() -> tailCall).flatMap(tailResult -> new OpObjFilterObjs(json).filter_(headPath,
                                                                                                                                                              predicate
                                                                                                                                                             )
                                                                                                                                                     .map(tailResult::prepend)),
                                                                            p -> tailCall

                                                                           ),
                                                      array -> more(() -> tailCall).flatMap(json -> new OpArrFilterObjs(array).filter_(headPath.index(-1),
                                                                                                                                       predicate
                                                                                                                                      )
                                                                                                                              .map(json::prepend)),
                                                      value -> more(() -> tailCall).map(it -> it.prepend(value))
                                                     )
                                    .apply(head);
                                }

                               );
    }

    @Override
    JsArray _filter_(final JsPath startingPath,
                     final BiPredicate<? super JsPath, ? super JsObj> predicate
                    )
    {
        JsPath currentPath = startingPath;
        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsElem next = iterator.next();
            if (next.isObj() && predicate.negate()
                                         .test(currentPath,
                                               next.asJsObj()
                                              )
            ) iterator.remove();

        }

        return json;
    }

    @Override
    JsArray _filter__(final JsPath startingPath,
                      final BiPredicate<? super JsPath, ? super JsObj> predicate
                     )
    {
        JsPath currentPath = startingPath;
        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
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