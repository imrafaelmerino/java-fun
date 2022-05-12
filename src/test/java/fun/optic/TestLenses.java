package fun.optic;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestLenses {

    @Test
    public void testPair() {


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


        Pair<String, Integer> pair = new Pair<>("hi",
                                                1);
        Assertions.assertEquals(first.get.apply(pair),
                                "hi");
        Assertions.assertEquals(first.set.apply("bye").apply(pair),
                                new Pair<>("bye",
                                           1));
        Assertions.assertEquals(first.modify.apply(String::toUpperCase).apply(pair),
                                new Pair<>("HI",
                                           1));


        Assertions.assertEquals(second.get.apply(pair),
                                1);
        Assertions.assertEquals(second.set.apply(2).apply(pair),
                                new Pair<>("hi",
                                           2));
        Assertions.assertEquals(second.modify.apply(a -> a * 10).apply(pair),
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
                            Object::toString);

        Option<Pair<String, Integer>, Integer> firstNumber = first.compose(number);

        Assertions.assertEquals(firstNumber.get.apply(new Pair<>("1",
                                                                 10)),
                                Optional.of(1));
        Assertions.assertEquals(firstNumber.get.apply(new Pair<>("a",
                                                                 10)),
                                Optional.empty());


    }

}
