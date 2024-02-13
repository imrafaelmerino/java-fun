package fun.optic;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

public class TestOptionals {

    final Option<Pair<?, String>, String> pairFirstStr =
            new Option<>(p -> p.first() instanceof String ?
                              Optional.of(((String) p.first())) :
                              Optional.empty(),
                         str -> p -> Pair.of(str,
                                             p.second()));

    @Test
    public void testModify() {

        Function<Pair<?, String>, Pair<?, String>> fn =
                pairFirstStr.modify.apply(String::toUpperCase);

        Assertions.assertEquals("HI",
                                fn.apply(Pair.of("hi",
                                                 "bye"))
                                  .first());

        Assertions.assertEquals(1,
                                fn.apply(Pair.of(1,
                                                 "bye"))
                                  .first());
    }


    @Test
    public void testCompose() {

        Option<Pair<?, ?>, Pair<?, ?>> pairFirst =
                new Option<>(p -> p.first() instanceof Pair<?, ?> pair ?
                                  Optional.of(pair) :
                                  Optional.empty(),
                             first -> p -> Pair.of(first,
                                                   p.second()));

        Option<Pair<?, ?>, Pair<?, ?>> compose = pairFirst.compose(pairFirst);


        Assertions.assertFalse(compose.get.apply(Pair.of(1,
                                                         1)).isPresent());

        Assertions.assertFalse(compose.get.apply(Pair.of(Pair.of(1,
                                                                 1),
                                                         1)).isPresent());

        Assertions.assertTrue(compose.get.apply(Pair.of(Pair.of(Pair.of(1,
                                                                        1),
                                                                1),
                                                        1)).isPresent());

        Assertions.assertEquals(Pair.of(1,
                                        1),
                                compose.get.apply(Pair.of(Pair.of(Pair.of(1,
                                                                          1),
                                                                  1),
                                                          1)).get());

        Function<Pair<?, ?>, Pair<?, ?>> turn =
                compose.modify
                        .apply(p -> Pair.of(p.second(),
                                            p.first()));

        Assertions.assertEquals(Pair.of(Pair.of(Pair.of(2,
                                                        1),
                                                1),
                                        1),
                                turn.apply(Pair.of(Pair.of(Pair.of(1,
                                                                   2),
                                                           1),
                                                   1)));

        Assertions.assertEquals(Pair.of(Pair.of("hi",
                                                1),
                                        1),
                                turn.apply(Pair.of(Pair.of("hi",
                                                           1),
                                                   1)));

    }

}
