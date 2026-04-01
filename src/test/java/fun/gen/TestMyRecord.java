package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class TestMyRecord {

    @Test
    void boxedGettersShouldReturnNullWhenKeyIsMissing() {
        var record = new MyRecord(Map.of());

        Assertions.assertNull(record.getInt("missing"));
        Assertions.assertNull(record.getLong("missing"));
        Assertions.assertNull(record.getDouble("missing"));
        Assertions.assertNull(record.getBool("missing"));
        Assertions.assertNull(record.getChar("missing"));
    }
}
