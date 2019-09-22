package jsonvalues.mymaps.immutable;

import clojure.lang.IPersistentMap;
import clojure.lang.PersistentTreeMap;

public class ClojureTreeMap extends ClojureMap
{

    private ClojureTreeMap(final IPersistentMap map)
    {
        super(map);
    }

    @Override
    protected ClojureMap of(final IPersistentMap map)
    {
        return new ClojureTreeMap(map);
    }

    public ClojureTreeMap()
    {
        super(PersistentTreeMap.EMPTY);
    }

}
