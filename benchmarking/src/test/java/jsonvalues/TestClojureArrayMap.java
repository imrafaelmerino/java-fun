package jsonvalues;

import jsonvalues.mymaps.immutable.ClojureArrayMap;

public class TestClojureArrayMap extends TestImmutableMap
{
    public TestClojureArrayMap()
    {
        super(new ClojureArrayMap());
    }
}
