package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiPredicate;

final class OpFilterMutableObjObjs extends OpFilterObjs<JsObj>
{
    OpFilterMutableObjObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate
                            )
    {
        assert startingPath.isEmpty();
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsElem value = entry.getValue();
            if (value.isObj() && predicate.negate()
                                          .test(startingPath.key(entry.getKey()),
                                                value.asJsObj()
                                               )
            ) iterator.remove();
        }
        return Trampoline.done(json);
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> filter_(final JsPath startingPath,
                              final BiPredicate<? super JsPath, ? super JsObj> predicate
                             )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = json.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(startingPath.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (pair.elem.isJson())
            {
                if (pair.elem.isObj() && predicate.negate()
                                                  .test(pair.path,
                                                        pair.elem.asJsObj()
                                                       )
                ) iterator.remove();
                else if (pair.elem.isObj()) new OpFilterMutableObjObjs(pair.elem.asJsObj()).filter_(pair.path,
                                                                                                    predicate
                                                                                                   );
                else if (pair.elem.isArray()) new OpFilterMutableArrObjs(pair.elem.asJsArray()).filter_(pair.path.index(-1),
                                                                                                        predicate
                                                                                                       );

            }

        }
        return Trampoline.done(json);

    }
}
