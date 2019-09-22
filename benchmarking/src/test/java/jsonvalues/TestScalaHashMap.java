package jsonvalues;

import jsonvalues.mymaps.immutable.ScalaHashMap;

public class TestScalaHashMap extends TestImmutableMap
{
    public TestScalaHashMap()
    {
        super(new ScalaHashMap());
    }
}
