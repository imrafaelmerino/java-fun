package jsonvalues.mymaps.immutable;

import clojure.java.api.Clojure;
import clojure.lang.APersistentMap;
import clojure.lang.IFn;
import clojure.lang.IPersistentMap;
import jsonvalues.ImmutableMap;
import jsonvalues.JsElem;
import jsonvalues.JsNothing;

import java.util.*;
import java.util.function.Function;

abstract class ClojureMap implements ImmutableMap
{
    private static final Function<String, IFn> cljFn = fn -> Clojure.var("clojure.core",
                                                                         fn
                                                                        );

    private static final IFn keys = cljFn.apply("keys");
    private static final IFn first = cljFn.apply("first");
    private static final IFn get = cljFn.apply("get");
    private IPersistentMap map;

    ClojureMap(final IPersistentMap map)
    {
        this.map = map;
    }

    protected abstract ClojureMap of(IPersistentMap map);

    @Override
    public boolean contains(final String key)
    {
        return map.containsKey(key);
    }

    @Override
    public Set<String> keys()
    {
        Set<String> xs = new HashSet<>();
        final Iterator iterator = ((APersistentMap.KeySeq) keys.invoke(map)).iterator();
        while (iterator.hasNext())
        {
            xs.add((String) iterator.next());
        }
        return xs;

    }

    @Override
    public JsElem get(final String key)
    {
        return (JsElem) get.invoke(map,
                                   key,
                                   JsNothing.NOTHING
                                  );
    }

    @Override
    public Optional<JsElem> getOptional(final String key)
    {
        final JsElem e = (JsElem) get.invoke(map,
                                             key
                                            );
        return Optional.ofNullable(e);
    }

    @Override
    public Map.Entry<String, JsElem> head()
    {
        return (Map.Entry<String, JsElem>) first.invoke(map);
    }

    @Override
    public boolean isEmpty()
    {
        return map.count() == 0;
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    public int size()
    {
        return map.count();
    }

    @Override
    public ImmutableMap tail(final String key)
    {
        return of(map.without(key));
    }

    @Override
    public ImmutableMap remove(final String key)
    {
        return of(map.without(key));
    }

    @Override
    public ImmutableMap update(final String key,
                               final JsElem e
                              )
    {
        return of(map.assoc(key,
                            e
                           ));
    }


}
