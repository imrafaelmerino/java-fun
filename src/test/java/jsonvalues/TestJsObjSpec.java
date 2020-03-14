package jsonvalues;

import jsonvalues.spec.ERROR_CODE;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.*;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjSpec
{

  @Test
  public void testIsStrSpec(){

    final JsObjSpec spec = new JsObjSpec("a", isStr);


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                  JsInt.of(1)));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                               .findFirst().get();

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
  public void testIsIntSpec(){

    final JsObjSpec spec = new JsObjSpec("a", isInt);


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst().get();

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
  public void testIsLongSpec(){

    final JsObjSpec spec = new JsObjSpec("a", isLong);


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst().get();

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
  public void testIsDecimalSpec(){

    final JsObjSpec spec = new JsObjSpec("a", isDecimal);


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst().get();

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
  public void testIsBooleanSpec(){

    final JsObjSpec spec = new JsObjSpec("a", isBool);


    final Set<JsErrorPair> error = spec.test(JsObj.of("a",
                                                      JsStr.of("a")));

    Assertions.assertFalse(error.isEmpty());

    final JsErrorPair pair = error.stream()
                                  .findFirst().get();

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
}
