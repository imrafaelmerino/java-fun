package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class TestJson
{

  @Test
  public void testAppendStringsCreateArrayInObj()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                            "a",
                            "b"
                           );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendStringsCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                            "a",
                            "b"
                           );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendStringsOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                  .put(JsPath.path("/a/b"),
                       1
                      );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                            "a",
                            "b"
                           );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendStringsOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                    .put(JsPath.path("/a/b"),
                         1
                        );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                            "a",
                            "b"
                           );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendValuesCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                            JsStr.of("a"),
                            JsInt.of(1)
                           );
    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendValuesCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                            JsStr.of("a"),
                            JsInt.of(1)
                           );
    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendValuesOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                  .put(JsPath.path("/a/b"),
                       1
                      );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                            JsStr.of("a"),
                            JsInt.of(1)
                           );

    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendValuesOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                    .put(JsPath.path("/a/b"),
                         1
                        );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                            JsStr.of("a"),
                            JsInt.of(1)
                           );

    Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                       JsInt.of(1)
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendIntCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                            1,
                            2
                           );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendIntCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                            1,
                            2
                           );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendIntOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                  .put(JsPath.path("/a/b"),
                       1
                      );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                            1,
                            2
                           );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendIntOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                    .put(JsPath.path("/a/b"),
                         1
                        );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                            1,
                            2
                           );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                            1L,
                            2L
                           );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                            1L,
                            2L
                           );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongOverwriteAnyElementInObject()
  {

    Json<JsObj> a = JsObj.empty()
                  .put(JsPath.path("/a/b"),
                       1
                      );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                            1L,
                            2L
                           );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendLongOverwriteAnyElementInArray()
  {

    Json<JsArray> a = JsArray.empty()
                    .put(JsPath.path("/a/b"),
                         1
                        );

    final JsPath path = JsPath.path("/0/b");
    final Json<JsArray> b = a.append(path,
                            1L,
                            2L
                           );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendBoolCreateArrayInObject()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                            true,
                            false
                           );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendBoolCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                            true,
                            false
                           );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendBoolOverwriteAnyElement()
  {

    Json<JsObj> a = JsObj.empty()
                  .put(JsPath.path("/a/b"),
                       1
                      );

    final JsPath path = JsPath.path("/a/b");
    final Json<JsObj> b = a.append(path,
                            true,
                            false
                           );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }


  @Test
  public void testAppendBoolOverwriteAnyElementInArray()
  {

    final JsPath path = JsPath.path("/0/b");
    Json<JsArray> a = JsArray.empty()
                    .put(path,
                         1
                        );

    final Json<JsArray> b = a.append(path,
                            true,
                            false
                           );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleCreateArrayInObj()
  {

    Json<JsObj> a = JsObj.empty();

    final JsPath path = JsPath.path("/a/b/0");
    final Json<JsObj> b = a.append(path,
                            1.5,
                            1.6
                           );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleCreateArrayInArray()
  {

    Json<JsArray> a = JsArray.empty();

    final JsPath path = JsPath.path("/0/b/0");
    final Json<JsArray> b = a.append(path,
                            1.5,
                            1.6
                           );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleOverwriteAnyElementInObject()
  {

    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> a = JsObj.empty()
                  .put(path,
                       1
                      );

    final Json<JsObj> b = a.append(path,
                            1.5,
                            1.6
                           );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void testAppendDoubleOverwriteAnyElementInArray()
  {

    final JsPath path = JsPath.path("/0/b");
    Json<JsArray> a = JsArray.empty()
                    .put(path,
                         1
                        );

    final Json<JsArray> b = a.append(path,
                            1.5,
                            1.6
                           );

    Assertions.assertEquals(JsArray.of(1.5,
                                       1.6
                                      ),
                            b.getArray(path)
                           );

  }

  @Test
  public void appendAllIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendAllIfPresent(path,
                                     () -> JsArray.of(1,
                                                      2,
                                                      3
                                                     )
                                    );

    Assertions.assertEquals(JsArray.of(1,
                                       2,
                                       3
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                  .appendAllIfPresent(path,
                                      () -> JsArray.of(1,
                                                       2,
                                                       3
                                                      )
                                     );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }

  @Test
  public void appendIfPresentWithSupplier()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendIfPresent(path,
                                  () -> JsInt.of(1)
                                 );

    Assertions.assertTrue(1 == a.getInt(path.index(0)));

    Json<JsObj> b = JsObj.empty()
                  .appendIfPresent(path,
                                   () -> JsInt.of(1)
                                  );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }


  @Test
  public void appendArrayOfStringsIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendIfPresent(path,
                                  "a",
                                  "b"
                                 );

    Assertions.assertEquals(JsArray.of("a",
                                       "b"
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                  .appendIfPresent(path,
                                   "a",
                                   "b"
                                  );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }


  @Test
  public void appendArrayOfIntegersIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendIfPresent(path,
                                  1,
                                  2
                                 );

    Assertions.assertEquals(JsArray.of(1,
                                       2
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                  .appendIfPresent(path,
                                   1,
                                   2
                                  );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }

  @Test
  public void appendArrayOfLongsIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendIfPresent(path,
                                  1L,
                                  2L
                                 );

    Assertions.assertEquals(JsArray.of(1L,
                                       2L
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                  .appendIfPresent(path,
                                   1L,
                                   2L
                                  );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }

  @Test
  public void appendArrayOfDoubleIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendIfPresent(path,
                                  1.5,
                                  2.5
                                 );

    Assertions.assertEquals(JsArray.of(1.5,
                                       2.5
                                      ),
                            a.getArray(path)
                           );

    Json b = JsObj.empty()
                  .appendIfPresent(path,
                                   1.5,
                                   2.5
                                  );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }


  @Test
  public void appendArrayOfBoolIfPresent()
  {
    final JsPath path = JsPath.path("/a/b");
    Json<JsObj> json = JsObj.of(JsPair.of(path,
                                   JsArray.empty()
                                  ));

    Json<JsObj> a = json.appendIfPresent(path,
                                  true,
                                  false
                                 );

    Assertions.assertEquals(JsArray.of(true,
                                       false
                                      ),
                            a.getArray(path)
                           );

    Json<JsObj> b = JsObj.empty()
                  .appendIfPresent(path,
                                   true,
                                   false
                                  );

    Assertions.assertEquals(Optional.empty(),
                            b.getOptArray(path)
                           );

  }


  @Test
  public void testGetMethods()
  {

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
    Assertions.assertEquals("hi",
                            a.getStr(JsPath.path("/b")));
    Assertions.assertEquals(true,
                            a.getBool(JsPath.path("/c")));
    Assertions.assertTrue(1L == a.getLong(JsPath.path("/d")));
    Assertions.assertTrue(1.5 == a.getDouble(JsPath.path("/e")));
    Assertions.assertEquals(BigInteger.TEN,
                            a.getBigInt(JsPath.path("/f")));
    Assertions.assertEquals(BigDecimal.TEN,
                            a.getBigDec(JsPath.path("/g")));
    Assertions.assertEquals(JsArray.of(1,
                                       2,
                                       3),
                            a.getArray(JsPath.path("/i/b")));

    Assertions.assertEquals(JsObj.empty(),
                            a.getObj(JsPath.path("/i/c")));

    Assertions.assertEquals(null,
                            a.getInt(JsPath.path("/b")));
    Assertions.assertEquals(null,
                            a.getLong(JsPath.path("/b")));
    Assertions.assertEquals(null,
                            a.getBigDec(JsPath.path("/b")));
    Assertions.assertEquals(null,
                            a.getBigInt(JsPath.path("/b")));
    Assertions.assertEquals(null,
                            a.getBool(JsPath.path("/b")));
    Assertions.assertEquals(null,
                            a.getObj(JsPath.path("/b")));
    Assertions.assertEquals(null,
                            a.getArray(JsPath.path("/b")));

    Assertions.assertEquals(OptionalInt.empty(),
                            a.getOptInt(JsPath.path("/b"))
                           );
    Assertions.assertEquals(OptionalLong.empty(),
                            a.getOptLong(JsPath.path("/b"))
                           );
    Assertions.assertEquals(Optional.empty(),
                            a.getOptBigDec(JsPath.path("/b")));
    Assertions.assertEquals(Optional.empty(),
                            a.getOptBigInt(JsPath.path("/b")));
    Assertions.assertEquals(Optional.empty(),
                            a.getOptBool(JsPath.path("/b")));
    Assertions.assertEquals(Optional.empty(),
                            a.getOptObj(JsPath.path("/b")));
    Assertions.assertEquals(Optional.empty(),
                            a.getOptArray(JsPath.path("/b")));

  }
}
