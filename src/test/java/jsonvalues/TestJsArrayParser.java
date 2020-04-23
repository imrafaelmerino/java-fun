package jsonvalues;

import jsonvalues.spec.*;
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

    final JsArraySpec spec = JsArraySpec.tuple(str,
                                               intNumber,
                                               longNumber,
                                               decimal,
                                               bool,
                                               integral,
                                               obj,
                                               array,
                                               intNumber(i -> i > 0),
                                               any(v -> v.isStr() || v.isInt()),
                                               str(s -> s.startsWith("1")),
                                               arrayOfIntegralSuchThat(a -> a.size() == 1),
                                               arrayOfNumberSuchThat(a -> a.size() == 2),
                                               number(a -> a.isDecimal())
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
                               JsArray.of(1.5,
                                          2.5),
                               JsDouble.of(1.5)
                              );

    Assertions.assertEquals(array,
                            new JsArrayParser(spec).parse(array.toPrettyString()));
  }

  @Test
  public void testNullable()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      nullableArrayOfStr
                                     );

    JsObjParser parser = new JsObjParser(spec);
    final JsObj a = JsObj.of("a",
                             JsArray.of("a",
                                        "b"
                                       )
                            );
    Assertions.assertTrue(spec.test(a)
                              .isEmpty());
    Assertions.assertTrue(parser.parse(a.toString())
                                .equals(a));


    final JsObj b = JsObj.of("a",
                             JsNull.NULL
                            );
    Assertions.assertTrue(spec.test(b)
                              .isEmpty());
    Assertions.assertTrue(parser.parse(b.toString())
                                .equals(b));


    JsObjSpec specST = JsObjSpec.strict("a",
                                        nullableArrayOfStrSuchThat(it -> it.size() % 2 == 0)
                                       );
    JsObjParser parserST = new JsObjParser(specST);

    Assertions.assertTrue(specST.test(a)
                                .isEmpty());
    Assertions.assertTrue(parserST.parse(a.toString())
                                  .equals(a));

    Assertions.assertTrue(specST.test(b)
                                .isEmpty());
    Assertions.assertTrue(parserST.parse(b.toString())
                                  .equals(b));

  }

  @Test
  public void testArrayOfObject()
  {
    JsObjSpec spec = JsObjSpec.strict("a",
                                      JsSpecs.arrayOf(JsObjSpec.strict("a",
                                                                       str,
                                                                       "b",
                                                                       intNumber)));

    final JsObj a = JsObj.of("a",
                              JsArray.of(JsObj.of("a",
                                                  JsStr.of("a"),
                                                  "b",
                                                  JsInt.of(1)
                                                 ))
                             );


    Assertions.assertEquals(a,new JsObjParser(spec).parse(a.toString())
                         );

    JsObjSpec specNullable = JsObjSpec.strict("a",
                                      JsSpecs.nullableArrayOf(JsObjSpec.strict("a",
                                                                       str,
                                                                       "b",
                                                                       intNumber)));

    final JsObj b = JsObj.of("a",
                              JsNull.NULL);
    Assertions.assertEquals(b, new JsObjParser(specNullable).parse(b
                                                                .toString())
                           );

    JsObjSpec specTested = JsObjSpec.strict("a",
                                              JsSpecs.arrayOfObj(o->o.containsKey("a")));

    final JsObj c = JsObj.of("a",
                             JsArray.of(JsObj.of("a",JsBool.TRUE),JsObj.of("a",JsInt.of(1))));
    Assertions.assertEquals(c, new JsObjParser(specTested).parse(c
                                                                     .toString())
                           );

    JsObjSpec specSuchThat = JsObjSpec.strict("a",
                                              JsSpecs.arrayOfObjSuchThat(arr->arr.size() > 1),
                                              "b",JsSpecs.arrayOfBoolSuchThat(arr->arr.size()>2),
                                              "c",JsSpecs.arrayOfNumberSuchThat(arr->arr.head().equals(JsInt.of(1))),
                                              "d",JsSpecs.arrayOfDecSuchThat(arr->arr.head().equals(JsBigDec.of(BigDecimal.TEN))),
                                              "e",JsSpecs.arrayOfLongSuchThat(arr->arr.size()==3),
                                              "f",JsSpecs.arrayOfIntSuchThat(arr->arr.size()==3));

    final JsObj d = JsObj.of("a",
                             JsArray.of(JsObj.empty(),JsObj.empty()),
                             "b",JsArray.of(true,true,false),
                             "c",JsArray.of(1,2,3),
                             "d",JsArray.of(JsBigDec.of(BigDecimal.TEN)),
                             "e",JsArray.of(1L,2L,3L),
                             "f",JsArray.of(1,2,3)
                             );
    Assertions.assertEquals(d, new JsObjParser(specSuchThat).parse(d.toString()));

  }

  @Test
  public void testArrayOf(){
      Assertions.assertEquals(true, true);
  }
}
