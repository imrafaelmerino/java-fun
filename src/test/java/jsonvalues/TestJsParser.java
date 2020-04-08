package jsonvalues;

import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsParser
{


  @Test
  public void test_parse_string_into_obj() throws IOException
  {

    final JsObjParser parser = new JsObjParser(JsObjSpec.of("a",
                                                            isStr,
                                                            "b",
                                                            isInt,
                                                            "c",
                                                            isBool,
                                                            "d",
                                                            isLong,
                                                            "e",
                                                            isDecimal,
                                                            "f",
                                                            JsArraySpec.of(isDecimal,
                                                                           isDecimal
                                                                          ),
                                                            "g",
                                                            isIntegral,
                                                            "h",
                                                            isObj,
                                                            "i",
                                                            isArray,
                                                            "j",arrayOf(JsObjSpec.of("a",isStr))
                                                           ));


    final JsObj example = JsObj.of("a",
                                   JsStr.of("a"),
                                   "b",
                                   JsInt.of(10),
                                   "c",
                                   JsBool.TRUE,
                                   "d",
                                   JsLong.of(10),
                                   "e",
                                   JsBigDec.of(BigDecimal.TEN),
                                   "f",
                                   JsArray.of(1.5,
                                              1.5
                                             ),
                                   "g",
                                   JsBigInt.of(BigInteger.TEN),
                                   "h",
                                   JsObj.empty(),
                                   "i",
                                   JsArray.empty(),
                                   "j",
                                   JsArray.of(JsObj.of("a",JsStr.of("hi")))
                                  );

    final JsObj parsed = parser.parse(example
                                        .toString());

    Assertions.assertEquals(parsed,
                            example
                           );
  }
}