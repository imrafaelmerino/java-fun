package jsonvalues.mymaps.immutable;

import clojure.lang.IPersistentMap;
import clojure.lang.PersistentArrayMap;

public class ClojureArrayMap extends ClojureMap
{

    public ClojureArrayMap()
    {
        super(PersistentArrayMap.EMPTY);
    }

    private ClojureArrayMap(final IPersistentMap map)
    {
        super(map);
    }

    @Override
    protected ClojureMap of(final IPersistentMap map)
    {
        return new ClojureArrayMap(map);
    }
}
