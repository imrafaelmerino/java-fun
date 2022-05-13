package jsonvalues;


<<<<<<< HEAD
import fun.gen.*;
import fun.tuple.Pair;

import jsonvalues.gen.*;
import jsonvalues.spec.JsErrorPair;
=======
import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.SetGen;
import fun.tuple.Pair;
import jsonvalues.gen.*;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
<<<<<<< HEAD
import java.util.Set;
import java.util.function.Supplier;

import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;
import static jsonvalues.spec.JsSpecs.tuple;
=======
import java.util.function.Supplier;

>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
import static jsonvalues.spec.JsSpecs.*;

public class TestGenerators {


    @Test
    public void test_js_array() {
        final Gen<JsObj> gen = JsObjGen.of("a",
<<<<<<< HEAD
                IntGen.arbitrary(0, 10)
                        .then(n -> JsArrayGen.of(JsStrGen.alpha(10),
                                        n
                                )
                        ),
                "b",
                IntGen.arbitrary(0,
                        10
                ).then(n -> JsArrayGen.of(JsIntGen.arbitrary,
                                n
                        )
                )
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                arrayOfStrSuchThat(a -> a.size() <= 10).optional()
                        .nullable(),
                "b",
                arrayOfIntSuchThat(a -> a.size() <= 10).nullable()
                        .optional()
=======
                                           IntGen.arbitrary(0,
                                                            10)
                                                 .then(n -> JsArrayGen.arbitrary(n).apply(JsStrGen.alphanumeric(0,
                                                                                                                10))),
                                           "b",
                                           IntGen.arbitrary(0,
                                                            10
                                           ).then(n -> JsArrayGen.arbitrary(n).apply(JsIntGen.arbitrary)
                                           )
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                                          arrayOfStrSuchThat(a -> a.size() <= 10).optional()
                                                                                 .nullable(),
                                          "b",
                                          arrayOfIntSuchThat(a -> a.size() <= 10).nullable()
                                                                                 .optional()
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );
    }


    @Test
    public void test_js_obj() {

        final JsObjGen gen = JsObjGen.of("a",
<<<<<<< HEAD
                JsIntGen.arbitrary,
                "b",
                JsStrGen.arbitrary(0,10),
                "c",
                JsStrGen.alphanumeric(10),
                "d",
                JsTupleGen.of(JsIntGen.arbitrary,
                        JsStrGen.alpha(10)
                )
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                integer,
                "b",
                str(s -> s.length() <= 10),
                "c",
                str(s -> s.length() <= 10),
                "d",
                tuple(integer,
                        str(s -> s.length() <= 10)
                )
=======
                                         JsIntGen.arbitrary,
                                         "b",
                                         JsStrGen.arbitrary(0,
                                                            10),
                                         "c",
                                         JsStrGen.alphanumeric(0,
                                                               10),
                                         "d",
                                         JsTupleGen.of(JsIntGen.arbitrary,
                                                       JsStrGen.alphanumeric(0,
                                                                             10)
                                         )
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                                          integer,
                                          "b",
                                          str(s -> s.length() <= 10),
                                          "c",
                                          str(s -> s.length() <= 10),
                                          "d",
                                          tuple(integer,
                                                str(s -> s.length() <= 10)
                                          )
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );

    }

