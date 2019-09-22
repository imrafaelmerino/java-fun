package jsonvalues.mymaps.immutable;

import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;

public class ClojureHashMap extends ClojureMap
{

    private ClojureHashMap(IPersistentMap map)
    {
        super(map);
    }

    public ClojureHashMap()
    {
        super(PersistentHashMap.EMPTY);
    }


    @Override
    protected ClojureMap of(final IPersistentMap map)
    {
        return new ClojureHashMap(map);
    }
}
