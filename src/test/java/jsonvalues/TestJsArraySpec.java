package jsonvalues;

import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpec;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static jsonvalues.Functions.assertErrorIs;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;
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
}
