package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

class TestCsvStreamBuilder {

    @Test
    void shouldParseHeadersUsingConfiguredSeparator() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-separator",
                                         ".csv");
        Files.writeString(file,
                          "a;b\n1;2\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ";")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a"));
            Assertions.assertEquals(2,
                                    records.get(0).getInt("b"));
        }
    }

    @Test
    void shouldConvertQuotedFalseToBoolean() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-quoted-bool",
                                         ".csv");
        Files.writeString(file,
                          "flag\n\"false\"\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertFalse(records.get(0).getBool("flag"));
        }
    }

    @Test
    void shouldSupportRegexMetaCharacterSeparator() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-pipe-separator",
                                         ".csv");
        Files.writeString(file,
                          "a|b\n1|2\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              "|")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a"));
            Assertions.assertEquals(2,
                                    records.get(0).getInt("b"));
        }
    }

    @Test
    void shouldUnquoteEmptyStringValues() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-empty-quoted",
                                         ".csv");
        Files.writeString(file,
                          "name\n\"\"\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals("",
                                    records.get(0).getStr("name"));
        }
    }

    @Test
    void shouldValidateExpectedHeaders() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-expected-headers",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n");

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> CsvStreamBuilder.of(file.toFile(),
                                                          ",")
                                                      .withExpectedHeaders("a",
                                                                           "c")
                                                      .get()
                                                      .toList());
    }

    @Test
    void shouldPassWhenExpectedHeadersMatchAfterMapping() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-expected-headers-pass",
                                         ".csv");
        Files.writeString(file,
                          " A , B \n1,2\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withHeaderMapper(String::trim)
                                          .withExpectedHeaders("A",
                                                               "B")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("A"));
            Assertions.assertEquals(2,
                                    records.get(0).getInt("B"));
        }
    }

    @Test
    void shouldFailOnRowsWithDifferentWidthInStrictMode() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-strict-width-fail",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2,3\n");

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> CsvStreamBuilder.of(file.toFile(),
                                                          ",")
                                                      .withStrictRowWidth()
                                                      .get()
                                                      .toList());
    }

    @Test
    void shouldAcceptTrailingEmptyColumnInStrictMode() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-strict-width-empty-tail",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withStrictRowWidth()
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals("1",
                                    records.get(0).getStr("a"));
            Assertions.assertEquals("",
                                    records.get(0).getStr("b"));
        }
    }

    @Test
    void shouldMapConfiguredNullTokensToNull() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-null-tokens",
                                         ".csv");
        Files.writeString(file,
                          "a,b,c\nnull,N/A,\"\"\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withNullTokens(Set.of("null",
                                                                 "N/A",
                                                                 ""))
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertTrue(records.get(0).getOptStr("a").isEmpty());
            Assertions.assertTrue(records.get(0).getOptStr("b").isEmpty());
            Assertions.assertTrue(records.get(0).getOptStr("c").isEmpty());
        }
    }

    @Test
    void shouldSupportVarargsNullTokens() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-null-tokens-varargs",
                                         ".csv");
        Files.writeString(file,
                          "x\nNULL\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withNullTokens("NULL")
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertTrue(records.get(0).getOptStr("x").isEmpty());
        }
    }
}
