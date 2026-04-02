package fun.gen;

import com.sun.management.UnixOperatingSystemMXBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class CsvStreamBuilderTest {

    private static final String DEFAULT_SEPARATOR = ",";
    private static final int HEADER_VALIDATION_REPETITIONS = 250;
    private static final long MAX_ALLOWED_OPEN_FD_DELTA = 30L;

    private static Stream<MyRecord> openCsvStream(Path file,
                                                  UnaryOperator<CsvStreamBuilder> configuration) {
        return configuration.apply(CsvStreamBuilder.of(file.toFile(),
                                                       DEFAULT_SEPARATOR)).get();
    }

    private static List<MyRecord> readCsv(Path file,
                                          UnaryOperator<CsvStreamBuilder> configuration) throws Exception {
        return readCsv(file,
                       DEFAULT_SEPARATOR,
                       configuration);
    }

    private static List<MyRecord> readCsv(Path file,
                                          String separator,
                                          UnaryOperator<CsvStreamBuilder> configuration) throws Exception {
        try (var stream = configuration.apply(CsvStreamBuilder.of(file.toFile(),
                                                                  separator)).get()) {
            return stream.toList();
        }
    }

    private static Path writeTempCsv(String testName,
                                     String content) throws Exception {
        Path file = Files.createTempFile("java-fun-csv-" + testName,
                                         ".csv");
        Files.writeString(file,
                          content);
        return file;
    }

    private static void assertSingleRecord(List<MyRecord> records) {
        Assertions.assertEquals(1,
                                records.size());
    }

    private static long openFileDescriptorCount() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof UnixOperatingSystemMXBean unix) {
            return unix.getOpenFileDescriptorCount();
        }
        return -1L;
    }

    @Nested
    class ParsingAndMappingTests {

        @Test
        void shouldParseHeadersWhenUsingConfiguredSeparator() throws Exception {
            Path file = writeTempCsv("separator",
                                     "a;b\n1;2\n");

            List<MyRecord> records = readCsv(file,
                                             ";",
                                             UnaryOperator.identity());
            assertSingleRecord(records);
            Assertions.assertEquals(1,
                                    records.getFirst().getInt("a"));
            Assertions.assertEquals(2,
                                    records.getFirst().getInt("b"));
        }

        @Test
        void shouldTrimValuesWhenUsingDefaultConfiguration() throws Exception {
            Path file = writeTempCsv("default-trim",
                                     "name\n  value  \n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("value",
                                    records.getFirst().getString("name"));
        }

        @Test
        void shouldAllowDisablingTrimValuesWhenConfigured() throws Exception {
            Path file = writeTempCsv("disable-trim",
                                     "name\n  value  \n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withTrimValues(false)
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("  value  ",
                                    records.getFirst().getString("name"));
        }

        @Test
        void shouldKeepLegacyRawInputByDefaultWhenUsingCustomValueMapper() throws Exception {
            Path file = writeTempCsv("custom-mapper-raw",
                                     "name\n  value  \n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withValueMapper((header, value) -> "[" + value + "]")
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("[  value  ]",
                                    records.getFirst().getString("name"));
        }

        @Test
        void shouldApplyTrimBeforeCustomValueMapperWhenTrimIsEnabled() throws Exception {
            Path file = writeTempCsv("custom-mapper-trim",
                                     "name\n  value  \n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withValueMapper((header, value) -> "[" + value + "]")
                                                               .withTrimValues(true)
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("[value]",
                                    records.getFirst().getString("name"));
        }

        @Test
        void shouldConvertQuotedFalseToBooleanWhenUsingDefaultMapper() throws Exception {
            Path file = writeTempCsv("quoted-bool",
                                     "flag\n\"false\"\n");

            List<MyRecord> records = readCsv(file,
                                             UnaryOperator.identity());
            assertSingleRecord(records);
            Assertions.assertFalse(records.getFirst().getBoolean("flag"));
        }

        @Test
        void shouldSupportRegexMetaCharacterSeparatorWhenConfiguredAsSeparator() throws Exception {
            Path file = writeTempCsv("pipe-separator",
                                     "a|b\n1|2\n");

            List<MyRecord> records = readCsv(file,
                                             "|",
                                             UnaryOperator.identity());
            assertSingleRecord(records);
            Assertions.assertEquals(1,
                                    records.getFirst().getInt("a"));
            Assertions.assertEquals(2,
                                    records.getFirst().getInt("b"));
        }

        @Test
        void shouldKeepSeparatorInsideQuotedValueWhenFieldIsQuoted() throws Exception {
            Path file = writeTempCsv("quoted-separator",
                                     "a,b\n\"1,2\",x\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withoutTypeConversion()
                                                               .withStrictRowWidth());
            assertSingleRecord(records);
            Assertions.assertEquals("1,2",
                                    records.getFirst().getString("a"));
            Assertions.assertEquals("x",
                                    records.getFirst().getString("b"));
        }

        @Test
        void shouldUnescapeDoubleQuotesWhenParsingQuotedValues() throws Exception {
            Path file = writeTempCsv("escaped-quotes",
                                     "text\n\"he said \"\"hi\"\"\"\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("he said \"hi\"",
                                    records.getFirst().getString("text"));
        }

        @Test
        void shouldUnquoteEmptyStringValuesWhenConfiguredAsQuoted() throws Exception {
            Path file = writeTempCsv("empty-quoted",
                                     "name\n\"\"\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("",
                                    records.getFirst().getString("name"));
        }
    }

    @Nested
    class HeaderAndRowWidthTests {

        @Test
        void shouldValidateExpectedHeadersWhenConfigured() throws Exception {
            Path file = writeTempCsv("expected-headers",
                                     "a,b\n1,2\n");

            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> readCsv(file,
                                                  builder -> builder.withExpectedHeaders("a",
                                                                                         "c")));
        }

        @Test
        void shouldPassWhenExpectedHeadersMatchAfterMapping() throws Exception {
            Path file = writeTempCsv("expected-headers-pass",
                                     " A , B \n1,2\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withHeaderMapper(String::trim)
                                                               .withExpectedHeaders("A",
                                                                                    "B"));
            assertSingleRecord(records);
            Assertions.assertEquals(1,
                                    records.getFirst().getInt("A"));
            Assertions.assertEquals(2,
                                    records.getFirst().getInt("B"));
        }

        @Test
        void shouldBeAppliedOnlyOnceWhenUsingHeaderMapper() throws Exception {
            Path file = writeTempCsv("header-mapper-once",
                                     "a\n1\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withHeaderMapper(h -> h + "_x"));
            assertSingleRecord(records);
            Assertions.assertEquals(1,
                                    records.getFirst().getInt("a_x"));
            Assertions.assertTrue(records.getFirst().getOptionalInt("a_x_x").isEmpty());
        }

        @Test
        void shouldFailOnRowsWithDifferentWidthWhenUsingStrictMode() throws Exception {
            Path file = writeTempCsv("strict-width-fail",
                                     "a,b\n1,2,3\n");

            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> readCsv(file,
                                                  builder -> builder.withStrictRowWidth()));
        }

        @Test
        void shouldAcceptTrailingEmptyColumnWhenUsingStrictMode() throws Exception {
            Path file = writeTempCsv("strict-width-empty-tail",
                                     "a,b\n1,\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withStrictRowWidth()
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertEquals("1",
                                    records.getFirst().getString("a"));
            Assertions.assertEquals("",
                                    records.getFirst().getString("b"));
        }
    }

    @Nested
    class NullTokenHandlingTests {

        @Test
        void shouldMapConfiguredNullTokensToNullWhenParsingValues() throws Exception {
            Path file = writeTempCsv("null-tokens",
                                     "a,b,c\nnull,N/A,\"\"\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withNullTokens(Set.of("null",
                                                                                      "N/A",
                                                                                      ""))
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertTrue(records.getFirst().getOptionalString("a").isEmpty());
            Assertions.assertTrue(records.getFirst().getOptionalString("b").isEmpty());
            Assertions.assertTrue(records.getFirst().getOptionalString("c").isEmpty());
        }

        @Test
        void shouldSupportVarargsNullTokensWhenConfigured() throws Exception {
            Path file = writeTempCsv("null-tokens-varargs",
                                     "x\nNULL\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withNullTokens("NULL")
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertTrue(records.getFirst().getOptionalString("x").isEmpty());
        }

        @Test
        void shouldMapTokensToNullWhenUsingCustomNullMatcher() throws Exception {
            Path file = writeTempCsv("null-token-matcher",
                                     "a,b,c\nNuLl,  n/a  ,ok\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withNullTokenMatcher(token -> {
                                                                   String normalized = token.trim().toLowerCase(Locale.ROOT);
                                                                   return normalized.equals("null")
                                                                           || normalized.equals("n/a");
                                                               })
                                                               .withoutTypeConversion());
            assertSingleRecord(records);
            Assertions.assertTrue(records.getFirst().getOptionalString("a").isEmpty());
            Assertions.assertTrue(records.getFirst().getOptionalString("b").isEmpty());
            Assertions.assertEquals("ok",
                                    records.getFirst().getString("c"));
        }
    }

    @Nested
    class ErrorHandlingAndResourcesTests {

        @Test
        void shouldSkipMalformedRowsWhenConfigured() throws Exception {
            Path file = writeTempCsv("skip-malformed",
                                     "a,b\n1,2\n3,4,5\n6,7\n");

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withStrictRowWidth()
                                                               .withSkipMalformedRows());
            Assertions.assertEquals(2,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a"));
            Assertions.assertEquals(6,
                                    records.get(1).getInt("a"));
        }

        @Test
        void shouldReceiveCsvRowNumberWhenUsingRowErrorHandler() throws Exception {
            Path file = writeTempCsv("row-handler-row-number",
                                     "a,b\n1,2\n3,4,5\n");
            AtomicLong rowSeen = new AtomicLong(-1);

            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> readCsv(file,
                                                  builder -> builder.withStrictRowWidth()
                                                                    .withRowErrorHandler((rowNumber, ex) -> {
                                                                        rowSeen.set(rowNumber);
                                                                        return CsvRowErrorAction.THROW;
                                                                    })));
            Assertions.assertEquals(3,
                                    rowSeen.get());
        }

        @Test
        void shouldCaptureMalformedRowsWhenSkippingWithErrorCollector() throws Exception {
            Path file = writeTempCsv("error-collector-skip",
                                     "a,b\n1,2\n3,4,5\n6,7,8\n9,10\n");
            List<Long> rows = new ArrayList<>();

            List<MyRecord> records = readCsv(file,
                                             builder -> builder.withStrictRowWidth()
                                                               .withErrorCollector((row, ex) -> rows.add(row))
                                                               .withSkipMalformedRows());
            Assertions.assertEquals(2,
                                    records.size());
            Assertions.assertEquals(List.of(3L,
                                            4L),
                                    rows);
        }

        @Test
        void shouldCaptureRowBeforeThrowingWhenUsingErrorCollector() throws Exception {
            Path file = writeTempCsv("error-collector-throw",
                                     "a,b\n1,2\n3,4,5\n");
            AtomicLong rowSeen = new AtomicLong(-1);

            Assertions.assertThrows(IllegalArgumentException.class,
                                    () -> readCsv(file,
                                                  builder -> builder.withStrictRowWidth()
                                                                    .withErrorCollector((row, ex) -> rowSeen.set(row))));
            Assertions.assertEquals(3,
                                    rowSeen.get());
        }

        @Test
        void shouldPropagateDownstreamConsumerExceptionsEvenWhenSkippingMalformedRows() throws Exception {
            Path file = writeTempCsv("downstream-exception",
                                     "a,b\n1,2\n3,4\n");
            AtomicInteger handlerCalls = new AtomicInteger();

            try (var stream = openCsvStream(file,
                                            builder -> builder.withRowErrorHandler((row, ex) -> {
                                                handlerCalls.incrementAndGet();
                                                return CsvRowErrorAction.SKIP;
                                            }))) {
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
            Path file = writeTempCsv("expected-headers-close",
                                     "a,b\n1,2\n");

            long before = openFileDescriptorCount();
            Assumptions.assumeTrue(before >= 0,
                                   "Open file descriptor count not available on this platform");

            for (int i = 0; i < HEADER_VALIDATION_REPETITIONS; i++) {
                Assertions.assertThrows(IllegalArgumentException.class,
                                        () -> readCsv(file,
                                                      builder -> builder.withExpectedHeaders("a",
                                                                                             "c")));
            }

            long after = openFileDescriptorCount();
            Assertions.assertTrue(after - before < MAX_ALLOWED_OPEN_FD_DELTA,
                                  "Potential reader leak detected during header validation mismatch");
        }
    }
}
