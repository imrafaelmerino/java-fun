package jsonvalues;

import jsonvalues.myseqs.immutable.ClojureVector;

public class TestClojureVector extends TestImmutableSeq
{

    public TestClojureVector()
    {
        super(new ClojureVector());
    }
}
