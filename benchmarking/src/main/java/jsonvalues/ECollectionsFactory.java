package jsonvalues;

import jsonvalues.mymaps.mutable.EclipseCollectionMap;
import jsonvalues.myseqs.mutable.EclipseCollectionList;

public class ECollectionsFactory
{
    private ECollectionsFactory() { }

    public static final MutableJsons hml = Jsons.mutable.withMap(EclipseCollectionMap.class)
                                                        .withSeq(EclipseCollectionList.class);
}
