package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.AbstractJsObj.put;
import static jsonvalues.AbstractJson.put_;
import static jsonvalues.MatchFns.ifJsonElse;
import static jsonvalues.MatchFns.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class MapFunctions
{
    static Function<JsArray, Trampoline<JsArray>> mapArrJsObj(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                                              final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                              final JsPath startingPath
                                                             )
    {
        return arr ->
        arr.ifEmptyElse(Trampoline.done(arr),
                        (head, tail) ->
                        {
                            final JsPath headPath = startingPath.inc();
                            final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapArrJsObj(fn,
                                                                                                   predicate,
                                                                                                   headPath
                                                                                                  ).apply(tail));
                            return ifObjElse(headJson -> JsPair.of(headPath,
                                                                   headJson
                                                                  )
                                                               .ifElse(p -> predicate.test(p.path,
                                                                                           headJson
                                                                                          ),
                                                                       p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(p.path,
                                                                                                                                               headJson
                                                                                                                                              ))),
                                                                       p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headJson))

                                                                      ),
                                             headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                            )
                            .apply(head);
                        }
                       );
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> mapArrJsObj_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                                               final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                               final JsPath startingPath
                                                              )
    {
        return arr ->
        arr.ifEmptyElse(Trampoline.done(arr),
                        (head, tail) ->
                        {
                            final JsPath headPath = startingPath.inc();
                            final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapArrJsObj_(fn,
                                                                                                    predicate,
                                                                                                    headPath
                                                                                                   ).apply(tail));
                            return ifJsonElse(headObj -> JsPair.of(headPath,
                                                                   headObj
                                                                  )
                                                               .ifElse(p -> predicate.test(headPath,
                                                                                           headObj
                                                                                          ),
                                                                       p -> more(() -> tailCall).flatMap(tailResult -> mapJsObj_(fn,
                                                                                                                                 predicate,
                                                                                                                                 headPath
                                                                                                                                ).apply(fn.apply(headPath,
                                                                                                                                                 headObj
                                                                                                                                                )
                                                                                                                                       )
                                                                                                                                 .map(tailResult::prepend)),
                                                                       p -> more(() -> tailCall).flatMap(tailResult -> mapJsObj_(fn,
                                                                                                                                 predicate,
                                                                                                                                 headPath
                                                                                                                                ).apply(headObj
                                                                                                                                       )
                                                                                                                                 .map(tailResult::prepend))
                                                                      )
                            ,
                                              headArr -> more(() -> tailCall).flatMap(tailResult -> mapArrJsObj_(fn,
                                                                                                                 predicate,
                                                                                                                 headPath.index(-1)
                                                                                                                ).apply(headArr)
                                                                                                                 .map(tailResult::prepend)),
                                              headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                             )
                            .apply(head);
                        }

                       );
    }

    static Function<JsObj, Trampoline<JsObj>> mapJsObj_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                                        final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                        final JsPath startingPath
                                                       )
    {
        return obj ->
        obj.ifEmptyElse(Trampoline.done(obj),
                        (head, tail) ->
                        {
                            final JsPath headPath = startingPath.key(head.getKey());
                            final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapJsObj_(fn,
                                                                                               predicate,
                                                                                               startingPath
                                                                                              ).apply(tail)
                                                                              );
                            return ifJsonElse(headObj ->
                                              {
                                                  JsObj headMapped = JsPair.of(headPath,
                                                                               headObj
                                                                              )
                                                                           .ifElse(p -> predicate.test(headPath,
                                                                                                       headObj
                                                                                                      ),
                                                                                   p -> fn.apply(headPath,
                                                                                                 headObj
                                                                                                ),
                                                                                   p -> headObj
                                                                                  );
                                                  return more(() -> tailCall).flatMap(tailResult -> mapJsObj_(fn,
                                                                                                              predicate,
                                                                                                              headPath
                                                                                                             ).apply(headMapped)
                                                                                                              .map(headMappedResult ->
                                                                                                                   tailResult.put(head.getKey(),
                                                                                                                                  headMappedResult
                                                                                                                                 )
                                                                                                                  )
                                                                                     );
                                              },
                                              headArray -> more(() -> tailCall).flatMap(tailResult -> mapArrJsObj_(fn,
                                                                                                                   predicate,
                                                                                                                   headPath.index(-1)
                                                                                                                  ).apply(headArray)
                                                                                                                   .map(headResult ->
                                                                                                                        tailResult.put(head.getKey(),
                                                                                                                                       headResult
                                                                                                                                      )
                                                                                                                       )
                                                                                       ),
                                              headElement -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                   headElement
                                                                                                                  ))
                                             )
                            .apply(head.getValue());
                        }
                       );
    }

    static Function<JsObj, Trampoline<JsObj>> mapJsObj(final BiFunction<? super JsPath, ? super jsonvalues.JsObj, jsonvalues.JsObj> fn,
                                                       final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                       final JsPath startingPath
                                                      )
    {
        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = startingPath.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapJsObj(fn,
                                                                                                            predicate,
                                                                                                            startingPath
                                                                                                           ).apply(tail));
                                          return ifObjElse(headObj -> more(() -> tailCall).map(tailResult ->
                                                                                               {
                                                                                                   final JsElem headMapped = JsPair.of(headPath,
                                                                                                                                       headObj
                                                                                                                                      )
                                                                                                                                   .ifElse(p -> predicate.test(p.path,
                                                                                                                                                               headObj
                                                                                                                                                              ),
                                                                                                                                           p -> fn.apply(p.path,
                                                                                                                                                         headObj
                                                                                                                                                        ),
                                                                                                                                           p -> p.elem
                                                                                                                                          );
                                                                                                   return tailResult.put(head.getKey(),
                                                                                                                         headMapped
                                                                                                                        );
                                                                                               }),
                                                           headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                             headElem
                                                                                                                            ))
                                                          )
                                          .apply(head.getValue());
                                      }
                                     );
    }

    static Function<JsObj, Trampoline<JsObj>> mapKeys(final Function<? super JsPair, String> fn,
                                                      final Predicate<? super JsPair> predicate,
                                                      final JsPath startingPath
                                                     )
    {
        return obj ->
        obj.ifEmptyElse(Trampoline.done(obj),
                        (head, tail) ->
                        {
                            final JsPath headPath = startingPath.key(head.getKey());
                            final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapKeys(fn,
                                                                                             predicate,
                                                                                             startingPath
                                                                                            ).apply(obj.tail(head.getKey()))
                                                                              );
                            return Trampoline.more(() -> tailCall)
                                             .map(tailResult ->
                                                  {
                                                      final String keyMapped = JsPair.of(headPath,
                                                                                         head.getValue()
                                                                                        )
                                                                                     .ifElse(predicate,
                                                                                             fn,
                                                                                             p -> head.getKey()
                                                                                            );
                                                      return tailResult.put(keyMapped,
                                                                            head.getValue()
                                                                           );
                                                  });
                        }
                       );
    }

    private static Function<Json<?>, Trampoline<? extends Json<?>>> mapJsonKeys_(final Function<? super JsPair, String> fn,
                                                                                 final Predicate<? super JsPair> predicate,
                                                                                 final JsPath startingPath
                                                                                )
    {

        return json -> json.isObj() ? mapKeys_(fn,
                                               predicate,
                                               startingPath
                                              ).apply(json.asJsObj()) : mapArrKeys_(fn,
                                                                                    predicate,
                                                                                    startingPath.index(-1)
                                                                                   ).apply(json.asJsArray());


    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> mapArrKeys_(final Function<? super JsPair, String> fn,
                                                              final Predicate<? super JsPair> predicate,
                                                              final JsPath path
                                                             )
    {
        return array -> array.ifEmptyElse(Trampoline.done(array),
                                          (head, tail) ->
                                          {
                                              final JsPath headPath = path.inc();
                                              final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapArrKeys_(fn,
                                                                                                                     predicate,
                                                                                                                     headPath
                                                                                                                    ).apply(array.tail()
                                                                                                                           ));
                                              return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> mapJsonKeys_(fn,
                                                                                                                                    predicate,
                                                                                                                                    headPath
                                                                                                                                   ).apply(headJson)
                                                                                                                                    .map(tailResult::prepend)),
                                                                headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                               )
                                              .apply(head);
                                          }
                                         );
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Function<JsObj, Trampoline<JsObj>> mapKeys_(final Function<? super JsPair, String> fn,
                                                       final Predicate<? super JsPair> predicate,
                                                       final JsPath startingPath
                                                      )
    {

        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = startingPath.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapKeys_(fn,
                                                                                                            predicate,
                                                                                                            startingPath
                                                                                                           ).apply(obj.tail(head.getKey()))
                                                                                            );
                                          JsPair pair = JsPair.of(headPath,
                                                                  head.getValue()
                                                                 );

                                          return pair.ifElse(predicate,
                                                             headTrue -> headTrue.ifJsonElse((path, headJson) ->
                                                                                             {
                                                                                                 return more(() -> tailCall).flatMap(tailResult -> mapJsonKeys_(fn,
                                                                                                                                                                predicate,
                                                                                                                                                                headPath
                                                                                                                                                               ).apply(headJson
                                                                                                                                                                      )
                                                                                                                                                                .map(headJsonResult ->
                                                                                                                                                                     tailResult.put(fn.apply(pair),
                                                                                                                                                                                    headJsonResult
                                                                                                                                                                                   )
                                                                                                                                                                    )
                                                                                                                                    );
                                                                                             },
                                                                                             (path, headElem) ->
                                                                                             {
                                                                                                 return more(() -> tailCall).map(tailResult -> tailResult.put(fn.apply(pair),
                                                                                                                                                              headElem
                                                                                                                                                             ));

                                                                                             }
                                                                                            ),
                                                             headFalse -> headFalse.ifJsonElse((path, headJson) ->
                                                                                               {
                                                                                                   return more(() -> tailCall).flatMap(tailResult -> mapJsonKeys_(fn,
                                                                                                                                                                  predicate,
                                                                                                                                                                  headPath
                                                                                                                                                                 ).apply(headJson
                                                                                                                                                                        )
                                                                                                                                                                  .map(headJsonResult ->
                                                                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                                                                      headJsonResult
                                                                                                                                                                                     )
                                                                                                                                                                      )
                                                                                                                                      );
                                                                                               },
                                                                                               (path, headElem) ->
                                                                                               {
                                                                                                   return more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                                headElem
                                                                                                                                                               ));

                                                                                               }

                                                                                              )
                                                            )
                                          ;


                                      }
                                     );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Function<JsArray, Trampoline<JsArray>> mapArrElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                                               final Predicate<? super JsPair> predicate,
                                                               final JsPath startingPath
                                                              )
    {

        return array -> array.ifEmptyElse(Trampoline.done(array),
                                          (head, tail) ->
                                          {
                                              final JsPath headPath = startingPath.inc();

                                              final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapArrElems_(fn,
                                                                                                                      predicate,
                                                                                                                      headPath
                                                                                                                     ).apply(tail)
                                                                                                  );
                                              return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> mapJsonElems_(fn,
                                                                                                                                     predicate,
                                                                                                                                     headPath
                                                                                                                                    ).apply(headJson
                                                                                                                                           )
                                                                                                                                     .map(tailResult::prepend)
                                                                                                        ),
                                                                headElem -> JsPair.of(headPath,
                                                                                      headElem
                                                                                     )
                                                                                  .ifElse(predicate,
                                                                                          p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(p))),
                                                                                          p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                                                         )
                                                               )
                                              .apply(head);


                                          }
                                         );
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static BiFunction<JsArray, JsArray, Trampoline<JsArray>> _mapArrElems__(final Function<? super JsPair, ? extends JsElem> fn,
                                                                            final Predicate<? super JsPair> predicate,
                                                                            final JsPath startingPath
                                                                           )
    {


        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {
                                                             final JsPath headPath = startingPath.inc();

                                                             final Trampoline<JsArray> tailCall = more(() -> _mapArrElems__(fn,
                                                                                                                            predicate,
                                                                                                                            headPath
                                                                                                                           ).apply(acc,
                                                                                                                                   tail
                                                                                                                                  ));


                                                             return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> _mapJsonElems__(fn,
                                                                                                                                                      predicate,
                                                                                                                                                      headPath
                                                                                                                                                     ).apply(headJson)
                                                                                                                                                      .map(headMapped ->
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

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static BiFunction<JsObj, JsObj, Trampoline<JsObj>> _mapElems__(final Function<? super JsPair, ? extends JsElem> fn,
                                                                   final Predicate<? super JsPair> predicate,
                                                                   final JsPath startingPath
                                                                  )
    {
        return (acc, remaining) ->
        remaining.ifEmptyElse(done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = startingPath.key(head.getKey());
                                  final Trampoline<JsObj> tailCall = more(() -> _mapElems__(fn,
                                                                                            predicate,
                                                                                            startingPath
                                                                                           ).apply(acc,
                                                                                                   tail
                                                                                                  ));
                                  return ifJsonElse(headJson ->
                                                    more(() -> tailCall).flatMap(tailResult -> _mapJsonElems__(fn,
                                                                                                               predicate,
                                                                                                               headPath
                                                                                                              ).apply(headJson
                                                                                                                     )
                                                                                                               .map(headMapped -> tailResult.put(head.getKey(),
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
                                                                                             return tailResult.put(head.getKey(),
                                                                                                                   headMapped
                                                                                                                  );
                                                                                         })
                                                   ).apply(head.getValue());
                              }
                             );
    }

    static Function<JsArray, Trampoline<JsArray>> mapArrElems(final Function<? super JsPair, ? extends JsElem> fn,
                                                              final Predicate<? super JsPair> predicate,
                                                              final JsPath startingPath
                                                             )
    {


        return array -> array.ifEmptyElse(Trampoline.done(array),
                                          (head, tail) ->
                                          {
                                              final JsPath headPath = startingPath.inc();

                                              final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapArrElems(fn,
                                                                                                                     predicate,
                                                                                                                     headPath
                                                                                                                    ).apply(tail));
                                              return ifJsonElse(headJson -> more(() -> tailCall).map(it -> it.prepend(headJson)),
                                                                headElem ->
                                                                {
                                                                    JsElem headMapped = JsPair.of(headPath,
                                                                                                  headElem
                                                                                                 )
                                                                                              .ifElse(predicate,
                                                                                                      fn::apply,
                                                                                                      p -> p.elem
                                                                                                     );
                                                                    return more(() -> tailCall).map(tailResult -> tailResult.prepend(headMapped));
                                                                }
                                                               ).apply(head);
                                          }
                                         );
    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Function<JsObj, Trampoline<JsObj>> mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                                        final Predicate<? super JsPair> predicate,
                                                        final JsPath startingPath
                                                       )
    {

        return obj -> obj.ifEmptyElse(Trampoline.done(obj),
                                      (head, tail) ->
                                      {
                                          final JsPath headPath = startingPath.key(head.getKey());

                                          final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapElems_(fn,
                                                                                                             predicate,
                                                                                                             startingPath
                                                                                                            ).apply(tail)
                                                                                            );
                                          return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> mapJsonElems_(fn,
                                                                                                                                 predicate,
                                                                                                                                 headPath
                                                                                                                                ).apply(headJson)
                                                                                                                                 .map(headMapped ->
                                                                                                                                      tailResult.put(head.getKey(),
                                                                                                                                                     headMapped
                                                                                                                                                    )
                                                                                                                                     )
                                                                                                    ),
                                                            headElem ->
                                                            {
                                                                JsElem headMapped = JsPair.of(headPath,
                                                                                              headElem
                                                                                             )
                                                                                          .ifElse(predicate,
                                                                                                  fn::apply,
                                                                                                  p -> headElem
                                                                                                 );
                                                                return more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                             headMapped
                                                                                                                            ));
                                                            }
                                                           ).apply(head.getValue());
                                      }
                                     );


    }

    private static Function<Json<?>, Trampoline<? extends Json<?>>> mapJsonElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                                                                  final Predicate<? super JsPair> predicate,
                                                                                  final JsPath startingPath
                                                                                 )
    {

        return json -> json.isObj() ? mapElems_(fn,
                                                predicate,
                                                startingPath
                                               ).apply(json.asJsObj()) : mapArrElems_(fn,
                                                                                      predicate,
                                                                                      startingPath.index(-1)
                                                                                     ).apply(json.asJsArray());


    }

    private static Function<Json<?>, Trampoline<? extends Json<?>>> _mapJsonElems__(final Function<? super JsPair, ? extends JsElem> fn,
                                                                                    final Predicate<? super JsPair> predicate,
                                                                                    final JsPath startingPath
                                                                                   )
    {

        return json -> json.isObj() ? _mapElems__(fn,
                                                  predicate,
                                                  startingPath
                                                 ).apply(json.asJsObj(),
                                                         json.asJsObj()
                                                        ) : _mapArrElems__(fn,
                                                                           predicate,
                                                                           startingPath.index(-1)
                                                                          ).apply(json.asJsArray(),
                                                                                  json.asJsArray()
                                                                                 );


    }


    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static BiFunction<JsArray, JsArray, Trampoline<JsArray>> _mapArrJsObj__(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                                                            final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                                            final JsPath path
                                                                           )
    {
        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {
                                                             final JsPath headPath = path.inc();

                                                             final Trampoline<JsArray> tailCall = more(() -> _mapArrJsObj__(fn,
                                                                                                                            predicate,
                                                                                                                            headPath
                                                                                                                           ).apply(acc,
                                                                                                                                   tail
                                                                                                                                  ));
                                                             return ifJsonElse(obj -> JsPair.of(headPath,
                                                                                                obj
                                                                                               )
                                                                                            .ifElse(p -> predicate.test(p.path,
                                                                                                                        obj
                                                                                                                       ),
                                                                                                    p -> more(() -> tailCall).flatMap(json ->
                                                                                                                                      {

                                                                                                                                          final JsObj mapped = fn.apply(headPath,
                                                                                                                                                                        obj
                                                                                                                                                                       );
                                                                                                                                          return _mapJsObj__(fn,
                                                                                                                                                             predicate,
                                                                                                                                                             headPath
                                                                                                                                                            ).apply(mapped,
                                                                                                                                                                    mapped
                                                                                                                                                                   )
                                                                                                                                                             .map(it ->
                                                                                                                                                                  json.put(new JsPath(headPath.last()),
                                                                                                                                                                           it
                                                                                                                                                                          ));
                                                                                                                                      }),
                                                                                                    p -> more(() -> tailCall).flatMap(json -> _mapJsObj__(fn,
                                                                                                                                                          predicate,
                                                                                                                                                          headPath
                                                                                                                                                         ).apply(obj,
                                                                                                                                                                 obj
                                                                                                                                                                )
                                                                                                                                                          .map(it ->
                                                                                                                                                               json.put(new JsPath(headPath.last()),
                                                                                                                                                                        it
                                                                                                                                                                       )))
                                                                                                   )
                                                             ,
                                                                               arr -> more(() -> tailCall).flatMap(tailResult -> _mapArrJsObj__(fn,
                                                                                                                                                predicate,
                                                                                                                                                headPath.index(-1)
                                                                                                                                               ).apply(arr,
                                                                                                                                                       arr
                                                                                                                                                      )
                                                                                                                                                .map(it ->
                                                                                                                                                     tailResult.put(new JsPath(headPath.last()),
                                                                                                                                                                    it
                                                                                                                                                                   ))),
                                                                               value -> more(() -> tailCall).map(it -> it.put(new JsPath(headPath.last()),
                                                                                                                              value
                                                                                                                             ))
                                                                              )
                                                             .apply(head);
                                                         }

                                                        );
    }


    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static BiFunction<JsObj, JsObj, Trampoline<JsObj>> _mapJsObj__(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                                                   final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                                   final JsPath path
                                                                  )
    {
        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {
                                                             final JsPath headPath = path.key(head.getKey());

                                                             final Trampoline<JsObj> tailCall = more(() -> _mapJsObj__(fn,
                                                                                                                       predicate,
                                                                                                                       path
                                                                                                                      ).apply(acc,
                                                                                                                              tail
                                                                                                                             ));
                                                             return ifJsonElse(json -> JsPair.of(headPath,
                                                                                                 json
                                                                                                )
                                                                                             .ifElse(p -> predicate.test(p.path,
                                                                                                                         json
                                                                                                                        ),
                                                                                                     p -> put_(JsPath.of(head.getKey()),
                                                                                                               () ->
                                                                                                               {
                                                                                                                   final JsObj mapped = fn.apply(headPath,
                                                                                                                                                 json
                                                                                                                                                );

                                                                                                                   return _mapJsObj__(fn,
                                                                                                                                      predicate,
                                                                                                                                      headPath
                                                                                                                                     ).apply(mapped,
                                                                                                                                             mapped
                                                                                                                                            );
                                                                                                               },
                                                                                                               () -> tailCall
                                                                                                              ),
                                                                                                     p -> put_(JsPath.of(head.getKey()),
                                                                                                               () -> _mapJsObj__(fn,
                                                                                                                                 predicate,
                                                                                                                                 headPath
                                                                                                                                ).apply(json,
                                                                                                                                        json
                                                                                                                                       ),
                                                                                                               () -> tailCall
                                                                                                              )
                                                                                                    )
                                                             ,
                                                                               arr -> put_(JsPath.of(head.getKey()),
                                                                                           () -> _mapArrJsObj__(fn,
                                                                                                                predicate,
                                                                                                                headPath.index(-1)
                                                                                                               ).apply(arr,
                                                                                                                       arr
                                                                                                                      ),
                                                                                           () -> tailCall
                                                                                          ),
                                                                               value -> put(head.getKey(),
                                                                                            value,
                                                                                            () -> tailCall
                                                                                           )
                                                                              )
                                                             .apply(head.getValue());
                                                         }

                                                        );
    }


    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static BiFunction<JsObj, JsObj, Trampoline<JsObj>> _mapKeys__(final Function<? super JsPair, String> fn,
                                                                  final Predicate<? super JsPair> predicate,
                                                                  final JsPath startingPath
                                                                 )
    {

        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {
                                                             final JsPath headPath = startingPath.key(head.getKey());
                                                             final Trampoline<JsObj> tailCall = more(() -> _mapKeys__(fn,
                                                                                                                      predicate,
                                                                                                                      startingPath
                                                                                                                     ).apply(acc,
                                                                                                                             remaining.tail(head.getKey())
                                                                                                                            ));
                                                             final JsPair pair = JsPair.of(headPath,
                                                                                           head.getValue()
                                                                                          );
                                                             return pair.ifElse(predicate,
                                                                                p -> p.ifJsonElse((path, headJson) ->
                                                                                                  {
                                                                                                      String newKey = fn.apply(p);//applying the function before doing any update in the object is important
                                                                                                      return more(() -> tailCall).flatMap(tailResult -> _mapJsonKeys_(fn,
                                                                                                                                                                      predicate,
                                                                                                                                                                      headPath
                                                                                                                                                                     ).apply(headJson)
                                                                                                                                                                      .map(it ->
                                                                                                                                                                           {
                                                                                                                                                                               tailResult.remove(head.getKey());
                                                                                                                                                                               return tailResult.put(newKey,
                                                                                                                                                                                                     it
                                                                                                                                                                                                    );
                                                                                                                                                                           }
                                                                                                                                                                          )
                                                                                                                                         );
                                                                                                  },
                                                                                                  (path, headElem) ->
                                                                                                  {
                                                                                                      String newKey = fn.apply(p);//applying the function before doing any update in the object is important
                                                                                                      return more(() -> tailCall).map(tailResult ->
                                                                                                                                      {
                                                                                                                                          tailResult.remove(head.getKey());
                                                                                                                                          return tailResult.put(newKey,
                                                                                                                                                                headElem
                                                                                                                                                               );
                                                                                                                                      });

                                                                                                  }
                                                                                                 ),
                                                                                p -> p.ifJsonElse((path, headJson) -> more(() -> tailCall).flatMap(tailResult -> _mapJsonKeys_(fn,
                                                                                                                                                                               predicate,
                                                                                                                                                                               headPath
                                                                                                                                                                              ).apply(headJson)
                                                                                                                                                                               .map(headMapped ->
                                                                                                                                                                                    tailResult.put(head.getKey(),
                                                                                                                                                                                                   headMapped
                                                                                                                                                                                                  )
                                                                                                                                                                                   )
                                                                                                                                                  ),
                                                                                                  (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                                            headElem
                                                                                                                                                                           ))

                                                                                                 )
                                                                               );


                                                         }
                                                        );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static BiFunction<JsArray, JsArray, Trampoline<JsArray>> _mapArrKeys__(final Function<? super JsPair, String> fn,
                                                                           final Predicate<? super JsPair> predicate,
                                                                           final JsPath path
                                                                          )
    {


        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {

                                                             final JsPath headPath = path.inc();

                                                             final Trampoline<JsArray> tailCall = more(() -> _mapArrKeys__(fn,
                                                                                                                           predicate,
                                                                                                                           headPath
                                                                                                                          ).apply(acc,
                                                                                                                                  remaining.tail()
                                                                                                                                 ));

                                                             return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> _mapJsonKeys_(fn,
                                                                                                                                                    predicate,
                                                                                                                                                    headPath
                                                                                                                                                   ).apply(headJson)
                                                                                                                                                    .map(headMapped ->
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


    private static Function<Json<?>, Trampoline<? extends Json<?>>> _mapJsonKeys_(final Function<? super JsPair, String> fn,
                                                                                  final Predicate<? super JsPair> predicate,
                                                                                  final JsPath startingPath
                                                                                 )
    {

        return json -> json.isObj() ? _mapKeys__(fn,
                                                 predicate,
                                                 startingPath
                                                ).apply(json.asJsObj(),
                                                        json.asJsObj()
                                                       ) : _mapArrKeys__(fn,
                                                                         predicate,
                                                                         startingPath.index(-1)
                                                                        ).apply(json.asJsArray(),
                                                                                json.asJsArray()
                                                                               );


    }

    @SuppressWarnings("squid:S00100")
    //  naming convention: _xx_ returns immutable object
    static BiFunction<JsArray, JsArray, Trampoline<JsArray>> _mapJsObj_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                                                        final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                                        final JsPath path
                                                                       )
    {
        return (acc, remaining) -> remaining.ifEmptyElse(done(acc),
                                                         (head, tail) ->
                                                         {
                                                             final JsPath headPath = path.inc();

                                                             final Trampoline<JsArray> tailCall = more(() -> _mapJsObj_(fn,
                                                                                                                        predicate,
                                                                                                                        headPath
                                                                                                                       ).apply(acc,
                                                                                                                               tail
                                                                                                                              ));
                                                             return ifObjElse(obj -> JsPair.of(headPath,
                                                                                               obj
                                                                                              )
                                                                                           .ifElse(p -> predicate.test(p.path,
                                                                                                                       obj
                                                                                                                      ),
                                                                                                   p ->
                                                                                                   {
                                                                                                       final JsObj newObj = fn.apply(p.path,
                                                                                                                                     obj
                                                                                                                                    );
                                                                                                       return more(() -> tailCall).map(it -> it.put(new JsPath(headPath.last()),
                                                                                                                                                    newObj
                                                                                                                                                   ));

                                                                                                   },
                                                                                                   p -> more(() -> tailCall).map(it -> it.put(new JsPath(headPath.last()),
                                                                                                                                              p.elem
                                                                                                                                             ))
                                                                                                  ),
                                                                              value -> more(() -> tailCall).map(it -> it.put(new JsPath(headPath.last()),
                                                                                                                             value
                                                                                                                            ))
                                                                             ).apply(head);
                                                         }
                                                        );
    }



}
