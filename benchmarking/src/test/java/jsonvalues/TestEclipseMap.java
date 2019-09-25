package jsonvalues;

import jsonvalues.mymaps.mutable.EclipseCollectionMap;

public class TestEclipseMap extends TestMutableMap
{
    public TestEclipseMap()
    {
        super(EclipseCollectionMap::new);
    }
}
