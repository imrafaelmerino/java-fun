package fun.optic;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestLenses {

    final Lens<Pair<String, Integer>, String> first =
            new Lens<>(Pair::first,
                       str -> pair -> Pair.of(str,
                                              pair.second())
            );


    final Lens<Pair<String, Integer>, Integer> second =
            new Lens<>(Pair::second,
                       integer -> pair -> Pair.of(pair.first(),
                                                  integer)
            );

    @Test
    public void testPair() {

        Pair<String, Integer> pair = Pair.of("hi",
                                             1);

        Assertions.assertEquals(first.get.apply(pair),
                                "hi");

        Assertions.assertEquals(first.set.apply("bye").apply(pair),
                                Pair.of("bye",
                                        1));

        Assertions.assertEquals(first.modify.apply(String::toUpperCase)
                                            .apply(pair),
                                Pair.of("HI",
                                        1));

        Assertions.assertEquals("abc",
                                first.find.apply(it -> it.startsWith("a"))
                                          .apply(Pair.of("abc",
                                                         3))
                                          .get());

        Assertions.assertEquals(Optional.empty(),
                                first.find.apply(it -> it.startsWith("a"))
                                          .apply(Pair.of("bc",
                                                         3)));

        Assertions.assertEquals(second.get.apply(pair),
                                1);

        Assertions.assertEquals(second.set.apply(2)
                                          .apply(pair),
                                Pair.of("hi",
                                        2));

        Assertions.assertEquals(second.modify.apply(a -> a * 10)
                                             .apply(pair),
                                Pair.of("hi",
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

        Assertions.assertEquals(firstNumber.get.apply(Pair.of("1",
                                                              10)),
                                Optional.of(1));

        Assertions.assertEquals(firstNumber.get.apply(Pair.of("a",
                                                              10)),
                                Optional.empty());

        Assertions.assertEquals(Pair.of("4",
                                        10),
                                firstNumber.set.apply(4)
                                               .apply(Pair.of("",
                                                              10))
        );

    }

    @Test
    public void testCompose() {

        Lens<Pair<String, Integer>, String> first =
                new Lens<>(Pair::first,
                           str -> pair -> Pair.of(str,
                                                  pair.second())
                );


        Lens<Pair<Pair<String, Integer>, Integer>, Pair<String, Integer>> firstPair =
                new Lens<>(Pair::first,
                           newFirst -> pair -> Pair.of(newFirst,
                                                       pair.second())
                );

        Lens<Pair<Pair<String, Integer>, Integer>, String> compose =
                firstPair.compose(first);

        Assertions.assertEquals("a",
                                compose.get.apply(Pair.of(Pair.of("a",
                                                                  2),
                                                          1)));
        Assertions.assertEquals(Pair.of(Pair.of("A",
                                                2),
                                        1),
                                compose.modify.apply(String::toUpperCase).apply(Pair.of(Pair.of("a",
                                                                                                2),
                                                                                        1)));

        Assertions.assertEquals("a",
                                compose.get.apply(Pair.of(Pair.of("a",
                                                                  2),
                                                          1)));

        Assertions.assertEquals(Pair.of(Pair.of("b",
                                                2),
                                        1),
                                compose.set.apply("b")
                                           .apply(Pair.of(Pair.of("a",
                                                                  2),
                                                          1)));
    }

    @Test
    public void testComposeOption() {

        Lens<Map<String, Map<String, Long>>, Map<String, Long>> lens =
                new Lens<>(p -> p.get("hi"),
                           s -> b -> {
                               b.put("hi",
                                     s);
                               return b;
                           }
                );

        Option<Map<String, Long>, Long> option =
                new Option<>(m -> Optional.ofNullable(m.get("bye")),
                             b -> c -> {
                                 c.put("bye",
                                       b);
                                 return c;
                             });


        Option<Map<String, Map<String, Long>>, Long> compose =
                lens.compose(option);

        Map<String, Map<String, Long>> a = new HashMap<>();
        Map<String, Long> b = new HashMap<>();
        a.put("hi",
              b);
        b.put("bye",
              4L);

        Assertions.assertEquals(Optional.of(4L),
                                compose.get.apply(a));
        Map<String, Map<String, Long>> c = compose.set.apply(0L).apply(a);

        Assertions.assertEquals(Optional.of(0L),
                                compose.get.apply(c));


        Map<String, Map<String, Long>> d = new HashMap<>();
        Map<String, Long> e = new HashMap<>();
        d.put("hi",
              e);


        Assertions.assertEquals(d,
                                compose.set.apply(0L).apply(d));

        Assertions.assertEquals(new HashMap<>(),
                                compose.set.apply(0L).apply(new HashMap<>()));


    }

}
