package jsonvalues.mymaps.immutable;

import jsonvalues.ImmutableMap;
import jsonvalues.JsElem;
import org.pcollections.HashTreePMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PMap implements ImmutableMap
{

    private final org.pcollections.PMap<String, JsElem> map;

    PMap(final org.pcollections.PMap<String, JsElem> map)
    {
        this.map = map;
    }

    public PMap()
    {
        map = HashTreePMap.empty();
    }

    @Override
    public boolean contains(final String key)
    {
        return map.containsKey(key);
    }

    @Override
    public Set<String> keys()
    {
        return map.keySet();
    }

    @Override
    public JsElem get(final String s)
    {
        return map.get(s);
    }

    @Override
    public Optional<JsElem> getOptional(final String key)
    {
        return Optional.ofNullable(get(key));
    }

    @Override
    public Map.Entry<String, JsElem> head()
    {
        return map.entrySet()
                  .iterator()
                  .next();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {

        return map.entrySet()
                  .iterator();
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public ImmutableMap tail(final String key)
    {
        return new PMap(map.minus(key));
    }

    @Override
    public ImmutableMap remove(final String key)
    {
        return new PMap(map.minus(key));
    }

    @Override
    public ImmutableMap update(final String key,
                               final JsElem e
                              )
    {
        //TODO PROBAR PLUS DIRECTAMENTE
        return new PMap(map.minus(key)
                           .plus(key,
                                 e
                                ));
    }
}
