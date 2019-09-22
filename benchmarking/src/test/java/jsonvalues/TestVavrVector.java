package jsonvalues;

import jsonvalues.myseqs.immutable.VavrVector;

public class TestVavrVector extends TestImmutableSeq
{

    public TestVavrVector()
    {
        super(new VavrVector());
    }
}
