package fun.gen;

import java.io.File;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * CsvStreamBuilder is a builder class for creating instances of CsvStream with customizable options.
 * The builder allows users to configure header and value mapping, type conversion, separator, and default values.
 * The resulting CsvStream processes CSV files where the header row defines the names of the records,
 * and each subsequent row represents a record with column values.
 * Type conversions from string include converting to boolean (e.g., "true" to true),
 * integers (e.g., "123" to 123), longs, and doubles. The header row is mandatory in the CSV file.
 */
public final class CsvStreamBuilder implements Supplier<Stream<MyRecord>> {

    private final File path;
    private final String separator;
    private Function<String, String> headerMapper = String::trim;
    private BiFunction<String, String, String> valueMapper = (header, val) -> val.trim();
    private boolean enableTypeConversion = true;

    private CsvStreamBuilder(File path,
                             String separator) {
        this.path = Objects.requireNonNull(path);
        if (!path.exists()) throw new IllegalArgumentException("File %s not found".formatted(path.getAbsolutePath()));
        this.separator = Objects.requireNonNull(separator);
        if (separator.isBlank()) throw new IllegalArgumentException("separator is empty");
    }

    /**
     * Creates a new CsvStreamBuilder instance with the given CSV file path and column separator.
     *
     * @param path      The path to the CSV file.
     * @param separator The CSV column separator.
     * @return A CsvStreamBuilder instance.
     */
    public static CsvStreamBuilder of(File path,
                                      String separator) {
        return new CsvStreamBuilder(path,
                                    separator);
    }

    /**
     * Sets the header mapping function to customize how headers are processed.
     *
     * @param headerMapper The header mapping function.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withHeaderMapper(Function<String, String> headerMapper) {
        this.headerMapper = Objects.requireNonNull(headerMapper);
        return this;
    }

    /**
     * Sets the value mapping function to customize how values are processed based on headers.
     *
     * @param valueMapper The value mapping function.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withValueMapper(BiFunction<String, String, String> valueMapper) {
        this.valueMapper = Objects.requireNonNull(valueMapper);
        return this;
    }

    /**
     * Disables automatic type conversion of values.
     *
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withoutTypeConversion() {
        this.enableTypeConversion = false;
        return this;
    }

    /**
     * Creates a Supplier of Stream of Records based on the configured options.
     *
     * @return A Supplier of Stream of Records with the specified configurations.
     * @throws IllegalArgumentException If a required field is not set.
     */
    @Override
    public Stream<MyRecord> get() {
        return new CsvStream(path,
                             headerMapper,
                             valueMapper,
                             enableTypeConversion,
                             separator)
                .get();
    }

}