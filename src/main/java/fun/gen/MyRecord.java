package fun.gen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A class representing an immutable record with named fields and associated values.
 * This class provides methods for safely retrieving field values of various types.
 */
public final class MyRecord {

    /**
     * The underlying immutable map that holds the record data.
     */
    private final Map<String, ?> values;

    /**
     * Constructs a new record instance with the provided map of field names and values.
     * The map is defensively copied and wrapped as unmodifiable.
     *
     * @param map A map containing field names as keys and their associated values.
     */
    public MyRecord(final Map<String, ?> map) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(requireNonNull(map)));
    }

    /**
     * Returns an immutable view of the underlying key-value data.
     *
     * @return immutable record values map
     */
    public Map<String, ?> asMap() {
        return values;
    }

    /**
     * Checks whether this record contains the given key.
     *
     * @param key field name
     * @return {@code true} if the key exists in the record map, {@code false} otherwise
     */
    public boolean containsKey(final String key) {
        return values.containsKey(key);
    }

    /**
     * Returns the number of keys stored in this record.
     *
     * @return record size
     */
    public int size() {
        return values.size();
    }

    /**
     * Returns whether this record has no keys.
     *
     * @return {@code true} when the record has no keys
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * Retrieves the value associated with the specified field name as an optional byte array.
     *
     * @param key The name of the field.
     * @return An optional containing the byte array value if present, otherwise an empty optional.
     */
    public Optional<byte[]> getOptionalBytes(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof byte[] b) return Optional.of(b);
        throw new RecordTypeNotExpected("byte[]",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a byte array.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default byte array if the field is null.
     * @return The byte array value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a byte array,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public byte[] getBytes(final String key,
                           final Supplier<byte[]> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof byte[] d) return d;
        throw new RecordTypeNotExpected("byte[]",
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a byte array.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The byte array value if present, or null if the field is not found.
     * @see #getBytes(String, Supplier)
     */
    public byte[] getBytes(final String key) {
        return getBytes(key,
                        () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional long integer.
     *
     * @param key The name of the field.
     * @return An optional containing the long value if present, otherwise an empty optional.
     */
    public Optional<Long> getOptionalLong(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Long n) return Optional.of(n);
        if (value instanceof Integer n) return Optional.of(Long.valueOf(n));
        if (value instanceof Short n) return Optional.of(Long.valueOf(n));
        if (value instanceof Byte n) return Optional.of(Long.valueOf(n));
        throw new RecordTypeNotExpected(Long.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a long integer.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default-long value if the field is null.
     * @return The long value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a long integer,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public long getLong(final String key,
                        final Supplier<Long> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Long n) return n;
        if (value instanceof Integer n) return Long.valueOf(n);
        if (value instanceof Short n) return Long.valueOf(n);
        if (value instanceof Byte n) return Long.valueOf(n);
        throw new RecordTypeNotExpected(Long.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a long integer.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The long value if present, or null if the field is not found.
     * @see #getLong(String, Supplier)
     */
    public Long getLong(final String key) {
        return getOptionalLong(key).orElse(null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional string.
     *
     * @param key The name of the field.
     * @return An optional containing the string value if present, otherwise an empty optional.
     */
    public Optional<String> getOptionalString(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof String) return Optional.of(((String) value));
        throw new RecordTypeNotExpected(String.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a string.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default string value if the field is null.
     * @return The string value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a string,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public String getString(final String key,
                            final Supplier<String> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof String d) return d;
        throw new RecordTypeNotExpected(String.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a string.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The string value if present, or null if the field is not found.
     * @see #getString(String, Supplier)
     */
    public String getString(final String key) {
        return getString(key,
                      () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional integer.
     *
     * @param key The name of the field.
     * @return An optional containing the integer value if present, otherwise an empty optional.
     */
    public Optional<Integer> getOptionalInt(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Integer n) return Optional.of(n);
        if (value instanceof Short n) return Optional.of(Integer.valueOf(n));
        if (value instanceof Byte n) return Optional.of(Integer.valueOf(n));
        throw new RecordTypeNotExpected(Integer.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an integer.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default integer value if the field is null.
     * @return The integer value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not an integer,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public int getInt(final String key,
                      final Supplier<Integer> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Integer d) return d;
        if (value instanceof Short n) return Integer.valueOf(n);
        if (value instanceof Byte n) return Integer.valueOf(n);
        throw new RecordTypeNotExpected(Integer.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as an integer.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The integer value if present, or null if the field is not found.
     * @see #getInt(String, Supplier)
     */
    public Integer getInt(final String key) {
        return getOptionalInt(key).orElse(null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional decimal.
     * <p>
     * Accepted source types:
     * {@link BigDecimal}, {@link BigInteger}, {@link Long}, {@link Integer}, {@link Short},
     * {@link Byte}, {@link Double}, and {@link Float}.
     * <p>
     * Conversion semantics:
     * - {@code BigDecimal}, {@code BigInteger}, and integral wrappers are converted exactly.
     * - {@code Double}/{@code Float} are converted with {@link BigDecimal#valueOf(double)}.
     * Their binary floating-point approximation is preserved, so decimal artifacts may appear.
     * <p>
     * This method does not perform extra rounding or normalization.
     *
     * @param key The name of the field.
     * @return An optional containing the decimal value if present, otherwise an empty optional.
     */
    public Optional<BigDecimal> getOptionalDecimal(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigDecimal bd) return Optional.of(bd);
        if (value instanceof BigInteger n) return Optional.of(new BigDecimal(n));
        if (value instanceof Long n) return Optional.of(BigDecimal.valueOf(n));
        if (value instanceof Integer n) return Optional.of(BigDecimal.valueOf(n));
        if (value instanceof Short n) return Optional.of(BigDecimal.valueOf(n));
        if (value instanceof Byte n) return Optional.of(BigDecimal.valueOf(n));
        if (value instanceof Double n) return Optional.of(BigDecimal.valueOf(n));
        if (value instanceof Float n) return Optional.of(BigDecimal.valueOf(n));
        throw new RecordTypeNotExpected(BigDecimal.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a decimal.
     * If the field does not exist, it returns the result from the provided supplier.
     * <p>
     * Accepted source types and conversion semantics are the same as in
     * {@link #getOptionalDecimal(String)}.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default BigDecimal value if the field is null.
     * @return The BigDecimal value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not one of the accepted numeric types.
     */
    public BigDecimal getDecimal(final String key,
                                 final Supplier<BigDecimal> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof BigDecimal d) return d;
        if (value instanceof BigInteger n) return new BigDecimal(n);
        if (value instanceof Long n) return BigDecimal.valueOf(n);
        if (value instanceof Integer n) return BigDecimal.valueOf(n);
        if (value instanceof Short n) return BigDecimal.valueOf(n);
        if (value instanceof Byte n) return BigDecimal.valueOf(n);
        if (value instanceof Double n) return BigDecimal.valueOf(n);
        if (value instanceof Float n) return BigDecimal.valueOf(n);
        throw new RecordTypeNotExpected(BigDecimal.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a decimal.
     * If the field does not exist, it returns a default value of null.
     * <p>
     * Accepted source types and conversion semantics are the same as in
     * {@link #getOptionalDecimal(String)}.
     *
     * @param key The name of the field.
     * @return The BigDecimal value if present, or null if the field is not found.
     * @see #getDecimal(String, Supplier)
     */
    public BigDecimal getDecimal(final String key) {
        return getDecimal(key,
                          () -> null);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional list of a generic type.
     * <p>
     * This method is used to get the value of a JSON field as a list of elements of a generic type. If the field
     * exists and its value is a list, it returns an optional containing that list; otherwise, it returns an empty optional.
     *
     * @param key The name of the field.
     * @param <O> The generic type of the elements in the list.
     * @return An optional containing the list value if present, otherwise an empty optional.
     * @throws RecordTypeNotExpected If the field exists but its value is not a list, this exception is thrown, indicating
     *                               that the expected type was a list, but the actual type was different.
     */

    @SuppressWarnings("unchecked")
    public <O> Optional<List<O>> getOptionalList(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof List<?> list) return Optional.of((List<O>) list);

        throw new RecordTypeNotExpected(List.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a list of a generic type.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default List value if the field is null.
     * @param <O>      The generic type of the elements in the list.
     * @return The List value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a List,
     *                               this exception is thrown, indicating an unexpected type.
     */
    @SuppressWarnings("unchecked")
    public <O> List<O> getList(final String key,
                               final Supplier<List<O>> supplier) {
        Object value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof List<?>) return (List<O>) value;
        throw new RecordTypeNotExpected(List.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a list of a generic type.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @param <O> The generic type of the elements in the list.
     * @return The List value if present, or null if the field is not found.
     * @see #getList(String, Supplier)
     */
    public <O> List<O> getList(final String key) {
        return getList(key,
                       () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional set of a generic type.
     * <p>
     * This method is used to get the value of a JSON field as a set of elements of a generic type. If the field
     * exists and its value is a set, it returns an optional containing that set; otherwise, it returns an empty optional.
     *
     * @param key The name of the field.
     * @param <O> The generic type of the elements in the set.
     * @return An optional containing the set value if present, otherwise an empty optional.
     * @throws RecordTypeNotExpected If the field exists but its value is not a set, this exception is thrown, indicating
     *                               that the expected type was a set, but the actual type was different.
     */
    @SuppressWarnings("unchecked")
    public <O> Optional<Set<O>> getOptionalSet(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Set<?> set) return Optional.of((Set<O>) set);

        throw new RecordTypeNotExpected(Set.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a set of a generic type.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default Set value if the field is null.
     * @param <O>      The generic type of the elements in the set.
     * @return The Set value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a Set,
     *                               this exception is thrown, indicating an unexpected type.
     */
    @SuppressWarnings("unchecked")
    public <O> Set<O> getSet(final String key,
                             final Supplier<Set<O>> supplier) {
        Object value = values.get(key);
        if (value == null) return supplier.get();

        if (value instanceof Set<?>) return (Set<O>) value;

        throw new RecordTypeNotExpected(Set.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a set of a generic type.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @param <O> The generic type of the elements in the set.
     * @return The Set value if present, or null if the field is not found.
     * @see #getSet(String, Supplier)
     */
    public <O> Set<O> getSet(final String key) {
        return getSet(key,
                      () -> null);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional map of generic types for keys and values.
     * <p>
     * This method is used to get the value of a JSON field as a map with generic types for keys and values. If the field
     * exists and its value is a map, it returns an optional containing that map; otherwise, it returns an empty optional.
     *
     * @param key The name of the field.
     * @param <K> The generic type of map keys.
     * @param <V> The generic type of map values.
     * @return An optional containing the map value if present, otherwise an empty optional.
     * @throws RecordTypeNotExpected If the field exists but its value is not a map, this exception is thrown, indicating
     *                               that the expected type was a map, but the actual type was different.
     */
    @SuppressWarnings("unchecked")
    public <K, V> Optional<Map<K, V>> getOptionalMap(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Map) return Optional.of((Map<K, V>) value);
        throw new RecordTypeNotExpected(Map.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a map with generic types for keys and values.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default Map value if the field is null.
     * @param <K>      The generic type of map keys.
     * @param <V>      The generic type of map values.
     * @return The Map value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a Map,
     *                               this exception is thrown, indicating an unexpected type.
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(final String key,
                                   Supplier<Map<K, V>> supplier) {
        Object value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Map<?, ?> o) return (Map<K, V>) o;
        throw new RecordTypeNotExpected(Map.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a map with generic types for keys and values.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @param <K> The generic type of map keys.
     * @param <V> The generic type of map values.
     * @return The Map value if present, or null if the field is not found.
     * @see #getMap(String, Supplier)
     */
    public <K, V> Map<K, V> getMap(final String key) {
        return getMap(key,
                      () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional instant.
     *
     * @param key The name of the field.
     * @return An optional containing the instant value if present, otherwise an empty optional.
     */
    public Optional<Instant> getOptionalInstant(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Instant i) return Optional.of(i);
        throw new RecordTypeNotExpected(Instant.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an {@link Instant}.
     * If the field is absent or null, the provided supplier is used to provide a fallback value.
     *
     * @param key      The name of the field.
     * @param supplier A supplier used when the field is absent or null.
     * @return The stored instant value, or the supplier value when the field is absent or null.
     * @throws RecordTypeNotExpected If the field exists but cannot be interpreted as an {@link Instant}.
     */
    public Instant getInstant(final String key,
                              final Supplier<Instant> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Instant d) return d;
        throw new RecordTypeNotExpected(Instant.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as an {@link Instant}.
     *
     * @param key The name of the field.
     * @return The instant value if present; otherwise {@code null}.
     * @see #getInstant(String, Supplier)
     */
    public Instant getInstant(final String key) {
        return getInstant(key,
                          () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional double precision floating-point number.
     *
     * @param key The name of the field.
     * @return An optional containing the double value if present, otherwise an empty optional.
     */
    public Optional<Double> getOptionalDouble(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Double d) return Optional.of(d);
        if (value instanceof Long n) return Optional.of(Double.valueOf(n));
        if (value instanceof Integer n) return Optional.of(Double.valueOf(n));
        if (value instanceof Short n) return Optional.of(Double.valueOf(n));
        if (value instanceof Byte n) return Optional.of(Double.valueOf(n));
        if (value instanceof Float n) return Optional.of(Double.valueOf(n));
        throw new RecordTypeNotExpected(Double.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a {@code double}.
     * Numeric widening is supported from {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, and {@link Float}.
     * If the field is absent or null, the provided supplier is used.
     *
     * @param key      The name of the field.
     * @param supplier A supplier used when the field is absent or null.
     * @return The converted double value, or the supplier value when absent/null.
     * @throws RecordTypeNotExpected If the field exists but is not compatible with a double value.
     */
    public double getDouble(final String key,
                            final Supplier<Double> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Double d) return d;
        if (value instanceof Long n) return Double.valueOf(n);
        if (value instanceof Integer n) return Double.valueOf(n);
        if (value instanceof Short n) return Double.valueOf(n);
        if (value instanceof Byte n) return Double.valueOf(n);
        if (value instanceof Float n) return Double.valueOf(n);
        throw new RecordTypeNotExpected(Double.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a boxed {@link Double}.
     *
     * @param key The name of the field.
     * @return The value if present; otherwise {@code null}.
     * @see #getDouble(String, Supplier)
     */
    public Double getDouble(final String key) {
        return getOptionalDouble(key).orElse(null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional boolean.
     *
     * @param key The name of the field.
     * @return An optional containing the boolean value if present, otherwise an empty optional.
     */
    public Optional<Boolean> getOptionalBoolean(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Boolean b) return Optional.of(b);
        throw new RecordTypeNotExpected(Boolean.class.getName(),
                                        value.getClass(),
                                        key
        );

    }

    /**
     * Retrieves the value associated with the specified field name as a {@code boolean}.
     * If the field is absent or null, the provided supplier is used.
     *
     * @param key      The name of the field.
     * @param supplier A supplier used when the field is absent or null.
     * @return The stored boolean value, or the supplier value when absent/null.
     * @throws RecordTypeNotExpected If the field exists but is not a {@link Boolean}.
     */
    public boolean getBoolean(final String key,
                              final Supplier<Boolean> supplier) {
        var value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Boolean bool) return bool;
        throw new RecordTypeNotExpected(Boolean.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a boxed {@link Boolean}.
     *
     * @param key The name of the field.
     * @return The value if present; otherwise {@code null}.
     * @see #getBoolean(String, Supplier)
     */
    public Boolean getBoolean(final String key) {
        return getOptionalBoolean(key).orElse(null);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional character.
     *
     * @param key The name of the field.
     * @return An optional containing the character value if present, otherwise an empty optional.
     */
    public Optional<Character> getOptionalChar(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Character c) return Optional.of(c);
        throw new RecordTypeNotExpected(Character.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a {@code char}.
     * If the field is absent or null, the provided supplier is used.
     *
     * @param key      The name of the field.
     * @param supplier A supplier used when the field is absent or null.
     * @return The stored character value, or the supplier value when absent/null.
     * @throws RecordTypeNotExpected If the field exists but is not a {@link Character}.
     */
    public char getChar(final String key,
                        final Supplier<Character> supplier) {
        Object value = values.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Character c) return c;
        throw new RecordTypeNotExpected(Character.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a boxed {@link Character}.
     *
     * @param key The name of the field.
     * @return The value if present; otherwise {@code null}.
     * @see #getChar(String, Supplier)
     */
    public Character getChar(final String key) {
        return getOptionalChar(key).orElse(null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional big integer.
     *
     * @param key The name of the field.
     * @return An optional containing the big integer value if present, otherwise an empty optional.
     */
    public Optional<BigInteger> getOptionalBigInteger(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigInteger bi) return Optional.of(bi);
        if (value instanceof Long n) return Optional.of(BigInteger.valueOf(n));
        if (value instanceof Integer n) return Optional.of(BigInteger.valueOf(n));
        if (value instanceof Short n) return Optional.of(BigInteger.valueOf(n));
        if (value instanceof Byte n) return Optional.of(BigInteger.valueOf(n));
        throw new RecordTypeNotExpected(BigInteger.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as a {@link BigInteger}.
     * Numeric widening is supported from {@link Byte}, {@link Short}, {@link Integer}, and {@link Long}.
     * If the field is absent or null, the provided supplier is used.
     *
     * @param key The name of the field.
     * @param bi  A supplier used when the field is absent or null.
     * @return The converted big integer value, or the supplier value when absent/null.
     * @throws RecordTypeNotExpected If the field exists but cannot be interpreted as a big integer.
     */
    public BigInteger getBigInteger(final String key,
                                final Supplier<BigInteger> bi) {
        Object value = values.get(key);
        if (value == null) return bi.get();
        if (value instanceof BigInteger b) return b;
        if (value instanceof Long n) return BigInteger.valueOf(n);
        if (value instanceof Integer n) return BigInteger.valueOf(n);
        if (value instanceof Short n) return BigInteger.valueOf(n);
        if (value instanceof Byte n) return BigInteger.valueOf(n);
        throw new RecordTypeNotExpected(BigInteger.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as a {@link BigInteger}.
     *
     * @param key The name of the field.
     * @return The value if present; otherwise {@code null}.
     * @see #getBigInteger(String, Supplier)
     */
    public BigInteger getBigInteger(final String key) {
        return getBigInteger(key,
                         () -> null);
    }


    /**
     * Checks if this Record is equal to another object.
     *
     * @param o The object to compare with this Record.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var record = (MyRecord) o;
        return Objects.equals(values,
                              record.values);
    }

    /**
     * Computes a hash code for this Record.
     *
     * @return A hash code for this Record.
     */
    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    /**
     * Returns a string representation of this Record, including its map of field names and values.
     *
     * @return A string representation of this Record.
     */
    @Override
    public String toString() {
        return values.toString();
    }
}
