package fun.gen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A class representing a record with named fields and associated values.
 * This class provides methods for safely retrieving field values of various types.
 * <p>
 * Construction modes:
 * <ul>
 *     <li>Default constructor: deep-frozen snapshot (safe by default).</li>
 *     <li>{@link #wrap(Map)}: no-copy wrap with live-reference semantics.</li>
 * </ul>
 * Getter semantics:
 * <ul>
 *     <li>{@code *View} getters return typed views over stored values.</li>
 *     <li>{@code *Copy} getters return defensive immutable copies.</li>
 * </ul>
 */
public final class MyRecord {

    /**
     * The underlying immutable map that holds the record data.
     */
    private final Map<String, ?> values;

    /**
     * Constructs a new record instance with the provided map of field names and values.
     * The map and nested container values are defensively copied and deep-frozen.
     *
     * @param map A map containing field names as keys and their associated values.
     */
    public MyRecord(final Map<String, ?> map) {
        this(map,
             true);
    }

    private MyRecord(final Map<String, ?> map,
                     final boolean deepFreeze) {
        if (!deepFreeze) {
            this.values = Collections.unmodifiableMap(requireNonNull(map));
            return;
        }
        Map<String, Object> copy = new LinkedHashMap<>();
        for (var entry : requireNonNull(map).entrySet()) {
            copy.put(entry.getKey(),
                     freezeValue(entry.getValue()));
        }
        this.values = Collections.unmodifiableMap(copy);
    }

    /**
     * Creates a record by wrapping the provided map without deep-freezing nested structures.
     * <p>
     * This avoids defensive copy cost and can be useful in hot paths, but external mutations to the source map
     * (or nested mutable values) are visible through this record and can affect equals/hashCode stability over time.
     *
     * @param map map to wrap (live reference)
     * @return record wrapping the provided map with live-reference semantics
     */
    public static MyRecord wrap(final Map<String, ?> map) {
        return new MyRecord(map,
                            false);
    }

    private static Object freezeValue(final Object value) {
        if (value instanceof byte[] bytes) {
            return Arrays.copyOf(bytes,
                                 bytes.length);
        }
        if (value instanceof List<?> list) {
            List<Object> frozen = new ArrayList<>(list.size());
            for (Object elem : list) {
                frozen.add(freezeValue(elem));
            }
            return Collections.unmodifiableList(frozen);
        }
        if (value instanceof Set<?> set) {
            Set<Object> frozen = new LinkedHashSet<>(set.size());
            for (Object elem : set) {
                frozen.add(freezeValue(elem));
            }
            return Collections.unmodifiableSet(frozen);
        }
        if (value instanceof Map<?, ?> map) {
            Map<Object, Object> frozen = new LinkedHashMap<>(map.size());
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                frozen.put(freezeValue(entry.getKey()),
                           freezeValue(entry.getValue()));
            }
            return Collections.unmodifiableMap(frozen);
        }
        return value;
    }

    private static <T> List<T> immutableListCopy(final List<T> list) {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    private static <T> Set<T> immutableSetCopy(final Set<T> set) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(set));
    }

    private static <K, V> Map<K, V> immutableMapCopy(final Map<K, V> map) {
        return Collections.unmodifiableMap(new LinkedHashMap<>(map));
    }

    private static boolean deepValueEquals(final Object left,
                                           final Object right) {
        if (left == right) return true;
        if (left == null || right == null) return false;
        if (left instanceof byte[] a && right instanceof byte[] b) return Arrays.equals(a,
                                                                                        b);
        if (left instanceof short[] a && right instanceof short[] b) return Arrays.equals(a,
                                                                                          b);
        if (left instanceof int[] a && right instanceof int[] b) return Arrays.equals(a,
                                                                                      b);
        if (left instanceof long[] a && right instanceof long[] b) return Arrays.equals(a,
                                                                                        b);
        if (left instanceof float[] a && right instanceof float[] b) return Arrays.equals(a,
                                                                                          b);
        if (left instanceof double[] a && right instanceof double[] b) return Arrays.equals(a,
                                                                                            b);
        if (left instanceof char[] a && right instanceof char[] b) return Arrays.equals(a,
                                                                                        b);
        if (left instanceof boolean[] a && right instanceof boolean[] b) return Arrays.equals(a,
                                                                                              b);
        if (left instanceof Object[] a && right instanceof Object[] b) return Arrays.deepEquals(a,
                                                                                                b);
        return Objects.equals(left,
                              right);
    }

    private static int deepValueHash(final Object value) {
        if (value == null) return 0;
        if (value instanceof byte[] a) return Arrays.hashCode(a);
        if (value instanceof short[] a) return Arrays.hashCode(a);
        if (value instanceof int[] a) return Arrays.hashCode(a);
        if (value instanceof long[] a) return Arrays.hashCode(a);
        if (value instanceof float[] a) return Arrays.hashCode(a);
        if (value instanceof double[] a) return Arrays.hashCode(a);
        if (value instanceof char[] a) return Arrays.hashCode(a);
        if (value instanceof boolean[] a) return Arrays.hashCode(a);
        if (value instanceof Object[] a) return Arrays.deepHashCode(a);
        return value.hashCode();
    }

    private static boolean deepMapEquals(final Map<String, ?> left,
                                         final Map<String, ?> right) {
        if (left.size() != right.size()) return false;
        for (var entry : left.entrySet()) {
            if (!right.containsKey(entry.getKey())) return false;
            if (!deepValueEquals(entry.getValue(),
                                 right.get(entry.getKey()))) return false;
        }
        return true;
    }

    private static int deepMapHash(final Map<String, ?> map) {
        int hash = 0;
        for (var entry : map.entrySet()) {
            hash += Objects.hashCode(entry.getKey()) ^ deepValueHash(entry.getValue());
        }
        return hash;
    }

    /**
     * Returns an immutable view of the underlying key-value data.
     * <p>
     * In default constructor mode this view points to deep-frozen snapshot data.
     * In {@link #wrap(Map)} mode it is an unmodifiable live view over the wrapped source map.
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
        if (value instanceof byte[] b) return Optional.of(Arrays.copyOf(b,
                                                                        b.length));
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
        if (value instanceof byte[] d) return Arrays.copyOf(d,
                                                            d.length);
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
     * Returns an optional live view of the list value stored at {@code key}.
     * <p>
     * The returned list is not copied at read time. In default constructor mode, it points to deep-frozen
     * internal state. In {@link #wrap(Map)} mode, it may reflect external mutations.
     *
     * @param key The name of the field.
     * @param <O> The list element type.
     * @return An optional containing the stored list view, or empty when the key is missing.
     * @throws RecordTypeNotExpected If the stored value is not a list.
     */
    @SuppressWarnings("unchecked")
    public <O> Optional<List<O>> getOptionalListView(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.<List<O>>empty();
        if (value instanceof List<?> list) return Optional.of((List<O>) list);
        throw new RecordTypeNotExpected(List.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Returns an optional immutable copy of the list value stored at {@code key}.
     *
     * @param key The name of the field.
     * @param <O> The list element type.
     * @return An optional containing an immutable copy, or empty when the key is missing.
     * @throws RecordTypeNotExpected If the stored value is not a list.
     */
    public <O> Optional<List<O>> getOptionalListCopy(final String key) {
        Optional<List<O>> view = getOptionalListView(key);
        if (view.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(immutableListCopy(view.get()));
    }

    /**
     * Returns the live list view stored at {@code key}, or a fallback from {@code supplier} when absent.
     *
     * @param key      The name of the field.
     * @param supplier Fallback supplier used when the field is absent or null.
     * @param <O>      The list element type.
     * @return The stored live list view, or the fallback value.
     * @throws RecordTypeNotExpected If the stored value is not a list.
     */
    public <O> List<O> getListView(final String key,
                                   final Supplier<List<O>> supplier) {
        Optional<List<O>> view = getOptionalListView(key);
        return view.isPresent() ?
               view.get() :
               supplier.get();
    }

    /**
     * Returns the live list view stored at {@code key}, or {@code null} when absent.
     *
     * @param key The name of the field.
     * @param <O> The list element type.
     * @return The stored live list view, or {@code null}.
     * @see #getListView(String, Supplier)
     */
    public <O> List<O> getListView(final String key) {
        return getListView(key,
                           () -> null);
    }

    /**
     * Returns an immutable copy of the list value stored at {@code key}, or the fallback from {@code supplier}.
     *
     * @param key      The name of the field.
     * @param supplier Fallback supplier used when the field is absent or null.
     * @param <O>      The list element type.
     * @return An immutable copy of the stored list, or the fallback value.
     * @throws RecordTypeNotExpected If the stored value is not a list.
     */
    public <O> List<O> getListCopy(final String key,
                                   final Supplier<List<O>> supplier) {
        List<O> value = getListView(key,
                                    supplier);
        return value == null ?
               null :
               immutableListCopy(value);
    }

    /**
     * Returns an immutable copy of the list value stored at {@code key}, or {@code null} when absent.
     *
     * @param key The name of the field.
     * @param <O> The list element type.
     * @return An immutable copy of the stored list, or {@code null}.
     * @see #getListCopy(String, Supplier)
     */
    public <O> List<O> getListCopy(final String key) {
        return getListCopy(key,
                           () -> null);
    }


     /**
      * Returns an optional live view of the set value stored at {@code key}.
     * In {@link #wrap(Map)} mode, it may reflect external mutations.
     *
     * @param key The name of the field.
     * @param <O> The set element type.
     * @return An optional containing the stored set view, or empty when absent.
     * @throws RecordTypeNotExpected If the stored value is not a set.
     */
    @SuppressWarnings("unchecked")
    public <O> Optional<Set<O>> getOptionalSetView(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.<Set<O>>empty();
        if (value instanceof Set<?> set) return Optional.of((Set<O>) set);
        throw new RecordTypeNotExpected(Set.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Returns an optional immutable copy of the set value stored at {@code key}.
     *
     * @param key The name of the field.
     * @param <O> The set element type.
     * @return An optional containing an immutable copy, or empty when absent.
     * @throws RecordTypeNotExpected If the stored value is not a set.
     */
    public <O> Optional<Set<O>> getOptionalSetCopy(final String key) {
        Optional<Set<O>> view = getOptionalSetView(key);
        if (view.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(immutableSetCopy(view.get()));
    }

    /**
     * Returns the live set view stored at {@code key}, or a fallback from {@code supplier} when absent.
     *
     * @param key      The name of the field.
     * @param supplier Fallback supplier used when the field is absent or null.
     * @param <O>      The set element type.
     * @return The stored live set view, or the fallback value.
     * @throws RecordTypeNotExpected If the stored value is not a set.
     */
    public <O> Set<O> getSetView(final String key,
                                 final Supplier<Set<O>> supplier) {
        Optional<Set<O>> view = getOptionalSetView(key);
        return view.isPresent() ?
               view.get() :
               supplier.get();
    }

    /**
     * Returns the live set view stored at {@code key}, or {@code null} when absent.
     *
     * @param key The name of the field.
     * @param <O> The set element type.
     * @return The stored live set view, or {@code null}.
     * @see #getSetView(String, Supplier)
     */
    public <O> Set<O> getSetView(final String key) {
        return getSetView(key,
                          () -> null);
    }

    /**
     * Returns an immutable copy of the set value stored at {@code key}, or the fallback from {@code supplier}.
     *
     * @param key      The name of the field.
     * @param supplier Fallback supplier used when the field is absent or null.
     * @param <O>      The set element type.
     * @return An immutable copy of the stored set, or the fallback value.
     * @throws RecordTypeNotExpected If the stored value is not a set.
     */
    public <O> Set<O> getSetCopy(final String key,
                                 final Supplier<Set<O>> supplier) {
        Set<O> value = getSetView(key,
                                  supplier);
        return value == null ?
               null :
               immutableSetCopy(value);
    }

    /**
     * Returns an immutable copy of the set value stored at {@code key}, or {@code null} when absent.
     *
     * @param key The name of the field.
     * @param <O> The set element type.
     * @return An immutable copy of the stored set, or {@code null}.
     * @see #getSetCopy(String, Supplier)
     */
    public <O> Set<O> getSetCopy(final String key) {
        return getSetCopy(key,
                          () -> null);
    }


     /**
      * Returns an optional live view of the map value stored at {@code key}.
     * In {@link #wrap(Map)} mode, it may reflect external mutations.
     *
     * @param key The name of the field.
     * @param <K> The map key type.
     * @param <V> The map value type.
     * @return An optional containing the stored map view, or empty when absent.
     * @throws RecordTypeNotExpected If the stored value is not a map.
     */
    @SuppressWarnings("unchecked")
    public <K, V> Optional<Map<K, V>> getOptionalMapView(final String key) {
        Object value = values.get(key);
        if (value == null) return Optional.<Map<K, V>>empty();
        if (value instanceof Map<?, ?> map) return Optional.of((Map<K, V>) map);
        throw new RecordTypeNotExpected(Map.class.getName(),
                                        value.getClass(),
                                        key);
    }

    /**
     * Returns an optional immutable copy of the map value stored at {@code key}.
     *
     * @param key The name of the field.
     * @param <K> The map key type.
     * @param <V> The map value type.
     * @return An optional containing an immutable copy, or empty when absent.
     * @throws RecordTypeNotExpected If the stored value is not a map.
     */
    public <K, V> Optional<Map<K, V>> getOptionalMapCopy(final String key) {
        Optional<Map<K, V>> view = getOptionalMapView(key);
        if (view.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(immutableMapCopy(view.get()));
    }

    /**
     * Returns the live map view stored at {@code key}, or a fallback from {@code supplier} when absent.
     *
     * @param key      The name of the field.
     * @param supplier Fallback supplier used when the field is absent or null.
     * @param <K>      The map key type.
     * @param <V>      The map value type.
     * @return The stored live map view, or the fallback value.
     * @throws RecordTypeNotExpected If the stored value is not a map.
     */
    public <K, V> Map<K, V> getMapView(final String key,
                                       final Supplier<Map<K, V>> supplier) {
        Optional<Map<K, V>> view = getOptionalMapView(key);
        return view.isPresent() ?
               view.get() :
               supplier.get();
    }

    /**
     * Returns the live map view stored at {@code key}, or {@code null} when absent.
     *
     * @param key The name of the field.
     * @param <K> The map key type.
     * @param <V> The map value type.
     * @return The stored live map view, or {@code null}.
     * @see #getMapView(String, Supplier)
     */
    public <K, V> Map<K, V> getMapView(final String key) {
        return getMapView(key,
                          () -> null);
    }

    /**
     * Returns an immutable copy of the map value stored at {@code key}, or the fallback from {@code supplier}.
     *
     * @param key      The name of the field.
     * @param supplier Fallback supplier used when the field is absent or null.
     * @param <K>      The map key type.
     * @param <V>      The map value type.
     * @return An immutable copy of the stored map, or the fallback value.
     * @throws RecordTypeNotExpected If the stored value is not a map.
     */
    public <K, V> Map<K, V> getMapCopy(final String key,
                                       final Supplier<Map<K, V>> supplier) {
        Map<K, V> value = getMapView(key,
                                     supplier);
        return value == null ?
               null :
               immutableMapCopy(value);
    }

    /**
     * Returns an immutable copy of the map value stored at {@code key}, or {@code null} when absent.
     *
     * @param key The name of the field.
     * @param <K> The map key type.
     * @param <V> The map value type.
     * @return An immutable copy of the stored map, or {@code null}.
     * @see #getMapCopy(String, Supplier)
     */
    public <K, V> Map<K, V> getMapCopy(final String key) {
        return getMapCopy(key,
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
        return deepMapEquals(values,
                             record.values);
    }

    /**
     * Computes a hash code for this Record.
     *
     * @return A hash code for this Record.
     */
    @Override
    public int hashCode() {
        return deepMapHash(values);
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
