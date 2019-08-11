package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpMap
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
                                                                                                     p ->
                                                                                                     {
                                                                                                         final JsObj mapped = fn.apply(headPath,
                                                                                                                                       json
                                                                                                                                      );

                                                                                                         return more(() -> tailCall).flatMap(tailResult -> _mapJsObj__(fn,
                                                                                                                                                                       predicate,
                                                                                                                                                                       headPath
                                                                                                                                                                      ).apply(mapped,
                                                                                                                                                                              mapped
                                                                                                                                                                             )
                                                                                                                                                                       .map(headMappedResult ->
                                                                                                                                                                            tailResult.put(head.getKey(),
                                                                                                                                                                                           headMappedResult
                                                                                                                                                                                          )
                                                                                                                                                                           )
                                                                                                                                            );

                                                                                                     },
                                                                                                     p -> more(() -> tailCall).flatMap(tailResult -> _mapJsObj__(fn,
                                                                                                                                                                 predicate,
                                                                                                                                                                 headPath
                                                                                                                                                                ).apply(json,
                                                                                                                                                                        json
                                                                                                                                                                       )
                                                                                                                                                                 .map(it ->
                                                                                                                                                                      tailResult.put(head.getKey(),
                                                                                                                                                                                     it
                                                                                                                                                                                    )
                                                                                                                                                                     )
                                                                                                                                      )
                                                                                                    )
                                                             ,
                                                                               arr -> more(() -> tailCall).flatMap(tailResult -> _mapArrJsObj__(fn,
                                                                                                                                                predicate,
                                                                                                                                                headPath.index(-1)
                                                                                                                                               ).apply(arr,
                                                                                                                                                       arr
                                                                                                                                                      )
                                                                                                                                                .map(it ->
                                                                                                                                                     tailResult.put(head.getKey(),
                                                                                                                                                                    it
                                                                                                                                                                   )
                                                                                                                                                    )
                                                                                                                  ),
                                                                               value -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                                              value
                                                                                                                             ))
                                                                              )
                                                             .apply(head.getValue());
                                                         }

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
