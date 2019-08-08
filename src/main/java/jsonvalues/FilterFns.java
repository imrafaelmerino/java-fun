package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.function.*;

import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.MatchFns.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class FilterFns
{
    //squid:S00100_ naming convention: xx_ traverses the whole json
    @SuppressWarnings("squid:S00100")
    static Function<JsObj, Trampoline<JsObj>> filterObjElems_(final Predicate<? super JsPair> predicate,
                                                              final JsPath path
                                                             )
    {
        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterObjElems_(predicate,
                                                                                                                   path
                                                                                                                  ).apply(tail)
                                                                                            );
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonElems_(predicate,
                                                                                                                                    headPath
                                                                                                                                   ).apply(headJson)
                                                                                                                                    .map(headFiltered ->
                                                                                                                                         tailResult.put(head.getKey(),
                                                                                                                                                        headFiltered
                                                                                                                                                       )
                                                                                                                                        )
                                                                                                    ),
                                                            headElem -> JsPair.of(headPath,
                                                                                  headElem
                                                                                 )
                                                                              .ifElse(predicate,
                                                                                      p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                  headElem
                                                                                                                                                 )),
                                                                                      p -> tailCall
                                                                                     )

                                                           )
                                          .apply(head.getValue());

                                      }
                                     );

    }

    //squid:S00100_ naming convention: xx_ traverses the whole json
    @SuppressWarnings("squid:S00100")
    static Function<JsArray, Trampoline<JsArray>> filterArrElems_(final Predicate<? super JsPair> predicate,
                                                                  final JsPath path
                                                                 )
    {


        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {

                                          final JsPath headPath = path.inc();

                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrElems_(predicate,
                                                                                                                     headPath
                                                                                                                    ).apply(tail));
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonElems_(predicate,
                                                                                                                                    headPath
                                                                                                                                   ).apply(headJson)
                                                                                                                                    .map(tailResult::prepend)),
                                                            headElem -> JsPair.of(headPath,
                                                                                  headElem
                                                                                 )
                                                                              .ifElse(predicate,
                                                                                      p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem)),
                                                                                      p -> tailCall
                                                                                     )
                                                           )
                                          .apply(head);
                                      }
                                     );


    }

    static Function<JsArray, Trampoline<JsArray>> filterElems(final Predicate<? super JsPair> predicate,
                                                              final JsPath path
                                                             )
    {
        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {

                                          final JsPath headPath = path.inc();

                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterElems(predicate,
                                                                                                                 headPath
                                                                                                                ).apply(tail));
                                          return ifJsonElse(elem -> more(() -> tailCall).map(it -> it.prepend(elem)),
                                                            elem -> JsPair.of(headPath,
                                                                              elem
                                                                             )
                                                                          .ifElse(predicate,
                                                                                  p -> more(() -> tailCall).map(it -> it.prepend(elem)),
                                                                                  p -> tailCall
                                                                                 )
                                                           )
                                          .apply(head);
                                      }
                                     );
    }

    //squid:S00100_ naming convention: xx_ traverses the whole json
    @SuppressWarnings("squid:S00100")
    private static Function<Json<?>, Trampoline<? extends Json<?>>> filterJsonElems_(final Predicate<? super JsPair> predicate,
                                                                                     final JsPath startingPath
                                                                                    )
    {

        return json -> json.isObj() ? filterObjElems_(predicate,
                                                      startingPath
                                                     ).apply(json.asJsObj()) : filterArrElems_(predicate,
                                                                                               startingPath.index(-1)
                                                                                              ).apply(json.asJsArray());


    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsObj, Trampoline<JsObj>> filterObjObjs_(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                             final JsPath path
                                                            )
    {


        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterObjObjs_(predicate,
                                                                                                                  path
                                                                                                                 ).apply(tail));
                                          return ifJsonElse(headJson -> JsPair.of(headPath,
                                                                                  headJson
                                                                                 )
                                                                              .ifElse(p -> predicate.test(p.path,
                                                                                                          headJson
                                                                                                         ),
                                                                                      p -> more(() -> tailCall).flatMap(tailResult -> filterObjObjs_(predicate,
                                                                                                                                                     headPath
                                                                                                                                                    ).apply(headJson)
                                                                                                                                                     .map(headFiltered ->
                                                                                                                                                          tailResult.put(head.getKey(),
                                                                                                                                                                         headFiltered
                                                                                                                                                                        )
                                                                                                                                                         )
                                                                                                                       ),
                                                                                      p -> tailCall
                                                                                     ),
                                                            headArray -> more(() -> tailCall).flatMap(tailResult -> filterArrJsObjs_(predicate,
                                                                                                                                     headPath.index(-1)
                                                                                                                                    ).apply(headArray)
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

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> filterArrJsObjs_(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                                   final JsPath path
                                                                  )
    {


        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.inc();

                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrJsObjs_(predicate,
                                                                                                                      headPath
                                                                                                                     ).apply(tail));
                                          return ifJsonElse(json -> JsPair.of(headPath,
                                                                              json
                                                                             )
                                                                          .ifElse(p -> predicate.test(p.path,
                                                                                                      json
                                                                                                     ),
                                                                                  p -> more(() -> tailCall).flatMap(tailResult -> filterObjObjs_(predicate,
                                                                                                                                                 headPath
                                                                                                                                                ).apply(json)
                                                                                                                                                 .map(tailResult::prepend)),
                                                                                  p -> tailCall

                                                                                 ),
                                                            array -> more(() -> tailCall).flatMap(json -> filterArrJsObjs_(predicate,
                                                                                                                           headPath.index(-1)
                                                                                                                          ).apply(array)
                                                                                                                           .map(json::prepend)),
                                                            value -> more(() -> tailCall).map(it -> it.prepend(value))
                                                           )
                                          .apply(head);
                                      }

                                     );


    }

    static Function<JsArray, Trampoline<JsArray>> filterArrObjs(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                                final JsPath path
                                                               )
    {

        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.inc();

                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrObjs(predicate,
                                                                                                                   headPath
                                                                                                                  ).apply(tail));
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


    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsObj, Trampoline<JsObj>> filterObjKeys_(final Predicate<? super JsPair> predicate,
                                                             final JsPath path
                                                            )
    {
        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.key(head.getKey());
                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterObjKeys_(predicate,
                                                                                                                  path
                                                                                                                 ).apply(tail));
                                          return JsPair.of(headPath,
                                                           head.getValue()
                                                          )
                                                       .ifElse(predicate,
                                                               p -> ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonKeys_(predicate,
                                                                                                                                                       headPath
                                                                                                                                                      ).apply(headJson)
                                                                                                                                                       .map(headMapped ->
                                                                                                                                                            tailResult.put(head.getKey(),
                                                                                                                                                                           headMapped
                                                                                                                                                                          )
                                                                                                                                                           )
                                                                                                                        ),
                                                                                headElem -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                                  headElem
                                                                                                                                 ))

                                                                               )
                                                               .apply(head.getValue()),
                                                               p -> tailCall
                                                              );
                                      }
                                     );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> filterArrKeys_(final Predicate<? super JsPair> predicate,
                                                                 final JsPath path
                                                                )
    {


        return arr -> arr.ifEmptyElse(Trampoline.done(arr),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = path.inc();
                                          final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterArrKeys_(predicate,
                                                                                                                    headPath
                                                                                                                   ).apply(tail));
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> filterJsonKeys_(predicate,
                                                                                                                                   headPath
                                                                                                                                  ).apply(headJson)
                                                                                                                                   .map(tailResult::prepend)),
                                                            headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                           )
                                          .apply(head);
                                      }
                                     );


    }

    //squid:S00100_ naming convention: xx_ traverses the whole json
    @SuppressWarnings("squid:S00100")
    private static Function<Json<?>, Trampoline<? extends Json<?>>> filterJsonKeys_(final Predicate<? super JsPair> predicate,
                                                                                    final JsPath startingPath
                                                                                   )
    {

        return json -> json.isObj() ? filterObjKeys_(predicate,
                                                     startingPath
                                                    ).apply(json.asJsObj()) : filterArrKeys_(predicate,
                                                                                             startingPath.index(-1)
                                                                                            ).apply(json.asJsArray());


    }


    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static BiFunction<JsArray, JsArray, Trampoline<JsArray>> _mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                                                        final Predicate<? super JsPair> predicate,
                                                                        final JsPath path
                                                                       )
    {


        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {
                                                             final JsPath headPath = path.inc();

                                                             final Trampoline<JsArray> tailCall = more(() -> _mapElems_(fn,
                                                                                                                        predicate,
                                                                                                                        headPath
                                                                                                                       ).apply(acc,
                                                                                                                               tail
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


    static UnaryOperator<JsArray> _filterArrObjs__(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                   final JsPath path
                                                  )
    {
        return arr ->
        {
            JsPath currentPath = path;
            final Iterator<JsElem> iterator = arr.iterator();
            while (iterator.hasNext())
            {
                currentPath = currentPath.inc();
                final JsPair pair = JsPair.of(currentPath,
                                              iterator.next()
                                             );
                _filterJsonObjs__(predicate,
                                  iterator,
                                  pair
                                 );

            }
            return arr;
        };
    }


    static UnaryOperator<JsObj> _filterObjObjs__(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                 final JsPath path
                                                )
    {
        return obj ->
        {
            final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
            while (iterator.hasNext())
            {
                final Map.Entry<String, JsElem> entry = iterator.next();
                final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                              entry.getValue()
                                             );

                _filterJsonObjs__(predicate,
                                  iterator,
                                  pair
                                 );

            }
            return obj;
        };

    }

    private static <T> void _filterJsonObjs__(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                              final Iterator<T> iterator,
                                              final JsPair pair
                                             )
    {
        if (pair.elem.isJson())
        {
            if (pair.elem.isObj() && predicate.negate()
                                              .test(pair.path,
                                                    pair.elem.asJsObj()
                                                   )
            ) iterator.remove();
            else if (pair.elem.isObj()) _filterObjObjs__(predicate,
                                                         pair.path
                                                        ).apply(pair.elem.asJsObj());
            else if (pair.elem.isArray()) _filterArrObjs__(predicate,
                                                           pair.path.index(-1)
                                                          ).apply(pair.elem.asJsArray());

        }
    }


    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static UnaryOperator<JsArray> _filterArrKeys__(final Predicate<? super JsPair> predicate,
                                                   final JsPath path
                                                  )
    {
        return arr ->
        {
            JsPath currentPath = path;
            for (final JsElem elem : arr)
            {
                currentPath = currentPath.inc();
                final JsPair pair = JsPair.of(currentPath,
                                              elem
                                             );
                if (pair.elem.isArray())
                    _filterArrKeys__(predicate,
                                     JsPath.empty().index(-1)
                                    ).apply(pair.elem.asJsArray());
                else if (pair.elem.isObj())
                    _filterObjKeys__(predicate,
                                     currentPath
                                    ).apply(pair.elem.asJsObj());

            }
            return arr;
        };

    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static UnaryOperator<JsObj> _filterObjKeys__(final Predicate<? super JsPair> predicate,
                                                 final JsPath path
                                                )
    {
        return obj ->
        {
            final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
            while (iterator.hasNext())
            {
                final Map.Entry<String, JsElem> entry = iterator.next();
                final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                              entry.getValue()
                                             );
                if (predicate.negate()
                             .test(pair))
                    iterator.remove();
                else if (pair.elem.isObj())
                    _filterObjKeys__(predicate,
                                     pair.path
                                    ).apply(pair.elem.asJsObj());
                else if (pair.elem.isArray())
                    _filterArrKeys__(predicate,
                                     pair.path.index(-1)
                                    ).apply(pair.elem.asJsArray());
            }

            return obj;
        };


    }

}
