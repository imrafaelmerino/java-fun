package fun.optic;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

public class TestOptionals {

    Option<Pair<?, String>, String> pairFirstStr =
            new Option<>(p -> p.first() instanceof String ?
                              Optional.ofNullable(((String) p.first())) :
                              Optional.empty(),
                         str -> p -> new Pair<>(str,
                                                p.second()));

    @Test
    public void testModify() {

        Function<Pair<?, String>, Pair<?, String>> fn =
                pairFirstStr.modify.apply(String::toUpperCase);

        Assertions.assertEquals("HI",
                                fn.apply(new Pair<>("hi",
                                                    "bye"))
                                  .first());

        Assertions.assertEquals(1,
                                fn.apply(new Pair<>(1,
                                                    "bye"))
                                  .first());
    }


    @Test
    public void testCompose() {

        Option<Pair<?, ?>, Pair<?, ?>> pairFirst =
                new Option<>(p -> p.first() instanceof Pair ?
                                  Optional.of(((Pair) p.first())) :
                                  Optional.empty(),
                             first -> p -> new Pair<>(first,
                                                      p.second()));

        Option<Pair<?, ?>, Pair<?, ?>> compose = pairFirst.compose(pairFirst);


        Assertions.assertFalse(compose.get.apply(new Pair<>(1,
                                                            1)).isPresent());

        Assertions.assertFalse(compose.get.apply(new Pair<>(new Pair<>(1,
                                                                       1),
                                                            1)).isPresent());

        Assertions.assertTrue(compose.get.apply(new Pair<>(new Pair<>(new Pair<>(1,
                                                                                 1),
                                                                      1),
                                                           1)).isPresent());

        Assertions.assertEquals(new Pair<>(1,
                                           1),
                                compose.get.apply(new Pair<>(new Pair<>(new Pair<>(1,
                                                                                   1),
                                                                        1),
                                                             1)).get());

        Function<Pair<?, ?>, Pair<?, ?>> turn =
                compose.modify
                        .apply(p -> new Pair<>(p.second(),
                                               p.first()));

        Assertions.assertEquals(new Pair<>(new Pair<>(new Pair<>(2,
                                                                 1),
                                                      1),
                                           1),
                                turn.apply(new Pair<>(new Pair<>(new Pair<>(1,
                                                                            2),
                                                                 1),
                                                      1)));

        Assertions.assertEquals(new Pair<>(new Pair<>("hi",
                                                      1),
                                           1),
                                turn.apply(new Pair<>(new Pair<>("hi",
                                                                 1),
                                                      1)));

    }

}
