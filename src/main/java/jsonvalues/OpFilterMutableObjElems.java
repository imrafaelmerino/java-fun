package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

class OpFilterMutableObjElems extends OpFilterElems<JsObj>
{
    OpFilterMutableObjElems(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> filter_(final JsPath startingPath,
                              final Predicate<? super JsPair> predicate
                             )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(startingPath.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
            else if (pair.elem.isObj())
                new OpFilterMutableObjElems(pair.elem.asJsObj()).filter_(pair.path,
                                                                         predicate
                                                                        );
            else if (pair.elem.isArray())
                new OpFilterMutableArrElems(pair.elem.asJsArray()).filter_(pair.path.index(-1),
                                                                           predicate
                                                                          );
        }

        return Trampoline.done(json);
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final Predicate<? super JsPair> predicate
                            )
    {
        JsPath path = JsPath.empty();
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();
        }
        return Trampoline.done(json);
    }
}
