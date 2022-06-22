package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class TestRecordGen {

    @Test
    public void testConstructors() {

        RecordGen one = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10));

        Assertions.assertTrue(one.sample(100).allMatch(it -> it.map.size() == 1));

        RecordGen two = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10),
                                     "b",
                                     StrGen.letters(1,
                                                    1));

        Assertions.assertTrue(two.sample(100).allMatch(it -> it.map.size() == 2));


        RecordGen three = RecordGen.of("a",
                                       IntGen.arbitrary(),
                                       "b",
                                       StrGen.letters(1,
                                                      1),
                                       "c",
                                       BoolGen.arbitrary());

        Assertions.assertTrue(three.sample(100).allMatch(it -> it.map.size() == 3));


        RecordGen four = RecordGen.of("a",
                                      IntGen.arbitrary(),
                                      "b",
                                      StrGen.letters(1,
                                                     1),
                                      "c",
                                      BoolGen.arbitrary(),
                                      "d",
                                      LongGen.arbitrary());

        Assertions.assertTrue(four.sample(100).allMatch(it -> it.map.size() == 4));


        RecordGen five = RecordGen.of("a",
                                      IntGen.arbitrary(),
                                      "b",
                                      StrGen.letters(1,
                                                     1),
                                      "c",
                                      BoolGen.arbitrary(),
                                      "d",
                                      LongGen.arbitrary(),
                                      "e",
                                      DoubleGen.arbitrary());

        Assertions.assertTrue(five.sample(100).allMatch(it -> it.map.size() == 5));


        RecordGen six = RecordGen.of("a",
                                     IntGen.arbitrary(),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     BoolGen.arbitrary(),
                                     "d",
                                     LongGen.arbitrary(),
                                     "e",
                                     DoubleGen.arbitrary(),
                                     "f",
                                     DoubleGen.arbitrary());

        Assertions.assertTrue(six.sample(100).allMatch(it -> it.map.size() == 6));

        RecordGen seven = RecordGen.of("a",
                                       IntGen.arbitrary(),
                                       "b",
                                       StrGen.letters(1,
                                                      1),
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

        Assertions.assertTrue(seven.sample(100).allMatch(it -> it.map.size() == 7));

        RecordGen eight = RecordGen.of("a",
                                       IntGen.arbitrary(),
                                       "b",
                                       StrGen.letters(1,
                                                      1),
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

        Assertions.assertTrue(eight.sample(100).allMatch(it -> it.map.size() == 8));


        RecordGen nine = RecordGen.of("a",
                                      IntGen.arbitrary(),
                                      "b",
                                      StrGen.letters(1,
                                                     1),
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
                                                        10),
                                      "i",
                                      BoolGen.arbitrary()
        );


        Assertions.assertTrue(nine.sample(100).allMatch(it -> it.map.size() == 9));

        RecordGen ten = RecordGen.of("a",
                                     IntGen.arbitrary(),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
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
                                                       10),
                                     "i",
                                     BoolGen.arbitrary(),
                                     "j",
                                     IntGen.biased()
        );
        Assertions.assertTrue(ten.sample(100).allMatch(it -> it.map.size() == 10));

        RecordGen eleven = RecordGen.of("a",
                                        IntGen.arbitrary(),
                                        "b",
                                        StrGen.letters(1,
                                                       1),
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
                                                          10),
                                        "i",
                                        BoolGen.arbitrary(),
                                        "j",
                                        IntGen.biased(),
                                        "k",
                                        IntGen.biased()
        );
        Assertions.assertTrue(eleven.sample(100).allMatch(it -> it.map.size() == 11));

        RecordGen twelve = RecordGen.of("a",
                                        IntGen.arbitrary(),
                                        "b",
                                        StrGen.letters(1,
                                                       1),
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
                                                          10),
                                        "i",
                                        BoolGen.arbitrary(),
                                        "j",
                                        IntGen.biased(),
                                        "k",
                                        IntGen.biased(),
                                        "l",
                                        LongGen.biased()
        );
        Assertions.assertTrue(twelve.sample(100).allMatch(it -> it.map.size() == 12));

        RecordGen thirteen = RecordGen.of("a",
                                          IntGen.arbitrary(),
                                          "b",
                                          StrGen.letters(1,
                                                         1),
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
                                                            10),
                                          "i",
                                          BoolGen.arbitrary(),
                                          "j",
                                          IntGen.biased(),
                                          "k",
                                          IntGen.biased(),
                                          "l",
                                          LongGen.biased(),
                                          "m",
                                          BigIntGen.arbitrary(10)
        );
        Assertions.assertTrue(thirteen.sample(100).allMatch(it -> it.map.size() == 13));

        RecordGen fourteen = RecordGen.of("a",
                                          IntGen.arbitrary(),
                                          "b",
                                          StrGen.letters(1,
                                                         1),
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
                                                            10),
                                          "i",
                                          BoolGen.arbitrary(),
                                          "j",
                                          IntGen.biased(),
                                          "k",
                                          IntGen.biased(),
                                          "l",
                                          LongGen.biased(),
                                          "m",
                                          BigIntGen.arbitrary(10),
                                          "n",
                                          BigDecGen.biased()
        );
        Assertions.assertTrue(fourteen.sample(100).allMatch(it -> it.map.size() == 14));

        RecordGen fifteen = RecordGen.of("a",
                                         IntGen.arbitrary(),
                                         "b",
                                         StrGen.letters(1,
                                                        1),
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
                                                           10),
                                         "i",
                                         BoolGen.arbitrary(),
                                         "j",
                                         IntGen.biased(),
                                         "k",
                                         IntGen.biased(),
                                         "l",
                                         LongGen.biased(),
                                         "m",
                                         BigIntGen.arbitrary(10),
                                         "n",
                                         BigDecGen.biased(),
                                         "o",
                                         BigDecGen.arbitrary()
        );
        Assertions.assertTrue(fifteen.sample(100).allMatch(it -> it.map.size() == 15));

        RecordGen sixteen = RecordGen.of("a",
                                         IntGen.arbitrary(),
                                         "b",
                                         StrGen.letters(1,
                                                        1),
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
                                                           10),
                                         "i",
                                         BoolGen.arbitrary(),
                                         "j",
                                         IntGen.biased(),
                                         "k",
                                         IntGen.biased(),
                                         "l",
                                         LongGen.biased(),
                                         "m",
                                         BigIntGen.arbitrary(10),
                                         "n",
                                         BigDecGen.biased(),
                                         "o",
                                         BigDecGen.arbitrary(),
                                         "p",
                                         CharGen.arbitrary()
        );
        Assertions.assertTrue(sixteen.sample(100).allMatch(it -> it.map.size() == 16));

        RecordGen seventeen = RecordGen.of("a",
                                           IntGen.arbitrary(),
                                           "b",
                                           StrGen.letters(1,
                                                          1),
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
                                                             10),
                                           "i",
                                           BoolGen.arbitrary(),
                                           "j",
                                           IntGen.biased(),
                                           "k",
                                           IntGen.biased(),
                                           "l",
                                           LongGen.biased(),
                                           "m",
                                           BigIntGen.arbitrary(10),
                                           "n",
                                           BigDecGen.biased(),
                                           "o",
                                           BigDecGen.arbitrary(),
                                           "p",
                                           CharGen.arbitrary(),
                                           "q",
                                           DoubleGen.biased()
        );
        Assertions.assertTrue(seventeen.sample(100).allMatch(it -> it.map.size() == 17));

        RecordGen eighteen = RecordGen.of("a",
                                          IntGen.arbitrary(),
                                          "b",
                                          StrGen.letters(1,
                                                         1),
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
                                                            10),
                                          "i",
                                          BoolGen.arbitrary(),
                                          "j",
                                          IntGen.biased(),
                                          "k",
                                          IntGen.biased(),
                                          "l",
                                          LongGen.biased(),
                                          "m",
                                          BigIntGen.arbitrary(10),
                                          "n",
                                          BigDecGen.biased(),
                                          "o",
                                          BigDecGen.arbitrary(),
                                          "p",
                                          CharGen.arbitrary(),
                                          "q",
                                          DoubleGen.biased(),
                                          "r",
                                          DoubleGen.arbitrary()
        );
        Assertions.assertTrue(eighteen.sample(100).allMatch(it -> it.map.size() == 18));


        RecordGen nineteen = RecordGen.of("a",
                                          IntGen.arbitrary(),
                                          "b",
                                          StrGen.letters(1,
                                                         1),
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
                                                            10),
                                          "i",
                                          BoolGen.arbitrary(),
                                          "j",
                                          IntGen.biased(),
                                          "k",
                                          IntGen.biased(),
                                          "l",
                                          LongGen.biased(),
                                          "m",
                                          BigIntGen.arbitrary(10),
                                          "n",
                                          BigDecGen.biased(),
                                          "o",
                                          BigDecGen.arbitrary(),
                                          "p",
                                          CharGen.arbitrary(),
                                          "q",
                                          DoubleGen.biased(),
                                          "r",
                                          DoubleGen.arbitrary(),
                                          "s",
                                          InstantGen.biased()
        );
        Assertions.assertTrue(nineteen.sample(100).allMatch(it -> it.map.size() == 19));

        RecordGen twenty = RecordGen.of("a",
                                        IntGen.arbitrary(),
                                        "b",
                                        StrGen.letters(1,
                                                       1),
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
                                                          10),
                                        "i",
                                        BoolGen.arbitrary(),
                                        "j",
                                        IntGen.biased(),
                                        "k",
                                        IntGen.biased(),
                                        "l",
                                        LongGen.biased(),
                                        "m",
                                        BigIntGen.arbitrary(10),
                                        "n",
                                        BigDecGen.biased(),
                                        "o",
                                        BigDecGen.arbitrary(),
                                        "p",
                                        CharGen.arbitrary(),
                                        "q",
                                        DoubleGen.biased(),
                                        "r",
                                        DoubleGen.arbitrary(),
                                        "s",
                                        InstantGen.biased(),
                                        "t",
                                        InstantGen.arbitrary(1000000,
                                                             1000000000)
        );
        Assertions.assertTrue(twenty.sample(100).allMatch(it -> it.map.size() == 20));
    }


    @Test
    public void testRecordGen() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      10),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     BoolGen.arbitrary());


        Map<Record, Long> counts = TestFun.generate(5000000,
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
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     IntGen.arbitrary(0,
                                                      40000));

        Map<Record, Long> map =
                TestFun.generate(20000,
                                 gen.setOptionals("a",
                                                  "b"));
        long allKeys =
                TestFun.countKeys(map,
                                  record -> record.map.containsKey("a") && record.map.containsKey("b"));


        long someKeyRemoved =
                TestFun.countKeys(map,
                                  record -> !record.map.containsKey("a") || !record.map.containsKey("b"));

        System.out.println(allKeys);
        System.out.println(someKeyRemoved);


        //50%
        Predicate<Long> inMargin = TestFun.isInMargin(map.size() / 2,
                                                      0.1);

        Assertions.assertTrue(inMargin.test(allKeys));
        Assertions.assertTrue(inMargin.test(someKeyRemoved));


        long aRemoved =
                TestFun.countKeys(map,
                                  record -> !record.map.containsKey("a")
                                          && record.map.containsKey("b"));

        long bRemoved =
                TestFun.countKeys(map,
                                  record -> !record.map.containsKey("b")
                                          && record.map.containsKey("a"));


        long allRemoved =
                TestFun.countKeys(map,
                                  record -> !record.map.containsKey("b")
                                          && record.map.containsKey("a"));

        Predicate<Long> x = TestFun.isInMargin(someKeyRemoved / 3,
                                               0.1);

        Assertions.assertTrue(x.test(aRemoved));
        Assertions.assertTrue(x.test(bRemoved));
        Assertions.assertTrue(x.test(allRemoved));


    }


    @Test
    public void testRecordGenWithAllOptionals() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      400000),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     IntGen.arbitrary(0,
                                                      400000));

        Map<Record, Long> map =
                TestFun.generate(200000,
                                 gen.setOptionals(Arrays.asList("a",
                                                                "b",
                                                                "c")));

        // half of the times no field is removed, 7 possible subsets from [a,b,c],
        Predicate<Long> x = TestFun.isInMargin(200000 / 2 / 7,
                                               0.1);

        // empty record, all fields are removed
        Assertions.assertTrue(x.test(map.get(new Record(new HashMap<>()))));


    }

    @Test
    public void testRecordGenWithAllNullable() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      400000),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     IntGen.arbitrary(0,
                                                      400000));

        Map<Record, Long> map =
                TestFun.generate(200000,
                                 gen.setAllNullable());

        // half of the times no field is removed, 7 possible subsets from [a,b,c],
        Predicate<Long> x = TestFun.isInMargin(200000 / 2 / 7,
                                               0.1);

        // all fields are null
        Map<String, ?> allNull = new HashMap<>();
        allNull.put("a",
                    null);
        allNull.put("b",
                    null);
        allNull.put("c",
                    null);
        Assertions.assertTrue(x.test(map.get(new Record(allNull))));


    }


    @Test
    public void testRecordGenWithNullable() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      40000),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     IntGen.arbitrary(0,
                                                      800000));

        Map<Record, Long> map = TestFun.generate(400000,
                                                 gen.setNullables("a",
                                                                  "b"));
        long noneNull =
                TestFun.countKeys(map,
                                  record -> record.map.get("a") != null && record.map.get("b") != null);


        long someNull =
                TestFun.countKeys(map,
                                  record -> record.map.get("a") == null || record.map.get("b") == null);


        //50%
        Predicate<Long> inMargin = TestFun.isInMargin(map.size() / 2,
                                                      0.05);

        Assertions.assertTrue(inMargin.test(noneNull));
        Assertions.assertTrue(inMargin.test(someNull));


        long aNull =
                TestFun.countKeys(map,
                                  record -> record.map.containsKey("a") && record.map.get("a") == null && record.map.get("b") != null);

        long bNull = TestFun.countKeys(map,
                                       record -> record.map.get("a") != null && record.map.containsKey("b") && record.map.get("b") == null);


        long allNull = TestFun.countKeys(map,
                                         record -> record.map.containsKey("a")
                                                 && record.map.get("a") == null
                                                 && record.map.containsKey("b")
                                                 && record.map.get("b") == null);

        Predicate<Long> x = TestFun.isInMargin(someNull / 3,
                                               0.05);
        Assertions.assertTrue(x.test(aNull));
        Assertions.assertTrue(x.test(bNull));
        Assertions.assertTrue(x.test(allNull));


    }


    @Test
    public void testGetRecordData() {

        Assertions.assertTrue(RecordGen.of("a",
                                           IntGen.biased())
                                       .sample(100)
                                       .allMatch(it -> it.getInt("a").isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           LongGen.biased())
                                       .sample(100)
                                       .allMatch(it -> it.getLong("a").isPresent()));


        Assertions.assertTrue(RecordGen.of("a",
                                           BigIntGen.biased(10))
                                       .sample(100)
                                       .allMatch(it -> it.getBigInt("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           BigDecGen.arbitrary())
                                       .sample(100)
                                       .allMatch(it -> it.getDecimal("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           DoubleGen.arbitrary())
                                       .sample(100)
                                       .allMatch(it -> it.getDouble("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           StrGen.biased(1,
                                                         100))
                                       .sample(100)
                                       .allMatch(it -> it.getStr("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           CharGen.alphabetic())
                                       .sample(100)
                                       .allMatch(it -> it.getChar("a")
                                                         .isPresent()));


        Assertions.assertTrue(RecordGen.of("a",
                                           ListGen.biased(IntGen.arbitrary(),
                                                          1,
                                                          100))
                                       .sample(100)
                                       .allMatch(it -> it.getList("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           InstantGen.arbitrary())
                                       .sample(100)
                                       .allMatch(it -> it.getInstant("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           BoolGen.arbitrary())
                                       .sample(100)
                                       .allMatch(it -> it.getBool("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           BytesGen.arbitrary(1,
                                                              10))
                                       .sample(100)
                                       .allMatch(it -> it.getBytes("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           SetGen.of(IntGen.arbitrary(),
                                                     10))
                                       .sample(100)
                                       .allMatch(it -> it.getSet("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           BytesGen.arbitrary(1,
                                                              10))
                                       .sample(100)
                                       .allMatch(it -> it.getBytes("a")
                                                         .isPresent()));

        Assertions.assertTrue(RecordGen.of("a",
                                           MapGen.of(StrGen.alphanumeric(1,
                                                                         10),
                                                     IntGen.arbitrary(),
                                                     100))
                                       .sample(100)
                                       .allMatch(it -> {
                                           Optional<Map<String, Integer>> a = it.getMap("a");
                                           return a.isPresent();
                                       }));
    }

}
