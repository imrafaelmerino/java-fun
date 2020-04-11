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
                                        isFalse,
                                        "g",
                                        isDecimal,
                                        "h",
                                        isIntegral,
                                        "i",
                                        JsObjSpec.of("a",
                                                     isNumber,
                                                     "b",
                                                     isArray,
                                                     "c",
                                                     isObj,
                                                     "d",
                                                     isInt(i->i%2==0),
                                                     "e",
                                                     isLong(i->i%2==1),
                                                     "f",
                                                     isStr(s->s.startsWith("a")),
                                                     "g",
                                                     isDecimal(d->d.doubleValue() > 1.0),
                                                     "h",
                                                     isObj(o->o.containsKey("b")),
                                                     "i",
                                                     isArray(a-> a.head().equals(JsStr.of("first"))),
                                                     "j",
                                                     JsArraySpec.of(isNumber(JsValue::isDecimal), any)
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
                                        JsObj.of("b",JsStr.of("b")),
                                        "i",
                                        JsArray.of(JsStr.of("first"),JsBool.TRUE),
                                        "j",
                                        JsArray.of(JsDouble.of(1.2),JsBool.TRUE)
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

    final JsObjSpec spec = JsObjSpec.of("a",
                                        isArrayOfInt,
                                        "b",
                                        isArrayOfStr
                                       );


    final JsObj obj = JsObj.of("a",
                               JsArray.of(1,2,3),
                               "b",
                               JsArray.of("a","b","c")
                              );

    Assertions.assertEquals(obj,
                            new JsObjParser(spec).parse(obj
                                                          .toPrettyString())
                           );

  }
}
