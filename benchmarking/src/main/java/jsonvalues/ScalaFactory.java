package jsonvalues;

import jsonvalues.mymaps.immutable.ScalaTreeMap;
import jsonvalues.myseqs.immutable.ScalaList;

public class ScalaFactory
{
    private ScalaFactory() { }
    public static final ImmutableJsons hmv = Jsons.immutable.withMap(ScalaImmutableMap.class)
                                                            .withSeq(ScalaImmutableVector.class);

    public static final ImmutableJsons hml = Jsons.immutable.withMap(ScalaImmutableMap.class)
                                                            .withSeq(ScalaList.class);

    public static final ImmutableJsons tmv = Jsons.immutable.withMap(ScalaTreeMap.class)
                                                            .withSeq(ScalaImmutableVector.class);

    public static final ImmutableJsons tml = Jsons.immutable.withMap(ScalaTreeMap.class)
                                                            .withSeq(ScalaList.class);
}
