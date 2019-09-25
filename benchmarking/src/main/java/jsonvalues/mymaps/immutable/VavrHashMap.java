package jsonvalues.mymaps.immutable;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsElem;

public class VavrHashMap extends VavrMap
{

    private VavrHashMap(final Map<String, JsElem> map)
    {
        super(map);
    }

    @Override
    protected VavrMap of(final Map<String, JsElem> map)
    {
        return new VavrHashMap(map);
    }

    public VavrHashMap()
    {
        super(HashMap.empty());
    }


}
