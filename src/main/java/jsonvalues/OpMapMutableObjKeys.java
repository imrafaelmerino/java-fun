package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpMapMutableObjKeys extends OpMapKeys<JsObj>
{
    OpMapMutableObjKeys(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, String> fn,
                          final Predicate<? super JsPair> predicate,
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
                                  final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate,
                                  final JsPath startingPath
                                 )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.key(head.getKey());
                                         final Trampoline<JsObj> tailCall = more(() -> map(acc,
                                                                                           remaining.tail(head.getKey()),
                                                                                           fn,
                                                                                           predicate,
                                                                                           startingPath
                                                                                          ));
                                         return JsPair.of(headPath,
                                                          head.getValue()
                                                         )
                                                      .ifElse(predicate,
                                                              p -> more(() -> tailCall).map(it ->
                                                                                            {
                                                                                                it.remove(head.getKey());
                                                                                                return it.put(fn.apply(p),
                                                                                                              p.elem
                                                                                                             );
                                                                                            }),
                                                              p -> more(() -> tailCall).map(tailResult -> tailResult.put(head.getKey(),
                                                                                                                         p.elem
                                                                                                                        ))
                                                             );
                                     }
                                    );
    }

    @Override
    Trampoline<JsObj> map_(final Function<? super JsPair, String> fn,
                           final Predicate<? super JsPair> predicate,
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

    private Trampoline<JsObj> map_(final JsObj acc,
                                   final JsObj remaining,
                                   final Function<? super JsPair, String> fn,
                                   final Predicate<? super JsPair> predicate,
                                   final JsPath startingPath
                                  )
    {
        return remaining.ifEmptyElse(done(acc),
                                     (head, tail) ->
                                     {
                                         final JsPath headPath = startingPath.key(head.getKey());
                                         final Trampoline<JsObj> tailCall = more(() -> map_(acc,
                                                                                            remaining.tail(head.getKey()),
                                                                                            fn,
                                                                                            predicate,
                                                                                            startingPath
                                                                                           ));
                                         final JsPair pair = JsPair.of(headPath,
                                                                       head.getValue()
                                                                      );
                                         return pair.ifElse(predicate,
                                                            p -> p.ifJsonElse((path, headObj) ->
                                                                              {
                                                                                  String newKey = fn.apply(p);//applying the function before doing any update in the object is important
                                                                                  return more(() -> tailCall).flatMap(tailResult -> map_(headObj,
                                                                                                                                         headObj,
                                                                                                                                         fn,
                                                                                                                                         predicate,
                                                                                                                                         headPath
                                                                                                                                        )
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
                                                                              (path, headArr) ->
                                                                              {
                                                                                  String newKey = fn.apply(p);//applying the function before doing any update in the object is important
                                                                                  return more(() -> tailCall).flatMap(tailResult -> new OpMapMutableArrKeys(headArr).map_(fn,
                                                                                                                                                                          predicate,
                                                                                                                                                                          headPath.index(-1)
                                                                                                                                                                         )
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
                                                            p -> p.ifJsonElse((path, headJson) -> more(() -> tailCall).flatMap(tailResult -> map_(headJson,
                                                                                                                                                  headJson,
                                                                                                                                                  fn,
                                                                                                                                                  predicate,
                                                                                                                                                  headPath
                                                                                                                                                 ).map(headMapped ->
                                                                                                                                                       tailResult.put(head.getKey(),
                                                                                                                                                                      headMapped
                                                                                                                                                                     )
                                                                                                                                                      )
                                                                                                                              ),

                                                                              (path, headArr) -> more(() -> tailCall).flatMap(tailResult -> new OpMapMutableArrKeys(headArr).map_(fn,
                                                                                                                                                                                  predicate,
                                                                                                                                                                                  headPath.index(-1)
                                                                                                                                                                                 )
                                                                                                                                                                            .map(it ->
                                                                                                                                                                                 {
                                                                                                                                                                                     tailResult.remove(head.getKey());
                                                                                                                                                                                     return tailResult.put(head.getKey(),
                                                                                                                                                                                                           it
                                                                                                                                                                                                          );
                                                                                                                                                                                 }
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
}
