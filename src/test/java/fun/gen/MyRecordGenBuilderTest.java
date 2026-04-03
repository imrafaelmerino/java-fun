package fun.gen;

import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MyRecordGenBuilderTest {

    @Test
    void shouldCreateRecordGeneratorWhenUsingBuilder() {
        MyRecordGen gen = MyRecordGen.builder()
                                     .field("id",
                                            Gen.constant(1))
                                     .field("name",
                                            Gen.constant("ana"))
                                     .build();

        MyRecord record = gen.sample().get();

        Assertions.assertEquals(1,
                                record.getInt("id"));
        Assertions.assertEquals("ana",
                                record.getString("name"));
    }

    @Test
    void shouldPreserveInsertionOrderWhenUsingOfEntries() {
        MyRecordGen gen = MyRecordGen.ofEntries(
                Pair.of("a",
                        Gen.constant(1)),
                Pair.of("b",
                        Gen.constant(2)),
                Pair.of("c",
                        Gen.constant(3))
        );

        MyRecord record = gen.sample().get();
        Assertions.assertEquals(List.of("a",
                                        "b",
                                        "c"),
                                new ArrayList<>(record.asMap().keySet()));
    }

    @Test
    void shouldPreserveInsertionOrderWhenUsingCompactOf() {
        MyRecordGen gen = MyRecordGen.of("a",
                                         Gen.constant(1),
                                         "b",
                                         Gen.constant(2),
                                         "c",
                                         Gen.constant(3));

        MyRecord record = gen.sample().get();
        Assertions.assertEquals(List.of("a",
                                        "b",
                                        "c"),
                                new ArrayList<>(record.asMap().keySet()));
    }

    @Test
    void shouldRejectOddAdditionalPairsWhenUsingCompactOf() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MyRecordGen.of("a",
                                                     Gen.constant(1),
                                                     "b"));
    }

    @Test
    void shouldRejectNonStringKeysWhenUsingCompactOf() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MyRecordGen.of("a",
                                                     Gen.constant(1),
                                                     2,
                                                     Gen.constant(2)));
    }

    @Test
    void shouldRejectNonGeneratorValuesWhenUsingCompactOf() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MyRecordGen.of("a",
                                                     Gen.constant(1),
                                                     "b",
                                                     2));
    }

    @Test
    void shouldSupportOptionalAndNullableConfigurationWhenUsingBuilder() {
        MyRecordGen gen = MyRecordGen.builder()
                                     .field("required",
                                            Gen.constant(1))
                                     .field("optional",
                                            Gen.constant(2))
                                     .optional("optional")
                                     .nullable("required")
                                     .build();

        Assertions.assertDoesNotThrow(() -> gen.sample(100)
                                               .forEach(record -> {
                                                   Assertions.assertTrue(record.containsKey("required"));
                                               }));
    }
}
