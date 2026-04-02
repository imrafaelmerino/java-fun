package fun.gen;

import fun.tuple.Quadruple;
import fun.tuple.Quintuple;
import fun.tuple.Sextuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HighArityTuplesTest {

    @Test
    void shouldSupportEqualityAndAccessorsWhenUsingTuple4Type() {
        Quadruple<String, Integer, Boolean, Double> one = Quadruple.of("a",
                                                                        1,
                                                                        true,
                                                                        2.5);
        Quadruple<String, Integer, Boolean, Double> same = Quadruple.of("a",
                                                                         1,
                                                                         true,
                                                                         2.5);
        Quadruple<String, Integer, Boolean, Double> other = Quadruple.of("b",
                                                                          1,
                                                                          true,
                                                                          2.5);

        Assertions.assertEquals("a",
                                one.first());
        Assertions.assertEquals(1,
                                one.second());
        Assertions.assertEquals(true,
                                one.third());
        Assertions.assertEquals(2.5,
                                one.fourth());

        Assertions.assertEquals(one,
                                same);
        Assertions.assertEquals(one.hashCode(),
                                same.hashCode());
        Assertions.assertNotEquals(one,
                                   other);
        Assertions.assertEquals("(a, 1, true, 2.5)",
                                one.toString());
    }

    @Test
    void shouldSupportNullsWhenUsingTuple5Type() {
        Quintuple<String, String, String, String, String> one = Quintuple.of(null,
                                                                              "b",
                                                                              null,
                                                                              "d",
                                                                              null);
        Quintuple<String, String, String, String, String> same = Quintuple.of(null,
                                                                               "b",
                                                                               null,
                                                                               "d",
                                                                               null);

        Assertions.assertEquals(one,
                                same);
        Assertions.assertEquals(one.hashCode(),
                                same.hashCode());
        Assertions.assertNull(one.first());
        Assertions.assertEquals("b",
                                one.second());
        Assertions.assertNull(one.third());
        Assertions.assertEquals("d",
                                one.fourth());
        Assertions.assertNull(one.fifth());
    }

    @Test
    void shouldSupportNullsWhenUsingTuple6Type() {
        Sextuple<String, String, String, String, String, String> one = Sextuple.of(null,
                                                                                    "b",
                                                                                    null,
                                                                                    "d",
                                                                                    null,
                                                                                    "f");
        Sextuple<String, String, String, String, String, String> same = Sextuple.of(null,
                                                                                     "b",
                                                                                     null,
                                                                                     "d",
                                                                                     null,
                                                                                     "f");

        Assertions.assertEquals(one,
                                same);
        Assertions.assertEquals(one.hashCode(),
                                same.hashCode());
        Assertions.assertNull(one.first());
        Assertions.assertEquals("b",
                                one.second());
        Assertions.assertNull(one.third());
        Assertions.assertEquals("d",
                                one.fourth());
        Assertions.assertNull(one.fifth());
        Assertions.assertEquals("f",
                                one.sixth());
    }
}
