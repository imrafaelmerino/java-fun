package jsonvalues.spec;

import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestMinAndMaxOfJsArrayTestedSpecs {


    @Test
    public void testArrayOfString() {

        JsStr A = JsStr.of("a");
        JsArraySpec spec = JsSpecs.arrayOfStr(s -> s.length() < 2,
                                              1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(A,
                                                              A,
                                                              A).toString()));

        Assertions.assertEquals(JsArray.of(A),
                                parser.parse(JsArray.of(A).toString()));

        Assertions.assertEquals(JsArray.of(A,
                                           A),
                                parser.parse(JsArray.of(A,
                                                        A).toString()));
    }

    @Test
    public void testArrayOfInt() {

        JsInt ONE = JsInt.of(1);
        JsArraySpec spec = JsSpecs.arrayOfInt(i -> i == 1,
                                              1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(ONE,
                                                              ONE,
                                                              ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE),
                                parser.parse(JsArray.of(ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE,
                                           ONE),
                                parser.parse(JsArray.of(ONE,
                                                        ONE).toString()));
    }


    @Test
    public void testArrayOfLong() {

        JsLong MAX = JsLong.of(Long.MAX_VALUE);
        JsArraySpec spec = JsSpecs.arrayOfLong(i -> i == Long.MAX_VALUE,
                                               1,
                                               2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(MAX,
                                                              MAX,
                                                              MAX).toString()));

        Assertions.assertEquals(JsArray.of(MAX),
                                parser.parse(JsArray.of(MAX).toString()));

        Assertions.assertEquals(JsArray.of(MAX,
                                           MAX),
                                parser.parse(JsArray.of(MAX,
                                                        MAX).toString()));
    }

    @Test
    public void testArrayOfObj() {

        JsObj EMPTY = JsObj.empty();
        JsArraySpec spec = JsSpecs.arrayOfObj(JsObj::isEmpty,
                                              1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(EMPTY,
                                                              EMPTY,
                                                              EMPTY).toString()));

        Assertions.assertEquals(JsArray.of(EMPTY),
                                parser.parse(JsArray.of(EMPTY).toString()));

        Assertions.assertEquals(JsArray.of(EMPTY,
                                           EMPTY),
                                parser.parse(JsArray.of(EMPTY,
                                                        EMPTY).toString()));
    }

    @Test
    public void testArrayOfNumber() {

        JsInt ONE = JsInt.of(1);
        JsArraySpec spec = JsSpecs.arrayOfNumber(JsValue::isInt,
                                                 1,
                                                 2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(ONE,
                                                              ONE,
                                                              ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE),
                                parser.parse(JsArray.of(ONE).toString()));

        Assertions.assertEquals(JsArray.of(ONE,
                                           ONE),
                                parser.parse(JsArray.of(ONE,
                                                        ONE).toString()));
    }

    @Test
    public void testArrayOfDec() {

        JsBigDec TEN = JsBigDec.of(BigDecimal.TEN);
        JsArraySpec spec = JsSpecs.arrayOfDec(d -> d.compareTo(BigDecimal.TEN)==0,
                                              1,
                                              2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(TEN,
                                                              TEN,
                                                              TEN).toString()));

        Assertions.assertEquals(JsArray.of(TEN),
                                parser.parse(JsArray.of(TEN).toString()));

        Assertions.assertEquals(JsArray.of(TEN,
                                           TEN),
                                parser.parse(JsArray.of(TEN,
                                                        TEN).toString()));
    }

    @Test
    public void testArrayOfIntegral() {

        JsBigInt A = JsBigInt.of(new BigInteger("111111111111111111111111111111111111111111111111"));
        JsArraySpec spec = JsSpecs.arrayOfBigInt(i -> i.compareTo(BigInteger.ZERO) > 0,
                                                   1,
                                                   2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(A,
                                                              A,
                                                              A).toString()));

        Assertions.assertEquals(JsArray.of(A),
                                parser.parse(JsArray.of(A).toString()));

        Assertions.assertEquals(JsArray.of(A,
                                           A),
                                parser.parse(JsArray.of(A,
                                                        A).toString()));
    }

    @Test
    public void testArrayOfObjSpec() {

        JsObj A = JsObj.of("a",
                           JsInt.of(1));
        JsArraySpec spec = JsSpecs.arrayOfObjSpec(JsObjSpec.of("a",
                                                                   JsSpecs.integer()),
                                                  1,
                                                  2);

        JsArraySpecParser parser = new JsArraySpecParser(spec);

        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.empty().toString()));


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(JsArray.of(A,
                                                              A,
                                                              A).toString()));

        Assertions.assertEquals(JsArray.of(A),
                                parser.parse(JsArray.of(A).toString()));

        Assertions.assertEquals(JsArray.of(A,
                                           A),
                                parser.parse(JsArray.of(A,
                                                        A).toString()));
    }
}
