package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpMapMutableArrObjs extends OpMapObjs<JsArray>
{
    OpMapMutableArrObjs(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                            final BiPredicate<? super JsPath, ? super JsObj> predicate,
                            final JsPath startingPath
                           )
    {
        return map(json,
                   json,
                   fn,
                   predicate,
                   startingPath
                  );
    }

    private Trampoline<JsArray> map(final JsArray acc,
                                    final JsArray remaining,
                                    final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                    final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                    final JsPath startingPath
                                   )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> map(acc,
                                                                                             tail,
                                                                                             fn,
                                                                                             predicate,
                                                                                             headPath
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

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsArray> map_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate,
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
                                     final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                     final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                     final JsPath startingPath
                                    )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.inc();

                                         final Trampoline<JsArray> tailCall = more(() -> map_(acc,
                                                                                              tail,
                                                                                              fn,
                                                                                              predicate,
                                                                                              headPath
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
                                                                                                                      return new OpMapMutableObjObjs(mapped).map_(fn,
                                                                                                                                                                  predicate,
                                                                                                                                                                  headPath
                                                                                                                                                                 )
                                                                                                                                                            .map(it ->
                                                                                                                                                                 json.put(new JsPath(headPath.last()),
                                                                                                                                                                          it
                                                                                                                                                                         ));
                                                                                                                  }),
                                                                                p -> more(() -> tailCall).flatMap(json -> new OpMapMutableObjObjs(obj).map_(fn,
                                                                                                                                                            predicate,
                                                                                                                                                            headPath
                                                                                                                                                           )
                                                                                                                                                      .map(it ->
                                                                                                                                                           json.put(new JsPath(headPath.last()),
                                                                                                                                                                    it
                                                                                                                                                                   )))
                                                                               )
                                         ,
                                                           arr -> more(() -> tailCall).flatMap(tailResult -> map_(arr,
                                                                                                                  arr,
                                                                                                                  fn,
                                                                                                                  predicate,
                                                                                                                  headPath.index(-1)
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

}
