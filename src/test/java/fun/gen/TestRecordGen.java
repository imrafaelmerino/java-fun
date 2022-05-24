package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
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
                                     StrGen.letter());

        RecordGen three = RecordGen.of("a",
                                       IntGen.arbitrary(),
                                       "b",
                                       StrGen.letter(),
                                       "c",
                                       BoolGen.arbitrary());

        RecordGen four = RecordGen.of("a",
                                      IntGen.arbitrary(),
                                      "b",
                                      StrGen.letter(),
                                      "c",
                                      BoolGen.arbitrary(),
                                      "d",
                                      LongGen.arbitrary());

        RecordGen five = RecordGen.of("a",
                                      IntGen.arbitrary(),
                                      "b",
                                      StrGen.letter(),
                                      "c",
                                      BoolGen.arbitrary(),
                                      "d",
                                      LongGen.arbitrary(),
                                      "e",
                                      DoubleGen.arbitrary());

        RecordGen six = RecordGen.of("a",
                                     IntGen.arbitrary(),
                                     "b",
                                     StrGen.letter(),
                                     "c",
                                     BoolGen.arbitrary(),
                                     "d",
                                     LongGen.arbitrary(),
                                     "e",
                                     DoubleGen.arbitrary(),
                                     "f",
                                     DoubleGen.arbitrary());
        RecordGen seven = RecordGen.of("a",
                                       IntGen.arbitrary(),
                                       "b",
                                       StrGen.letter(),
                                       "c",
                                       BoolGen.arbitrary(),
                                       "d",
                                       LongGen.arbitrary(),
                                       "e",
                                       DoubleGen.arbitrary(),
                                       "f",
                                       DoubleGen.arbitrary(),
                                       "g",
                                       BytesGen.arbitrary(0,
                                                          1024));
        RecordGen eight = RecordGen.of("a",
                                       IntGen.arbitrary(),
                                       "b",
                                       StrGen.letter(),
                                       "c",
                                       BoolGen.arbitrary(),
                                       "d",
                                       LongGen.arbitrary(),
                                       "e",
                                       DoubleGen.arbitrary(),
                                       "f",
                                       DoubleGen.arbitrary(),
                                       "g",
                                       BytesGen.arbitrary(0,
                                                          1024),
                                       "h",
                                       ListGen.arbitrary(BigDecGen.arbitrary(),
                                                         0,
                                                         10));


        RecordGen nine = RecordGen.of("a",
                                      IntGen.arbitrary(),
                                      "b",
                                      StrGen.letter(),
                                      "c",
                                      BoolGen.arbitrary(),
                                      "d",
                                      LongGen.arbitrary(),
                                      "e",
                                      DoubleGen.arbitrary(),
                                      "f",
                                      DoubleGen.arbitrary(),
                                      "g",
                                      BytesGen.arbitrary(0,
                                                         1024),
                                      "h",
                                      ListGen.arbitrary(BigDecGen.arbitrary(),
                                                        0,
                                                        10));

    }


    @Test
    public void testRecordGen() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10),
                                     "b",
                                     StrGen.letter(),
                                     "c",
                                     BoolGen.arbitrary());


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
                                     StrGen.letter(),
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
                                     StrGen.letter(),
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

    @Test
    public void testGetMethods() {

        RecordGen gen = RecordGen.of("a",
                                     BigIntGen.arbitrary(3),
                                     "b",
                                     CharGen.arbitrary(),
                                     "c",
                                     InstantGen.arbitrary(),
                                     "d",
                                     StrGen.alphanumeric(),
                                     "e",
                                     BoolGen.arbitrary(),
                                     "f",
                                     IntGen.biased(),
                                     "g",
                                     LongGen.arbitrary(),
                                     "h",
                                     BigDecGen.arbitrary(),
                                     "i",
                                     DoubleGen.arbitrary(),
                                     "j",
                                     ListGen.arbitrary(IntGen.arbitrary(),
                                                       1,
                                                       10
                                     ),
                                     "k",
                                     new SetGen<>(StrGen.letter(),5)

        );

        Function<Map<String, ?>, BigInteger> getA =
                RecordGen.getBigInt("a");

        Function<Map<String, ?>, Character> getB =
                RecordGen.getChar("b");

        Function<Map<String, ?>, Instant> getC =
                RecordGen.getInstant("c");

        Function<Map<String, ?>, String> getD =
                RecordGen.getStr("d");

        Function<Map<String, ?>, Boolean> getE =
                RecordGen.getBool("e");

        Function<Map<String, ?>, Integer> getF =
                RecordGen.getInt("f");

        Function<Map<String, ?>, Long> getG =
                RecordGen.getLong("g");

        Function<Map<String, ?>, BigDecimal> getH =
                RecordGen.getDecimal("h");

        Function<Map<String, ?>, Double> getI =
                RecordGen.getDouble("i");

        Function<Map<String, ?>, List<Integer>> getJ =
                RecordGen.getList("j");

        Function<Map<String, ?>, Set<String>> getK =
                RecordGen.getSet("k");


        Assertions.assertTrue(gen.sample(1000)
                                 .allMatch(record -> getA.apply(record).getClass().equals(BigInteger.class)
                                         && getB.apply(record).getClass().equals(Character.class)
                                         && getC.apply(record).getClass().equals(Instant.class)
                                         && getD.apply(record).getClass().equals(String.class)
                                         && getE.apply(record).getClass().equals(Boolean.class)
                                         && getF.apply(record).getClass().equals(Integer.class)
                                         && getG.apply(record).getClass().equals(Long.class)
                                         && getH.apply(record).getClass().equals(BigDecimal.class)
                                         && getI.apply(record).getClass().equals(Double.class)
                                         && getJ.apply(record) instanceof List
                                         && getK.apply(record) instanceof Set

                                 )
        );

        Assertions.assertTrue(gen.sample(100).allMatch(record -> {
            List<Integer> integers = getJ.apply(record);
            return integers.stream().allMatch(Objects::nonNull);
        }));


    }

    @Test
    public void testGetPptMethods() {

        RecordGen gen = RecordGen.of();

        Function<Map<String, ?>, Optional<BigInteger>> getA =
                RecordGen.getOptBigInt("a");

        Function<Map<String, ?>, Optional<Character>> getB =
                RecordGen.getOptChar("b");

        Function<Map<String, ?>, Optional<Instant>> getC =
                RecordGen.getOptInstant("c");

        Function<Map<String, ?>, Optional<String>> getD =
                RecordGen.getOptStr("d");

        Function<Map<String, ?>, Optional<Boolean>> getE =
                RecordGen.getOptBool("e");

        Function<Map<String, ?>, Optional<Integer>> getF =
                RecordGen.getOptInt("f");

        Function<Map<String, ?>, Optional<Long>> getG =
                RecordGen.getOptLong("g");

        Function<Map<String, ?>, Optional<BigDecimal>> getH =
                RecordGen.getOptDecimal("h");

        Function<Map<String, ?>, Optional<Double>> getI =
                RecordGen.getOptDouble("i");

        Function<Map<String, ?>, Optional<List<Object>>> getJ =
                RecordGen.getOptList("j");

        Function<Map<String, ?>, Optional<Set<Object>>> getK =
                RecordGen.getOptSet("k");


        Assertions.assertTrue(gen.sample(1000)
                                 .allMatch(record -> !getA.apply(record).isPresent()
                                         && !getB.apply(record).isPresent()
                                         && !getC.apply(record).isPresent()
                                         && !getD.apply(record).isPresent()
                                         && !getE.apply(record).isPresent()
                                         && !getF.apply(record).isPresent()
                                         && !getG.apply(record).isPresent()
                                         && !getH.apply(record).isPresent()
                                         && !getI.apply(record).isPresent()
                                         && !getJ.apply(record).isPresent()
                                         && !getK.apply(record).isPresent()

                                 )
        );




    }
}
