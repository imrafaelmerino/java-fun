package fun.optic;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestLenses {

    Lens<Pair<String, Integer>, String> first =
            new Lens<>(Pair::first,
                       str -> pair -> new Pair<>(str,
                                                 pair.second())
            );


    Lens<Pair<String, Integer>, Integer> second =
            new Lens<>(Pair::second,
                       integer -> pair -> new Pair<>(pair.first(),
                                                     integer)
            );

    @Test
    public void testPair() {

        Pair<String, Integer> pair = new Pair<>("hi",
                                                1);

        Assertions.assertEquals(first.get.apply(pair),
                                "hi");

        Assertions.assertEquals(first.set.apply("bye").apply(pair),
                                new Pair<>("bye",
                                           1));

        Assertions.assertEquals(first.modify.apply(String::toUpperCase)
                                            .apply(pair),
                                new Pair<>("HI",
                                           1));

        Assertions.assertEquals("abc",
                                first.find.apply(it -> it.startsWith("a"))
                                          .apply(new Pair<>("abc",
                                                            3))
                                          .get());

        Assertions.assertEquals(Optional.empty(),
                                first.find.apply(it -> it.startsWith("a"))
                                          .apply(new Pair<>("bc",
                                                            3)));

        Assertions.assertEquals(second.get.apply(pair),
                                1);

        Assertions.assertEquals(second.set.apply(2)
                                          .apply(pair),
                                new Pair<>("hi",
                                           2));

        Assertions.assertEquals(second.modify.apply(a -> a * 10)
                                             .apply(pair),
                                new Pair<>("hi",
                                           10));

        //prism
        Prism<String, Integer> number =
                new Prism<>(s -> {
                    try {
                        return Optional.of(Integer.parseInt(s));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                },
                            Object::toString
                );

        Option<Pair<String, Integer>, Integer> firstNumber = first.compose(number);

        Assertions.assertEquals(firstNumber.get.apply(new Pair<>("1",
                                                                 10)),
                                Optional.of(1));

        Assertions.assertEquals(firstNumber.get.apply(new Pair<>("a",
                                                                 10)),
                                Optional.empty());

        Assertions.assertEquals(new Pair<>("4",
                                           10),
                                firstNumber.set.apply(4)
                                               .apply(new Pair<>("",
                                                                 10))
        );

    }

    @Test
    public void testCompose() {

        Lens<Pair<String, Integer>, String> first =
                new Lens<>(Pair::first,
                           str -> pair -> new Pair<>(str,
                                                     pair.second())
                );


        Lens<Pair<Pair<String, Integer>, Integer>, Pair<String, Integer>> firstPair =
                new Lens<>(Pair::first,
                           newFirst -> pair -> new Pair<>(newFirst,
                                                          pair.second())
                );

        Lens<Pair<Pair<String, Integer>, Integer>, String> compose =
                firstPair.compose(first);

        Assertions.assertEquals("a",
                                compose.get.apply(new Pair<>(new Pair<>("a",
                                                                        2),
                                                             1)));
        Assertions.assertEquals(new Pair<>(new Pair<>("A",
                                                      2),
                                           1),
                                compose.modify.apply(String::toUpperCase).apply(new Pair<>(new Pair<>("a",
                                                                                                      2),
                                                                                           1)));

        Assertions.assertEquals("a",
                                compose.get.apply(new Pair<>(new Pair<>("a",
                                                                        2),
                                                             1)));

        Assertions.assertEquals(new Pair<>(new Pair<>("b",
                                                      2),
                                           1),
                                compose.set.apply("b")
                                           .apply(new Pair<>(new Pair<>("a",
                                                                        2),
                                                             1)));
    }

}
