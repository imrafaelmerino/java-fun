package jsonvalues;

import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjParser
{


  @Test
  public void test_parse_obj_all_primitive_types()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            isInt,
                                            "b",
                                            isStr,
                                            "c",
                                            isLong,
                                            "d",
                                            isBool,
                                            "e",
                                            isTrue,
                                            "f",
                                            isFalse,
                                            "g",
                                            isDecimal,
                                            "h",
                                            isIntegral,
                                            "i",
                                            JsObjSpec.strict("a",
                                                             isNumber,
                                                             "b",
                                                             isArray,
                                                             "c",
                                                             isObj,
                                                             "d",
                                                             isInt(i -> i % 2 == 0),
                                                             "e",
                                                             isLong(i -> i % 2 == 1),
                                                             "f",
                                                             isStr(s -> s.startsWith("a")),
                                                             "g",
                                                             isDecimal(d -> d.doubleValue() > 1.0),
                                                             "h",
                                                             isObj(o -> o.containsKey("b")),
                                                             "i",
                                                             isArray(a -> a.head()
                                                                           .equals(JsStr.of("first"))),
                                                             "j",
                                                             JsArraySpec.of(isNumber(JsValue::isDecimal),
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
                                            isArrayOfInt,
                                            "b",
                                            isArrayOfStr,
                                            "c",
                                            isArrayOfBool,
                                            "d",
                                            isArrayOfDec,
                                            "e",
                                            isArrayOfNumber,
                                            "f",
                                            isArrayOfIntegral,
                                            "g",
                                            isArrayOfLong,
                                            "h",
                                            JsObjSpec.strict("a",
                                                             isArrayOfInt(v -> v > 5),
                                                             "b",
                                                             isArrayOfStr(s -> s.startsWith("a")),
                                                             "c",
                                                             isArrayOfLong(l -> l < 10)
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
                                            isInt(i -> i > 0),
                                            "b",
                                            isStr(a -> a.length() > 0),
                                            "c",
                                            isLong(c -> c > 0),
                                            "d",
                                            isBool,
                                            "e",
                                            isTrue,
                                            "f",
                                            isFalse,
                                            "g",
                                            isDecimal(d -> d.doubleValue() > 0.0),
                                            "h",
                                            isIntegral(i -> i.longValueExact() % 2 == 0),
                                            "i",
                                            JsObjSpec.strict("a",
                                                             isNumber,
                                                             "b",
                                                             isArray,
                                                             "c",
                                                             isObj(JsObj::isEmpty),
                                                             "d",
                                                             isInt(i -> i % 2 == 0),
                                                             "e",
                                                             isLong(i -> i % 2 == 1),
                                                             "f",
                                                             isStr(s -> s.startsWith("a")),
                                                             "g",
                                                             isDecimal(d -> d.doubleValue() > 1.0),
                                                             "h",
                                                             isObj(o -> o.containsKey("b")),
                                                             "i",
                                                             isArray(a -> a.head()
                                                                           .equals(JsStr.of("first"))),
                                                             "j",
                                                             JsArraySpec.of(isNumber(JsValue::isDecimal),
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
                                            isInt(false,
                                                  true
                                                 ),
                                            "b",
                                            isStr(false,
                                                  true
                                                 ),
                                            "c",
                                            isLong(false,
                                                   true
                                                  ),
                                            "d",
                                            isObj(false,
                                                  true
                                                 ),
                                            "e",
                                            isArray(false,
                                                    true
                                                   ),
                                            "f",
                                            isBool(false,
                                                   true
                                                  ),
                                            "g",
                                            isDecimal(false,
                                                      true
                                                     ),
                                            "h",
                                            isIntegral(false,
                                                       true
                                                      ),
                                            "i",
                                            JsArraySpec.of(isArrayOfInt(true,
                                                                        true
                                                                       ),
                                                           isArrayOfLong(true,
                                                                         true
                                                                        )
                                                          ),
                                            "j",
                                            isObj(false,
                                                  true
                                                 ),
                                            "k",
                                            isObj(false,
                                                  true,
                                                  a -> a.fields()
                                                        .equals(2)
                                                 ),
                                            "j",
                                            isArrayOfObj(false,
                                                         true,
                                                         a -> a.fields()
                                                               .size() == 2
                                                        )
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
                                             isStr(false,
                                                   true)
                                            );


    final JsObj obj = JsObj.of("b",
                               JsStr.of("b")
                              );

    final JsObj parsed = new JsObjParser(spec).parse(obj.toPrettyString());

    Assertions.assertEquals(obj,
                            parsed);
  }


}
