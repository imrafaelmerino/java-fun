package jsonvalues;

import java.util.function.Predicate;

class OpFilterMutableArrKeys extends OpFilterKeys<JsArray>
{
    OpFilterMutableArrKeys(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> filter_(JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               )
    {
        assert startingPath.last()
                           .isIndex(i -> i == -1);

        for (final JsElem elem : json)
        {
            startingPath = startingPath.inc();
            final JsPair pair = JsPair.of(startingPath,
                                          elem
                                         );
            if (pair.elem.isArray())
                new OpFilterMutableArrKeys(pair.elem.asJsArray()).filter_(startingPath.index(-1),
                                                                          predicate
                                                                         );
            else if (pair.elem.isObj())
                new OpFilterMutableObjKeys(pair.elem.asJsObj()).filter_(startingPath,
                                                                        predicate
                                                                       );

        }
        return Trampoline.done(json);
    }

    @Override
    Trampoline<JsArray> filter(final Predicate<? super JsPair> predicate)
    {
        throw new UnsupportedOperationException("filter keys of array");

    }
}
