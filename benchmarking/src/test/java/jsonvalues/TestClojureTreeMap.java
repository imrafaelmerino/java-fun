package jsonvalues;

import jsonvalues.mymaps.immutable.ClojureTreeMap;

public class TestClojureTreeMap extends TestImmutableMap
{
    public TestClojureTreeMap()
    {
        super(new ClojureTreeMap());
    }
}
