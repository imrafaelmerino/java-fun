package jsonvalues.mymaps.mutable;

import jsonvalues.JsElem;
import jsonvalues.MutableMap;
import org.eclipse.collections.api.factory.Maps;

import java.util.*;

public class EclipseCollectionMap implements MutableMap
{

    private final org.eclipse.collections.api.map.MutableMap<String, JsElem> map;

    private EclipseCollectionMap(final org.eclipse.collections.api.map.MutableMap<String, JsElem> map)
    {
        this.map = map;
    }

    public EclipseCollectionMap()
    {
        this.map = Maps.mutable.empty();
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
    public JsElem get(final String key)
    {
        return map.get(key);
    }

    @Override
    public Optional<JsElem> getOptional(final String key)
    {
        final JsElem elem = get(key);
        if (elem.isNothing()) return Optional.empty();
        else return Optional.of(elem);
    }

    @Override
    public Map.Entry<String, JsElem> head()
    {
        return map.entrySet()
                  .stream()
                  .findFirst()
                  .get();
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
    public MutableMap tail(final String head)
    {
        return new EclipseCollectionMap(map.withoutKey(head));
    }

    @Override
    public void remove(final String key)
    {
        map.remove(key);
    }

    @Override
    public void update(final String key,
                       final JsElem je
                      )
    {
        map.put(key,
                je
               );
    }


    @Override
    public MutableMap copy()
    {
        return new EclipseCollectionMap(Maps.mutable.withMap(map));
    }


    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EclipseCollectionMap that = (EclipseCollectionMap) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(map);
    }
}
