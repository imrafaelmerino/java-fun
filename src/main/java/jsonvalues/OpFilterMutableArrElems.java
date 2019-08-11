package jsonvalues;

import java.util.Iterator;
import java.util.function.Predicate;

public class OpFilterMutableArrElems extends OpFilterElems<JsArray>
{
    OpFilterMutableArrElems(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> filter_(JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               )
    {
        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            startingPath = startingPath.inc();
            final JsPair pair = JsPair.of(startingPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
            else if (pair.elem.isObj())
                new OpFilterMutableObjElems(pair.elem.asJsObj()).filter_(startingPath,
                                                                         predicate
                                                                        );
            else if (pair.elem.isArray())
                new OpFilterMutableArrElems(pair.elem.asJsArray()).filter_(startingPath.index(-1),
                                                                           predicate
                                                                          );
        }
        return Trampoline.done(json);
    }

    @Override
    Trampoline<JsArray> filter(JsPath startingPath,
                               final Predicate<? super JsPair> predicate
                              )
    {
        assert startingPath.last()
                           .isIndex(i -> i == -1);
        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            startingPath = startingPath.inc();
            final JsPair pair = JsPair.of(startingPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();

        }
        return Trampoline.done(json);
    }
}
