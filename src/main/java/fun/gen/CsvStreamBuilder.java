package fun.gen;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Builder for creating CSV streams as {@link Stream} of {@link MyRecord}.
 * <p>
 * The first line is treated as the header; each subsequent line becomes one record.
 * This builder is intentionally composable so callers can choose strictness and normalization rules.
 * <p>
 * Processing order for each cell:
 * <ol>
 *   <li>Optional raw-trim ({@link #withTrimValues(boolean)}).</li>
 *   <li>Value mapping ({@link #withValueMapper(BiFunction)}).</li>
 *   <li>Quote normalization.</li>
 *   <li>Null mapping ({@link #withNullTokens(Set)} and/or {@link #withNullTokenMatcher(Predicate)}).</li>
 *   <li>Optional primitive-friendly type conversion (unless {@link #withoutTypeConversion()} is used).</li>
 * </ol>
 * <p>
 * Error handling for malformed rows can be configured with
 * {@link #withRowErrorHandler(BiFunction)}, {@link #withSkipMalformedRows()}, and
 * {@link #withErrorCollector(BiConsumer)}.
 * <p>
 * Typical strict ingestion setup:
 * <pre>{@code
 * try (var rows = CsvStreamBuilder.of(file, ",")
 *     .withExpectedHeaders("id", "name", "age")
 *     .withStrictRowWidth()
 *     .withNullTokens("null", "N/A", "")
 *     .withNullTokenMatcher(token -> token.trim().equalsIgnoreCase("none"))
 *     .withErrorCollector((row, ex) -> log.warn("CSV row {} ignored: {}", row, ex.getMessage()))
 *     .withSkipMalformedRows()
 *     .get()) {
 *
 *   var list = rows.toList();
 * }
 * }</pre>
 */
public final class CsvStreamBuilder implements Supplier<Stream<MyRecord>> {

    private final File path;
    private final String separator;
    private Function<String, String> headerMapper = String::trim;
    private BiFunction<String, String, String> valueMapper = (header, val) -> val;
    private boolean enableTypeConversion = true;
    private boolean trimValues = true;
    private boolean trimValuesConfigured;
    private List<String> expectedHeaders;
    private boolean strictRowWidth;
    private Set<String> nullTokens;
    private Predicate<String> nullTokenMatcher;
    private BiFunction<Long, RuntimeException, CsvRowErrorAction> rowErrorHandler;
    private BiConsumer<Long, RuntimeException> errorCollector;

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
     * @throws NullPointerException     If {@code path} or {@code separator} is null.
     */
    public static CsvStreamBuilder of(File path,
                                      String separator) {
        return new CsvStreamBuilder(path,
                                    separator);
    }

    /**
     * Sets the header mapping function used when reading header names.
     * <p>
     * The mapped headers are used for:
     * <ul>
     *   <li>Record key names.</li>
     *   <li>Expected-header validation configured with {@link #withExpectedHeaders(List)}.</li>
     * </ul>
     *
     * @param headerMapper The header mapping function.
     * @return The CsvStreamBuilder instance for method chaining.
     * @throws NullPointerException If {@code headerMapper} is null.
     */
    public CsvStreamBuilder withHeaderMapper(Function<String, String> headerMapper) {
        this.headerMapper = Objects.requireNonNull(headerMapper);
        return this;
    }

    /**
     * Sets the value mapping function to customize how values are processed based on headers.
     * <p>
     * Mapper input receives either trimmed or raw cell text depending on {@link #withTrimValues(boolean)}.
     * If trim policy has not been explicitly configured yet, calling this method preserves legacy behavior
     * by disabling trim (raw input passed to the custom mapper).
     *
     * @param valueMapper The value mapping function.
     * @return The CsvStreamBuilder instance for method chaining.
     * @throws NullPointerException If {@code valueMapper} is null.
     */
    public CsvStreamBuilder withValueMapper(BiFunction<String, String, String> valueMapper) {
        this.valueMapper = Objects.requireNonNull(valueMapper);
        // Preserve legacy behavior for custom mappers unless caller explicitly configured trimming.
        if (!trimValuesConfigured) {
            this.trimValues = false;
        }
        return this;
    }

    /**
     * Controls whether raw CSV cell values are trimmed before value mapping.
     * Default is {@code true} for the default mapper.
     * <p>
     * If a custom value mapper was configured earlier, this method can be used to opt back into trimming.
     *
     * @param trimValues true to trim values before applying {@link #withValueMapper(BiFunction)}.
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withTrimValues(boolean trimValues) {
        this.trimValues = trimValues;
        this.trimValuesConfigured = true;
        return this;
    }

    /**
     * Disables automatic type conversion of values.
     * <p>
     * When disabled, values are emitted as strings after mapping, quote normalization, and null rules.
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
     * @throws NullPointerException If {@code expectedHeaders} is null or contains null entries.
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
     * @throws NullPointerException If {@code expectedHeaders} is null or contains null entries.
     */
    public CsvStreamBuilder withExpectedHeaders(String... expectedHeaders) {
        return withExpectedHeaders(List.of(Objects.requireNonNull(expectedHeaders)));
    }

    /**
     * Enables strict row width validation: every data row must have the same number
     * of columns as the header. Extra and missing columns are considered malformed rows.
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
     * <p>
     * Matching is exact and case-sensitive. Use {@link #withNullTokenMatcher(Predicate)} for
     * custom matching strategies.
     *
     * @param nullTokens tokens to map to null.
     * @return The CsvStreamBuilder instance for method chaining.
     * @throws NullPointerException If {@code nullTokens} is null or contains null entries.
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
     * @throws NullPointerException If {@code nullTokens} is null or contains null entries.
     */
    public CsvStreamBuilder withNullTokens(String... nullTokens) {
        return withNullTokens(Set.of(Objects.requireNonNull(nullTokens)));
    }

    /**
     * Sets a matcher used to decide whether a normalized value should be mapped to {@code null}.
     * The matcher is evaluated after value mapping and quote normalization.
     * <p>
     * If both this matcher and {@link #withNullTokens(Set)} are configured, a value becomes null
     * when either rule matches.
     *
     * @param nullTokenMatcher predicate to decide if a token should become null.
     * @return The CsvStreamBuilder instance for method chaining.
     * @throws NullPointerException If {@code nullTokenMatcher} is null.
     */
    public CsvStreamBuilder withNullTokenMatcher(Predicate<String> nullTokenMatcher) {
        this.nullTokenMatcher = Objects.requireNonNull(nullTokenMatcher);
        return this;
    }

    /**
     * Sets the handler used when a data row fails to parse.
     * The handler receives the 1-based CSV row number (including header row) and the thrown exception.
     * <p>
     * Return {@link CsvRowErrorAction#THROW} to propagate the exception, or
     * {@link CsvRowErrorAction#SKIP} to discard that row and continue.
     *
     * @param rowErrorHandler function deciding whether to throw or skip the malformed row.
     * @return The CsvStreamBuilder instance for method chaining.
     * @throws NullPointerException If {@code rowErrorHandler} is null.
     */
    public CsvStreamBuilder withRowErrorHandler(BiFunction<Long, RuntimeException, CsvRowErrorAction> rowErrorHandler) {
        this.rowErrorHandler = Objects.requireNonNull(rowErrorHandler);
        return this;
    }

    /**
     * Convenience method that skips malformed data rows.
     * <p>
     * Equivalent to:
     * <pre>{@code
     * withRowErrorHandler((row, ex) -> CsvRowErrorAction.SKIP)
     * }</pre>
     *
     * @return The CsvStreamBuilder instance for method chaining.
     */
    public CsvStreamBuilder withSkipMalformedRows() {
        this.rowErrorHandler = (rowNumber, ex) -> CsvRowErrorAction.SKIP;
        return this;
    }

    /**
     * Registers a collector called every time a data row fails to parse.
     * The collector receives the 1-based CSV row number (including header row) and the thrown exception.
     * <p>
     * Collector invocation is independent from row error strategy: it is called before deciding
     * whether to throw or skip.
     *
     * @param errorCollector error collector callback.
     * @return The CsvStreamBuilder instance for method chaining.
     * @throws NullPointerException If {@code errorCollector} is null.
     */
    public CsvStreamBuilder withErrorCollector(BiConsumer<Long, RuntimeException> errorCollector) {
        this.errorCollector = Objects.requireNonNull(errorCollector);
        return this;
    }

    /**
     * Creates the configured CSV stream.
     * <p>
     * The caller is responsible for closing the returned stream.
     *
     * @return A lazy stream of {@link MyRecord} values.
     * @throws IllegalArgumentException If header validation fails or malformed rows are configured to throw.
     */
    @Override
    public Stream<MyRecord> get() {
        return new CsvStream(path,
                             headerMapper,
                             valueMapper,
                             enableTypeConversion,
                             trimValues,
                             separator,
                             expectedHeaders,
                             strictRowWidth,
                             nullTokens,
                             nullTokenMatcher,
                             rowErrorHandler,
                             errorCollector)
                .get();
    }

}
