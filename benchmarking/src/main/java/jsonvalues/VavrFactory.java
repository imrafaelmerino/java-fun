package jsonvalues;

import jsonvalues.mymaps.immutable.VavrHashMap;
import jsonvalues.mymaps.immutable.VavrTreeMap;
import jsonvalues.myseqs.immutable.VavrHashSet;
import jsonvalues.myseqs.immutable.VavrList;
import jsonvalues.myseqs.immutable.VavrVector;

public class VavrFactory
{
    private VavrFactory() { }

    public static final ImmutableJsons hmv = Jsons.immutable.withMap(VavrHashMap.class)
                                                            .withSeq(VavrVector.class);

    public static final ImmutableJsons hml = Jsons.immutable.withMap(VavrHashMap.class)
                                                            .withSeq(VavrList.class);

    public static final ImmutableJsons hms = Jsons.immutable.withMap(VavrHashMap.class)
                                                            .withSeq(VavrHashSet.class);

    public static final ImmutableJsons tmv = Jsons.immutable.withMap(VavrTreeMap.class)
                                                            .withSeq(VavrVector.class);

    public static final ImmutableJsons tml = Jsons.immutable.withMap(VavrTreeMap.class)
                                                            .withSeq(VavrList.class);

    public static final ImmutableJsons tms = Jsons.immutable.withMap(VavrTreeMap.class)
                                                            .withSeq(VavrHashSet.class);


}
