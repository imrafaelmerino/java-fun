package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestJson {


    @Test
    public void testSet(){

        Json json = JsObj.empty();

        JsPath path = JsPath.path("/a/b/c");

        Assertions.assertEquals("a", json.set(path, JsStr.of("a")).getStr(path));


        JsPath first = JsPath.path("/a/b/1");
        JsPath second = JsPath.path("/a/b/0");
        Json json1 = json.set(first,
                          JsStr.of("b"));
        Assertions.assertEquals("b", json1.getStr(first));
        Assertions.assertEquals(JsNull.NULL, json1.get(second));

    }

    @Test
    public void testInsertJsNothingDeleteElement() {

        JsObj obj = JsObj.of("a",
                             JsObj.of("b",
                                      JsStr.of("hi"),
                                      "c",
                                      JsInt.of(1)
                             )
        );

        JsPath path = JsPath.path("/a,b");
        Assertions.assertEquals(JsNothing.NOTHING,obj.set(path, JsNothing.NOTHING).get(path));
    }

    @Test
    public void testGetMethods() {

        Json<JsObj> a = JsObj.of("a",
                                 JsInt.of(1),
                                 "b",
                                 JsStr.of("hi"),
                                 "c",
                                 JsBool.TRUE,
                                 "d",
                                 JsLong.of(1),
                                 "e",
                                 JsDouble.of(1.5),
                                 "f",
                                 JsBigInt.of(BigInteger.TEN),
                                 "g",
                                 JsBigDec.of(BigDecimal.TEN),
                                 "h",
                                 JsArray.of(JsStr.of("bye"),
                                            JsInt.of(1),
                                            JsBool.FALSE,
                                            JsLong.of(1L)
                                 ),
                                 "i",
                                 JsObj.of("a",
                                          JsInt.of(1),
                                          "b",
                                          JsArray.of(1,
                                                     2,
                                                     3
                                          ),
                                          "c",
                                          JsObj.empty()
                                 )
        );

        Assertions.assertTrue(1 == a.getInt(JsPath.path("/a")));
        Assertions.assertTrue(100 == a.getInt(JsPath.path("/apple"),()->100));

        Assertions.assertEquals("default",
                                a.getStr(JsPath.path("/a"),
                                         () -> "default"));
        Assertions.assertTrue(1L == a.getLong(JsPath.path("/a"),
                                              () -> 2L));
        Assertions.assertTrue(a.getBool(JsPath.path("/a"),
                                        () -> true));
        Assertions.assertEquals(JsArray.EMPTY,
                                a.getArray(JsPath.path("/a"),
                                           () -> JsArray.EMPTY));
        Assertions.assertEquals(JsObj.EMPTY,
                                a.getObj(JsPath.path("/a"),
                                         () -> JsObj.EMPTY));
        Assertions.assertTrue(BigDecimal.TEN == a.getBigDec(JsPath.path("/a"),
                                                            () -> BigDecimal.TEN));
        Assertions.assertTrue(BigInteger.ONE == a.getBigInt(JsPath.path("/a"),
                                                            () -> BigInteger.TEN));

        Assertions.assertEquals("hi",
                                a.getStr(JsPath.path("/b"))
        );
        Assertions.assertEquals(true,
                                a.getBool(JsPath.path("/c"))
        );
        Assertions.assertEquals(true,
                                a.getBool(JsPath.path("/hi"),()->true)
        );
        Assertions.assertTrue(1L == a.getLong(JsPath.path("/d")));
        Assertions.assertTrue(10L == a.getLong(JsPath.path("/dime"),()->10L));

        Assertions.assertTrue(1.5 == a.getDouble(JsPath.path("/e")));
        Assertions.assertTrue(10.5 == a.getDouble(JsPath.path("/table"),()->10.5));

        Assertions.assertEquals(BigInteger.TEN,
                                a.getBigInt(JsPath.path("/f"))
        );

        Assertions.assertEquals(BigInteger.ONE,
                                a.getBigInt(JsPath.path("/yes"),()->BigInteger.ONE)
        );
        Assertions.assertEquals(BigDecimal.TEN,
                                a.getBigDec(JsPath.path("/g"))
        );

        Assertions.assertEquals(BigDecimal.ONE,
                                a.getBigDec(JsPath.path("/bye"),()->BigDecimal.ONE)
        );
        Assertions.assertEquals(JsArray.of(1,
                                           2,
                                           3
                                ),
                                a.getArray(JsPath.path("/i/b"))
        );

        Assertions.assertEquals(JsObj.empty(),
                                a.getObj(JsPath.path("/i/c"))
        );

        Assertions.assertNull(a.getInt(JsPath.path("/b")));
        Assertions.assertNull(a.getLong(JsPath.path("/b")));
        Assertions.assertNull(a.getBigDec(JsPath.path("/b")));
        Assertions.assertNull(a.getBigInt(JsPath.path("/b")));
        Assertions.assertNull(a.getBool(JsPath.path("/b")));
        Assertions.assertNull(a.getObj(JsPath.path("/b")));
        Assertions.assertNull(a.getArray(JsPath.path("/b")));

        Assertions.assertNull(
                a.getInt(JsPath.path("/b"))
        );
        Assertions.assertEquals(null,
                                a.getLong(JsPath.path("/b"))
        );
        Assertions.assertNull(
                a.getBigDec(JsPath.path("/b"))
        );
        Assertions.assertNull(
                a.getBigInt(JsPath.path("/b"))
        );
        Assertions.assertNull(
                a.getBool(JsPath.path("/b"))
        );
        Assertions.assertNull(
                a.getObj(JsPath.path("/b"))
        );
        Assertions.assertNull(
                a.getArray(JsPath.path("/b"))
        );

    }


    @Test
    public void test_times() {

        final JsObj a = JsObj.of("a",
                                 JsArray.of(JsObj.of("a",
                                                     JsInt.of(1)
                                            ),
                                            JsNull.NULL,
                                            JsInt.of(1)
                                 ),
                                 "b",
                                 JsInt.of(1)
        );

        Assertions.assertEquals(1,
                                a.times(JsInt.of(1))
        );
        Assertions.assertEquals(3,
                                a.timesAll(JsInt.of(1))
        );
        Assertions.assertEquals(2,
                                a.size()
        );


    }


}
