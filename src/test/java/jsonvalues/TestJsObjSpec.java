package jsonvalues;

import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static jsonvalues.Functions.assertErrorIs;
import static jsonvalues.spec.ERROR_CODE.*;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjSpec
{

  @Test
  public void testIsStrSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            str
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsInt.of(1)
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            STRING_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsInt.of(1)
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );

  }

  @Test
  public void testIsStrPredicateSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            str(s -> s.startsWith("h"))
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("bye")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  STRING_CONDITION,
                  JsStr.of("bye"),
                  JsPath.fromKey("a")
                 );

  }


  @Test
  public void testIsIntSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            intNumber
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  INT_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsIntPredicateSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            intNumber(n -> n % 2 == 0)
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsInt.of(5)
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            INT_CONDITION
                           );

    Assertions.assertEquals(pair.error.value,
                            JsInt.of(5)
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsLongSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            longNumber
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  LONG_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsLongPredicateSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            longNumber(l -> l % 2 == 1)
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsLong.of(4)
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            LONG_CONDITION
                           );

    Assertions.assertEquals(pair.error.value,
                            JsLong.of(4)
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsDecimalSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            decimal
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  DECIMAL_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsDecimalSpecPredicate()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            decimal(d -> d.longValueExact() == Long.MAX_VALUE)
                                           );


    final JsBigDec bd = JsBigDec.of(new BigDecimal(Long.MAX_VALUE - 1));
    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      bd
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            DECIMAL_CONDITION
                           );

    Assertions.assertEquals(pair.error.value,
                            bd
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsBooleanSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            bool
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     )
                                            );

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  BOOLEAN_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsTrueSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            TRUE
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsBool.FALSE
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            TRUE_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsBool.FALSE
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsFalseSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            FALSE
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsBool.TRUE
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            FALSE_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsBool.TRUE
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsNumberSpec()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            integral,
                                            "b",
                                            integral,
                                            "c",
                                            integral,
                                            "d",
                                            number
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsInt.of(1),
                                                      "b",
                                                      JsLong.of(2),
                                                      "c",
                                                      JsBigInt.of(new BigInteger("100")),
                                                      "d",
                                                      JsDouble.of(3.2)
                                                     )
                                            );

    Assertions.assertTrue(error.isEmpty());


  }

  @Test
  public void testIsNumberSpecReturnError()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            integral
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  INTEGRAL_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsObjectSpecReturnError()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            obj
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  OBJ_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsArraySpecReturnError()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            array
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    assertErrorIs(error,
                  ARRAY_EXPECTED,
                  JsStr.of("a"),
                  JsPath.fromKey("a")
                 );


  }

  @Test
  public void testIsObjSpec()
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
                                            JsObjSpec.strict("a",
                                                             str,
                                                             "b",
                                                             intNumber,
                                                             "c",
                                                             JsArraySpec.tuple(str,
                                                                               intNumber
                                                                              ),
                                                             "d",
                                                             JsSpecs.array(a -> a.head() == JsNull.NULL)
                                                            )
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsInt.of(1),
                                                      "b",
                                                      JsStr.of("hi"),
                                                      "c",
                                                      JsLong.of(100),
                                                      "d",
                                                      JsBool.TRUE,
                                                      "e",
                                                      JsBool.TRUE,
                                                      "f",
                                                      JsObj.of("a",
                                                               JsStr.of("hi"),
                                                               "b",
                                                               JsInt.of(1),
                                                               "c",
                                                               JsArray.of(JsStr.of("a"),
                                                                          JsInt.of(1)
                                                                         ),
                                                               "d",
                                                               JsArray.of(JsNull.NULL,
                                                                          JsInt.of(1)
                                                                         )
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }


  @Test
  public void testIsArrayOfPrimitivesSpecs()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            arrayOfInt,
                                            "b",
                                            arrayOfStr,
                                            "c",
                                            arrayOfLong,
                                            "d",
                                            arrayOfDec,
                                            "e",
                                            arrayOfIntegral,
                                            "f",
                                            JsObjSpec.strict("a",
                                                             arrayOfNumber,
                                                             "b",
                                                             arrayOfObj,
                                                             "c",
                                                             arrayOfBool
                                                            )
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsArray.of(1,
                                                                 2,
                                                                 3
                                                                ),
                                                      "b",
                                                      JsArray.of("a",
                                                                 "b"
                                                                ),
                                                      "c",
                                                      JsArray.of(100L,
                                                                 200L
                                                                ),
                                                      "d",
                                                      JsArray.of(1.2,
                                                                 1.3
                                                                ),
                                                      "e",
                                                      JsArray.of(BigInteger.TEN,
                                                                 BigInteger.TEN
                                                                ),
                                                      "f",
                                                      JsObj.of("a",
                                                               JsArray.of(JsInt.of(1),
                                                                          JsDouble.of(1.5)
                                                                         ),
                                                               "b",
                                                               JsArray.of(JsObj.empty(),
                                                                          JsObj.empty()
                                                                         ),
                                                               "c",
                                                               JsArray.of(true,
                                                                          false,
                                                                          true
                                                                         )
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }


  @Test
  public void testIsArrayOfTestedPrimitivesSpecs()
  {

    final JsObjSpec spec = JsObjSpec.strict("a",
                                            arrayOfInt(i -> i > 0),
                                            "b",
                                            arrayOfStr(s -> s.startsWith("a")),
                                            "c",
                                            arrayOfLong(i -> i < 0),
                                            "d",
                                            arrayOfDec(b -> b.doubleValue() > 1.5),
                                            "e",
                                            arrayOfIntegral(i -> i.longValue() < 100),
                                            "f",
                                            JsObjSpec.strict("a",
                                                             arrayOfNumber(JsValue::isInt),
                                                             "b",
                                                             arrayOfObj(JsObj::isEmpty),
                                                             "c",
                                                             JsArraySpec.tuple(arrayOfStrSuchThat(a -> a.size() > 2),
                                                                               arrayOfIntSuchThat(a -> a.size() > 1),
                                                                               arrayOfLongSuchThat(a -> a.containsValue(JsLong.of(10))),
                                                                               arrayOfDecSuchThat(a -> a.size() == 1)
                                                                              ),
                                                             "d",
                                                             integral(i -> i.longValue() > 10),
                                                             "e",
                                                             number(s -> s.isDouble()),
                                                             "f",
                                                             obj(o -> o.isEmpty())
                                                            )
                                           );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsArray.of(1,
                                                                 2,
                                                                 3
                                                                ),
                                                      "b",
                                                      JsArray.of("a",
                                                                 "abc"
                                                                ),
                                                      "c",
                                                      JsArray.of(-100L,
                                                                 -200L
                                                                ),
                                                      "d",
                                                      JsArray.of(2.0,
                                                                 3.0
                                                                ),
                                                      "e",
                                                      JsArray.of(BigInteger.TEN,
                                                                 BigInteger.TEN
                                                                ),
                                                      "f",
                                                      JsObj.of("a",
                                                               JsArray.of(JsInt.of(1),
                                                                          JsInt.of(5)
                                                                         ),
                                                               "b",
                                                               JsArray.of(JsObj.empty(),
                                                                          JsObj.empty()
                                                                         ),
                                                               "c",
                                                               JsArray.of(JsArray.of("a",
                                                                                     "b",
                                                                                     "c"
                                                                                    ),
                                                                          JsArray.of(1,
                                                                                     2
                                                                                    ),
                                                                          JsArray.of(10L),
                                                                          JsArray.of(1.2)
                                                                         ),
                                                               "d",
                                                               JsInt.of(11),
                                                               "e",
                                                               JsDouble.of(2.5),
                                                               "f",
                                                               JsObj.empty()
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }

  @Test
  public void test_value_without_spec_should_return_error()
  {

    final JsObj obj = JsObj.of("a",
                               JsInt.of(1),
                               "b",
                               JsStr.of("a")
                              );
    final JsObjSpec spec = JsObjSpec.strict("a",
                                            intNumber
                                           );
    final Set<JsErrorPair> error = spec.test(obj);

    assertErrorIs(error,
                  SPEC_MISSING,
                  JsStr.of("a"),
                  JsPath.fromKey("b")
                 );

  }


  @Test
  public void test_any_spec()
  {


    JsObjSpec spec = JsObjSpec.strict("a",
                                      any,
                                      "b",
                                      optAny,
                                      "d",
                                      any(JsValue::isStr)
                                     );

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsNull.NULL
                                            ))
                              .isEmpty());
    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsNull.NULL,
                                             "b",
                                             JsBool.TRUE,
                                             "d",
                                             JsStr.of("hi")
                                            ))
                              .isEmpty());
  }

  @Test
  public void test_is_object_spec()
  {


    JsObjSpec spec = JsObjSpec.strict("a",
                                      spec(JsObjSpec.strict("a",
                                                            any,
                                                            "b",
                                                            optAny,
                                                            "d",
                                                            any(JsValue::isStr)
                                                           ))
                                     );


    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsObj.of("a",
                                                      JsNull.NULL,
                                                      "b",
                                                      JsBool.TRUE,
                                                      "d",
                                                      JsStr.of("hi")
                                                     )
                                            ))
                              .isEmpty());
  }

  @Test
  public void test_is_array_spec()
  {


    JsObjSpec spec = JsObjSpec.strict("a",
                                      spec(JsArraySpec.tuple(
                                        any,
                                        intNumber

                                                            ))
                                     );


    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(
                                               JsNull.NULL,
                                               JsInt.of(1)
                                                       )
                                            ))
                              .isEmpty());

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(
                                               JsStr.of("hi"),
                                               JsInt.of(1)
                                                       )
                                            ))
                              .isEmpty());
  }

  @Test
  public void testJsSpec()
  {

    JsObjSpec spec = JsObjSpec.strict("a",
                                      array,
                                      "b",
                                      arrayOfArray,
                                      "c",
                                      arrayOfBool,
                                      "d",
                                      arrayOfDec,
                                      "e",
                                      arrayOfInt,
                                      "f",
                                      arrayOfLong,
                                      "g",
                                      arrayOfIntegral,
                                      "h",
                                      arrayOfNumber,
                                      "i",
                                      arrayOfObj,
                                      "j",
                                      arrayOfStr,
                                      "k",
                                      arrayOf(JsObjSpec.lenient("a",
                                                                bool,
                                                                "b",
                                                                str,
                                                                "c",
                                                                intNumber,
                                                                "d",
                                                                longNumber,
                                                                "e",
                                                                obj,
                                                                "f",
                                                                array,
                                                                "g",
                                                                integral,
                                                                "h",
                                                                decimal,
                                                                "i",
                                                                number
                                                               )
                                             )
                                     );

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(1,
                                                        2),
                                             "b",
                                             JsArray.of(JsArray.empty(),
                                                        JsArray.empty()),
                                             "c",
                                             JsArray.of(true,
                                                        false),
                                             "d",
                                             JsArray.of(1.1,
                                                        1.2),
                                             "e",
                                             JsArray.of(1,
                                                        2,
                                                        3),
                                             "f",
                                             JsArray.of(1L,
                                                        2L),
                                             "g",
                                             JsArray.of(JsInt.of(1),
                                                        JsLong.of(1L),
                                                        JsBigInt.of(BigInteger.TEN)),
                                             "h",
                                             JsArray.of(JsInt.of(1),
                                                        JsLong.of(1L),
                                                        JsBigInt.of(BigInteger.TEN),
                                                        JsBigDec.of(BigDecimal.TEN)),
                                             "i",
                                             JsArray.of(JsObj.empty(),
                                                        JsObj.empty()),
                                             "j",
                                             JsArray.of("a",
                                                        "b"),
                                             "k",
                                             JsArray.of(JsObj.of("a",
                                                                 JsBool.TRUE,
                                                                 "b",
                                                                 JsStr.of("a"),
                                                                 "c",
                                                                 JsInt.of(1),
                                                                 "d",
                                                                 JsLong.of(1L),
                                                                 "e",
                                                                 JsObj.empty(),
                                                                 "f",
                                                                 JsArray.empty(),
                                                                 "g",
                                                                 JsBigInt.of(BigInteger.TEN),
                                                                 "h",
                                                                 JsBigDec.of(BigDecimal.TEN),
                                                                 "i",
                                                                 JsLong.of(1L)
                                                                )
                                                       )
                                            ))
                              .isEmpty()
                         );
  }

  @Test
  public void testNullableJsSpec()
  {

    JsObjSpec spec = JsObjSpec.strict("a",
                                      nullableArray,
                                      "b",
                                      nullableArrayOfArray,
                                      "c",
                                      nullableArrayOfBool,
                                      "d",
                                      nullableArrayOfDec,
                                      "e",
                                      nullableArrayOfInt,
                                      "f",
                                      nullableArrayOfLong,
                                      "g",
                                      nullableArrayOfIntegral,
                                      "h",
                                      nullableArrayOfNumber,
                                      "i",
                                      nullableArrayOfObj,
                                      "j",
                                      nullableArrayOfStr,
                                      "k",
                                      nullableArrayOf(JsObjSpec.lenient("a",
                                                                        nullableBool,
                                                                        "b",
                                                                        nullableStr,
                                                                        "c",
                                                                        nullableIntNumber,
                                                                        "d",
                                                                        nullableLongNumber,
                                                                        "e",
                                                                        nullableObj,
                                                                        "f",
                                                                        nullableArray,
                                                                        "g",
                                                                        nullableIntegral,
                                                                        "h",
                                                                        nullableDecimal,
                                                                        "i",
                                                                        nullableNumber
                                                                       )
                                                     )
                                     );

    final Set<JsErrorPair> result = spec.test(JsObj.of("a",
                                                       JsNull.NULL,
                                                       "b",
                                                       JsNull.NULL,
                                                       "c",
                                                       JsNull.NULL,
                                                       "d",
                                                       JsNull.NULL,
                                                       "e",
                                                       JsNull.NULL,
                                                       "f",
                                                       JsNull.NULL,
                                                       "g",
                                                       JsNull.NULL,
                                                       "h",
                                                       JsNull.NULL,
                                                       "i",
                                                       JsNull.NULL,
                                                       "j",
                                                       JsNull.NULL,
                                                       "k",
                                                       JsNull.NULL
                                                      )
                                             );

    System.out.println(result);

    Assertions.assertTrue(result
                            .isEmpty()
                         );
  }

  @Test
  public void testOptionalNullableJsSpec()
  {

    JsObjSpec spec = JsObjSpec.strict("a",
                                      optNullableArray,
                                      "b",
                                      optNullableArrayOfArray,
                                      "c",
                                      optNullableArrayOfBool,
                                      "d",
                                      optNullableArrayOfDec,
                                      "e",
                                      optNullableArrayOfInt,
                                      "f",
                                      optNullableArrayOfLong,
                                      "g",
                                      optNullableArrayOfIntegral,
                                      "h",
                                      optNullableArrayOfNumber,
                                      "i",
                                      optNullableArrayOfObj,
                                      "j",
                                      optNullableArrayOfStr,
                                      "k",
                                      optNullableArrayOf(JsObjSpec.lenient("a",
                                                                           optNullableBool,
                                                                           "b",
                                                                           optNullableStr,
                                                                           "c",
                                                                           optNullableIntNumber,
                                                                           "d",
                                                                           optNullableLongNumber,
                                                                           "e",
                                                                           optNullableObj,
                                                                           "f",
                                                                           optNullableArray,
                                                                           "g",
                                                                           optNullableIntegral,
                                                                           "h",
                                                                           optNullableDecimal,
                                                                           "i",
                                                                           optNullableNumber
                                                                          )
                                                        )
                                     );

    final Set<JsErrorPair> result = spec.test(JsObj.of("a",
                                                       JsNull.NULL,
                                                       "b",
                                                       JsNull.NULL,
                                                       "c",
                                                       JsNull.NULL,
                                                       "d",
                                                       JsNull.NULL,
                                                       "e",
                                                       JsNull.NULL,
                                                       "f",
                                                       JsNull.NULL,
                                                       "g",
                                                       JsNull.NULL,
                                                       "h",
                                                       JsNull.NULL,
                                                       "i",
                                                       JsNull.NULL,
                                                       "j",
                                                       JsNull.NULL,
                                                       "k",
                                                       JsNull.NULL
                                                      )
                                             );


    Assertions.assertTrue(result
                            .isEmpty()
                         );


    Assertions.assertTrue(spec.test(JsObj.empty())
                              .isEmpty());


    final Set<JsErrorPair> result1 = spec.test(JsObj.of("a",
                                                        JsNull.NULL,
                                                        "b",
                                                        JsNull.NULL,
                                                        "c",
                                                        JsNull.NULL,
                                                        "d",
                                                        JsNull.NULL,
                                                        "e",
                                                        JsNull.NULL,
                                                        "f",
                                                        JsNull.NULL,
                                                        "g",
                                                        JsNull.NULL,
                                                        "h",
                                                        JsNull.NULL,
                                                        "i",
                                                        JsNull.NULL,
                                                        "j",
                                                        JsNull.NULL,
                                                        "k",
                                                        JsArray.of(JsObj.of("a",
                                                                            JsNull.NULL,
                                                                            "b",
                                                                            JsNull.NULL,
                                                                            "c",
                                                                            JsNull.NULL,
                                                                            "d",
                                                                            JsNull.NULL,
                                                                            "e",
                                                                            JsNull.NULL,
                                                                            "f",
                                                                            JsNull.NULL,
                                                                            "g",
                                                                            JsNull.NULL,
                                                                            "h",
                                                                            JsNull.NULL,
                                                                            "i",
                                                                            JsNull.NULL
                                                                           )
                                                                  )
                                                       )
                                              );

    Assertions.assertTrue(result1.isEmpty());
  }


  @Test
  public void testOptionalJsSpec()
  {

    JsObjSpec spec = JsObjSpec.strict("a",
                                      optArray,
                                      "b",
                                      optArrayOfArray,
                                      "c",
                                      optArrayOfBool,
                                      "d",
                                      optArrayOfDec,
                                      "e",
                                      optArrayOfInt,
                                      "f",
                                      optArrayOfLong,
                                      "g",
                                      optArrayOfIntegral,
                                      "h",
                                      optArrayOfNumber,
                                      "i",
                                      optArrayOfObj,
                                      "j",
                                      optArrayOfStr,
                                      "k",
                                      optArrayOf(JsObjSpec.lenient("a",
                                                                   optBool,
                                                                   "b",
                                                                   optStr,
                                                                   "c",
                                                                   optIntNumber,
                                                                   "d",
                                                                   optLongNumber,
                                                                   "e",
                                                                   optObj,
                                                                   "f",
                                                                   optArray,
                                                                   "g",
                                                                   optIntegral,
                                                                   "h",
                                                                   optDecimal,
                                                                   "i",
                                                                   optNumber
                                                                  )
                                                )
                                     );


    Assertions.assertTrue(spec.test(JsObj.empty())
                              .isEmpty());
    Assertions.assertTrue(spec.test(JsObj.of("k",
                                             JsArray.of(JsObj.empty())))
                              .isEmpty());
  }


  @Test
  public void testIsNullableObjSpec()
  {

    final JsObjSpec objSpec = JsObjSpec.lenient("a",
                                                str
                                               );
    JsObjSpec spec = JsObjSpec.strict("a",
                                      spec(objSpec),
                                      "b",
                                      nullableSpec(objSpec),
                                      "c",
                                      optSpec(objSpec),
                                      "d",
                                      optNullableSpec(objSpec)
                                     );

    final Set<JsErrorPair> set = spec.test(JsObj.of("a",
                                                    JsObj.of("a",
                                                             JsStr.of("a"),
                                                             "b",
                                                             JsBool.TRUE
                                                            ),
                                                    "b",JsNull.NULL
                                                   ));

    System.out.println(set);

    Assertions.assertTrue(set.isEmpty());

  }

}
