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
                                                             JsArraySpec.of(number(JsValue::isDecimal),
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
                                                             JsArraySpec.of(number(JsValue::isDecimal),
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
                                            intNumber(false,
                                                      true
                                                     ),
                                            "b",
                                            str(false,
                                                true
                                               ),
                                            "c",
                                            longNumber(false,
                                                       true
                                                      ),
                                            "d",
                                            obj(false,
                                                true
                                               ),
                                            "e",
                                            array(false,
                                                  true
                                                 ),
                                            "f",
                                            bool(false,
                                                 true
                                                ),
                                            "g",
                                            decimal(false,
                                                    true
                                                   ),
                                            "h",
                                            integral(false,
                                                     true
                                                    ),
                                            "i",
                                            JsArraySpec.of(arrayOfInt(true,
                                                                      true
                                                                     ),
                                                           arrayOfLong(true,
                                                                       true
                                                                      )
                                                          ),
                                            "j",
                                            obj(false,
                                                true
                                               ),
                                            "k",
                                            obj(false,
                                                true,
                                                  a -> a.keySet()
                                                        .equals(2)
                                               ),
                                            "j",
                                            arrayOfObj(false,
                                                       true,
                                                         a -> a.keySet()
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
                                             str(false,
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
