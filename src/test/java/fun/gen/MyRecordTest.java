package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MyRecordTest {

    @Test
    void shouldReturnNullWhenKeyIsMissingWhenUsingBoxedGetters() {
        var record = new MyRecord(Map.of());

        Assertions.assertNull(record.getInt("missing"));
        Assertions.assertNull(record.getLong("missing"));
        Assertions.assertNull(record.getDouble("missing"));
        Assertions.assertNull(record.getBoolean("missing"));
        Assertions.assertNull(record.getChar("missing"));
    }

    @Test
    void shouldAcceptBigIntegerValuesWhenUsingGetDecimal() {
        var record = new MyRecord(Map.of("n",
                                         BigInteger.valueOf(42)));

        Assertions.assertEquals(BigDecimal.valueOf(42),
                                record.getDecimal("n"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldBeImmutableAndDefensivelyCopiedWhenUsingAsMap() {
        Map<String, Object> source = new HashMap<>();
        source.put("a",
                   1);
        var record = new MyRecord(source);

        source.put("b",
                   2);

        Assertions.assertEquals(1,
                                record.size());
        Assertions.assertFalse(record.containsKey("b"));
        Assertions.assertFalse(record.isEmpty());

        Map<String, Object> values = (Map<String, Object>) record.asMap();
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> values.put("c",
                                                 3));
    }

    @Test
    void shouldTreatByteArraysByContentWhenUsingEqualsAndHashCode() {
        var left = new MyRecord(Map.of("payload",
                                       new byte[]{1, 2, 3}));
        var right = new MyRecord(Map.of("payload",
                                        new byte[]{1, 2, 3}));

        Assertions.assertTrue(Arrays.equals(left.getBytes("payload"),
                                            right.getBytes("payload")));
        Assertions.assertEquals(left,
                                right);
        Assertions.assertEquals(left.hashCode(),
                                right.hashCode());
    }

    @Test
    void shouldDefensivelyCopyByteArraysWhenUsingConstructor() {
        byte[] payload = new byte[]{1, 2, 3};
        var record = new MyRecord(Map.of("payload",
                                         payload));

        payload[0] = 9;

        Assertions.assertArrayEquals(new byte[]{1, 2, 3},
                                     record.getBytes("payload"));
    }

    @Test
    void shouldReturnDefensiveCopiesWhenUsingGetBytes() {
        var record = new MyRecord(Map.of("payload",
                                         new byte[]{4, 5, 6}));
        byte[] extracted = record.getBytes("payload");

        extracted[0] = 9;

        Assertions.assertArrayEquals(new byte[]{4, 5, 6},
                                     record.getBytes("payload"));
        Assertions.assertArrayEquals(new byte[]{4, 5, 6},
                                     record.getOptionalBytes("payload").orElseThrow());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldExposeFrozenStoredValuesWhenUsingCollectionViewsAndCopies() {
        List<Integer> list = new ArrayList<>(List.of(1, 2));
        Set<String> set = new HashSet<>(Set.of("a", "b"));
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("x",
                1);

        var record = new MyRecord(Map.of("list",
                                         list,
                                         "set",
                                         set,
                                         "map",
                                         map));

        List<Integer> listView = record.getListView("list");
        List<Integer> listCopy = record.getListCopy("list");
        Set<String> setView = record.getSetView("set");
        Set<String> setCopy = record.getSetCopy("set");
        Map<String, Integer> mapView = record.getMapView("map");
        Map<String, Integer> mapCopy = record.getMapCopy("map");

        list.add(3);
        set.add("c");
        map.put("y",
                2);

        Assertions.assertEquals(List.of(1, 2),
                                listView);
        Assertions.assertEquals(List.of(1, 2),
                                listCopy);
        Assertions.assertFalse(setView.contains("c"));
        Assertions.assertFalse(setCopy.contains("c"));
        Assertions.assertFalse(mapView.containsKey("y"));
        Assertions.assertFalse(mapCopy.containsKey("y"));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> listView.add(99));
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> setView.add("z"));
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> mapView.put("z",
                                                  3));

        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> listCopy.add(99));
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> setCopy.add("z"));
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> mapCopy.put("z",
                                                  3));
    }

    @Test
    void shouldPreserveFrozenSemanticsWhenUsingViewAndCopyCollectionGetters() {
        List<Integer> list = new ArrayList<>(List.of(1, 2));
        Set<String> set = new HashSet<>(Set.of("a", "b"));
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("x",
                1);

        var record = new MyRecord(Map.of("list",
                                         list,
                                         "set",
                                         set,
                                         "map",
                                         map));

        List<Integer> listDeprecated = record.getListView("list");
        Set<String> setDeprecated = record.getSetView("set");
        Map<String, Integer> mapDeprecated = record.getMapView("map");

        list.add(3);
        set.add("c");
        map.put("y",
                2);

        Assertions.assertEquals(List.of(1, 2),
                                listDeprecated);
        Assertions.assertFalse(setDeprecated.contains("c"));
        Assertions.assertFalse(mapDeprecated.containsKey("y"));
    }

    @Test
    void shouldDeepFreezeNestedCollectionValuesWhenUsingConstructor() {
        List<Map<String, Integer>> nested = new ArrayList<>();
        Map<String, Integer> node = new LinkedHashMap<>();
        node.put("a",
                 1);
        nested.add(node);

        MyRecord record = new MyRecord(Map.of("nested",
                                              nested));

        node.put("b",
                 2);
        nested.add(Map.of("x",
                          9));

        List<Map<String, Integer>> frozen = record.getListView("nested");
        Assertions.assertEquals(1,
                                frozen.size());
        Assertions.assertEquals(Map.of("a",
                                       1),
                                frozen.getFirst());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldExposeLiveSourceMutationsWhenUsingWrap() {
        List<Integer> list = new ArrayList<>(List.of(1, 2));
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("list",
                   list);
        source.put("a",
                   1);

        MyRecord record = MyRecord.wrap(source);

        List<Integer> copyBefore = record.getListCopy("list");

        list.add(3);
        source.put("b",
                   2);

        Assertions.assertTrue(record.containsKey("b"));
        Assertions.assertEquals(List.of(1, 2, 3),
                                record.getListView("list"));
        Assertions.assertEquals(List.of(1, 2),
                                copyBefore);

        Map<String, Object> raw = (Map<String, Object>) record.asMap();
        Assertions.assertThrows(UnsupportedOperationException.class,
                                () -> raw.put("c",
                                              3));
    }
}
