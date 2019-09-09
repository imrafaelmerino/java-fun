package jsonvalues.benchmarking;

import jsonvalues.JsElem;
import jsonvalues.MutableMap;
import jsonvalues.MutableSeq;
import org.eclipse.collections.api.factory.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EclipseCollectionMutableMap implements MutableMap
{

    final org.eclipse.collections.api.map.MutableMap<String,JsElem> map;

    private EclipseCollectionMutableMap(final org.eclipse.collections.api.map.MutableMap<String, JsElem> map)
    {
        this.map = map;
    }

    public EclipseCollectionMutableMap()
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
        if(elem.isNothing())return Optional.empty(); else return Optional.of(elem);
    }

    @Override
    public Map.Entry<String, JsElem> head()
    {
        return map.entrySet().stream().findFirst().get();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.entrySet().iterator();
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public MutableMap tail(final String head)
    {
        return null;
    }

    @Override
    public MutableMap empty()
    {
        return new EclipseCollectionMutableMap();

    }

    @Override
    public MutableMap remove(final String key)
    {
        map.remove(key);
        return new EclipseCollectionMutableMap(map);
    }

    @Override
    public MutableMap update(final String key,
                             final JsElem je
                            )
    {
        map.replace(key,je);
        return new EclipseCollectionMutableMap(map);
    }

    @Override
    public MutableSeq emptyArray()
    {
        return new EclipseCollectionMutableList();
    }

    @Override
    public MutableMap copy()
    {
        return new EclipseCollectionMutableMap(Maps.mutable.withMap(map));
    }
}
