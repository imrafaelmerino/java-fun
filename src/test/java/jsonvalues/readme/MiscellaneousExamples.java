package jsonvalues.readme;

import fun.gen.Gen;
import fun.optic.Lens;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.gen.*;
import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.*;


public class MiscellaneousExamples {


    @Test
    public void basic() {
        JsObjSpec spec = JsObjSpec.of("firstName",
                                          str(),
                                          "lastName",
                                          str(),
                                          "age",
                                          integer(i -> i >= 0)
        ).withOptKeys("firstName",
                       "lastName",
                       "age");

        Gen<JsObj> gen =
                JsObjGen.of("firstName",
                            JsStrGen.biased(0,
                                            100),
                            "lastName",
                            JsStrGen.biased(0,
                                            100),
                            "age",
                            JsIntGen.biased(0,
                                            200))
                        .withOptKeys("firstName",
                                      "lastName",
                                      "age");


        JsObjSpecParser parser = new JsObjSpecParser(spec);


        Assertions.assertTrue(gen.sample(100000)
                                 .allMatch(obj -> parser.parse(obj.toString())
                                                        .equals(obj)));


    }

    @Test
    public void testGeographicalCoordinates_Second_Option() {
        BigDecimal latMin = BigDecimal.valueOf(-90);
        BigDecimal latMax = BigDecimal.valueOf(90);
        BigDecimal longMin = BigDecimal.valueOf(-180);
        BigDecimal longMax = BigDecimal.valueOf(180);

        BiFunction<BigDecimal, BigDecimal, Predicate<BigDecimal>> between =
                (min, max) -> n -> n.compareTo(max) <= 0 && n.compareTo(min) >= 0;


        JsArraySpec spec = JsSpecs.tuple(decimal(between.apply(latMin,
                                                               latMax)),
                                         decimal(between.apply(longMin,
                                                               longMax))
        );

        Gen<JsArray> gen = JsTupleGen.of(JsBigDecGen.biased(latMin,
                                                            latMax),
                                         JsBigDecGen.biased(longMin,
                                                            longMax)
        );

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertTrue(gen.sample(100000)
                                 .allMatch(arr -> parser.parse(arr.toString())
                                                        .equals(arr)));


    }

    @Test
    public void testGeographicalCoordinates_First_Option() {
        BigDecimal latMin = BigDecimal.valueOf(-90);
        BigDecimal latMax = BigDecimal.valueOf(90);
        BigDecimal longMin = BigDecimal.valueOf(-180);
        BigDecimal longMax = BigDecimal.valueOf(180);

        BiFunction<BigDecimal, BigDecimal, Predicate<BigDecimal>> between =
                (min, max) -> n -> n.compareTo(max) <= 0 && n.compareTo(min) >= 0;


        JsObjSpec spec = JsObjSpec.of("latitude",
                                          decimal(between.apply(latMin,
                                                                latMax)),
                                          "longitude",
                                          decimal(between.apply(longMin,
                                                                longMax)));

        JsObjGen gen = JsObjGen.of("latitude",
                                   JsBigDecGen.biased(latMin,
                                                      latMax),
                                   "longitude",
                                   JsBigDecGen.biased(longMin,
                                                      longMax)
        );

        JsObjSpecParser parser = new JsObjSpecParser(spec);

        Assertions.assertTrue(gen.sample(100000)
                                 .allMatch(obj -> parser.parse(obj.toString())
                                                        .equals(obj)));


    }

    @Test
    public void testArrayOfThings() {
        JsObjSpec veggieSpec =
                JsObjSpec.of("veggieName",
                                 str(),
                                 "veggieLike",
                                 bool());

        JsObjSpec spec =
                JsObjSpec.of("fruits",
                                 arrayOfStr(),
                                 "vegetables",
                                 arrayOfObjSpec(veggieSpec));

        JsObjGen veggieGen =
                JsObjGen.of("veggieName",
                            JsStrGen.biased(0,
                                            50),
                            "veggieLike",
                            JsBoolGen.arbitrary());
        JsObjGen gen =
                JsObjGen.of("fruits",
                            JsArrayGen.biased(JsStrGen.biased(0,
                                                              20),
                                              0,
                                              10),
                            "vegetables",
                            JsArrayGen.biased(veggieGen,
                                              0,
                                              10)
                );

        JsObjSpecParser parser = new JsObjSpecParser(spec);


        Assertions.assertTrue(gen.sample(100000)
                                 .allMatch(obj -> parser.parse(obj.toString())
                                                        .equals(obj)));

    }
}
