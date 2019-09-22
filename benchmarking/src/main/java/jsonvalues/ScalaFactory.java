package jsonvalues;

import jsonvalues.mymaps.immutable.ScalaHashMap;
import jsonvalues.mymaps.immutable.ScalaTreeMap;
import jsonvalues.myseqs.immutable.ScalaList;
import jsonvalues.myseqs.immutable.ScalaVector;

public class ScalaFactory
{
    public static final ImmutableJsons hmv = Jsons.immutable.withMap(ScalaHashMap.class)
                                                            .withSeq(ScalaVector.class);

    public static final ImmutableJsons hml = Jsons.immutable.withMap(ScalaHashMap.class)
                                                            .withSeq(ScalaList.class);

    public static final ImmutableJsons tmv = Jsons.immutable.withMap(ScalaTreeMap.class)
                                                            .withSeq(ScalaVector.class);

    public static final ImmutableJsons tml = Jsons.immutable.withMap(ScalaTreeMap.class)
                                                            .withSeq(ScalaList.class);
}
