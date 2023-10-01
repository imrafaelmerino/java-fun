package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
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
                                     IntGen.arbitrary(0,
                                                      40000),
                                     "c",
                                     IntGen.arbitrary(0,
                                                      40000)
        ).withOptKeys("a",
                      "b");


        int times = 1000000;
        Map<List<?>, Long> generated =
                gen.collect(times,
                            record -> {
                                if (record.getInt("a").isPresent()
                                        && record.getInt("b").isPresent()
                                        && record.getInt("c").isPresent())
                                    return Arrays.asList("a",
                                                         "b",
                                                         "c");
                                if (record.getInt("a").isPresent()
                                        && record.getInt("b").isPresent()
                                )
                                    return Arrays.asList("a",
                                                         "b");
                                if (record.getInt("a").isPresent()
                                        && record.getInt("c").isPresent()
                                )
                                    return Arrays.asList("a",
                                                         "c");
                                if (record.getInt("b").isPresent()
                                        && record.getInt("c").isPresent()
                                )
                                    return Arrays.asList("b",
                                                         "c");
                                if (record.getInt("a").isPresent())
                                    return Arrays.asList("a");
                                if (record.getInt("b").isPresent())
                                    return Arrays.asList("b");
                                if (record.getInt("c").isPresent())
                                    return Arrays.asList("c");
                                return new ArrayList<>();
                            });

        Function<Long, Double> toPer =
                e -> ((double) (e * 100)) / times;

        Predicate<Map.Entry<List<?>, Long>> containsAll =
                it -> it.getKey().containsAll(Arrays.asList("a",
                                                            "b",
                                                            "c"));
        Assertions.assertTrue(generated
                                      .entrySet()
                                      .stream()
                                      .filter(containsAll)
                                      .map(it -> it.getValue())
                                      .map(toPer)
                                      .peek(System.out::println)
                                      .allMatch(it -> it <= 50.1 && it >= 49.9));

        Assertions.assertTrue(generated
                                      .entrySet()
                                      .stream()
                                      .filter(containsAll.negate())
                                      .map(it -> it.getValue())
                                      .map(toPer)
                                      .peek(System.out::println)
                                      .allMatch(it -> it <= 16.7 && it >= 16.6));


    }


    @Test
    public void testRecordGenWithAllNullableAndOptionals() {

        RecordGen gen = RecordGen.of("a",
                                     IntGen.arbitrary(0,
                                                      40000),
                                     "b",
                                     StrGen.letters(1,
                                                    1),
                                     "c",
                                     IntGen.arbitrary(0,
                                                      1000)
        );

        int times = 2000000;
        Function<Record, List<String>> nonNullKeys = r -> {
            if (r.map.get("a") != null
                    && r.map.get("b") != null
                    && r.map.get("c") != null)
                return Arrays.asList("a",
                                     "b",
                                     "c");
            if (r.map.get("a") != null
                    && r.map.get("b") != null
                    && r.map.get("c") == null)
                return Arrays.asList("a",
                                     "b");
            if (r.map.get("a") != null
                    && r.map.get("b") == null
                    && r.map.get("c") != null)
                return Arrays.asList("a",
                                     "c");
            if (r.map.get("a") == null
                    && r.map.get("b") != null
                    && r.map.get("c") != null)
                return Arrays.asList("b",
                                     "c");
            if (r.map.get("a") == null
                    && r.map.get("c") == null
                    && r.map.get("b") != null)
                return Arrays.asList("b");
            if (r.map.get("a") == null
                    && r.map.get("c") != null
                    && r.map.get("b") == null)
                return Arrays.asList("c");
            if (r.map.get("a") != null
                    && r.map.get("c") == null
                    && r.map.get("b") == null)
                return Arrays.asList("a");

            return Arrays.asList();
        };

        Function<Record, List<String>> presentKeys = r -> {
            if (r.map.containsKey("a")
                    && r.map.containsKey("b")
                    && r.map.containsKey("c"))
                return Arrays.asList("a",
                                     "b",
                                     "c");
            if (r.map.containsKey("a")
                    && r.map.containsKey("b")
                    && !r.map.containsKey("c"))
                return Arrays.asList("a",
                                     "b");
            if (r.map.containsKey("a")
                    && !r.map.containsKey("b")
                    && r.map.containsKey("c"))
                return Arrays.asList("a",
                                     "c");
            if (!r.map.containsKey("a")
                    && r.map.containsKey("b")
                    && r.map.containsKey("c"))
                return Arrays.asList("b",
                                     "c");
            if (!r.map.containsKey("a")
                    && !r.map.containsKey("c")
                    && r.map.containsKey("b"))
                return Arrays.asList("b");
            if (!r.map.containsKey("a")
                    && r.map.containsKey("c")
                    && !r.map.containsKey("b"))
                return Arrays.asList("c");
            if (r.map.containsKey("a")
                    && !r.map.containsKey("c")
                    && !r.map.containsKey("b"))
                return Arrays.asList("a");

            return Arrays.asList();
        };

        Predicate<Map.Entry<List<String>, Long>> containsAll =
                it -> it.getKey().containsAll(Arrays.asList("a",
                                                            "b",
                                                            "c"));
        Function<Map.Entry<List<String>, Long>, Double> toPer =
                e -> ((double) (e.getValue() * 100)) / times;

        Assertions.assertTrue(gen.withAllNullValues()
                                 .collect(times,
                                          nonNullKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll)
                                 .map(toPer)
                                 .allMatch(it -> it <= 50.5 && it >= 49.5));

        Assertions.assertTrue(gen.withAllOptKeys()
                                 .collect(times,
                                          presentKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll)
                                 .map(toPer)
                                 .allMatch(it -> it <= 50.5 && it >= 49.5));


        Assertions.assertTrue(
                gen
                        .withAllNullValues()
                        .collect(times,
                                 nonNullKeys)
                        .entrySet()
                        .stream()
                        .filter(containsAll.negate())
                        .map(toPer)
                        .allMatch(it -> it <= 7.2 && it >= 6.8));

        Assertions.assertTrue(
                gen
                        .withNullValues("a","b","c")
                        .collect(times,
                                 nonNullKeys)
                        .entrySet()
                        .stream()
                        .filter(containsAll.negate())
                        .map(toPer)
                        .allMatch(it -> it <= 7.2 && it >= 6.8));

        Assertions.assertTrue(gen.withAllOptKeys()
                                 .collect(times,
                                          presentKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll.negate())
                                 .map(toPer)
                                 .allMatch(it -> it <= 7.2 && it >= 6.8));

        Assertions.assertTrue(gen.withOptKeys("a","b","c")
                                 .collect(times,
                                          presentKeys)
                                 .entrySet()
                                 .stream()
                                 .filter(containsAll.negate())
                                 .map(toPer)
                                 .allMatch(it -> it <= 7.2 && it >= 6.8));
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
                                           SetGen.ofN(IntGen.arbitrary(),
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
