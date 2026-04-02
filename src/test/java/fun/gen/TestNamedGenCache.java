package fun.gen;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TestNamedGenCache {

    @BeforeEach
    void setUp() {
        GenCache.cache.clear();
    }

    @AfterEach
    void tearDown() {
        GenCache.cache.clear();
    }

    @Test
    public void shouldResolveRegisteredNamedGenerator() {
        Gen<Integer> named = NamedGen.of("answer",
                                         Gen.constant(42));

        Integer value = named.sample(new Random(1L)).get();

        Assertions.assertEquals(42,
                                value);
    }

    @Test
    public void shouldThrowWhenNameIsRegisteredTwice() {
        NamedGen.of("dup",
                    Gen.constant("first"));

        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class,
                                                              () -> NamedGen.of("dup",
                                                                                Gen.constant("second")));

        Assertions.assertTrue(ex.getMessage().contains("already been created"));
    }

    @Test
    public void shouldThrowWhenNamedGeneratorWasNotRegistered() {
        Gen<Integer> missing = NamedGen.of("missing");

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                                                      () -> missing.sample(new Random(2L)).get());

        Assertions.assertTrue(ex.getMessage().contains("doesn't exist"));
    }
}
