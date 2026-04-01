package fun.gen;

import com.sun.management.UnixOperatingSystemMXBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
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
                                    records.get(0).getString("name"));
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
                                    records.get(0).getString("name"));
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
                                    records.get(0).getString("name"));
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
                                    records.get(0).getString("name"));
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
            Assertions.assertFalse(records.get(0).getBoolean("flag"));
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
    void shouldKeepSeparatorInsideQuotedValue() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-quoted-separator",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n\"1,2\",x\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withoutTypeConversion()
                                          .withStrictRowWidth()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals("1,2",
                                    records.get(0).getString("a"));
            Assertions.assertEquals("x",
                                    records.get(0).getString("b"));
        }
    }

    @Test
    void shouldUnescapeDoubleQuotesInsideQuotedValues() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-escaped-quotes",
                                         ".csv");
        Files.writeString(file,
                          "text\n\"he said \"\"hi\"\"\"\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals("he said \"hi\"",
                                    records.get(0).getString("text"));
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
                                    records.get(0).getString("name"));
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
    void headerMapperShouldBeAppliedOnlyOnce() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-header-mapper-once",
                                         ".csv");
        Files.writeString(file,
                          "a\n1\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withHeaderMapper(h -> h + "_x")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a_x"));
            Assertions.assertTrue(records.get(0).getOptionalInt("a_x_x").isEmpty());
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
                                    records.get(0).getString("a"));
            Assertions.assertEquals("",
                                    records.get(0).getString("b"));
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
            Assertions.assertTrue(records.get(0).getOptionalString("a").isEmpty());
            Assertions.assertTrue(records.get(0).getOptionalString("b").isEmpty());
            Assertions.assertTrue(records.get(0).getOptionalString("c").isEmpty());
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
            Assertions.assertTrue(records.get(0).getOptionalString("x").isEmpty());
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
            Assertions.assertTrue(records.get(0).getOptionalString("a").isEmpty());
            Assertions.assertTrue(records.get(0).getOptionalString("b").isEmpty());
            Assertions.assertEquals("ok",
                                    records.get(0).getString("c"));
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

    @Test
    void shouldPropagateDownstreamConsumerExceptionsEvenWhenSkippingMalformedRows() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-downstream-exception",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n3,4\n");
        AtomicInteger handlerCalls = new AtomicInteger();

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withRowErrorHandler((row, ex) -> {
                                              handlerCalls.incrementAndGet();
                                              return CsvRowErrorAction.SKIP;
                                          })
                                          .get()) {
            Assertions.assertThrows(IllegalStateException.class,
                                    () -> stream.forEach(record -> {
                                        throw new IllegalStateException("downstream failure");
                                    }));
        }
        Assertions.assertEquals(0,
                                handlerCalls.get());
    }

    @Test
    void shouldCloseReaderWhenExpectedHeadersValidationFails() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-expected-headers-close",
                                         ".csv");
        Files.writeString(file,
                          "a,b\n1,2\n");

        long before = openFileDescriptorCount();
        Assumptions.assumeTrue(before >= 0,
                               "Open file descriptor count not available on this platform");

        for (int i = 0; i < 250; i++) {
            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> CsvStreamBuilder.of(file.toFile(),
                                                              ",")
                                                          .withExpectedHeaders("a",
                                                                               "c")
                                                          .get()
                                                          .toList());
        }

        long after = openFileDescriptorCount();
        Assertions.assertTrue(after - before < 30,
                              "Potential reader leak detected during header validation mismatch");
    }

    private static long openFileDescriptorCount() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof UnixOperatingSystemMXBean unix) {
            return unix.getOpenFileDescriptorCount();
        }
        return -1L;
    }
}
