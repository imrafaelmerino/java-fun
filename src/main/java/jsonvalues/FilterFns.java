package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

class FilterFns
{


    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns an immutable object, x_ traverses the whole json recursively
    static UnaryOperator<JsArray> _filterArrObjs__(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                   final JsPath path
                                                  )
    {
        return arr ->
        {
            JsPath currentPath = path;
            final Iterator<JsElem> iterator = arr.iterator();
            while (iterator.hasNext())
            {
                currentPath = currentPath.inc();
                final JsPair pair = JsPair.of(currentPath,
                                              iterator.next()
                                             );
                _filterJsonObjs__(predicate,
                                  iterator,
                                  pair
                                 );

            }
            return arr;
        };
    }


    static UnaryOperator<JsObj> _filterObjObjs__(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                                 final JsPath path
                                                )
    {
        return obj ->
        {
            final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
            while (iterator.hasNext())
            {
                final Map.Entry<String, JsElem> entry = iterator.next();
                final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                              entry.getValue()
                                             );

                _filterJsonObjs__(predicate,
                                  iterator,
                                  pair
                                 );

            }
            return obj;
        };

    }

    private static <T> void _filterJsonObjs__(final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                              final Iterator<T> iterator,
                                              final JsPair pair
                                             )
    {
        if (pair.elem.isJson())
        {
            if (pair.elem.isObj() && predicate.negate()
                                              .test(pair.path,
                                                    pair.elem.asJsObj()
                                                   )
            ) iterator.remove();
            else if (pair.elem.isObj()) _filterObjObjs__(predicate,
                                                         pair.path
                                                        ).apply(pair.elem.asJsObj());
            else if (pair.elem.isArray()) _filterArrObjs__(predicate,
                                                           pair.path.index(-1)
                                                          ).apply(pair.elem.asJsArray());

        }
    }





}
