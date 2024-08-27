package fun.gen;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * CsvStream provides a convenient way to process CSV files as a Stream of Records.
 * The class allows customization through various functions for header and value mapping,
 * as well as enabling/disabling type conversion.
 */
class CsvStream implements Supplier<Stream<Record>> {

    private final Function<String, String> headerMapper;
    private final BiFunction<String, String, String> valueMapper;

    private final boolean enableTypeConversion;

    private static final Pattern numberPattern = Pattern.compile("^-?(?:0|[1-9]\\d*)(\\.\\d+)?$");
    private final File path;

    private List<String> headers;

    private final String separator;

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
            String separator) {
        this.path = path;
        this.headerMapper = Objects.requireNonNull(headerMapper);
        this.valueMapper = Objects.requireNonNull(valueMapper);
        this.enableTypeConversion = enableTypeConversion;
        this.separator = separator;
    }


    /**
     * Attempts to convert a string value to a suitable data type, including boolean, integer,
     * long, and double. If conversion is not possible, the original string is returned.
     *
     * @param value The string value to convert.
     * @return The converted value or the original string.
     */
    private Object tryConvert(String value) {
        var xs = removeQuotesIfExist(value);

        if (xs.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
            return Boolean.parseBoolean(xs);

        if (numberPattern.matcher(xs).matches()) {
            try {
                return Integer.parseInt(xs);
            } catch (NumberFormatException ignored) {
            }

            try {
                return Long.parseLong(xs);
            } catch (NumberFormatException ignored) {
            }

            try {
                return Double.parseDouble(xs);
            } catch (NumberFormatException ignored) {
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
        return value.length() > 2 && value.startsWith("\"") && value.endsWith("\"")
               ?
               value.substring(1,
                               value.length() - 1) :
               value;
    }

    /**
     * Retrieves a Stream of Records from the CSV file.
     *
     * @return A Stream of Records.
     * @throws UncheckedIOException If an I/O error occurs while reading the CSV file.
     */
    @Override
    public Stream<Record> get() {
        try {
            var br = new BufferedReader(new FileReader(path));
            var headerLine = br.readLine();
            if (headerLine == null) throw new IllegalArgumentException("CSV file has no header line.");
            this.headers = Arrays.stream(headerLine.split(","))
                                 .map(CsvStream::removeQuotesIfExist)
                                 .map(headerMapper)
                                 .toList();
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
    private Record lineToRecord(String[] values) {
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

        return new Record(record);
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

        return enableTypeConversion ?
               tryConvert(mappedValue) :
               mappedValue;

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
                    String[] values = line.split(separator);
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
