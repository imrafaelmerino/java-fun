package jsonvalues;

import java.util.Map;

import static jsonvalues.MatchFns.isSameType;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

public class SetTheoryFns
{


    //squid:S1452 -> private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    //squid:S00100 ->  naming convention: xx_ traverses the whole json
    //squid:S00117 -> ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S1452", "squid:S00117"})
    static Trampoline<? extends Json<?>> intersection_(final Json<?> a,
                                                       final Json<?> b,
                                                       final JsArray.TYPE ARRAY_AS
                                                      )
    {

        if (a.isObj() && b.isObj()) return AbstractJsObj.intersection_(a.asJsObj(),
                                                                       b.asJsObj(),
                                                                       ARRAY_AS
                                                                      );
        if (ARRAY_AS == JsArray.TYPE.LIST) return AbstractJsArray.intersection_(a.asJsArray(),
                                                                                b.asJsArray()
                                                                               );
        return AbstractJsArray.intersection(a.asJsArray(),
                                            b.asJsArray(),
                                            ARRAY_AS
                                           );


    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS  should be a valid name
    static Trampoline<JsArray> union(JsArray a,
                                     JsArray b,
                                     JsArray.TYPE ARRAY_AS
                                    )
    {


        switch (ARRAY_AS)
        {
            case SET:
                return unionAsSet(a,
                                  b
                                 );
            case LIST:
                return unionAsList(a,
                                   b
                                  );
            case MULTISET:
                return unionAsMultiSet(a,
                                       b
                                      );
        }

        throw new IllegalArgumentException(ARRAY_AS.name() + " option not supported");

    }

    // squid:S1452: Json<?> has only two possible types: JsObj or JsArr,
    // squid:S00100: naming convention: xx_ traverses the whole json
    // squid:S00117: ARRAY_AS  should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S1452", "squid:S00117"})
    static Trampoline<? extends Json<?>> union_(final Json<?> a,
                                                final Json<?> b,
                                                final JsArray.TYPE ARRAY_AS
                                               )
    {

        if (a.isObj() && b.isObj()) return union_(a.asJsObj(),
                                                                b.asJsObj(),
                                                                ARRAY_AS
                                                               );
        if (ARRAY_AS == JsArray.TYPE.LIST) return union_(a.asJsArray(),
                                                         b.asJsArray()
                                                        );

        return union(a.asJsArray(),
                     b.asJsArray(),
                     ARRAY_AS
                    );


    }

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsArray> union_(final JsArray a,
                                      final JsArray b
                                     )
    {

        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = union_(a.tail(),
                                                    b.tail()
                                                   );


        if (head.isJson() && isSameType(otherHead).test(head))
        {


            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> union_(obj,
                                                                       obj1,
                                                                       JsArray.TYPE.LIST
                                                                      ));
            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        }
        return more(() -> tailCall).map(it -> it.prepend(head));
    }

    //squid:S00117 ARRAY_AS should be a valid name
    //squid:S00100 naming convention: xx_ traverses the whole json
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) //  ARRAY_AS  should be a valid name
    static Trampoline<JsObj> union_(final JsObj a,
                                    final JsObj b,
                                    final JsArray.TYPE ARRAY_AS
                                   )
    {

        if (b.isEmpty()) return done(a);

        Map.Entry<String, JsElem> head = b.head();

        JsObj tail = b.tail(head.getKey());

        Trampoline<JsObj> tailCall = more(() -> union_(a,
                                                       tail,
                                                       ARRAY_AS
                                                      ));

        return MatchFns.ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(head.getKey(),
                                                                                  head.getValue()
                                                                                 )),
                                      MatchFns.ifPredicateElse(e -> e.isJson() && isSameType(head.getValue())
                                                                                          .test(e),
                                                               it ->
                                                               {
                                                                   Json<?> obj = a.get(JsPath.empty()
                                                                                             .key(head.getKey()))
                                                                                  .asJson();
                                                                   Json<?> obj1 = head.getValue()
                                                                                      .asJson();

                                                                   Trampoline<? extends Json<?>> headCall = more(() -> union_(obj,
                                                                                                                              obj1,
                                                                                                                              ARRAY_AS
                                                                                                                             )
                                                                                                                );

                                                                   return more(() -> tailCall).flatMap(tailResult -> headCall.map(headUnion_ ->
                                                                                                                                  tailResult.put(head.getKey(),
                                                                                                                                                 headUnion_
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

    private static Trampoline<JsArray> unionAsList(final JsArray a,
                                                   final JsArray b
                                                  )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        final Trampoline<JsArray> tailCall = unionAsList(a.tail(),
                                                         b.tail()
                                                        );
        return more(() -> tailCall).map(it -> it.prepend(a.head()));

    }

    private static Trampoline<JsArray> unionAsMultiSet(final JsArray a,
                                                       final JsArray b
                                                      )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        return more(() -> () -> a.appendAll(b));

    }

    private static Trampoline<JsArray> unionAsSet(final JsArray a,
                                                  final JsArray b
                                                 )
    {
        if (b.isEmpty()) return done(a);
        if (a.isEmpty()) return done(b);
        JsElem last = b.last();
        final Trampoline<JsArray> initCall = unionAsSet(a,
                                                        b.init()
                                                       );
        if (!a.containsElem(last)) return more(() -> initCall).map(it -> it.append(last));
        return more(() -> initCall);
    }
}
