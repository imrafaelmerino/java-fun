package jsonvalues;

import jsonvalues.mymaps.immutable.PMap;
import jsonvalues.myseqs.immutable.PVector;

public class PCollectionsFactory
{

    public static final ImmutableJsons hmv = Jsons.immutable.withMap(PMap.class)
                                                            .withSeq(PVector.class);


}
