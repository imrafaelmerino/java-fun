package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
                         .setAllOptional();


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

        List<Integer> list = IntGen.arbitrary().distinct().sample(100000).collect(Collectors.toList());
        Assertions.assertEquals(list.size(),
                                new HashSet<>(list).size());


        List<String> letters = StrGen.alphabetic(1,
                                                 10).distinct().sample(100000).collect(Collectors.toList());
        Assertions.assertEquals(letters.size(),
                                new HashSet<>(letters).size());
    }
}
