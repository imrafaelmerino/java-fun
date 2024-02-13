package fun.gen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A class representing a record with named fields and associated values. This class provides methods for safely retrieving
 * field values of various types from the record.
 */
public final class Record {

    /**
     * The underlying map that holds the record data
     */
    public final Map<String, ?> map;

    /**
     * Constructs a new Record instance with the provided map of field names and values.
     *
     * @param map A map containing field names as keys and their associated values.
     */
    public Record(final Map<String, ?> map) {
        this.map = requireNonNull(map);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional byte array.
     *
     * @param key The name of the field.
     * @return An optional containing the byte array value if present, otherwise an empty optional.
     */
    public Optional<byte[]> getOptBytes(final String key) {
        Object value = map.get(key);
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
        var value = map.get(key);
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
    public Optional<Long> getOptLong(final String key) {
        Object value = map.get(key);
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
        var value = map.get(key);
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
        return getLong(key,
                       () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional string.
     *
     * @param key The name of the field.
     * @return An optional containing the string value if present, otherwise an empty optional.
     */
    public Optional<String> getOptStr(final String key) {
        Object value = map.get(key);
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
    public String getStr(final String key,
                         final Supplier<String> supplier) {
        var value = map.get(key);
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
     * @see #getStr(String, Supplier)
     */
    public String getStr(final String key) {
        return getStr(key,
                      () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional integer.
     *
     * @param key The name of the field.
     * @return An optional containing the integer value if present, otherwise an empty optional.
     */
    public Optional<Integer> getOptInt(final String key) {
        Object value = map.get(key);
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
        var value = map.get(key);
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
        return getInt(key,
                      () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional decimal.
     *
     * @param key The name of the field.
     * @return An optional containing the decimal value if present, otherwise an empty optional.
     */
    public Optional<BigDecimal> getOptDecimal(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigDecimal bd) return Optional.of(bd);
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
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default BigDecimal value if the field is null.
     * @return The BigDecimal value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a BigDecimal,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public BigDecimal getDecimal(final String key,
                                 final Supplier<BigDecimal> supplier) {
        var value = map.get(key);
        if (value == null) return supplier.get();
        if (value instanceof BigDecimal d) return d;
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
    public <O> Optional<List<O>> getOptList(final String key) {
        Object value = map.get(key);
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
        Object value = map.get(key);
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
    public <O> Optional<Set<O>> getOptSet(final String key) {
        Object value = map.get(key);
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
        Object value = map.get(key);
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
    public <K, V> Optional<Map<K, V>> getOptMap(final String key) {
        Object value = map.get(key);
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
        Object value = map.get(key);
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
    public Optional<Instant> getOptInstant(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Instant i) return Optional.of(i);
        throw new RecordTypeNotExpected(Instant.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional instant.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default Instant value if the field is null.
     * @return The Instant value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not an Instant,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public Instant getInstant(final String key,
                              final Supplier<Instant> supplier) {
        var value = map.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Instant d) return d;
        throw new RecordTypeNotExpected(Instant.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional instant.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The Instant value if present, or null if the field is not found.
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
    public Optional<Double> getOptDouble(final String key) {
        Object value = map.get(key);
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
     * Retrieves the value associated with the specified field name as an optional double precision floating-point number.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default Double value if the field is null.
     * @return The Double value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a Double,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public double getDouble(final String key,
                            final Supplier<Double> supplier) {
        var value = map.get(key);
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
     * Retrieves the value associated with the specified field name as an optional double precision floating-point number.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The Double value if present, or null if the field is not found.
     * @see #getDouble(String, Supplier)
     */
    public Double getDouble(final String key) {
        return getDouble(key,
                         () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional boolean.
     *
     * @param key The name of the field.
     * @return An optional containing the boolean value if present, otherwise an empty optional.
     */
    public Optional<Boolean> getOptBool(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Boolean b) return Optional.of(b);
        throw new RecordTypeNotExpected(Boolean.class.getName(),
                                        value.getClass(),
                                        key
        );

    }

    /**
     * Retrieves the value associated with the specified field name as an optional boolean.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default Boolean value if the field is null.
     * @return The Boolean value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a Boolean,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public boolean getBool(final String key,
                           final Supplier<Boolean> supplier) {
        var value = map.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Boolean bool) return bool;
        throw new RecordTypeNotExpected(Boolean.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional boolean.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The Boolean value if present, or null if the field is not found.
     * @see #getBool(String, Supplier)
     */
    public Boolean getBool(final String key) {
        return getBool(key,
                       () -> null);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional character.
     *
     * @param key The name of the field.
     * @return An optional containing the character value if present, otherwise an empty optional.
     */
    public Optional<Character> getOptChar(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Character c) return Optional.of(c);
        throw new RecordTypeNotExpected(Character.class.getName(),
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional character.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key      The name of the field.
     * @param supplier A supplier to provide a default Character value if the field is null.
     * @return The Character value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a Character,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public char getChar(final String key,
                        final Supplier<Character> supplier) {
        Object value = map.get(key);
        if (value == null) return supplier.get();
        if (value instanceof Character c) return c;
        throw new RecordTypeNotExpected(Character.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Retrieves the value associated with the specified field name as an optional character.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The Character value if present, or null if the field is not found.
     * @see #getChar(String, Supplier)
     */
    public Character getChar(final String key) {
        return getChar(key,
                       () -> null);
    }


    /**
     * Retrieves the value associated with the specified field name as an optional big integer.
     *
     * @param key The name of the field.
     * @return An optional containing the big integer value if present, otherwise an empty optional.
     */
    public Optional<BigInteger> getOptBigInt(final String key) {
        Object value = map.get(key);
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
     * Retrieves the value associated with the specified field name as an optional BigInteger.
     * If the field does not exist, it returns the result from the provided supplier.
     *
     * @param key The name of the field.
     * @param bi  A supplier to provide a default BigInteger value if the field is null.
     * @return The BigInteger value if present, or the result from the supplier if the field is null.
     * @throws RecordTypeNotExpected If the field exists but its value is not a BigInteger,
     *                               this exception is thrown, indicating an unexpected type.
     */
    public BigInteger getBigInt(final String key,
                                final Supplier<BigInteger> bi) {
        Object value = map.get(key);
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
     * Retrieves the value associated with the specified field name as an optional BigInteger.
     * If the field does not exist, it returns a default value of null.
     *
     * @param key The name of the field.
     * @return The BigInteger value if present, or null if the field is not found.
     * @see #getBigInt(String, Supplier)
     */
    public BigInteger getBigInt(final String key) {
        return getBigInt(key,
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
        var record = (Record) o;
        return Objects.equals(map,
                              record.map);
    }

    /**
     * Computes a hash code for this Record.
     *
     * @return A hash code for this Record.
     */
    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    /**
     * Returns a string representation of this Record, including its map of field names and values.
     *
     * @return A string representation of this Record.
     */
    @Override
    public String toString() {
        return map.toString();
    }
}
