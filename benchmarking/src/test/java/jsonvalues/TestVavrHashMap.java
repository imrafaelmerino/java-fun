package jsonvalues;

import jsonvalues.mymaps.immutable.VavrHashMap;

public class TestVavrHashMap extends TestImmutableMap
{
    public TestVavrHashMap()
    {
        super(new VavrHashMap());
    }
}
