package jsonvalues;

import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsArrayParser
{

  @Test
  public void test_array_of_different_elements()
  {

    final JsArraySpec spec = JsArraySpec.tuple(str,
                                               intNumber,
                                               longNumber,
                                               decimal,
                                               bool,
                                               integral,
                                               obj,
                                               array,
                                               intNumber(i -> i > 0),
                                               any(v -> v.isStr() || v.isInt()),
                                               str(s -> s.startsWith("1")),
                                               arrayOfIntegralSuchThat(a -> a.size() == 1),
                                               arrayOfNumberSuchThat(a -> a.size() == 2),
                                               number(a -> a.isDecimal()),
                                               arrayOfIntegral(a -> a.longValueExact() > 0),
                                               nullableArrayOfIntegral(a -> a.longValueExact() > 0),
                                               arrayOfObj,
                                               nullableArrayOfObj
                                              );


    JsArray array = JsArray.of(JsStr.of("a"),
                               JsInt.of(1),
                               JsLong.of(1),
                               JsBigDec.of(BigDecimal.TEN),
                               JsBool.TRUE,
                               JsBigInt.of(BigInteger.TEN),
                               JsObj.empty(),
                               JsArray.empty(),
                               JsInt.of(10),
                               JsStr.of("a"),
                               JsStr.of("1aaa"),
                               JsArray.of(1),
                               JsArray.of(1.5,
                                          2.5
                                         ),
                               JsDouble.of(1.5),
                               JsArray.of(1,
                                          2
                                         ),
                               JsNull.NULL,
                               JsArray.of(JsObj.empty(),
                                          JsObj.empty()
                                         ),
                               JsNull.NULL
                              );

    Assertions.assertEquals(array,
                            new JsArrayParser(spec).parse(array.toPrettyString())
                           );
  }

  @Test
  public void testNullable()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      nullableArrayOfStr
                                     );

    JsObjParser parser = new JsObjParser(spec);
    final JsObj a = JsObj.of("a",
                             JsArray.of("a",
                                        "b"
                                       )
                            );
    Assertions.assertTrue(spec.test(a)
                              .isEmpty());
    Assertions.assertTrue(parser.parse(a.toString())
                                .equals(a));


    final JsObj b = JsObj.of("a",
                             JsNull.NULL
                            );
    Assertions.assertTrue(spec.test(b)
                              .isEmpty());
    Assertions.assertTrue(parser.parse(b.toString())
                                .equals(b));


    JsObjSpec specST = JsObjSpec.strict("a",
                                        nullableArrayOfStrSuchThat(it -> it.size() % 2 == 0)
                                       );
    JsObjParser parserST = new JsObjParser(specST);

    Assertions.assertTrue(specST.test(a)
                                .isEmpty());
    Assertions.assertTrue(parserST.parse(a.toString())
                                  .equals(a));

    Assertions.assertTrue(specST.test(b)
                                .isEmpty());
    Assertions.assertTrue(parserST.parse(b.toString())
                                  .equals(b));

  }

  @Test
  public void testArrayOfObject()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      JsSpecs.arrayOf(JsObjSpec.strict("a",
                                                                       str,
                                                                       "b",
                                                                       intNumber
                                                                      ))
                                     );

    final JsObj a = JsObj.of("a",
                             JsArray.of(JsObj.of("a",
                                                 JsStr.of("a"),
                                                 "b",
                                                 JsInt.of(1)
                                                ))
                            );


    Assertions.assertEquals(a,
                            new JsObjParser(spec).parse(a.toString())
                           );

    JsObjSpec specNullable = JsObjSpec.strict("a",
                                              JsSpecs.arrayOf(JsObjSpec.strict("a",
                                                                                       str,
                                                                                       "b",
                                                                                       intNumber
                                                                                      )).nullable()
                                             );

    final JsObj b = JsObj.of("a",
                             JsNull.NULL
                            );
    Assertions.assertEquals(b,
                            new JsObjParser(specNullable).parse(b
                                                                  .toString())
                           );

    JsObjSpec specTested = JsObjSpec.strict("a",
                                            JsSpecs.arrayOfObj(o -> o.containsKey("a"))
                                           );

    final JsObj c = JsObj.of("a",
                             JsArray.of(JsObj.of("a",
                                                 JsBool.TRUE
                                                ),
                                        JsObj.of("a",
                                                 JsInt.of(1)
                                                )
                                       )
                            );
    Assertions.assertEquals(c,
                            new JsObjParser(specTested).parse(c
                                                                .toString())
                           );

    JsObjSpec specSuchThat = JsObjSpec.strict("a",
                                              arrayOfObjSuchThat(arr -> arr.size() > 1),
                                              "b",
                                              arrayOfBoolSuchThat(arr -> arr.size() > 2),
                                              "c",
                                              arrayOfNumberSuchThat(arr -> arr.head()
                                                                              .equals(JsInt.of(1))),
                                              "d",
                                              arrayOfDecSuchThat(arr -> arr.head()
                                                                           .equals(JsBigDec.of(BigDecimal.TEN))),
                                              "e",
                                              arrayOfLongSuchThat(arr -> arr.size() == 3),
                                              "f",
                                              arrayOfIntSuchThat(arr -> arr.size() == 3),
                                              "g",
                                              arrayOfDec(i -> i.longValueExact() % 2 == 0),
                                              "h",
                                              nullableArrayOfDec(i -> i.longValueExact() % 2 == 1),
                                              "i",
                                              nullableArrayOfStr(i -> i.length() % 2 == 0),
                                              "j",
                                              nullableArrayOfStr(i -> i.length() % 2 == 0),
                                              "k",
                                              arrayOfNumber(JsValue::isDecimal),
                                              "l",
                                              nullableArrayOfNumber(JsValue::isDecimal)
                                             );

    final JsObj d = JsObj.of("a",
                             JsArray.of(JsObj.empty(),
                                        JsObj.empty()
                                       ),
                             "b",
                             JsArray.of(true,
                                        true,
                                        false
                                       ),
                             "c",
                             JsArray.of(1,
                                        2,
                                        3
                                       ),
                             "d",
                             JsArray.of(JsBigDec.of(BigDecimal.TEN)),
                             "e",
                             JsArray.of(1L,
                                        2L,
                                        3L
                                       ),
                             "f",
                             JsArray.of(1,
                                        2,
                                        3
                                       ),
                             "g",
                             JsArray.of(4.000,
                                        10.000
                                       ),
                             "h",
                             JsNull.NULL,
                             "i",
                             JsNull.NULL,
                             "j",
                             JsArray.of("abce",
                                        "cdee"
                                       ),
                             "k",
                             JsArray.of(1.5,
                                        2.5
                                       ),
                             "l",
                             JsNull.NULL

                            );
    Assertions.assertEquals(d,
                            new JsObjParser(specSuchThat).parse(d.toString())
                           );

  }


  @Test
  public void testArrayOfValue()
  {


    JsObjSpec spec = JsObjSpec.lenient("a",
                                       array,
                                       "b",
                                       nullableArray,
                                       "c",
                                       optNullableArray,
                                       "d",
                                       optNullableArray(v -> v.isIntegral() || v.isStr()),
                                       "e",
                                       nullableArray(v -> v.isIntegral() || v.isStr()),
                                       "f",
                                       optNullableArray(v -> v.isIntegral() || v.isStr())
                                      );

    JsObjParser parser = new JsObjParser(spec);

    JsObj a = JsObj.of("a",
                       JsArray.of(1,
                                  2
                                 ),
                       "b",
                       JsNull.NULL,
                       "d",
                       JsArray.of(JsInt.of(10),
                                  JsStr.of("a")
                                 ),
                       "e",
                       JsNull.NULL
                      );

    Assertions.assertEquals(a,
                            parser.parse(a.toPrettyString())
                           );
  }
}
