package jsonvalues;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

final class OpCombinerArrs extends OpCombiner<JsArray>
{


    OpCombinerArrs(final JsArray a,
                   final JsArray b
                  )
    {
        super(a,
              b
             );
    }

    Trampoline<JsArray> combine(
                               )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = new OpCombinerArrs(a.tail(),
                                                                b.tail()
        ).combine();
        if (head.isJson() && head.isSameType(otherHead))
        {
            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();
            Trampoline<? extends Json<?>> headCall = more(() -> combine(obj,
                                                                        obj1
                                                                       ));
            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        }
        return more(() -> tailCall).map(it -> it.prepend(head.isNull() ? otherHead : head));
    }
}
