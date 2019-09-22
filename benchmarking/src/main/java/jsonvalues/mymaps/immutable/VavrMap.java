package jsonvalues.mymaps.immutable;

import jsonvalues.ImmutableMap;
import jsonvalues.JsElem;

import java.util.AbstractMap;

import io.vavr.collection.Map;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;


abstract class VavrMap implements ImmutableMap
{

    private final Map<String, JsElem> map;

    protected VavrMap(final io.vavr.collection.Map<String, JsElem> map)
    {
        this.map = map;
    }

    @Override
    public boolean contains(final String key)
    {
        return map.containsKey(key);
    }

    @Override
    public Set<String> keys()
    {
        return map.keySet()
                  .toJavaSet();
    }

    @Override
    public JsElem get(final String key)
    {
        return map.get(key)
                  .getOrElseThrow(() -> new UnsupportedOperationException("key not found"));
    }

    @Override
    public Optional<JsElem> getOptional(final String key)
    {
        return map.get(key)
                  .toJavaOptional();
    }

    @Override
    public java.util.Map.Entry<String, JsElem> head()
    {
        return map.head()
                  .toEntry();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public Iterator<java.util.Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator()
                  .map(it -> new AbstractMap.SimpleEntry<>(it._1,
                                                           it._2
                  ));
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public ImmutableMap tail(final String s)
    {
        return of(map.tail());
    }

    @Override
    public ImmutableMap remove(final String key)
    {
        return of(map.remove(key));
    }

    @Override
    public ImmutableMap update(final String key,
                               final JsElem e
                              )
    {
        return of(map.put(key,
                          e
                         ));
    }

    protected abstract VavrMap of(Map<String, JsElem> map);
}
