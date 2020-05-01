package jsonvalues;

import com.dslplatform.json.parsers.JsParserException;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjParser
{

  @Test
  public void test_parse_obj_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       obj(a -> a.containsKey("a"))
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsObj.of("b",
                                                                 JsBool.TRUE
                                                                )
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_int_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       intNumber(i -> i > 0)
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsInt.of(-1)
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_long_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       longNumber(i -> i > 0)
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsLong.of(-1L)
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_integral_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       integral(i -> i.longValueExact() > 0)
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsLong.of(-1L)
                                                       )
                                                    .toPrettyString())
                           );

  }


  @Test
  public void test_parse_number_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       number(JsValue::isDecimal)
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsBool.TRUE
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_string_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       str(i -> i.length() == 3)
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsStr.of("ab")
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_decimal_error()
  {
    JsObjSpec spec = JsObjSpec.lenient("a",
                                       decimal(i -> i.divide(BigDecimal.TEN)
                                                     .equals(new BigDecimal(1)))
                                      );

    JsObjParser parser = new JsObjParser(spec);


    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse(JsObj.of("a",
                                                        JsBigDec.of(BigDecimal.ONE)
                                                       )
                                                    .toPrettyString())
                           );

  }

  @Test
  public void test_parse_obj_all_primitive_types()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            intNum,
                                            "b",
                                            str,
                                            "c",
                                            longNum,
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
                                                             arraySuchThat(a -> a.head()
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
                                                             arrayOfLong(l -> l < 10),
                                                             "d",
                                                             arrayOfLong(i -> i > 0).nullable(),
                                                             "e",
                                                             arrayOfLong(i -> i > 0).nullable(),
                                                             "f",
                                                             arrayOfLong(i -> i > 0).optional().nullable(),
                                                             "g",
                                                             arrayOfLong(i -> i > 0).optional().nullable()
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
                                                  ),
                                        "d",
                                        JsArray.of(1,
                                                   2
                                                  ),
                                        "e",
                                        JsNull.NULL,
                                        "f",
                                        JsArray.of(4)

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
                                                             arraySuchThat(a -> a.head()
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
                                            intNum.optional().nullable(),
                                            "b",
                                            str.optional().nullable(),
                                            "c",
                                            longNum.optional().nullable(),
                                            "d",
                                            obj.nullable().optional(),
                                            "e",
                                            array.nullable().optional(),
                                            "f",
                                            bool.nullable().optional(),
                                            "g",
                                            decimal.nullable().optional(),
                                            "h",
                                            integral.optional().nullable(),
                                            "i",
                                            JsArraySpec.tuple(arrayOfInt.optional().nullable(),
                                                              arrayOfLong.nullable().optional()
                                                             ).optional(),
                                            "j",
                                            obj.optional().nullable(),
                                            "k",
                                            obj(a -> a.keySet()
                                                                 .size() == 2).optional().nullable(),
                                            "j",
                                            arrayOfObj(a -> a.keySet()
                                                                        .size() == 2).optional().nullable()
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
                                             str.optional().nullable()
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
                                       intNumber(i -> i > 0).nullable(),
                                       "b",
                                       intNumber(i -> i > 0).optional().nullable(),
                                       "c",
                                       intNumber(i -> i > 0),
                                       "d",
                                       intNumber(i -> i < 0).nullable(),
                                       "e",
                                       intNumber(i -> i % 2 == 1).optional().nullable(),
                                       "f",
                                       intNumber(i -> i % 2 == 0).nullable()
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
                            parser.parse(a.toString())
                           );

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
                            parser.parse(b.toString())
                           );
  }


  @Test
  public void test_obj_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       longNumber(i -> i > 0).nullable(),
                                       "b",
                                       longNumber(i -> i > 0).nullable().optional(),
                                       "c",
                                       longNumber(i -> i > 0),
                                       "d",
                                       longNumber(i -> i < 0).nullable(),
                                       "e",
                                       longNumber(i -> i % 2 == 1).optional().nullable(),
                                       "f",
                                       longNumber(i -> i % 2 == 0).nullable()
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
                            parser.parse(a.toString())
                           );

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
                            parser.parse(b.toString())
                           );
  }

  @Test
  public void test_dec_spec()
  {

    JsObjSpec isdec = JsObjSpec.strict("a",
                                       decimal(i -> i.longValueExact() > 0).nullable(),
                                       "b",
                                       decimal(i -> i.longValueExact() > 0).optional().nullable(),
                                       "c",
                                       decimal(i -> i.longValueExact() > 0),
                                       "d",
                                       decimal(i -> i.longValueExact() < 0).nullable(),
                                       "e",
                                       decimal(i -> i.longValueExact() % 2 == 1).optional().nullable(),
                                       "f",
                                       decimal(i -> i.longValueExact() % 2 == 0).nullable()
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
                            parser.parse(a.toString())
                           );

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
                            parser.parse(b.toString())
                           );
  }


  @Test
  public void test_integral_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       integral(i -> i.longValueExact() > 0).nullable(),
                                       "b",
                                       integral(i -> i.longValueExact() > 0).optional().nullable(),
                                       "c",
                                       integral(i -> i.longValueExact() > 0),
                                       "d",
                                       integral(i -> i.longValueExact() < 0).nullable(),
                                       "e",
                                       integral(i -> i.longValueExact() % 2 == 1).optional().nullable(),
                                       "f",
                                       integral(i -> i.longValueExact() % 2 == 0).nullable()
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
                            parser.parse(a.toString())
                           );

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
                            parser.parse(b.toString())
                           );
  }

  @Test
  public void test_number_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       number(JsValue::isDecimal).nullable(),
                                       "b",
                                       number(JsValue::isIntegral).nullable().optional(),
                                       "c",
                                       number(JsValue::isIntegral),
                                       "d",
                                       number(JsValue::isIntegral).nullable(),
                                       "e",
                                       number(JsValue::isDecimal).optional().nullable(),
                                       "f",
                                       number(JsValue::isIntegral).nullable(),
                                       "g",
                                       array.nullable()
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
                            parser.parse(a.toString())
                           );

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
                            parser.parse(b.toString())
                           );
  }

  @Test
  public void test_string_spec()
  {

    JsObjSpec isint = JsObjSpec.strict("a",
                                       str(i -> i.length() > 3).nullable(),
                                       "b",
                                       str(i -> i.length() > 3).nullable().optional(),
                                       "c",
                                       str(i -> i.length() > 3),
                                       "d",
                                       str(i -> i.length() > 2).nullable(),
                                       "e",
                                       str(i -> i.length() == 1).optional().nullable(),
                                       "f",
                                       str(i -> i.length() % 2 == 0).nullable()
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
                            parser.parse(a.toString())
                           );

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
                            parser.parse(b.toString())
                           );
  }

  @Test
  public void testArrayOfObjSpec()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      JsArraySpec.tuple(str,
                                                        bool).nullable()
                                     );

    JsObjParser parser = new JsObjParser(spec);

    final JsObj a = JsObj.of("a",
                              JsArray.of(JsStr.of("a"),
                                         JsBool.TRUE
                                        )
                             );

    final JsObj b = JsObj.of("a",
                            JsNull.NULL
                            );
    Assertions.assertEquals(a,parser.parse(a.toString()));
    Assertions.assertEquals(b,parser.parse(b.toString()));
  }


  @Test
  public void test_value_parser(){

    JsObjSpec spec = JsObjSpec.strict("a",any(JsValue::isBool));

    JsObjParser parser = new JsObjParser(spec);

    Assertions.assertThrows(JsParserException.class, ()->parser.parse(JsObj.of("a",
                                                                               JsStr.of("a"))
                                                                           .toString()
                                                                     )
                           );


  }

  @Test
  public void test_array_value_parser(){

    JsObjSpec spec = JsObjSpec.strict("a",array(a->a.isBool() || a.isIntegral()));

    JsObjParser parser = new JsObjParser(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(JsBool.FALSE,
                                        JsInt.of(1))
                            );
    Assertions.assertEquals(a,parser.parse(a.toString()));

  }

  @Test
  public void array_of_number(){
    JsObjSpec spec = JsObjSpec.strict("a",arrayOfNumber(a->a.isDecimal()),
                                      "b",arrayOfNumber(a->a.isDecimal()).nullable(),
                                      "c",arrayOfNumber(a->a.isIntegral()).optional(),
                                      "e",arrayOfNumber(a->a.isDecimal()).optional());

    JsObjParser parser = new JsObjParser(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(1.5,
                                        1.7),
                             "b",JsNull.NULL,
                             "c",JsArray.of(1,2,3)
                            );
    Assertions.assertEquals(a,parser.parse(a.toString()));
  }

  @Test
  public void array_of_number_with_predicate(){
    JsObjSpec spec = JsObjSpec.strict("a",arrayOfIntegral(a->a.longValueExact()>0),
                                      "b",arrayOfIntegral(a->a.longValueExact() < 0).nullable(),
                                      "c",arrayOfIntegral(a->a.longValueExact() % 2== 0).optional(),
                                      "e",arrayOfIntegral(a->a.longValueExact() % 3 == 0).optional());

    JsObjParser parser = new JsObjParser(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(1,
                                        2),
                             "b",JsNull.NULL,
                             "c",JsArray.of(2,4,6)
                            );
    Assertions.assertEquals(a,parser.parse(a.toString()));
  }

  @Test
  public void array_of_decimal_with_predicate(){
    JsObjSpec spec = JsObjSpec.strict("a",arrayOfDec(a->a.longValueExact()>0),
                                      "b",arrayOfDec(a->a.longValueExact() < 0).nullable(),
                                      "c",arrayOfDec(a->a.longValueExact() % 2== 0).optional(),
                                      "e",arrayOfDec(a->a.longValueExact() % 3 == 0).optional().nullable());

    JsObjParser parser = new JsObjParser(spec);

    final JsObj a = JsObj.of("a",
                             JsArray.of(15,
                                        25),
                             "b",JsNull.NULL,
                             "c",JsArray.of(24,46)
                            );
    Assertions.assertEquals(a,parser.parse(a.toString()));
  }

  @Test
  public void test_number_error(){
    JsObjSpec spec = JsObjSpec.strict("a",JsSpecs.number(a -> a.isDouble()));
    JsObjParser parser = new JsObjParser(spec);

    Assertions.assertThrows(JsParserException.class, ()->parser.parse(JsObj.of("a", JsInt.of(1)).toString()));

  }


}
