package jsonvalues;

import java.util.Iterator;
import java.util.function.BiPredicate;

final class OpFilterMutableArrObjs extends OpFilterObjs<JsArray>
{


    OpFilterMutableArrObjs(final JsArray json)
    {
        super(json);
    }


    @Override
    Trampoline<JsArray> filter( JsPath startingPath,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {
        assert startingPath.last()
                           .isIndex(i -> i == -1);
        final Iterator<JsElem> iterator = json.iterator();
        while (iterator.hasNext())
        {
            startingPath = startingPath.inc();
            final JsElem next = iterator.next();
            if (next.isObj() && predicate.negate()
                                         .test(startingPath,
                                               next.asJsObj()
                                              )
            ) iterator.remove();

        }

        return Trampoline.done(json);
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsArray> filter_(final JsPath startingPath,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
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