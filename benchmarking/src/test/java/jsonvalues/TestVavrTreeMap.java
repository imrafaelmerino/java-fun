package jsonvalues;

import jsonvalues.mymaps.immutable.VavrTreeMap;

public class TestVavrTreeMap extends TestImmutableMap
{
    public TestVavrTreeMap()
    {
        super(new VavrTreeMap());
    }
}
