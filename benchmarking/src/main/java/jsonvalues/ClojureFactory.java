package jsonvalues;

import jsonvalues.mymaps.immutable.ClojureArrayMap;
import jsonvalues.mymaps.immutable.ClojureHashMap;
import jsonvalues.mymaps.immutable.ClojureTreeMap;
import jsonvalues.myseqs.immutable.ClojureHashSet;
import jsonvalues.myseqs.immutable.ClojureVector;

public class ClojureFactory
{
    private ClojureFactory()
    {
    }


    /**
     hash-map and vector
     */
    public static final ImmutableJsons hmv = Jsons.immutable.withMap(ClojureHashMap.class)
                                                            .withSeq(ClojureVector.class);

    /**
     hash-map and set
     */
    public static final ImmutableJsons hms = Jsons.immutable.withMap(ClojureHashMap.class)
                                                            .withSeq(ClojureHashSet.class);

    /**
     tree-map and vector
     */
    public static final ImmutableJsons tmv = Jsons.immutable.withMap(ClojureTreeMap.class)
                                                            .withSeq(ClojureVector.class);

    /**
     tree-map and hash-set
     */
    public static final ImmutableJsons tms = Jsons.immutable.withMap(ClojureTreeMap.class)
                                                            .withSeq(ClojureHashSet.class);

    /**
     array-map and vector
     */
    public static final ImmutableJsons amv = Jsons.immutable.withMap(ClojureArrayMap.class)
                                                            .withSeq(ClojureVector.class);

    /**
     array-map and hash-set
     */
    public static final ImmutableJsons ams = Jsons.immutable.withMap(ClojureArrayMap.class)
                                                            .withSeq(ClojureHashSet.class);

}
