package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpMapMutableObjObjs extends OpMapObjs<JsObj>
{
    OpMapMutableObjObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
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

    private Trampoline<JsObj> map(final JsObj acc,
                                  final JsObj remaining,
                                  final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                  final JsPath startingPath
                                 )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.key(head.getKey());

                                         final Trampoline<JsObj> tailCall = more(() -> map(acc,
                                                                                           tail,
                                                                                           fn,
                                                                                           predicate,
                                                                                           startingPath
                                                                                          ));
                                         return ifObjElse(obj -> JsPair.of(headPath,
                                                                           obj
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   obj
                                                                                                  ),
                                                                               p ->
                                                                               {
                                                                                   obj.remove(head.getKey());
                                                                                   return more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                                fn.apply(p.path,
                                                                                                                                                         obj
                                                                                                                                                        )
                                                                                                                                               ));
                                                                               },
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                                          p.elem
                                                                                                                                         ))
                                                                              ),
                                                          value -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                                         value
                                                                                                        ))
                                                         )
                                         .apply(head.getValue());
                                     }
                                    );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> map_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
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
    private Trampoline<JsObj> map_(final JsObj acc,
                                   final JsObj remaining,
                                   final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                   final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                   final JsPath startingPath
                                  )
    {

        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.key(head.getKey());

                                         final Trampoline<JsObj> tailCall = more(() -> map_(acc,
                                                                                            tail,
                                                                                            fn,
                                                                                            predicate,
                                                                                            startingPath
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

                                                                                     return more(() -> tailCall).flatMap(tailResult -> map_(mapped,
                                                                                                                                            mapped,
                                                                                                                                            fn,
                                                                                                                                            predicate,
                                                                                                                                            headPath
                                                                                                                                           )
                                                                                                                         .map(headMappedResult ->
                                                                                                                              tailResult.put(head.getKey(),
                                                                                                                                             headMappedResult
                                                                                                                                            )
                                                                                                                             )
                                                                                                                        );

                                                                                 },
                                                                                 p -> more(() -> tailCall).flatMap(tailResult -> map_(json,
                                                                                                                                      json,
                                                                                                                                      fn,
                                                                                                                                      predicate,
                                                                                                                                      headPath
                                                                                                                                     )
                                                                                                                   .map(it ->
                                                                                                                        tailResult.put(head.getKey(),
                                                                                                                                       it
                                                                                                                                      )
                                                                                                                       )
                                                                                                                  )
                                                                                )
                                         ,
                                                           arr -> more(() -> tailCall).flatMap(tailResult -> new OpMapMutableArrObjs(arr).map_(fn,
                                                                                                                                               predicate,
                                                                                                                                               headPath.index(-1)
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
}
