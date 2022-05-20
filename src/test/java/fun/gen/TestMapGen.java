package fun.gen;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMapGen {

    @Test
    public void testNotAbleToGenerateMapOfSize() {
        MapGen<String, Integer> gen =
                new MapGen<>(StrGen.letters(1,
                                            1),
                             IntGen.arbitrary(0,
                                              10),
                             10000,
                             10);

        Assertions.assertThrows(RuntimeException.class,
                                () -> gen.sample(10).peek(System.out::println).count());
    }

    @Test
    public void test() {
        int SIZE = 100;
        MapGen<String, Integer> gen =
                new MapGen<>(StrGen.letters(1,
                                            2),
                             IntGen.arbitrary(0,
                                              10),
                             SIZE);

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.size() == SIZE));

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.values()
                                                   .stream()
                                                   .allMatch(n -> n <= 10))
        );


    }
}
