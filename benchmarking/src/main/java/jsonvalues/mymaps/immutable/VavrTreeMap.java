package jsonvalues.mymaps.immutable;

import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;
import jsonvalues.JsElem;

public class VavrTreeMap extends VavrMap
{

    private VavrTreeMap(final Map<String, JsElem> treeMap)
    {
        super(treeMap);
    }

    public VavrTreeMap()
    {
        super(TreeMap.empty());
    }

    @Override
    protected VavrMap of(final io.vavr.collection.Map<String, JsElem> map)
    {
        return new VavrTreeMap(map);
    }
}
