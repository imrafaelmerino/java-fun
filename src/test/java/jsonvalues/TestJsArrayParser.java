package jsonvalues;

import jsonvalues.spec.IsObjSpec;
import jsonvalues.spec.JsArrayParser;
import jsonvalues.spec.JsArraySpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsArrayParser
{

  @Test
  public void test_array_of_different_elements()
  {

    final JsArraySpec spec = JsArraySpec.of(isStr,
                                            isInt,
                                            isLong,
                                            isDecimal,
                                            isBool,
                                            isIntegral,
                                            isObj,
                                            isArray,
                                            isInt(i->i>0),
                                            any(v -> v.isStr() || v.isInt()),
                                            isStr(s-> s.startsWith("1")),
                                            isArrayOfIntegralSuchThat(a->a.size()==1),
                                            isArrayOfNumberSuchThat(a->a.size()==2),
                                            isNumber(a->a.isDecimal())
                                           );


    JsArray array = JsArray.of(JsStr.of("a"),
                               JsInt.of(1),
                               JsLong.of(1),
                               JsBigDec.of(BigDecimal.TEN),
                               JsBool.TRUE,
                               JsBigInt.of(BigInteger.TEN),
                               JsObj.empty(),
                               JsArray.empty(),
                               JsInt.of(10),
                               JsStr.of("a"),
                               JsStr.of("1aaa"),
                               JsArray.of(1),
                               JsArray.of(1.5,2.5),
                               JsDouble.of(1.5)
                              );

    Assertions.assertEquals(array,new JsArrayParser(spec).parse(array.toPrettyString()));
  }
}
