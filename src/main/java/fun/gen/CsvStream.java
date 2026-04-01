package fun.gen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Internal CSV reader that exposes rows as {@link MyRecord}.
 * Header and value mappers are applied on top of raw CSV tokens.
 */
class CsvStream implements Supplier<Stream<MyRecord>> {

    private final Function<String, String> headerMapper;
    private final BiFunction<String, String, String> valueMapper;

    private final boolean enableTypeConversion;

    private static final Pattern numberPattern = Pattern.compile("^-?(?:0|[1-9]\\d*)(\\.\\d+)?$");
    private final File path;

    private List<String> headers;

    private final String separator;
    private final List<String> expectedHeaders;
    private final boolean strictRowWidth;

    /**
     * Constructs a CsvStream with custom mapping functions, type conversion, and separator.
     *
     * @param path                 The path to the CSV file.
     * @param headerMapper         Function to map CSV headers.
     * @param valueMapper          BiFunction to map CSV values based on headers.
     * @param enableTypeConversion Enable or disable automatic type conversion.
     * @param separator            CSV value separator.
     */
    CsvStream(
            File path,
            Function<String, String> headerMapper,
            BiFunction<String, String, String> valueMapper,
            boolean enableTypeConversion,
            String separator,
            List<String> expectedHeaders,
            boolean strictRowWidth) {
        this.path = path;
        this.headerMapper = Objects.requireNonNull(headerMapper);
        this.valueMapper = Objects.requireNonNull(valueMapper);
        this.enableTypeConversion = enableTypeConversion;
        this.separator = separator;
        this.expectedHeaders = expectedHeaders == null ? null : List.copyOf(expectedHeaders);
        this.strictRowWidth = strictRowWidth;
    }


    /**
     * Attempts to convert a string value to a primitive-friendly type:
     * boolean, int, long, or double (in that order). If no conversion matches,
     * the original (possibly unquoted) string is returned.
     */
    private Object tryConvert(String value) {
        var xs = removeQuotesIfExist(value);

        if (xs.equalsIgnoreCase("true") || xs.equalsIgnoreCase("false"))
            return Boolean.parseBoolean(xs);

        if (numberPattern.matcher(xs).matches()) {
            try {
                return Integer.parseInt(xs);
            } catch (NumberFormatException ignored) {
                // it's not an integer; that's fine, just continue
            }

            try {
                return Long.parseLong(xs);
            } catch (NumberFormatException ignored) {
                // it's not a long; that's fine, just continue

            }

            try {
                return Double.parseDouble(xs);
            } catch (NumberFormatException ignored) {
                // it's not a double; that's fine, just continue

            }
        }

        return xs;
    }

    /**
     * Removes quotes from the start and end of a string if they exist.
     *
     * @param value The string value to process.
     * @return The processed string value.
     */
    static String removeQuotesIfExist(String value) {
        return value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")
               ?
               value.substring(1,
                               value.length() - 1) :
               value;
    }

    /**
     * Reads the CSV and returns it as a lazy stream of records.
     * The stream closes the underlying reader on stream close.
     *
     * @return A stream of parsed records.
     * @throws IllegalArgumentException If the CSV has no header line.
     * @throws UncheckedIOException     If an I/O error occurs while reading the file.
     */
    @Override
    public Stream<MyRecord> get() {
        try {
            var br = new BufferedReader(new FileReader(path,
                                                       StandardCharsets.UTF_8));
            var headerLine = br.readLine();
            if (headerLine == null) throw new IllegalArgumentException("CSV file has no header line.");
            this.headers = Arrays.stream(headerLine.split(Pattern.quote(separator),
                                                          -1))
                                 .map(CsvStream::removeQuotesIfExist)
                                 .map(headerMapper)
                                 .toList();
            validateExpectedHeaders();
            var spliterator = new CsvSpliterator(br,
                                                 separator);
            return StreamSupport.stream(spliterator,
                                        false)
                                .map(this::lineToRecord)
                                .onClose(
                                        () -> {
                                            try {
                                                br.close();
                                            } catch (IOException e) {
                                                throw new UncheckedIOException(e);
                                            }
                                        });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    /**
     * Converts an array of values to a Record using the provided header and value mapping functions.
     *
     * @param values The array of values from a CSV line.
     * @return A Record object representing the CSV line.
     */
    private MyRecord lineToRecord(String[] values) {
        if (strictRowWidth && values.length != headers.size()) {
            throw new IllegalArgumentException(
                    "CSV row width mismatch. Expected %s columns but got %s".formatted(headers.size(),
                                                                                        values.length));
        }
        Map<String, Object> record = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String strValue = (values.length > i) ?
                              values[i] :
                              "";
            record.put(headerMapper.apply(header),
                       parseValue(header,
                                  strValue));
        }

        return new MyRecord(record);
    }

    /**
     * Parses a string value based on the header, applying value mapping and type conversion if enabled.
     *
     * @param header   The header associated with the value.
     * @param strValue The string value to parse.
     * @return The parsed value.
     */
    private Object parseValue(String header,
                              String strValue) {
        if (strValue.isEmpty()) return strValue;

        String mappedValue = valueMapper.apply(header,
                                               strValue);
        if (mappedValue == null) {
            return null;
        }
        String normalizedValue = removeQuotesIfExist(mappedValue);

        return enableTypeConversion ?
               tryConvert(normalizedValue) :
               normalizedValue;

    }

    private void validateExpectedHeaders() {
        if (expectedHeaders == null) {
            return;
        }
        List<String> normalizedExpected = expectedHeaders.stream()
                                                         .map(CsvStream::removeQuotesIfExist)
                                                         .map(headerMapper)
                                                         .toList();
        if (!normalizedExpected.equals(headers)) {
            throw new IllegalArgumentException(
                    "CSV headers mismatch. Expected %s but got %s".formatted(normalizedExpected,
                                                                             headers));
        }
    }

    /**
     * CsvSpliterator is a custom Spliterator for efficiently streaming CSV lines from a BufferedReader.
     */
    private static class CsvSpliterator extends Spliterators.AbstractSpliterator<String[]> {

        private final BufferedReader reader;
        private final String separator;

        CsvSpliterator(BufferedReader reader,
                       String separator) {
            super(Long.MAX_VALUE,
                  Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL);
            this.reader = reader;
            this.separator = separator;
        }

        @Override
        public boolean tryAdvance(java.util.function.Consumer<? super String[]> action) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    String[] values = line.split(Pattern.quote(separator),
                                                 -1);
                    action.accept(values);
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading CSV file",
                                           e);
            }
        }
    }
}
