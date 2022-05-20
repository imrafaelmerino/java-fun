package jsonvalues.gen;


import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.SetGen;
import fun.tuple.Pair;
import jsonvalues.*;
import jsonvalues.gen.*;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import static jsonvalues.spec.JsSpecs.*;

public class TestGenerators {


    @Test
    public void test_js_array() {
        final Gen<JsObj> gen = JsObjGen.of("a",
                                           IntGen.arbitrary(0,
                                                            10)
                                                 .then(n -> JsArrayGen.arbitrary(n).apply(JsStrGen.alphanumeric(0,
                                                                                                                10))),
                                           "b",
                                           IntGen.arbitrary(0,
                                                            10
                                           ).then(n -> JsArrayGen.arbitrary(n).apply(JsIntGen.arbitrary())
                                           )
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                                          arrayOfStrSuchThat(a -> a.size() <= 10)
                                                  .nullable(),
                                          "b",
                                          arrayOfIntSuchThat(a -> a.size() <= 10).nullable()

        ).setOptionals("a",
                       "b");

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );
    }


    @Test
    public void test_js_obj() {

        final JsObjGen gen = JsObjGen.of("a",
                                         JsIntGen.arbitrary(),
                                         "b",
                                         JsStrGen.arbitrary(0,
                                                            10),
                                         "c",
                                         JsStrGen.alphanumeric(0,
                                                               10),
                                         "d",
                                         JsTupleGen.of(JsIntGen.arbitrary(),
                                                       JsStrGen.alphanumeric(0,
                                                                             10)
                                         ),
                                         "e",
                                         JsBigIntGen.arbitrary(1,
                                                               2)
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                                          integer(),
                                          "b",
                                          str(s -> s.length() <= 10),
                                          "c",
                                          str(s -> s.length() <= 10),
                                          "d",
                                          tuple(integer(),
                                                str(s -> s.length() <= 10)
                                          ),
                                          "e",
                                          integral()
        );

        Assertions.assertTrue(
                gen.sample(1000)
                   .allMatch(it -> spec.test(it)
                                       .isEmpty()
                   )
        );

    }

    @Test
    public void test_nested_gen() {
        JsObjGen gen = JsObjGen.of("a",
                                   JsArrayGen.arbitrary(5)
                                             .apply(JsStrGen.alphanumeric(0,
                                                                          10)),
                                   "b",
                                   JsTupleGen.of(JsStrGen.biased(0,
                                                                 10),
                                                 JsBoolGen.arbitrary(),
                                                 JsIntGen.arbitrary()),
                                   "c",
                                   JsObjGen.of("a",
                                               Combinators.oneOf(JsStr.of("a"),
                                                                 JsBool.TRUE
                                               )
                                   ),
                                   "d",
                                   JsBoolGen.arbitrary(),
                                   "e",
                                   Combinators.oneOf(JsStr.of("hi"),
                                                     JsNothing.NOTHING
                                   ),
                                   "f",
                                   Combinators.oneOf(JsStrGen.digits(0,
                                                                     10),
                                                     JsIntGen.arbitrary()
                                   ),
                                   "g",
                                   JsConsGen.cons(JsStr.of("a"))
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                                          arrayOfStr(),
                                          "b",
                                          tuple(str(),
                                                bool(),
                                                integer()
                                          ),
                                          "c",
                                          JsObjSpec.strict("a",
                                                           any(v -> v.isStr() || v.isBool())
                                          ),
                                          "d",
                                          bool(),
                                          "e",
                                          str(),
                                          "f",
                                          any(v -> v.isStr() || v.isIntegral()),
                                          "g",
                                          str(b -> b.equals("a"))
        ).setOptionals("e");

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );


    }

    @Test
    public void test_constructors() {

        final Gen<JsBool> boolGen = Combinators.oneOf(Arrays.asList(JsBool.TRUE,
                                                                    JsBool.FALSE
        ));
        JsObjGen gen = JsObjGen.of("a",
                                   JsStrGen.letters(0,
                                                    10),
                                   "b",
                                   JsIntGen.arbitrary(),
                                   "c",
                                   boolGen,
                                   "d",
                                   JsStrGen.biased(0,
                                                   100),
                                   "e",
                                   JsStrGen.alphabetic(0,
                                                       10),
                                   "f",
                                   JsLongGen.arbitrary(),
                                   "g",
                                   JsBigDecGen.arbitrary(),
                                   "h",
                                   JsConsGen.cons(JsBool.TRUE),
                                   "i",
                                   Combinators.oneOf(JsStrGen.arbitrary(0,
                                                                        100),
                                                     JsBigDecGen.arbitrary()
                                   ),
                                   "j",
                                   Combinators.oneOf(JsStr.of("a"),
                                                     JsBool.TRUE
                                   ),
                                   "k",
                                   Combinators.freq(new Pair<>(1,
                                                               JsStrGen.alphabetic(0,
                                                                                   10)),
                                                    new Pair<>(1,
                                                               JsLongGen.arbitrary())
                                   ),
                                   "l",
                                   new SetGen<>(JsIntGen.arbitrary(),
                                                5).map(JsArray::ofIterable),
                                   "m",
                                   JsStrGen.alphanumeric(0,
                                                         10),
                                   "n",
                                   JsStrGen.letter(),
                                   "o",
                                   JsBinaryGen.arbitrary(0,
                                                         10),
                                   "p",
                                   JsInstantGen.arbitrary(0,
                                                          1000
                                   )
                               )
                               .setNullables("a",
                                             "g")
                               .setOptionals("a",
                                             "g",
                                             "p",
                                             "o");

        JsObjSpec spec = JsObjSpec.lenient("a",
                                           str().nullable(),
                                           "b",
                                           integer(),
                                           "c",
                                           bool(),
                                           "d",
                                           str(),
                                           "e",
                                           str(),
                                           "f",
                                           integral(),
                                           "g",
                                           decimal().nullable(),
                                           "h",
                                           bool(),
                                           "i",
                                           any(v -> v.isStr() || v.isDecimal()),
                                           "j",
                                           any(v -> v.isStr() || v.isBool()),
                                           "k",
                                           any(i -> i.isStr() || i.isIntegral()),
                                           "l",
                                           arraySuchThat(a -> a.size() == 5),
                                           "m",
                                           str(),
                                           "n",
                                           str(s -> s.length() == 1),
                                           "o",
                                           binary(),
                                           "p",
                                           instant()
        ).setOptionals("a",
                       "g",
                       "o",
                       "p");

        Assertions.assertTrue(
                gen.sample(1000)
                   .allMatch(it -> {
                       Set<JsErrorPair> errors = spec.test(it);
                       return errors
                                           .isEmpty();
                   })
        );

    }

    @Test
    public void testSamples() {

        JsObjGen gen = JsObjGen.of("a",
                                   JsStrGen.digit(),
                                   "b",
                                   JsIntGen.arbitrary()
                               )
                               .setOptionals("b")
                               .setNullables("b");

        JsObjSpec spec = JsObjSpec.strict("a",
                                          str(),
                                          "b",
                                          integer()
                                                  .nullable()
        ).setOptionals("b");

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );
    }

    @Test
    public void testMapNumbers() {
        final Gen<JsInt> posInteger = JsIntGen.arbitrary().map(i -> i.map(v ->
                                                                          {
                                                                              if (v >= 0) return v;
                                                                              else return -v;
                                                                          })
        );

        final Supplier<JsInt> supplier = posInteger.sample(new Random());

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(supplier.get().value >= 0);
        }
    }

    @Test
    public void testSuchThat() {
        final Gen<JsInt> negative = new JsIntGen(IntGen.arbitrary.suchThat(i -> i < 0));

        final Supplier<JsInt> supplier = negative.sample(new Random());

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(supplier.get().value < 0);
        }

    }


    @Test
    public void testDigits() {

        final Gen<JsArray> gen = JsArrayGen.arbitrary(10).apply(JsStrGen.digit());

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> it.size() == 10)
        );


        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> arrayOfStr(s -> s.length() == 1).test(it).isEmpty())
        );


    }


    @Test
    public void testDates() {


        Gen<JsInstant> gen = JsInstantGen.arbitrary(ZonedDateTime.now(),
                                                    ZonedDateTime.now()
                                                                 .plus(Duration.ofDays(2))
        );


        Assertions.assertTrue(
                gen.sample(1000).allMatch(JsInstant::isInstant)
        );
    }


}
