package jsonvalues;

import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjParser
{


  @Test
  public void test_parse_obj_all_primitive_types()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            intNumber,
                                            "b",
                                            str,
                                            "c",
                                            longNumber,
                                            "d",
                                            bool,
                                            "e",
                                            TRUE,
                                            "f",
                                            FALSE,
                                            "g",
                                            decimal,
                                            "h",
                                            integral,
                                            "i",
                                            JsObjSpec.strict("a",
                                                             number,
                                                             "b",
                                                             array,
                                                             "c",
                                                             obj,
                                                             "d",
                                                             intNumber(i -> i % 2 == 0),
                                                             "e",
                                                             longNumber(i -> i % 2 == 1),
                                                             "f",
                                                             str(s -> s.startsWith("a")),
                                                             "g",
                                                             decimal(d -> d.doubleValue() > 1.0),
                                                             "h",
                                                             obj(o -> o.containsKey("b")),
                                                             "i",
                                                             array(a -> a.head()
                                                                         .equals(JsStr.of("first"))),
                                                             "j",
                                                             JsArraySpec.tuple(number(JsValue::isDecimal),
                                                                               any
                                                                              )
                                                            )
                                           );


    final JsObj obj = JsObj.of("a",
                               JsInt.of(1),
                               "b",
                               JsStr.of("a"),
                               "c",
                               JsLong.of(100),
                               "d",
                               JsBool.FALSE,
                               "e",
                               JsBool.TRUE,
                               "f",
                               JsBool.FALSE,
                               "g",
                               JsBigDec.of(BigDecimal.TEN),
                               "h",
                               JsBigInt.of(BigInteger.TEN),
                               "i",
                               JsObj.of("a",
                                        JsDouble.of(1.2),
                                        "b",
                                        JsArray.empty(),
                                        "c",
                                        JsObj.empty(),
                                        "d",
                                        JsInt.of(10),
                                        "e",
                                        JsLong.of(13),
                                        "f",
                                        JsStr.of("a"),
                                        "g",
                                        JsBigDec.of(new BigDecimal("3.0")),
                                        "h",
                                        JsObj.of("b",
                                                 JsStr.of("b")
                                                ),
                                        "i",
                                        JsArray.of(JsStr.of("first"),
                                                   JsBool.TRUE
                                                  ),
                                        "j",
                                        JsArray.of(JsDouble.of(1.2),
                                                   JsBool.TRUE
                                                  )
                                       )
                              );

    Assertions.assertEquals(obj,
                            new JsObjParser(spec).parse(obj
                                                          .toPrettyString())
                           );

  }


  @Test
  public void test_parse_obj_all_array_of_primitive_types()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            arrayOfInt,
                                            "b",
                                            arrayOfStr,
                                            "c",
                                            arrayOfBool,
                                            "d",
                                            arrayOfDec,
                                            "e",
                                            arrayOfNumber,
                                            "f",
                                            arrayOfIntegral,
                                            "g",
                                            arrayOfLong,
                                            "h",
                                            JsObjSpec.strict("a",
                                                             arrayOfInt(v -> v > 5),
                                                             "b",
                                                             arrayOfStr(s -> s.startsWith("a")),
                                                             "c",
                                                             arrayOfLong(l -> l < 10)
                                                            )
                                           );


    final JsObj obj = JsObj.of("a",
                               JsArray.of(1,
                                          2,
                                          3
                                         ),
                               "b",
                               JsArray.of("a",
                                          "b",
                                          "c"
                                         ),
                               "c",
                               JsArray.of(true,
                                          false,
                                          true
                                         ),
                               "d",
                               JsArray.of(1.2,
                                          1.3,
                                          1.5
                                         ),
                               "e",
                               JsArray.of(JsInt.of(1),
                                          JsLong.of(10),
                                          JsDouble.of(1.5)
                                         ),
                               "f",
                               JsArray.of(1,
                                          10,
                                          1000000
                                         ),
                               "g",
                               JsArray.of(1,
                                          2,
                                          4
                                         ),
                               "h",
                               JsObj.of("a",
                                        JsArray.of(13,
                                                   14,
                                                   15
                                                  ),
                                        "b",
                                        JsArray.of("ab",
                                                   "ac"
                                                  ),
                                        "c",
                                        JsArray.of(1,
                                                   2,
                                                   3
                                                  )
                                       )
                              );

    Assertions.assertEquals(obj,
                            new JsObjParser(spec).parse(obj
                                                          .toPrettyString())
                           );

  }


  @Test
  public void test_parse_obj_all_primitive_types_with_predicates()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            intNumber(i -> i > 0),
                                            "b",
                                            str(a -> a.length() > 0),
                                            "c",
                                            longNumber(c -> c > 0),
                                            "d",
                                            bool,
                                            "e",
                                            TRUE,
                                            "f",
                                            FALSE,
                                            "g",
                                            decimal(d -> d.doubleValue() > 0.0),
                                            "h",
                                            integral(i -> i.longValueExact() % 2 == 0),
                                            "i",
                                            JsObjSpec.strict("a",
                                                             number,
                                                             "b",
                                                             array,
                                                             "c",
                                                             obj(JsObj::isEmpty),
                                                             "d",
                                                             intNumber(i -> i % 2 == 0),
                                                             "e",
                                                             longNumber(i -> i % 2 == 1),
                                                             "f",
                                                             str(s -> s.startsWith("a")),
                                                             "g",
                                                             decimal(d -> d.doubleValue() > 1.0),
                                                             "h",
                                                             obj(o -> o.containsKey("b")),
                                                             "i",
                                                             array(a -> a.head()
                                                                         .equals(JsStr.of("first"))),
                                                             "j",
                                                             JsArraySpec.tuple(number(JsValue::isDecimal),
                                                                               any
                                                                              )
                                                            )
                                           );


    final JsObj obj = JsObj.of("a",
                               JsInt.of(1),
                               "b",
                               JsStr.of("a"),
                               "c",
                               JsLong.of(100),
                               "d",
                               JsBool.FALSE,
                               "e",
                               JsBool.TRUE,
                               "f",
                               JsBool.FALSE,
                               "g",
                               JsBigDec.of(BigDecimal.TEN),
                               "h",
                               JsBigInt.of(BigInteger.TEN),
                               "i",
                               JsObj.of("a",
                                        JsDouble.of(1.2),
                                        "b",
                                        JsArray.empty(),
                                        "c",
                                        JsObj.empty(),
                                        "d",
                                        JsInt.of(10),
                                        "e",
                                        JsLong.of(13),
                                        "f",
                                        JsStr.of("a"),
                                        "g",
                                        JsBigDec.of(new BigDecimal("3.0")),
                                        "h",
                                        JsObj.of("b",
                                                 JsStr.of("b")
                                                ),
                                        "i",
                                        JsArray.of(JsStr.of("first"),
                                                   JsBool.TRUE
                                                  ),
                                        "j",
                                        JsArray.of(JsDouble.of(1.2),
                                                   JsBool.TRUE
                                                  )
                                       )
                              );

    Assertions.assertEquals(obj,
                            new JsObjParser(spec).parse(obj
                                                          .toPrettyString())
                           );

  }


  @Test
  public void test_required_fields()
  {
    final JsObjSpec spec = JsObjSpec.strict("a",
                                            optNullableIntNumber,
                                            "b",
                                            optNullableStr,
                                            "c",
                                            optNullableLongNumber,
                                            "d",
                                            optNullableObj,
                                            "e",
                                            optNullableArray,
                                            "f",
                                            optNullableBool,
                                            "g",
                                            optNullableDecimal,
                                            "h",
                                            optNullableIntegral,
                                            "i",
                                            JsArraySpec.tuple(optNullableArrayOfInt,
                                                              optNullableArrayOfLong
                                                             ),
                                            "j",
                                            optNullableObj,
                                            "k",
                                            optNullableObj(a -> a.keySet()
                                                                 .size() == 2),
                                            "j",
                                            optNullableArrayOfObj(a -> a.keySet()
                                                                        .size() == 2)
                                           );


    Assertions.assertEquals(JsObj.empty(),
                            new JsObjParser(spec).parse(JsObj.empty()
                                                             .toPrettyString())
                           );

    final JsObj obj = JsObj.of("a",
                               JsNull.NULL,
                               "b",
                               JsNull.NULL,
                               "c",
                               JsNull.NULL,
                               "d",
                               JsNull.NULL,
                               "e",
                               JsNull.NULL,
                               "g",
                               JsNull.NULL,
                               "h",
                               JsNull.NULL,
                               "i",
                               JsArray.of(JsNull.NULL,
                                          JsNull.NULL
                                         ),
                               "j",
                               JsObj.empty(),
                               "k",
                               JsObj.of("a",
                                        JsInt.of(1),
                                        "b",
                                        JsStr.of("a")
                                       ),
                               "j",
                               JsArray.of(JsObj.of("a",
                                                   JsBool.TRUE,
                                                   "b",
                                                   JsBool.FALSE
                                                  ))
                              );


    Assertions.assertEquals(obj,
                            new JsObjParser(spec).parse(obj.toPrettyString())
                           );
  }

  @Test
  public void test_strict_mode()
  {
    final JsObjSpec spec = JsObjSpec.lenient("a",
                                             optNullableStr
                                            );


    final JsObj obj = JsObj.of("b",
                               JsStr.of("b")
                              );

    final JsObj parsed = new JsObjParser(spec).parse(obj.toPrettyString());

    Assertions.assertEquals(obj,
                            parsed
                           );
  }


  @Test
  public void test_int_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       nullableIntNumber(i -> i > 0),
                                       "b",
                                       optNullableIntNumber(i -> i > 0),
                                       "c",
                                       intNumber(i -> i > 0),
                                       "d",
                                       nullableIntNumber(i -> i < 0),
                                       "e",
                                       optNullableIntNumber(i -> i % 2 == 1),
                                       "f",
                                       nullableIntNumber(i -> i % 2 == 0)
                                      );

    final JsObj a = JsObj.of("a",
                              JsNull.NULL,
                              "c",
                              JsInt.of(3),
                              "d",
                              JsNull.NULL,
                              "f",
                              JsNull.NULL
                             );
    final JsObjParser parser = new JsObjParser(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString()));

    final JsObj b = JsObj.of("a",
                              JsInt.of(1),
                              "b",
                              JsInt.of(2),
                              "c",
                              JsInt.of(3),
                              "d",
                              JsInt.of(-5),
                              "e",
                              JsInt.of(11),
                              "f",
                              JsInt.of(20)
                             );


    Assertions.assertEquals(b,
                            parser.parse(b.toString()));
  }


  @Test
  public void test_obj_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       nullableLongNumber(i -> i > 0),
                                       "b",
                                       optNullableLongNumber(i -> i > 0),
                                       "c",
                                       longNumber(i -> i > 0),
                                       "d",
                                       nullableLongNumber(i -> i < 0),
                                       "e",
                                       optNullableLongNumber(i -> i % 2 == 1),
                                       "f",
                                       nullableLongNumber(i -> i % 2 == 0)
                                      );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsLong.of(3L),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjParser parser = new JsObjParser(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString()));

    final JsObj b = JsObj.of("a",
                             JsLong.of(1L),
                             "b",
                             JsLong.of(2L),
                             "c",
                             JsLong.of(3L),
                             "d",
                             JsLong.of(-5L),
                             "e",
                             JsLong.of(11L),
                             "f",
                             JsLong.of(20L)
                            );


    Assertions.assertEquals(b,
                            parser.parse(b.toString()));
  }

  @Test
  public void test_dec_spec()
  {

    JsObjSpec isdec = JsObjSpec.strict("a",
                                       nullableDecimal(i -> i.longValueExact() > 0),
                                       "b",
                                       optNullableDecimal(i -> i.longValueExact() > 0),
                                       "c",
                                       decimal(i -> i.longValueExact() > 0),
                                       "d",
                                       nullableDecimal(i -> i.longValueExact() < 0),
                                       "e",
                                       optNullableDecimal(i -> i.longValueExact() % 2 == 1),
                                       "f",
                                       nullableDecimal(i -> i.longValueExact() % 2 == 0)
                                      );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsBigDec.of(new BigDecimal(3L)),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjParser parser = new JsObjParser(isdec);
    Assertions.assertEquals(a,
                            parser.parse(a.toString()));

    final JsObj b = JsObj.of("a",
                             JsBigDec.of(new BigDecimal(1L)),
                             "b",
                             JsBigDec.of(new BigDecimal(2L)),
                             "c",
                             JsBigDec.of(new BigDecimal(3L)),

                             "d",
                             JsBigDec.of(new BigDecimal(-5L)),

                             "e",
                             JsBigDec.of(new BigDecimal(1L)),
                             "f",
                             JsBigDec.of(new BigDecimal(20L))
                            );


    Assertions.assertEquals(b,
                            parser.parse(b.toString()));
  }


  @Test
  public void test_integral_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       nullableIntegral(i -> i.longValueExact() > 0),
                                       "b",
                                       optNullableIntegral(i -> i.longValueExact() > 0),
                                       "c",
                                       integral(i -> i.longValueExact() > 0),
                                       "d",
                                       nullableIntegral(i -> i.longValueExact() < 0),
                                       "e",
                                       optNullableIntegral(i -> i.longValueExact() % 2 == 1),
                                       "f",
                                       nullableIntegral(i -> i.longValueExact() % 2 == 0)
                                      );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsInt.of(3),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjParser parser = new JsObjParser(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString()));

    final JsObj b = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsInt.of(2),
                             "c",
                             JsInt.of(3),
                             "d",
                             JsInt.of(-5),
                             "e",
                             JsInt.of(11),
                             "f",
                             JsInt.of(20)
                            );


    Assertions.assertEquals(b,
                            parser.parse(b.toString()));
  }

  @Test
  public void test_number_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       nullableNumber(JsValue::isDecimal),
                                       "b",
                                       optNullableNumber(JsValue::isIntegral),
                                       "c",
                                       number(JsValue::isIntegral),
                                       "d",
                                       nullableNumber(JsValue::isIntegral),
                                       "e",
                                       optNullableNumber(JsValue::isDecimal),
                                       "f",
                                       nullableNumber(JsValue::isIntegral),
                                       "g",
                                       nullableArray
                                      );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsInt.of(3),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL,
                             "g",
                             JsNull.NULL
                            );
    final JsObjParser parser = new JsObjParser(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString()));

    final JsObj b = JsObj.of("a",
                             JsDouble.of(1.5),
                             "b",
                             JsInt.of(2),
                             "c",
                             JsInt.of(3),
                             "d",
                             JsInt.of(-5),
                             "e",
                             JsDouble.of(11.5),
                             "f",
                             JsInt.of(20),
                             "g",
                             JsArray.empty()
                            );


    Assertions.assertEquals(b,
                            parser.parse(b.toString()));
  }

  @Test
  public void test_string_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       nullableStr(i -> i.length() > 3),
                                       "b",
                                       optNullableStr(i -> i.length() > 3),
                                       "c",
                                       str(i -> i.length() > 3),
                                       "d",
                                       nullableStr(i -> i.length() > 2),
                                       "e",
                                       optNullableStr(i -> i.length() == 1),
                                       "f",
                                       nullableStr(i -> i.length() % 2 == 0)
                                      );

    final JsObj a = JsObj.of("a",
                             JsNull.NULL,
                             "c",
                             JsStr.of("abcd"),
                             "d",
                             JsNull.NULL,
                             "f",
                             JsNull.NULL
                            );
    final JsObjParser parser = new JsObjParser(isint);
    Assertions.assertEquals(a,
                            parser.parse(a.toString()));

    final JsObj b = JsObj.of("a",
                             JsStr.of("abcd"),
                             "b",
                             JsStr.of("abcd"),
                             "c",
                             JsStr.of("abcd"),
                             "d",
                             JsStr.of("abcd"),
                             "e",
                             JsStr.of("a"),
                             "f",
                             JsStr.of("abcd")
                            );


    Assertions.assertEquals(b,
                            parser.parse(b.toString()));
  }

}
