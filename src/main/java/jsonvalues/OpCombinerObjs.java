package jsonvalues;

import java.util.Map;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class OpCombinerObjs extends OpCombiner<JsObj>
{
    OpCombinerObjs(final JsObj a,
                   final JsObj b
                  )
    {
        super(a,
              b
             );
    }

    @SuppressWarnings("squid:S00100")
        //  naming convention:  xx_ traverses the whole json
    Trampoline<JsObj> combine()
    {
        if (b.isEmpty()) return done(a);
        Map.Entry<String, JsElem> head = b.head();
        JsObj tail = b.tail(head.getKey());
        Trampoline<JsObj> tailCall = more(new OpCombinerObjs(a,
                                                             tail
        )::combine);
        return MatchExp.ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                  head.getValue()
                                                                                 )),
                                      MatchExp.ifPredicateElse(e -> e.isJson() && MatchExp.isSameType(head.getValue())
                                                                                          .test(e),
                                                               it ->
                                                               {
                                                                   Json<?> obj = a.get(JsPath.empty()
                                                                                             .key(head.getKey()))
                                                                                  .asJson();
                                                                   Json<?> obj1 = head.getValue()
                                                                                      .asJson();
                                                                   Trampoline<? extends Json<?>> headCall = more(() -> combine(obj,
                                                                                                                               obj1
                                                                                                                              )
                                                                                                                );
                                                                   return more(() -> tailCall).flatMap(tailResult -> headCall.map(headCombined_ ->
                                                                                                                                  tailResult.put(head.getKey(),
                                                                                                                                                 headCombined_
                                                                                                                                                )
                                                                                                                                 )
                                                                                                      );
                                                               },
                                                               it -> tailCall
                                                              )
                                     )
                       .apply(a.get(JsPath.empty()
                                          .key(head.getKey())));

    }
}
