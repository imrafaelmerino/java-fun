package jsonvalues;

import jsonvalues.myseqs.immutable.ClojureHashSet;

public class TestClojureHashSet extends TestImmutableSet
{

    public TestClojureHashSet()
    {
        super(new ClojureHashSet());
    }
}
