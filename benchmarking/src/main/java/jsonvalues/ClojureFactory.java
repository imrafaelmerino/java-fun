package jsonvalues;

import jsonvalues.mymaps.immutable.ClojureArrayMap;
import jsonvalues.mymaps.immutable.ClojureHashMap;
import jsonvalues.mymaps.immutable.ClojureTreeMap;
import jsonvalues.myseqs.immutable.ClojureHashSet;
import jsonvalues.myseqs.immutable.ClojureVector;

public class ClojureFactory
{

    public static final ImmutableJsons hmv = Jsons.immutable.withMap(ClojureHashMap.class)
                                                            .withSeq(ClojureVector.class);

    public static final ImmutableJsons hms = Jsons.immutable.withMap(ClojureHashMap.class)
                                                            .withSeq(ClojureHashSet.class);

    public static final ImmutableJsons tmv = Jsons.immutable.withMap(ClojureTreeMap.class)
                                                            .withSeq(ClojureVector.class);

    public static final ImmutableJsons tms = Jsons.immutable.withMap(ClojureTreeMap.class)
                                                            .withSeq(ClojureHashSet.class);

    public static final ImmutableJsons amv = Jsons.immutable.withMap(ClojureArrayMap.class)
                                                            .withSeq(ClojureVector.class);

    public static final ImmutableJsons ams = Jsons.immutable.withMap(ClojureArrayMap.class)
                                                            .withSeq(ClojureHashSet.class);

}
