package fun.gen;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Builder for creating CSV streams as {@link Stream} of {@link MyRecord}.
 * <p>
 * Supports customizable header/value mapping, optional type conversion, and custom separators.
 * The first line is treated as the header; each subsequent line becomes one record.
 */
public final class CsvStreamBuilder implements Supplier<Stream<MyRecord>> {

    private final File path;
    private final String separator;
    private Function<String, String> headerMapper = String::trim;
    private BiFunction<String, String, String> valueMapper = (header, val) -> val.trim();
    private boolean enableTypeConversion = true;
    private List<String> expectedHeaders;
    private boolean strictRowWidth;
    private Set<String> nullTokens;

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
     * @throws IllegalArgumentException If the file does not exist or the separator is blank.
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
     * Sets the expected headers. Parsed CSV headers are normalized with the current header mapper
     * and must match exactly in content and order.
     *
     * @param expectedHeaders expected header names in order.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withExpectedHeaders(List<String> expectedHeaders) {
        Objects.requireNonNull(expectedHeaders);
        this.expectedHeaders = List.copyOf(expectedHeaders);
        if (this.expectedHeaders.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("expectedHeaders contains null values");
        }
        return this;
    }

    /**
     * Varargs overload for expected headers.
     *
     * @param expectedHeaders expected header names in order.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withExpectedHeaders(String... expectedHeaders) {
        return withExpectedHeaders(List.of(Objects.requireNonNull(expectedHeaders)));
    }

    /**
     * Enables strict row width validation: every data row must have the same number
     * of columns as the header.
     *
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withStrictRowWidth() {
        this.strictRowWidth = true;
        return this;
    }

    /**
     * Configures strict row width validation.
     *
     * @param strictRowWidth whether to enforce exact row width equality with headers.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withStrictRowWidth(boolean strictRowWidth) {
        this.strictRowWidth = strictRowWidth;
        return this;
    }

    /**
     * Sets the string tokens that should be interpreted as {@code null}
     * after value mapping and quote normalization.
     *
     * @param nullTokens tokens to map to null.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withNullTokens(Set<String> nullTokens) {
        Objects.requireNonNull(nullTokens);
        this.nullTokens = Set.copyOf(nullTokens);
        if (this.nullTokens.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("nullTokens contains null values");
        }
        return this;
    }

    /**
     * Varargs overload for null tokens.
     *
     * @param nullTokens tokens to map to null.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withNullTokens(String... nullTokens) {
        return withNullTokens(Set.of(Objects.requireNonNull(nullTokens)));
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
                             separator,
                             expectedHeaders,
                             strictRowWidth,
                             nullTokens)
                .get();
    }

}
