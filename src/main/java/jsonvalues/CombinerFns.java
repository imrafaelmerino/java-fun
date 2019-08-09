package jsonvalues;

import java.util.Map;

import static jsonvalues.MatchFns.isSameType;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class CombinerFns
{
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<? extends Json<?>> combiner_(final Json<?> a,
                                                   final Json<?> b
                                                  )
    {

        if (a.isObj() && b.isObj()) return combiner_(a.asJsObj(),
                                                     b.asJsObj()
                                                    );
        return combiner_(a.asJsArray(),
                         b.asJsArray()
                        );

    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public static Trampoline<JsObj> combiner_(final JsObj a,
                                              final JsObj b
                                             )
    {

        if (b.isEmpty()) return done(a);

        Map.Entry<String, JsElem> head = b.head();

        JsObj tail = b.tail(head.getKey());

        Trampoline<JsObj> tailCall = more(() -> combiner_(a,
                                                          tail
                                                         ));

        return MatchFns.ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                  head.getValue()
                                                                                 )),
                                      MatchFns.ifPredicateElse(e -> e.isJson() && MatchFns.isSameType(head.getValue())
                                                                                          .test(e),
                                                               it ->
                                                               {
                                                                   Json<?> obj = a.get(JsPath.empty()
                                                                                             .key(head.getKey()))
                                                                                  .asJson();
                                                                   Json<?> obj1 = head.getValue()
                                                                                      .asJson();

                                                                   Trampoline<? extends Json<?>> headCall = more(() -> combiner_(obj,
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

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsArray> combiner_(final JsArray a,
                                         final JsArray b
                                        )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = combiner_(a.tail(),
                                                       b.tail()
                                                      );
        if (head.isJson() && isSameType(otherHead).test(head))
        {
            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();
            Trampoline<? extends Json<?>> headCall = more(() -> combiner_(obj,
                                                                          obj1
                                                                         ));
            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        }
        return more(() -> tailCall).map(it -> it.prepend(head.isNull() ? otherHead : head));
    }
}