    @Test
    public void test_nested_gen() {
        JsObjGen gen = JsObjGen.of("a",
<<<<<<< HEAD
                JsArrayGen.of(JsStrGen.alphanumeric(10),
                        5
                ),
                "b",
                JsTupleGen.of(JsStrGen.biased(0,10), JsBoolGen.arbitrary, JsIntGen.arbitrary),
                "c",
                JsObjGen.of("a",
                        Combinators.oneOf(JsStr.of("a"),
                                JsBool.TRUE
                        )
                ),
                "d",
                JsBoolGen.arbitrary,
                "e",
                Combinators.oneOf(JsStr.of("hi"),
                        JsNothing.NOTHING
                ),
                "f",
                Combinators.oneOf(JsStrGen.digits(10),
                        JsIntGen.arbitrary
                ),
                "g",
                JsConsGen.cons(JsStr.of("a"))
        );

        var spec = JsObjSpec.strict("a",
                arrayOfStr,
                "b",
                tuple(str,
                        bool,
                        integer
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
                str(b -> b.equals("a"))
=======
                                   JsArrayGen.arbitrary(5).apply(JsStrGen.alphanumeric(0,
                                                                                       10)),
                                   "b",
                                   JsTupleGen.of(JsStrGen.biased(0,
                                                                 10),
                                                 JsBoolGen.arbitrary,
                                                 JsIntGen.arbitrary),
                                   "c",
                                   JsObjGen.of("a",
                                               Combinators.oneOf(JsStr.of("a"),
                                                                 JsBool.TRUE
                                               )
                                   ),
                                   "d",
                                   JsBoolGen.arbitrary,
                                   "e",
                                   Combinators.oneOf(JsStr.of("hi"),
                                                     JsNothing.NOTHING
                                   ),
                                   "f",
                                   Combinators.oneOf(JsStrGen.digits(0,
                                                                     10),
                                                     JsIntGen.arbitrary
                                   ),
                                   "g",
                                   JsConsGen.cons(JsStr.of("a"))
        );

        JsObjSpec spec = JsObjSpec.strict("a",
                                          arrayOfStr,
                                          "b",
                                          tuple(str,
                                                bool,
                                                integer
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
                                          str(b -> b.equals("a"))
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );


    }

    @Test
    public void test_constructors() {

        final Gen<JsBool> boolGen = Combinators.oneOf(Arrays.asList(JsBool.TRUE,
<<<<<<< HEAD
                JsBool.FALSE
        ));
        JsObjGen gen = JsObjGen.of("a",
                        JsStrGen.letters(10),
                        "b",
                        JsIntGen.arbitrary,
                        "c",
                        boolGen,
                        "d",
                        JsStrGen.biased(0,100),
                        "e",
                        JsStrGen.alpha(10),
                        "f",
                        JsLongGen.arbitrary,
                        "g",
                        JsBigDecGen.arbitrary,
                        "h",
                        JsConsGen.cons(JsBool.TRUE),
                        "i",
                        Combinators.oneOf(JsStrGen.arbitrary(0,100),
                                JsBigDecGen.arbitrary
                        ),
                        "j",
                        Combinators.oneOf(JsStr.of("a"),
                                JsBool.TRUE
                        ),
                        "k",
                        Combinators.freq(new Pair<>(1, JsStrGen.alpha(10)),
                                         new Pair<>(1, JsLongGen.arbitrary)
                        ),
                        "l",
                        new SetGen<>(JsIntGen.arbitrary, 5).map(JsArray::ofIterable),
                        "m",
                        JsStrGen.alphanumeric(10),
                        "n",
                        JsStrGen.letter,
                        "o",
                        JsBinaryGen.arbitrary(0,10),
                        "p",
                        JsInstantGen.arbitrary(0,
                                1000
                        )
                )
                .setNullables("a", "g")
                .setOptionals("a", "g", "p", "o");

        JsObjSpec spec = JsObjSpec.lenient("a",
                str.optional().nullable(),
                "b",
                integer,
                "c",
                bool,
                "d",
                str,
                "e",
                str,
                "f",
                integral,
                "g",
                decimal.optional().nullable(),
                "h",
                bool,
                "i",
                any(v -> v.isStr() || v.isDecimal()),
                "j",
                any(v -> v.isStr() || v.isBool()),
                "k",
                any(i -> i.isStr() || i.isIntegral()),
                "l",
                arraySuchThat(a -> a.size() == 5),
                "m",
                str(s -> s.length() == 10),
                "n",
                str(s -> s.length() == 1),
                "o",
                JsSpecs.binary.optional(),
                "p",
                JsSpecs.instant.optional()
        );

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> {
                    Set<JsErrorPair> errors = spec.test(it);
                    return errors.isEmpty();
                })
=======
                                                                    JsBool.FALSE
        ));
        JsObjGen gen = JsObjGen.of("a",
                                   JsStrGen.letters(0,
                                                    10),
                                   "b",
                                   JsIntGen.arbitrary,
                                   "c",
                                   boolGen,
                                   "d",
                                   JsStrGen.biased(0,
                                                   100),
                                   "e",
                                   JsStrGen.alphabetic(0,
                                                       10),
                                   "f",
                                   JsLongGen.arbitrary,
                                   "g",
                                   JsBigDecGen.arbitrary,
                                   "h",
                                   JsConsGen.cons(JsBool.TRUE),
                                   "i",
                                   Combinators.oneOf(JsStrGen.arbitrary(0,
                                                                        100),
                                                     JsBigDecGen.arbitrary
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
                                                               JsLongGen.arbitrary)
                                   ),
                                   "l",
                                   new SetGen<>(JsIntGen.arbitrary,
                                                5).map(JsArray::ofIterable),
                                   "m",
                                   JsStrGen.alphanumeric(0,
                                                         10),
                                   "n",
                                   JsStrGen.letter,
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
                                           str.optional().nullable(),
                                           "b",
                                           integer,
                                           "c",
                                           bool,
                                           "d",
                                           str,
                                           "e",
                                           str,
                                           "f",
                                           integral,
                                           "g",
                                           decimal.optional().nullable(),
                                           "h",
                                           bool,
                                           "i",
                                           any(v -> v.isStr() || v.isDecimal()),
                                           "j",
                                           any(v -> v.isStr() || v.isBool()),
                                           "k",
                                           any(i -> i.isStr() || i.isIntegral()),
                                           "l",
                                           arraySuchThat(a -> a.size() == 5),
                                           "m",
                                           str,
                                           "n",
                                           str(s -> s.length() == 1),
                                           "o",
                                           JsSpecs.binary.optional(),
                                           "p",
                                           JsSpecs.instant.optional()
        );

        Assertions.assertTrue(
                gen.sample(1000)
                   .allMatch(it -> spec.test(it)
                                       .isEmpty())
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );

    }

    @Test
    public void testSamples() {

        JsObjGen gen = JsObjGen.of("a",
<<<<<<< HEAD
                JsStrGen.digit,
                "b",
                JsIntGen.arbitrary
        ).setOptionals("b").setNullables("b");

        JsObjSpec spec = JsObjSpec.strict("a",
                str,
                "b",
                integer.optional()
                        .nullable()
=======
                                   JsStrGen.digit,
                                   "b",
                                   JsIntGen.arbitrary
                               )
                               .setOptionals("b")
                               .setNullables("b");

        JsObjSpec spec = JsObjSpec.strict("a",
                                          str,
                                          "b",
                                          integer.optional()
                                                 .nullable()
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> spec.test(it).isEmpty())
        );
    }

    @Test
    public void testMapNumbers() {
        final Gen<JsInt> posInteger = JsIntGen.arbitrary.map(i -> i.map(v ->
<<<<<<< HEAD
                {
                    if (v >= 0) return v;
                    else return -v;
                })
=======
                                                                        {
                                                                            if (v >= 0) return v;
                                                                            else return -v;
                                                                        })
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );

        final Supplier<JsInt> supplier = posInteger.sample(new Random());

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(supplier.get().value >= 0);
        }
    }

    @Test
    public void testSuchThat() {
        final Gen<JsInt> negative = JsIntGen.of(IntGen.arbitrary.suchThat(i -> i < 0));

        final Supplier<JsInt> supplier = negative.sample(new Random());

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(supplier.get().value < 0);
        }

    }


    @Test
    public void testDigits() {

<<<<<<< HEAD
        final Gen<JsArray> gen = JsArrayGen.of(JsStrGen.digit,
                10
        );
=======
        final Gen<JsArray> gen = JsArrayGen.arbitrary(10).apply(JsStrGen.digit);
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> it.size() == 10)
        );


        Assertions.assertTrue(
                gen.sample(1000).allMatch(it -> JsSpecs.arrayOfStr(s -> s.length() == 1).test(it).isEmpty())
        );


    }


    @Test
    public void testDates() {


        Gen<JsInstant> gen = JsInstantGen.arbitrary(ZonedDateTime.now(),
<<<<<<< HEAD
                ZonedDateTime.now()
                        .plus(Duration.ofDays(2))
=======
                                                    ZonedDateTime.now()
                                                                 .plus(Duration.ofDays(2))
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        );


        Assertions.assertTrue(
                gen.sample(1000).allMatch(JsInstant::isInstant)
        );
    }


}
