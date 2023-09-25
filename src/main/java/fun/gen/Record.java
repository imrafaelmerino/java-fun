package fun.gen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

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
    public Optional<byte[]> getBytes(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof byte[]) return Optional.of(((byte[]) value));
        throw new RecordTypeNotExpected("byte[]",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional long integer.
     *
     * @param key The name of the field.
     * @return An optional containing the long value if present, otherwise an empty optional.
     */
    public Optional<Long> getLong(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Long) return Optional.of(((Long) value));
        throw new RecordTypeNotExpected("Long",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional string.
     *
     * @param key The name of the field.
     * @return An optional containing the string value if present, otherwise an empty optional.
     */
    public Optional<String> getStr(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof String) return Optional.of(((String) value));
        throw new RecordTypeNotExpected("String",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional integer.
     *
     * @param key The name of the field.
     * @return An optional containing the integer value if present, otherwise an empty optional.
     */
    public Optional<Integer> getInt(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Integer) return Optional.of(((Integer) value));
        throw new RecordTypeNotExpected("Integer",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional decimal.
     *
     * @param key The name of the field.
     * @return An optional containing the decimal value if present, otherwise an empty optional.
     */
    public Optional<BigDecimal> getDecimal(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigDecimal) return Optional.of(((BigDecimal) value));
        throw new RecordTypeNotExpected("BigDecimal",
                                        value.getClass(),
                                        key
        );
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
    public <O> Optional<List<O>> getList(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof List) return Optional.of(((List) value));

        throw new RecordTypeNotExpected("List",
                                        value.getClass(),
                                        key
        );
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
    public <O> Optional<Set<O>> getSet(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Set) return Optional.of(((Set) value));

        throw new RecordTypeNotExpected("Set",
                                        value.getClass(),
                                        key
        );
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
    public <K, V> Optional<Map<K, V>> getMap(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Map) return Optional.of(((Map) value));
        throw new RecordTypeNotExpected("Map",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional instant.
     *
     * @param key The name of the field.
     * @return An optional containing the instant value if present, otherwise an empty optional.
     */
    public Optional<Instant> getInstant(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Instant) return Optional.of(((Instant) value));
        throw new RecordTypeNotExpected("Instant",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional double precision floating-point number.
     *
     * @param key The name of the field.
     * @return An optional containing the double value if present, otherwise an empty optional.
     */
    public Optional<Double> getDouble(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Double) return Optional.of(((Double) value));
        throw new RecordTypeNotExpected("Double",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional boolean.
     *
     * @param key The name of the field.
     * @return An optional containing the boolean value if present, otherwise an empty optional.
     */
    public Optional<Boolean> getBool(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Boolean) return Optional.of(((Boolean) value));
        throw new RecordTypeNotExpected("Boolean",
                                        value.getClass(),
                                        key
        );

    }

    /**
     * Retrieves the value associated with the specified field name as an optional character.
     *
     * @param key The name of the field.
     * @return An optional containing the character value if present, otherwise an empty optional.
     */
    public Optional<Character> getChar(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Character) return Optional.of(((Character) value));
        throw new RecordTypeNotExpected("Character",
                                        value.getClass(),
                                        key
        );
    }

    /**
     * Retrieves the value associated with the specified field name as an optional big integer.
     *
     * @param key The name of the field.
     * @return An optional containing the big integer value if present, otherwise an empty optional.
     */
    public Optional<BigInteger> getBigInt(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigInteger) return Optional.of(((BigInteger) value));
        throw new RecordTypeNotExpected("BigInteger",
                                        value.getClass(),
                                        key
        );
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
        Record record = (Record) o;
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
