package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MyRecordAccessorCoverageTest {

    @Test
    void shouldConvertNumericValuesAcrossCompatibleGetterFamiliesWhenSourceTypesMatch() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("asLongFromInt", 7);
        values.put("asLongFromShort", (short) 8);
        values.put("asLongFromByte", (byte) 9);
        values.put("asIntFromShort", (short) 10);
        values.put("asIntFromByte", (byte) 11);
        values.put("asDoubleFromLong", 12L);
        values.put("asDoubleFromInt", 13);
        values.put("asDoubleFromShort", (short) 14);
        values.put("asDoubleFromByte", (byte) 15);
        values.put("asDoubleFromFloat", 16.5f);
        values.put("asBigIntFromLong", 17L);
        values.put("asBigIntFromInt", 18);
        values.put("asBigIntFromShort", (short) 19);
        values.put("asBigIntFromByte", (byte) 20);
        values.put("asDecimalFromBigInt", BigInteger.valueOf(21));
        values.put("asDecimalFromLong", 22L);
        values.put("asDecimalFromInt", 23);
        values.put("asDecimalFromShort", (short) 24);
        values.put("asDecimalFromByte", (byte) 25);
        values.put("asDecimalFromDouble", 26.75d);
        values.put("asDecimalFromFloat", 27.5f);
        MyRecord record = new MyRecord(values);

        Assertions.assertEquals(7L, record.getLong("asLongFromInt"));
        Assertions.assertEquals(8L, record.getLong("asLongFromShort"));
        Assertions.assertEquals(9L, record.getLong("asLongFromByte"));

        Assertions.assertEquals(10, record.getInt("asIntFromShort"));
        Assertions.assertEquals(11, record.getInt("asIntFromByte"));

        Assertions.assertEquals(12.0d, record.getDouble("asDoubleFromLong"));
        Assertions.assertEquals(13.0d, record.getDouble("asDoubleFromInt"));
        Assertions.assertEquals(14.0d, record.getDouble("asDoubleFromShort"));
        Assertions.assertEquals(15.0d, record.getDouble("asDoubleFromByte"));
        Assertions.assertEquals(16.5d, record.getDouble("asDoubleFromFloat"));

        Assertions.assertEquals(BigInteger.valueOf(17), record.getBigInteger("asBigIntFromLong"));
        Assertions.assertEquals(BigInteger.valueOf(18), record.getBigInteger("asBigIntFromInt"));
        Assertions.assertEquals(BigInteger.valueOf(19), record.getBigInteger("asBigIntFromShort"));
        Assertions.assertEquals(BigInteger.valueOf(20), record.getBigInteger("asBigIntFromByte"));

        Assertions.assertEquals(new BigDecimal("21"), record.getDecimal("asDecimalFromBigInt"));
        Assertions.assertEquals(new BigDecimal("22"), record.getDecimal("asDecimalFromLong"));
        Assertions.assertEquals(new BigDecimal("23"), record.getDecimal("asDecimalFromInt"));
        Assertions.assertEquals(new BigDecimal("24"), record.getDecimal("asDecimalFromShort"));
        Assertions.assertEquals(new BigDecimal("25"), record.getDecimal("asDecimalFromByte"));
        Assertions.assertEquals(BigDecimal.valueOf(26.75d), record.getDecimal("asDecimalFromDouble"));
        Assertions.assertEquals(BigDecimal.valueOf(27.5f), record.getDecimal("asDecimalFromFloat"));
    }

    @Test
    void shouldReturnExpectedOptionalsWhenUsingOptionalAccessorsForPresentAndMissingKeys() {
        Instant now = Instant.now();
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("bytes", new byte[]{1, 2});
        values.put("str", "s");
        values.put("bool", true);
        values.put("char", 'x');
        values.put("int", 1);
        values.put("long", 2L);
        values.put("double", 3.5d);
        values.put("bigInt", BigInteger.TEN);
        values.put("decimal", BigDecimal.ONE);
        values.put("instant", now);
        values.put("list", List.of(1, 2));
        values.put("set", Set.of("a"));
        values.put("map", Map.of("k", "v"));
        MyRecord record = new MyRecord(values);

        Assertions.assertArrayEquals(new byte[]{1, 2}, record.getOptionalBytes("bytes").orElseThrow());
        Assertions.assertEquals("s", record.getOptionalString("str").orElseThrow());
        Assertions.assertTrue(record.getOptionalBoolean("bool").orElseThrow());
        Assertions.assertEquals('x', record.getOptionalChar("char").orElseThrow());
        Assertions.assertEquals(1, record.getOptionalInt("int").orElseThrow());
        Assertions.assertEquals(2L, record.getOptionalLong("long").orElseThrow());
        Assertions.assertEquals(3.5d, record.getOptionalDouble("double").orElseThrow());
        Assertions.assertEquals(BigInteger.TEN, record.getOptionalBigInteger("bigInt").orElseThrow());
        Assertions.assertEquals(BigDecimal.ONE, record.getOptionalDecimal("decimal").orElseThrow());
        Assertions.assertEquals(now, record.getOptionalInstant("instant").orElseThrow());
        Assertions.assertEquals(List.of(1, 2), record.getOptionalListView("list").orElseThrow());
        Assertions.assertEquals(Set.of("a"), record.getOptionalSetView("set").orElseThrow());
        Assertions.assertEquals(Map.of("k", "v"), record.getOptionalMapView("map").orElseThrow());

        Assertions.assertTrue(record.getOptionalBytes("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalString("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalBoolean("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalChar("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalInt("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalLong("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalDouble("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalBigInteger("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalDecimal("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalInstant("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalListView("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalSetView("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalMapView("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalListCopy("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalSetCopy("missing").isEmpty());
        Assertions.assertTrue(record.getOptionalMapCopy("missing").isEmpty());
    }

    @Test
    void shouldUseSupplierFallbacksWhenKeysAreMissing() {
        MyRecord record = new MyRecord(Map.of());
        List<Integer> listFallback = new ArrayList<>(List.of(9));
        Set<String> setFallback = new LinkedHashSet<>(Set.of("x"));
        Map<String, Integer> mapFallback = new LinkedHashMap<>(Map.of("k", 1));
        Instant instant = Instant.ofEpochSecond(123);

        Assertions.assertArrayEquals(new byte[]{7}, record.getBytes("missing", () -> new byte[]{7}));
        Assertions.assertEquals("fallback", record.getString("missing", () -> "fallback"));
        Assertions.assertEquals(1, record.getInt("missing", () -> 1));
        Assertions.assertEquals(2L, record.getLong("missing", () -> 2L));
        Assertions.assertEquals(3.0d, record.getDouble("missing", () -> 3.0d));
        Assertions.assertEquals(true, record.getBoolean("missing", () -> true));
        Assertions.assertEquals('z', record.getChar("missing", () -> 'z'));
        Assertions.assertEquals(BigInteger.valueOf(4), record.getBigInteger("missing", () -> BigInteger.valueOf(4)));
        Assertions.assertEquals(BigDecimal.valueOf(5), record.getDecimal("missing", () -> BigDecimal.valueOf(5)));
        Assertions.assertEquals(instant, record.getInstant("missing", () -> instant));
        Assertions.assertSame(listFallback, record.getListView("missing", () -> listFallback));
        Assertions.assertSame(setFallback, record.getSetView("missing", () -> setFallback));
        Assertions.assertSame(mapFallback, record.getMapView("missing", () -> mapFallback));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> record.getListCopy("missing", () -> listFallback).add(10));
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> record.getSetCopy("missing", () -> setFallback).add("y"));
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> record.getMapCopy("missing", () -> mapFallback).put("z", 2));
    }

    @Test
    void shouldThrowRecordTypeNotExpectedWhenGetterTypeIsIncompatible() {
        MyRecord record = new MyRecord(Map.of(
                "string", "abc",
                "bool", true,
                "number", 1
        ));

        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalInt("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalBoolean("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalChar("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalBigInteger("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalDecimal("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalListView("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalSetView("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalMapView("string"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalInstant("number"));
        Assertions.assertThrows(RecordTypeNotExpected.class, () -> record.getOptionalString("bool"));
    }

    @Test
    void shouldCompareAndHashAllPrimitiveAndObjectArraysByDeepContentWhenUsingEqualsAndHashCode() {
        Map<String, Object> leftMap = new LinkedHashMap<>();
        leftMap.put("shorts", new short[]{1, 2});
        leftMap.put("ints", new int[]{3, 4});
        leftMap.put("longs", new long[]{5L, 6L});
        leftMap.put("floats", new float[]{1.25f, 2.5f});
        leftMap.put("doubles", new double[]{1.5d, 2.75d});
        leftMap.put("chars", new char[]{'a', 'b'});
        leftMap.put("booleans", new boolean[]{true, false});
        leftMap.put("objects", new Object[]{"x", List.of(1, 2)});

        Map<String, Object> rightMap = new LinkedHashMap<>();
        rightMap.put("shorts", new short[]{1, 2});
        rightMap.put("ints", new int[]{3, 4});
        rightMap.put("longs", new long[]{5L, 6L});
        rightMap.put("floats", new float[]{1.25f, 2.5f});
        rightMap.put("doubles", new double[]{1.5d, 2.75d});
        rightMap.put("chars", new char[]{'a', 'b'});
        rightMap.put("booleans", new boolean[]{true, false});
        rightMap.put("objects", new Object[]{"x", List.of(1, 2)});

        MyRecord left = new MyRecord(leftMap);
        MyRecord right = new MyRecord(rightMap);

        Assertions.assertEquals(left, right);
        Assertions.assertEquals(left.hashCode(), right.hashCode());
        Assertions.assertEquals(left.asMap().toString(), left.toString());
    }
}
