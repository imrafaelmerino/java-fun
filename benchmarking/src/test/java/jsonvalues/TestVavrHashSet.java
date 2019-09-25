package jsonvalues;

import jsonvalues.myseqs.immutable.VavrHashSet;

public class TestVavrHashSet extends TestImmutableSet
{

    public TestVavrHashSet()
    {
        super(new VavrHashSet());
    }

}
