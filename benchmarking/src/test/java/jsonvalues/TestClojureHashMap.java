package jsonvalues;

import jsonvalues.mymaps.immutable.ClojureHashMap;

public class TestClojureHashMap extends TestImmutableMap
{
    public TestClojureHashMap()
    {
        super(new ClojureHashMap());
    }
}
