package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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
    void shouldTrimValuesByDefault() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-default-trim",
                                         ".csv");
        Files.writeString(file,
                          "name\n  value  \n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals("value",
                                    records.get(0).getStr("name"));
        }
    }

    @Test
    void shouldAllowDisablingTrimValues() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-disable-trim",
                                         ".csv");
        Files.writeString(file,
                          "name\n  value  \n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withTrimValues(false)
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals("  value  ",
                                    records.get(0).getStr("name"));
        }
    }

    @Test
    void customValueMapperShouldKeepLegacyRawInputByDefault() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-custom-mapper-raw",
                                         ".csv");
        Files.writeString(file,
                          "name\n  value  \n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withValueMapper((header, value) -> "[" + value + "]")
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals("[  value  ]",
                                    records.get(0).getStr("name"));
        }
    }

    @Test
    void customValueMapperCanWorkWithExplicitTrimValues() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-custom-mapper-trim",
                                         ".csv");
        Files.writeString(file,
                          "name\n  value  \n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withValueMapper((header, value) -> "[" + value + "]")
                                          .withTrimValues(true)
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals("[value]",
                                    records.get(0).getStr("name"));
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

    @Test
    void shouldMapTokensToNullUsingCustomMatcher() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-null-token-matcher",
                                         ".csv");
        Files.writeString(file,
                          "a,b,c\nNuLl,  n/a  ,ok\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withNullTokenMatcher(token -> {
                                              String normalized = token.trim().toLowerCase(Locale.ROOT);
                                              return normalized.equals("null") || normalized.equals("n/a");
                                          })
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertTrue(records.get(0).getOptStr("a").isEmpty());
            Assertions.assertTrue(records.get(0).getOptStr("b").isEmpty());
            Assertions.assertEquals("ok",
                                    records.get(0).getStr("c"));
        }
    }

    @Test
    void shouldSkipMalformedRowsWhenConfigured() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-skip-malformed",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n3,4,5\n6,7\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withStrictRowWidth()
                                          .withSkipMalformedRows()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(2,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a"));
            Assertions.assertEquals(6,
                                    records.get(1).getInt("a"));
        }
    }

    @Test
    void rowErrorHandlerShouldReceiveCsvRowNumber() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-row-handler-row-number",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n3,4,5\n");
        AtomicLong rowSeen = new AtomicLong(-1);

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> CsvStreamBuilder.of(file.toFile(),
                                                          ",")
                                                      .withStrictRowWidth()
                                                      .withRowErrorHandler((rowNumber, ex) -> {
                                                          rowSeen.set(rowNumber);
                                                          return CsvRowErrorAction.THROW;
                                                      })
                                                      .get()
                                                      .toList());
        Assertions.assertEquals(3,
                                rowSeen.get());
    }

    @Test
    void errorCollectorShouldCaptureMalformedRowsWhenSkipping() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-error-collector-skip",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n3,4,5\n6,7,8\n9,10\n");
        List<Long> rows = new ArrayList<>();

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withStrictRowWidth()
                                          .withErrorCollector((row, ex) -> rows.add(row))
                                          .withSkipMalformedRows()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(2,
                                    records.size());
            Assertions.assertEquals(List.of(3L,
                                           4L),
                                    rows);
        }
    }

    @Test
    void errorCollectorShouldCaptureRowBeforeThrowing() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-error-collector-throw",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n3,4,5\n");
        AtomicLong rowSeen = new AtomicLong(-1);

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> CsvStreamBuilder.of(file.toFile(),
                                                          ",")
                                                      .withStrictRowWidth()
                                                      .withErrorCollector((row, ex) -> rowSeen.set(row))
                                                      .get()
                                                      .toList());
        Assertions.assertEquals(3,
                                rowSeen.get());
    }
}
