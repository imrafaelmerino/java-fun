package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

class TestMyRecord {

    @Test
    void boxedGettersShouldReturnNullWhenKeyIsMissing() {
        var record = new MyRecord(Map.of());

        Assertions.assertNull(record.getInt("missing"));
        Assertions.assertNull(record.getLong("missing"));
        Assertions.assertNull(record.getDouble("missing"));
        Assertions.assertNull(record.getBoolean("missing"));
        Assertions.assertNull(record.getChar("missing"));
    }

    @Test
    void getDecimalShouldAcceptBigIntegerValues() {
        var record = new MyRecord(Map.of("n",
                                         BigInteger.valueOf(42)));

        Assertions.assertEquals(BigDecimal.valueOf(42),
                                record.getDecimal("n"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void asMapShouldBeImmutableAndDefensivelyCopied() {
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
}
