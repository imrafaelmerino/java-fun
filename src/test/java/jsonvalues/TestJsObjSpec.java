package jsonvalues;

import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import static jsonvalues.spec.ERROR_CODE.*;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjSpec
{

  @Test
  public void testIsStrSpec()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
                                         isStr(s -> s.startsWith("h"))
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("bye")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            STRING_CONDITION
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("bye")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );

  }


  @Test
  public void testIsIntSpec()
  {

    final JsObjSpec spec = new JsObjSpec("a",
                                         isInt
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            INT_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsIntPredicateSpec()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
                                         isLong
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            LONG_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsLongPredicateSpec()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
                                         isDecimal
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            DECIMAL_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsDecimalSpecPredicate()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
                                         isBool
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            BOOLEAN_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsTrueSpec()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
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

    final JsObjSpec spec = new JsObjSpec("a",
                                         isIntegral
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            INTEGRAL_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsObjectSpecReturnError()
  {

    final JsObjSpec spec = new JsObjSpec("a",
                                         isObj
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            OBJ_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsArraySpecReturnError()
  {

    final JsObjSpec spec = new JsObjSpec("a",
                                         isArray
    );


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")
                                                     ));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst()
                                  .get();

    Assertions.assertEquals(pair.error.code,
                            ARRAY_EXPECTED
                           );

    Assertions.assertEquals(pair.error.value,
                            JsStr.of("a")
                           );

    Assertions.assertEquals(pair.path,
                            JsPath.fromKey("a")
                           );


  }

  @Test
  public void testIsObjSpec()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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
                                         new JsObjSpec("a",
                                                       isStr,
                                                       "b",
                                                       isInt,
                                                       "c", JsArraySpec.of(isStr,isInt)
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
                                                               "c",JsArray.of(JsStr.of("a"),JsInt.of(1))
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }


  @Test
  public void testIsArrayOfPrimitivesSpecs()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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
                                         new JsObjSpec("a",
                                                       isArrayOfNumber,
                                                       "b",
                                                       isArrayOfObj,
                                                       "c",
                                                       isArrayOfBool
                                         )
    );



    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsArray.of(1,2,3),
                                                      "b",
                                                      JsArray.of("a","b"),
                                                      "c",
                                                      JsArray.of(100L,200L),
                                                      "d",
                                                      JsArray.of(1.2,1.3),
                                                      "e",
                                                      JsArray.of(BigInteger.TEN,BigInteger.TEN),
                                                      "f",
                                                      JsObj.of("a",
                                                               JsArray.of(JsInt.of(1),JsDouble.of(1.5)),
                                                               "b",
                                                               JsArray.of(JsObj.empty(),JsObj.empty()),
                                                               "c",
                                                               JsArray.of(true,false,true)
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }


  @Test
  public void testIsArrayOfTestedPrimitivesSpecs()
  {

    final JsObjSpec spec = new JsObjSpec("a",
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
                                         new JsObjSpec("a",
                                                       isArrayOfNumber(JsValue::isInt),
                                                       "b",
                                                       isArrayOfObj(JsObj::isEmpty)
                                         )
    );



    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsArray.of(1,2,3),
                                                      "b",
                                                      JsArray.of("a","abc"),
                                                      "c",
                                                      JsArray.of(-100L,-200L),
                                                      "d",
                                                      JsArray.of(2.0,3.0),
                                                      "e",
                                                      JsArray.of(BigInteger.TEN,BigInteger.TEN),
                                                      "f",
                                                      JsObj.of("a",
                                                               JsArray.of(JsInt.of(1),JsInt.of(5)),
                                                               "b",
                                                               JsArray.of(JsObj.empty(),JsObj.empty())
                                                              )
                                                     )
                                            );

    System.out.println(error);

    Assertions.assertTrue(error.isEmpty());


  }
}
