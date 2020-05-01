package jsonvalues;

import jsonvalues.gen.*;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;
import static jsonvalues.gen.JsGens.*;
import static jsonvalues.spec.JsArraySpec.tuple;
import static jsonvalues.spec.JsSpecs.*;
import static jsonvalues.spec.JsSpecs.intNum;

public class TestGenerators
{


  @Test
  public void test_js_array()
  {
    final JsGen<JsObj> gen = JsObjGen.of("a",
                                         chooseGen(0,
                                                   10
                                                  ).flatMap(n -> JsArrayGen.of(alphabeticGen,
                                                                               n.value
                                                                              )
                                                           ),
                                         "b",
                                         chooseGen(0,
                                                   10
                                                  ).flatMap(n -> JsArrayGen.of(intNumGen,
                                                                               n.value
                                                                              )
                                                           )
                                        );

    test(gen,
         v -> JsObjSpec.strict("a",
                               arrayOfStrSuchThat(a -> a.size() <= 10).optional().nullable(),
                               "b",
                               arrayOfIntSuchThat(a -> a.size() <= 10).nullable().optional()
                              )
                       .test(v.toJsObj())
                       .isEmpty(),
         100
        );
  }


  @Test
  public void test_pair_gen()
  {

    JsObjGen gen = JsObjGen.of(JsGenPair.of("a",
                                            intNumGen
                                           ),
                               JsGenPair.of("b",
                                            strGen
                                           ),
                               JsGenPair.of("c",
                                            boolGen
                                           ),
                               JsGenPair.of("d",
                                            alphabeticGen
                                           ),
                               JsGenPair.of("e",
                                            alphanumericGen
                                           )
                              );

    test(gen,
         v -> JsObjSpec.strict("a",
                               intNum,
                               "b",
                               str,
                               "c",
                               bool,
                               "d",
                               str,
                               "e",
                               str
                              )
                       .test(v.toJsObj())
                       .isEmpty(),
         1000
        );
  }

  @Test
  public void test_js_obj()
  {

    final JsObjGen gen = JsObjGen.of("a",
                                     intNumGen,
                                     "b",
                                     strGen,
                                     "c",
                                     alphabeticGen,
                                     "d",
                                     JsTupleGen.of(intNumGen,
                                                   strGen
                                                  )
                                    );

    test(gen,
         v -> JsObjSpec.strict("a",
                               intNum,
                               "b",
                               str(s -> s.length() <= 10),
                               "c",
                               str(s -> s.length() <= 10),
                               "d",
                               tuple(intNum,
                                     str(s -> s.length() <= 10)
                                    )
                              )
                       .test(v.toJsObj())
                       .isEmpty(),
         100
        );


  }

  @Test
  public void test_nested_gen()
  {
    JsObjGen gen = JsObjGen.of("a",
                               JsArrayGen.of(alphanumericGen,
                                             5),
                               "b",
                               JsTupleGen.of(strGen,
                                             boolGen,
                                             intNumGen),
                               "c",
                               JsObjGen.of("a",
                                           JsGens.oneOfGen(JsStr.of("a"),
                                                           JsBool.TRUE)),
                               "d",
                               boolGen,
                               "e",
                               oneOfGen(JsStr.of("hi"),
                                        JsNothing.NOTHING),
                               "f",
                               oneOfGen(strGen,
                                        intNumGen),
                               "g",
                               singleGen(JsStr.of("a"))
                              );

    test(gen,
         a ->
           JsObjSpec.strict("a",
                            arrayOfStr,
                            "b",
                            tuple(str,
                                  bool,
                                  intNum
                                 ),
                            "c",
                            JsObjSpec.strict("a",
                                             any(v -> v.isStr() || v.isBool())
                                            ),
                            "d",
                            bool,
                            "e",
                            str.optional(),
                            "f",
                            any(v -> v.isStr() || v.isIntegral()),
                            "g",
                            str(b->b.equals("a"))
                           )
                    .test(a.toJsObj())
                    .isEmpty(),
         1000
        );
  }


  @Test
  public void test_constructors(){
    final JsGen<JsBool> listGen = oneOfGen(Arrays.asList(JsBool.TRUE,
                                                             JsBool.FALSE));
    JsObjGen gen = JsObjGen.of("a", strGen, "b", intNumGen, "c", listGen, "d", alphabeticGen,
                               "e", alphanumericGen, "f", longNumGen, "g", doubleNumGen, "h", singleGen(JsBool.TRUE),
                               "i", oneOfGen(strGen,doubleNumGen),
                               "j", oneOfGen(JsStr.of("a"),JsBool.TRUE), "k", strGen
                              );

    JsObjSpec spec = JsObjSpec.lenient("a",str,"b",intNum,"c",bool,"d",str,"e",str,"f",integral,"g",decimal,"h",bool,
                                       "i",any(v->v.isStr() || v.isDecimal()),"j",any(v->v.isStr() || v.isBool()),
                                       "k",str
                                      );

    test(gen,v -> spec.test(v.toJsObj()).isEmpty(), 1000);
  }

  public static void test(JsGen<?> gen,
                    Predicate<JsValue> condition,
                    int times
                   )
  {
    for (int i = 0; i < times; i++)
    {

      final JsValue value = gen.apply(new Random())
                               .get();
      Assertions.assertTrue(condition.test(value));
    }
  }
}
