package fun.gen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

import static java.util.Objects.requireNonNull;

public final class Record {

    public final Map<String, ?> map;

    public Record(final Map<String, ?> map) {
        this.map = requireNonNull(map);
    }

    @SuppressWarnings("unchecked")
    public Optional<byte[]> getBytes(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof byte[]) return Optional.ofNullable(((byte[]) value));
        throw new RecordTypeNotExpected("byte[]",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<Long> getLong(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Long) return Optional.ofNullable(((Long) value));
        throw new RecordTypeNotExpected("Long",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<String> getStr(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof String) return Optional.ofNullable(((String) value));
        throw new RecordTypeNotExpected("String",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> getInt(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Integer) return Optional.ofNullable(((Integer) value));
        throw new RecordTypeNotExpected("Integer",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<BigDecimal> getDecimal(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigDecimal) return Optional.ofNullable(((BigDecimal) value));
        throw new RecordTypeNotExpected("BigDecimal",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public <O> Optional<List<O>> getList(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof List) return Optional.ofNullable(((List) value));

        throw new RecordTypeNotExpected("List",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public <O> Optional<Set<O>> getSet(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Set) return Optional.ofNullable(((Set) value));

        throw new RecordTypeNotExpected("Set",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public <K,V> Optional<Map<K, V>> getMap(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Map) return Optional.ofNullable(((Map) value));
        throw new RecordTypeNotExpected("Map",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<Instant> getInstant(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Instant) return Optional.ofNullable(((Instant) value));
        throw new RecordTypeNotExpected("Instant",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<Double> getDouble(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Double) return Optional.ofNullable(((Double) value));
        throw new RecordTypeNotExpected("Double",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<Boolean> getBool(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();

        if (value instanceof Boolean) return Optional.ofNullable(((Boolean) value));
        throw new RecordTypeNotExpected("Boolean",
                                        value.getClass(),
                                        key);

    }

    @SuppressWarnings("unchecked")
    public Optional<Character> getChar(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof Character) return Optional.ofNullable(((Character) value));
        throw new RecordTypeNotExpected("Character",
                                        value.getClass(),
                                        key);
    }

    @SuppressWarnings("unchecked")
    public Optional<BigInteger> getBigInt(final String key) {
        Object value = map.get(key);
        if (value == null) return Optional.empty();
        if (value instanceof BigInteger) return Optional.ofNullable(((BigInteger) value));
        throw new RecordTypeNotExpected("BigInteger",
                                        value.getClass(),
                                        key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(map,
                              record.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
