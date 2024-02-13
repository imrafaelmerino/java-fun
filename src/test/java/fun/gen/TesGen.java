package fun.gen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TesGen {


    @Test
    public void testSuchThat() {


        RecordGen gen =
                RecordGen.of("a",
                             StrGen.alphanumeric(1,
                                                 1),
                             "b",
                             StrGen.alphanumeric(1,
                                                 1),
                             "c",
                             StrGen.alphanumeric(1,
                                                 1))
                         .withAllOptKeys();


        Assertions.assertTrue(gen.suchThat(it ->
                                                   it.map.containsKey("a") &&
                                                           it.map.containsKey("b") &&
                                                           it.map.containsKey("c"))
                                 .sample(100)
                                 .findAny()
                                 .isPresent());

        Assertions.assertTrue(gen.suchThat(it ->
                                                   !it.map.containsKey("a") &&
                                                           !it.map.containsKey("b") &&
                                                           !it.map.containsKey("c"))
                                 .sample(100)
                                 .findAny()
                                 .isPresent());

    }

    @Test
    public void testDistinct() {

        List<Integer> list = IntGen.arbitrary().distinct().sample(100000).toList();
        Assertions.assertEquals(list.size(),
                                new HashSet<>(list).size());


        List<String> letters = StrGen.alphabetic(1,
                                                 10).distinct().sample(100000).toList();
        Assertions.assertEquals(letters.size(),
                                new HashSet<>(letters).size());
    }

    @Test
    public void testClassify() {
        Map<String, Predicate<Integer>> classifier =
                new HashMap<>();
        classifier.put("2",
                       n -> n == 2);
        classifier.put("> 5",
                       n -> n > 5);
        classifier.put("= 10",
                       n -> n == 10);
        Map<String, Long> map =
                IntGen.arbitrary(0,
                                 10).classify(10000,
                                              classifier,
                                              "Others");

        System.out.println(map);
    }

    @Test
    public void testSeq() {
        Assertions.assertEquals(Arrays.asList(1,
                                              2,
                                              3,
                                              4,
                                              5),
                                Gen.seq(n -> n)
                                   .sample(5).collect(Collectors.toList()));
    }
}
