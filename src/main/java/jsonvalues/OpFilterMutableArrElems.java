package jsonvalues;

import java.util.Iterator;
import java.util.function.Predicate;

final class OpFilterMutableArrElems extends OpFilterElems<JsArray>
{
    OpFilterMutableArrElems(final JsArray json)
    {
        super(json);
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsArray> filter_(final JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               )
    {
        JsPath currentPath = startingPath;
        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
            else if (pair.elem.isObj())
                new OpFilterMutableObjElems(pair.elem.asJsObj()).filter_(currentPath,
                                                                         predicate
                                                                        );
            else if (pair.elem.isArray())
                new OpFilterMutableArrElems(pair.elem.asJsArray()).filter_(currentPath.index(-1),
                                                                           predicate
                                                                          );
        }
        return Trampoline.done(json);
    }

    @Override
    Trampoline<JsArray> filter(final JsPath startingPath,
                               final Predicate<? super JsPair> predicate
                              )
    {
        assert startingPath.last()
                           .isIndex(i -> i == -1);
        JsPath currentPath = startingPath;

        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();

        }
        return Trampoline.done(json);
    }
}
