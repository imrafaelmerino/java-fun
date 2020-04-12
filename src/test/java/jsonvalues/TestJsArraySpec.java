package jsonvalues;

import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static jsonvalues.Functions.assertErrorIs;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsArraySpec
{

  @Test
  public void test_value_without_spec_should_return_error()
  {

    final JsArray array = JsArray.of(JsInt.of(1),
                                     JsStr.of("a")
                                    );
    final JsArraySpec spec = JsArraySpec.of(isInt);
    final Set<JsErrorPair> error = spec.test(array);
    assertErrorIs(error,
                  SPEC_MISSING,
                  JsStr.of("a"),
                  JsPath.fromIndex(1)
                 );
  }

  @Test
  public void test_any_spec_array_of_one_or_two_elements()
  {


    JsArraySpec spec = JsArraySpec.of(any,
                                      any(false)
                                     );

    Assertions.assertTrue(spec.test(JsArray.of(JsNull.NULL))
                              .isEmpty());
    Assertions.assertTrue(spec.test(JsArray.of(JsNull.NULL,
                                               JsBool.TRUE
                                              ))
                              .isEmpty());
    Assertions.assertFalse(spec.test(JsArray.of(JsNull.NULL,
                                                JsBool.TRUE,
                                                JsBool.FALSE
                                               ))
                               .isEmpty());
  }

  @Test
  public void test_array_of_boolean_such_that()
  {
    final JsObjSpec spec = JsObjSpec.strict("a",
                                            JsSpecs.isArrayOfBoolSuchThat(a -> a.head() == JsBool.TRUE)
                                           );

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(true,
                                                        false,
                                                        false
                                                       )
                                            ))
                              .isEmpty()
                         );


    Assertions.assertFalse(spec.test(JsObj.of("a",
                                              JsArray.of(false,
                                                         false,
                                                         false
                                                        )
                                             ))
                               .isEmpty()
                          );

    Assertions.assertFalse(spec.test(JsObj.of("a",
                                              JsArray.of(true)
                                                     .append(JsLong.of(1))
                                             ))
                               .isEmpty()
                          );

  }

  @Test
  public void test_any_spec_array_of_two_elements()
  {


    JsArraySpec spec = JsArraySpec.of(any,
                                      any
                                     );

    Assertions.assertTrue(spec.test(JsArray.of(JsBool.FALSE,
                                               JsBool.TRUE
                                              ))
                              .isEmpty());
    Assertions.assertFalse(spec.test(JsArray.of(JsBool.FALSE))
                               .isEmpty());
    Assertions.assertFalse(spec.test(JsArray.of(JsBool.FALSE,
                                                JsBool.TRUE,
                                                JsBool.FALSE
                                               ))
                               .isEmpty());
  }

  @Test
  public void test_array_of_array_spec()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      isArrayOfArray);

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                                 JsArray.of(JsArray.empty(),
                                                            JsArray.empty()
                                                           )
                                                )
                                   )
                              .isEmpty());

    Assertions.assertFalse(spec.test(JsObj.of("a",
                                             JsArray.of(JsObj.empty(),
                                                        JsArray.empty()
                                                       )
                                            )
                                   )
                              .isEmpty());



  }

  @Test
  public void test_array_of_integral_spec()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      isArrayOfIntegralSuchThat(a->a.size()==3));

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(JsInt.of(1),
                                                       JsLong.of(2),
                                                        JsBigInt.of(BigInteger.TEN)
                                                       )
                                            )
                                   )
                              .isEmpty());


    Assertions.assertFalse(spec.test(JsObj.of("a",
                                             JsArray.of(JsInt.of(1),JsStr.of("a")
                                                       )
                                            )
                                   )
                              .isEmpty());


  }

  @Test
  public void test_array_of_number_spec()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      isArrayOfNumberSuchThat(a->a.size()==5));

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(JsInt.of(1),
                                                        JsLong.of(2),
                                                        JsDouble.of(4.5),
                                                        JsBigInt.of(BigInteger.TEN),
                                                        JsBigDec.of(BigDecimal.TEN)
                                                       )
                                            )
                                   )
                              .isEmpty());


    Assertions.assertFalse(spec.test(JsObj.of("a",
                                              JsArray.of(JsInt.of(1),JsStr.of("a")
                                                        )
                                             )
                                    )
                               .isEmpty());


  }

  @Test
  public void test_array_of_object_spec()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      isArrayOfObjSuchThat(a-> a.size()==2));

    Assertions.assertTrue(spec.test(JsObj.of("a",
                                             JsArray.of(JsObj.of("a",JsNull.NULL),
                                                        JsObj.empty()
                                                       )
                                            )
                                   )
                              .isEmpty());


    Assertions.assertFalse(spec.test(JsObj.of("a",
                                              JsArray.of(JsObj.empty()
                                                        )
                                             )
                                    )
                               .isEmpty());


  }
}
