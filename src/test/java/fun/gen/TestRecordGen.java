package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;

public class TestRecordGen {

    @Test
    public void testConstructors() {

        RecordGen one = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10));
        RecordGen two = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10),
                                     "b",
                                     StrGen.letter);

        RecordGen three = RecordGen.of("a",
                                       IntGen.arbitrary,
                                       "b",
                                       StrGen.letter,
                                       "c",
                                       BoolGen.arbitrary);

        RecordGen four = RecordGen.of("a",
                                      IntGen.arbitrary,
                                      "b",
                                      StrGen.letter,
                                      "c",
                                      BoolGen.arbitrary,
                                      "d",
                                      LongGen.arbitrary);

        RecordGen five = RecordGen.of("a",
                                      IntGen.arbitrary,
                                      "b",
                                      StrGen.letter,
                                      "c",
                                      BoolGen.arbitrary,
                                      "d",
                                      LongGen.arbitrary,
                                      "e",
                                      FloatGen.arbitrary);

        RecordGen six = RecordGen.of("a",
                                     IntGen.arbitrary,
                                     "b",
                                     StrGen.letter,
                                     "c",
                                     BoolGen.arbitrary,
                                     "d",
                                     LongGen.arbitrary,
                                     "e",
                                     FloatGen.arbitrary,
                                     "f",
                                     DoubleGen.arbitrary);
        RecordGen seven = RecordGen.of("a",
                                       IntGen.arbitrary,
                                       "b",
                                       StrGen.letter,
                                       "c",
                                       BoolGen.arbitrary,
                                       "d",
                                       LongGen.arbitrary,
                                       "e",
                                       FloatGen.arbitrary,
                                       "f",
                                       DoubleGen.arbitrary,
                                       "g",
                                       BytesGen.arbitrary(0,
                                                          1024));
        RecordGen eight = RecordGen.of("a",
                                       IntGen.arbitrary,
                                       "b",
                                       StrGen.letter,
                                       "c",
                                       BoolGen.arbitrary,
                                       "d",
                                       LongGen.arbitrary,
                                       "e",
                                       FloatGen.arbitrary,
                                       "f",
                                       DoubleGen.arbitrary,
                                       "g",
                                       BytesGen.arbitrary(0,
                                                          1024),
                                       "h",
                                       ListGen.<BigDecimal>arbitrary(0,10).apply(BigDecGen.arbitrary));


        RecordGen nine = RecordGen.of("a",
                                       IntGen.arbitrary,
                                       "b",
                                       StrGen.letter,
                                       "c",
                                       BoolGen.arbitrary,
                                       "d",
                                       LongGen.arbitrary,
                                       "e",
                                       FloatGen.arbitrary,
                                       "f",
                                       DoubleGen.arbitrary,
                                       "g",
                                       BytesGen.arbitrary(0,
                                                          1024),
                                       "h",
                                       ListGen.<BigDecimal>arbitrary(0,10).apply(BigDecGen.arbitrary)
                                      );

    }


    @Test
    public void testRecordGen() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10),
                                     "b",
                                     StrGen.letter,
                                     "c",
                                     BoolGen.arbitrary);


        Map<Map<String, ?>, Long> counts = TestFun.generate(1000000,
                                                            gen);


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         counts.keySet(),
                                                         0.1);

    }

    @Test
    public void testRecordGenWithOptionals() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      40000),
                                     "b",
                                     StrGen.letter,
                                     "c",
                                     IntGen.arbitrary(0,
                                                      40000));

        Map<Map<String, ?>, Long> map =
                TestFun.generate(20000,
                                 gen.setOptionals("a",
                                                  "b"));
        System.out.println(map.size());
        long allKeys =
                TestFun.countKeys(map,
                                  key -> key.containsKey("a") && key.containsKey("b"));

        System.out.println(allKeys);

        long someKeyRemoved =
                TestFun.countKeys(map,
                                  key -> !key.containsKey("a") || !key.containsKey("b"));

        System.out.println(someKeyRemoved);

        //50%
        Predicate<Long> inMargin = TestFun.isInMargin(map.size() / 2,
                                                      0.1);

        Assertions.assertTrue(inMargin.test(allKeys));
        Assertions.assertTrue(inMargin.test(someKeyRemoved));


        long aRemoved =
                TestFun.countKeys(map,
                                  key -> !key.containsKey("a") && key.containsKey("b"));

        long bRemoved =
                TestFun.countKeys(map,
                                  key -> !key.containsKey("b") && key.containsKey("a"));


        long allRemoved =
                TestFun.countKeys(map,
                                  key -> !key.containsKey("b") && key.containsKey("a"));

        Predicate<Long> x = TestFun.isInMargin(someKeyRemoved / 3,
                                               0.1);
        Assertions.assertTrue(x.test(aRemoved));
        Assertions.assertTrue(x.test(bRemoved));
        Assertions.assertTrue(x.test(allRemoved));


    }

    @Test
    public void testRecordGenWithNullable() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      40000),
                                     "b",
                                     StrGen.letter,
                                     "c",
                                     IntGen.arbitrary(0,
                                                      800000));

        Map<Map<String, ?>, Long> map = TestFun.generate(400000,
                                                         gen.setNullables("a",
                                                                          "b"));
        long noneNull =
                TestFun.countKeys(map,
                                  key -> key.get("a") != null && key.get("b") != null);

        System.out.println(noneNull);
        System.out.println(map.size());


        long someNull =
                TestFun.countKeys(map,
                                  key -> key.get("a") == null || key.get("b") == null);

        System.out.println(someNull);

        //50%
        Predicate<Long> inMargin = TestFun.isInMargin(map.size() / 2,
                                                      0.05);

        Assertions.assertTrue(inMargin.test(noneNull));
        Assertions.assertTrue(inMargin.test(someNull));


        long aNull =
                TestFun.countKeys(map,
                                  key -> key.containsKey("a") && key.get("a") == null && key.get("b") != null);

        long bNull = TestFun.countKeys(map,
                                       key -> key.get("a") != null && key.containsKey("b") && key.get("b") == null);


        long allNull = TestFun.countKeys(map,
                                         key -> key.containsKey("a")
                                                 && key.get("a") == null
                                                 && key.containsKey("b")
                                                 && key.get("b") == null);

        Predicate<Long> x = TestFun.isInMargin(someNull / 3,
                                               0.05);
        Assertions.assertTrue(x.test(aNull));
        Assertions.assertTrue(x.test(bNull));
        Assertions.assertTrue(x.test(allNull));


    }
}
