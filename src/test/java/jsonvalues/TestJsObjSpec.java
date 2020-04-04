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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isStr
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isStr(s -> s.startsWith("h"))
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isInt
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isInt(n -> n % 2 == 0)
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isLong
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isLong(l -> l % 2 == 1)
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isDecimal
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isDecimal(d -> d.longValueExact() == Long.MAX_VALUE)
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isBool
                                       );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isTrue
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isFalse
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isIntegral,
                                        "b",
                                        isIntegral,
                                        "c",
                                        isIntegral
                                       );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsInt.of(1),
                                                      "b",
                                                      JsLong.of(2),
                                                      "c",
                                                      JsBigInt.of(new BigInteger("100"))
                                                     )
                                            );

    Assertions.assertTrue(error.isEmpty());


  }

  @Test
  public void testIsNumberSpecReturnError()
  {

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isIntegral
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isObj
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isArray
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

    final JsObjSpec spec = JsObjSpec.of("a",
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
                                        JsObjSpec.of("a",
                                                     isStr,
                                                     "b",
                                                     isInt,
                                                     "c",
                                                     JsArraySpec.of(isStr,
                                                                    isInt)
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
                                                                          JsInt.of(1))
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }


  @Test
  public void testIsArrayOfPrimitivesSpecs()
  {

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isArrayOfInt,
                                        "b",
                                        isArrayOfStr,
                                        "c",
                                        isArrayOfLong,
                                        "d",
                                        isArrayOfDec,
                                        "e",
                                        isArrayOfIntegral,
                                        "f",
                                        JsObjSpec.of("a",
                                                     isArrayOfNumber,
                                                     "b",
                                                     isArrayOfObj,
                                                     "c",
                                                     isArrayOfBool
                                                    )
                                       );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsArray.of(1,
                                                                 2,
                                                                 3),
                                                      "b",
                                                      JsArray.of("a",
                                                                 "b"),
                                                      "c",
                                                      JsArray.of(100L,
                                                                 200L),
                                                      "d",
                                                      JsArray.of(1.2,
                                                                 1.3),
                                                      "e",
                                                      JsArray.of(BigInteger.TEN,
                                                                 BigInteger.TEN),
                                                      "f",
                                                      JsObj.of("a",
                                                               JsArray.of(JsInt.of(1),
                                                                          JsDouble.of(1.5)),
                                                               "b",
                                                               JsArray.of(JsObj.empty(),
                                                                          JsObj.empty()),
                                                               "c",
                                                               JsArray.of(true,
                                                                          false,
                                                                          true)
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }


  @Test
  public void testIsArrayOfTestedPrimitivesSpecs()
  {

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isArrayOfInt(i -> i > 0),
                                        "b",
                                        isArrayOfStr(s -> s.startsWith("a")),
                                        "c",
                                        isArrayOfLong(i -> i < 0),
                                        "d",
                                        isArrayOfDec(b -> b.doubleValue() > 1.5),
                                        "e",
                                        isArrayOfIntegral(i -> i.longValue() < 100),
                                        "f",
                                        JsObjSpec.of("a",
                                                     isArrayOfNumber(JsValue::isInt),
                                                     "b",
                                                     isArrayOfObj(JsObj::isEmpty),
                                                     "c",
                                                     JsArraySpec.of(isArrayOfStrSuchThat(a -> a.size() > 2),
                                                                    isArrayOfIntSuchThat(a -> a.size() > 1),
                                                                    isArrayOfLongSuchThat(a -> a.containsValue(JsLong.of(10))),
                                                                    isArrayOfDecSuchThat(a -> a.size() == 1)
                                                                   )
                                                    )
                                       );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsArray.of(1,
                                                                 2,
                                                                 3),
                                                      "b",
                                                      JsArray.of("a",
                                                                 "abc"),
                                                      "c",
                                                      JsArray.of(-100L,
                                                                 -200L),
                                                      "d",
                                                      JsArray.of(2.0,
                                                                 3.0),
                                                      "e",
                                                      JsArray.of(BigInteger.TEN,
                                                                 BigInteger.TEN),
                                                      "f",
                                                      JsObj.of("a",
                                                               JsArray.of(JsInt.of(1),
                                                                          JsInt.of(5)),
                                                               "b",
                                                               JsArray.of(JsObj.empty(),
                                                                          JsObj.empty()),
                                                               "c",
                                                               JsArray.of(JsArray.of("a",
                                                                                     "b",
                                                                                     "c"),
                                                                          JsArray.of(1,
                                                                                     2),
                                                                          JsArray.of(10L),
                                                                          JsArray.of(1.2)
                                                                         )
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
    final JsObjSpec spec = JsObjSpec.of("a",
                                        isInt
                                       );
    final Set<JsErrorPair> error = spec.test(obj);

    assertErrorIs(error,
                  SPEC_MISSING,
                  JsStr.of("a"),
                  JsPath.fromKey("b")
                 );

  }


}
