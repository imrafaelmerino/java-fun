package jsonvalues;

import jsonvalues.mymaps.immutable.ScalaTreeMap;

public class TestScalaTreeMap extends TestImmutableMap
{
    public TestScalaTreeMap()
    {
        super(new ScalaTreeMap());
    }
}
