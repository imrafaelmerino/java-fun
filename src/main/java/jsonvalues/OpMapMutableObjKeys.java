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
                                                                                                it.remove(JsPath.fromKey(head.getKey()));
                                                                                                return it.put(JsPath.fromKey(fn.apply(p)),
                                                                                                              p.elem
                                                                                                             );
                                                                                            }),
                                                              p -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                         p.elem
                                                                                                                        ))
                                                             );
                                     }
                                    );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
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
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
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
                                                                                                                               tailResult.remove(JsPath.fromKey(head.getKey()));
                                                                                                                               return tailResult.put(JsPath.fromKey(newKey),
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
                                                                                                                                                                             tailResult.remove(JsPath.fromKey(head.getKey()));
                                                                                                                                                                             return tailResult.put(JsPath.fromKey(newKey),
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
                                                                                                                      tailResult.remove(JsPath.fromKey(head.getKey()));
                                                                                                                      return tailResult.put(JsPath.fromKey(newKey),
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
                                                                                                                                                       tailResult.put(JsPath.fromKey(head.getKey()),
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
                                                                                                                                                                                     tailResult.remove(JsPath.fromKey(head.getKey()));
                                                                                                                                                                                     return tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                                                                                           it
                                                                                                                                                                                                          );
                                                                                                                                                                                 }
                                                                                                                                                                                )
                                                                                                                             ),
                                                                              (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                                        headElem
                                                                                                                                                       ))

                                                                             )
                                                           );


                                     }
                                    );

    }
}
