package fun.gen;

import fun.tuple.Pair;
import fun.tuple.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTuples {

    @Test
    public void testNullAllowed() {

        Pair<String, String> pair = Pair.of("a",
                                            null);
        Pair<String, String> pair1 = Pair.of("a",
                                             null);
        Assertions.assertEquals(pair1,
                                pair);
        Assertions.assertEquals(pair1.hashCode(),
                                pair.hashCode());
        Pair<String, String> pair2 = Pair.of(null,
                                             "b");
        Pair<String, String> pair3 = Pair.of(null,
                                             "b");
        Assertions.assertEquals(pair2,
                                pair3);
        Assertions.assertEquals(pair2.hashCode(),
                                pair3.hashCode());

        Assertions.assertEquals(Triple.of(null,
                                          null,
                                          null),
                                Triple.of(null,
                                          null,
                                          null));

        Assertions.assertEquals(Triple.of(null,
                                          null,
                                          null)
                                      .hashCode(),
                                Triple.of(null,
                                          null,
                                          null)
                                      .hashCode());
    }


}
