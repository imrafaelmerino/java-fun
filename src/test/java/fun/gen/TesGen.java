package fun.gen;

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
}
