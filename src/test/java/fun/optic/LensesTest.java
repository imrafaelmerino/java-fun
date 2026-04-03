package fun.optic;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class LensesTest {

    private final Lens<Pair<String, Integer>, String> first =
            new Lens<>(Pair::first,
                       str -> pair -> Pair.of(str,
                                              pair.second()));

    private final Lens<Pair<String, Integer>, Integer> second =
            new Lens<>(Pair::second,
                       integer -> pair -> Pair.of(pair.first(),
                                                  integer));

    @Nested
    class PairLensTests {

        @Test
        void shouldReadFocusedValueWhenUsingGetOnFirstLens() {
            Pair<String, Integer> pair = Pair.of("hi", 1);
            Assertions.assertEquals("hi", first.get.apply(pair));
        }

        @Test
        void shouldReplaceFocusedValueWhenUsingSetOnFirstLens() {
            Pair<String, Integer> pair = Pair.of("hi", 1);
            Assertions.assertEquals(Pair.of("bye", 1),
                                    first.set.apply("bye").apply(pair));
        }

        @Test
        void shouldTransformFocusedValueWhenUsingModifyOnFirstLens() {
            Pair<String, Integer> pair = Pair.of("hi", 1);
            Assertions.assertEquals(Pair.of("HI", 1),
                                    first.modify.apply(String::toUpperCase).apply(pair));
        }

        @Test
        void shouldFindFocusedValueWhenPredicateMatchesOnFirstLens() {
            Assertions.assertEquals(Optional.of("abc"),
                                    first.find.apply(it -> it.startsWith("a"))
                                              .apply(Pair.of("abc", 3)));
            Assertions.assertEquals(Optional.empty(),
                                    first.find.apply(it -> it.startsWith("a"))
                                              .apply(Pair.of("bc", 3)));
        }

        @Test
        void shouldReadAndModifyFocusedValueWhenUsingSecondLens() {
            Pair<String, Integer> pair = Pair.of("hi", 1);
            Assertions.assertEquals(1, second.get.apply(pair));
            Assertions.assertEquals(Pair.of("hi", 2),
                                    second.set.apply(2).apply(pair));
            Assertions.assertEquals(Pair.of("hi", 10),
                                    second.modify.apply(a -> a * 10).apply(pair));
        }
    }

    @Nested
    class CompositionTests {

        @Test
        void shouldComposeLensWithPrismWhenFocusingOptionalNumericFirstValue() {
            Prism<String, Integer> parseIntPrism =
                    new Prism<>(s -> {
                        try {
                            return Optional.of(Integer.parseInt(s));
                        } catch (NumberFormatException e) {
                            return Optional.empty();
                        }
                    },
                                Object::toString);

            Option<Pair<String, Integer>, Integer> firstNumber = first.compose(parseIntPrism);

            Assertions.assertEquals(Optional.of(1),
                                    firstNumber.get.apply(Pair.of("1", 10)));
            Assertions.assertEquals(Optional.empty(),
                                    firstNumber.get.apply(Pair.of("a", 10)));
            Assertions.assertEquals(Pair.of("4", 10),
                                    firstNumber.set.apply(4).apply(Pair.of("", 10)));
        }

        @Test
        void shouldComposeTwoLensesWhenAccessingNestedFields() {
            Lens<Pair<Pair<String, Integer>, Integer>, Pair<String, Integer>> firstPair =
                    new Lens<>(Pair::first,
                               newFirst -> pair -> Pair.of(newFirst,
                                                           pair.second()));

            Lens<Pair<Pair<String, Integer>, Integer>, String> compose = firstPair.compose(first);

            Pair<Pair<String, Integer>, Integer> value = Pair.of(Pair.of("a", 2), 1);
            Assertions.assertEquals("a", compose.get.apply(value));
            Assertions.assertEquals(Pair.of(Pair.of("A", 2), 1),
                                    compose.modify.apply(String::toUpperCase).apply(value));
            Assertions.assertEquals(Pair.of(Pair.of("b", 2), 1),
                                    compose.set.apply("b").apply(value));
        }

        @Test
        void shouldComposeLensAndOptionWhenNavigatingNestedMap() {
            Lens<Map<String, Map<String, Long>>, Map<String, Long>> lens =
                    new Lens<>(p -> p.get("hi"),
                               s -> b -> {
                                   b.put("hi", s);
                                   return b;
                               });

            Option<Map<String, Long>, Long> option =
                    new Option<>(m -> Optional.ofNullable(m.get("bye")),
                                 b -> c -> {
                                     c.put("bye", b);
                                     return c;
                                 });

            Option<Map<String, Map<String, Long>>, Long> compose = lens.compose(option);

            Map<String, Map<String, Long>> map = new HashMap<>();
            Map<String, Long> inner = new HashMap<>();
            map.put("hi", inner);
            inner.put("bye", 4L);

            Assertions.assertEquals(Optional.of(4L), compose.get.apply(map));
            Assertions.assertEquals(Optional.of(0L),
                                    compose.get.apply(compose.set.apply(0L).apply(map)));

            Map<String, Map<String, Long>> mapWithoutBye = new HashMap<>();
            mapWithoutBye.put("hi", new HashMap<>());
            Assertions.assertEquals(mapWithoutBye,
                                    compose.set.apply(0L).apply(mapWithoutBye));
            Assertions.assertEquals(new HashMap<>(),
                                    compose.set.apply(0L).apply(new HashMap<>()));
        }
    }
}
