package jsonvalues;

import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static jsonvalues.Functions.assertErrorIs;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;
import static jsonvalues.spec.JsSpecs.any;
import static jsonvalues.spec.JsSpecs.isInt;

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
    final JsObjSpec spec = JsObjSpec.of("a",
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
                                              JsArray.of(true).append(JsLong.of(1))
                                             ))
                               .isEmpty()
                          );

  }
}
