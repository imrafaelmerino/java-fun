package jsonvalues;

import jsonvalues.myseqs.mutable.JavaHashSet;

public class JavaFactory
{

    /**
     hash-map and hash-set
     */
    public static final MutableJsons hmhs = Jsons.mutable.withSeq(JavaHashSet.class);
}
