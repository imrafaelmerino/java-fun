package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TesGen {


    @Test
    public void testSuchThat() {


        RecordGen gen =
                RecordGen.of("a",
                             StrGen.alphanumeric(),
                             "b",
                             StrGen.alphanumeric(),
                             "c",
                             StrGen.alphanumeric())
                         .setAllOptionals();


        Assertions.assertTrue(gen.suchThat(it ->
                                                   it.map.containsKey("a") &&
                                                           it.map.containsKey("b") &&
                                                           it.map.containsKey("c")).sample(100).findAny().isPresent());

        Assertions.assertTrue(gen.suchThat(it ->
                                                   !it.map.containsKey("a") &&
                                                           !it.map.containsKey("b") &&
                                                           !it.map.containsKey("c")).sample(100).findAny().isPresent());

    }
}
