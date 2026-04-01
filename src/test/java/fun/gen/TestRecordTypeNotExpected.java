package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestRecordTypeNotExpected {

    @Test
    public void shouldKeepExplicitMessage() {
        RecordTypeNotExpected ex = new RecordTypeNotExpected("boom");

        Assertions.assertEquals("boom",
                                ex.getMessage());
    }

    @Test
    public void shouldBuildMessageFromExpectedAndRealType() {
        RecordTypeNotExpected ex = new RecordTypeNotExpected("Integer",
                                                             String.class,
                                                             "age");

        Assertions.assertTrue(ex.getMessage().contains("age"));
        Assertions.assertTrue(ex.getMessage().contains("Integer"));
        Assertions.assertTrue(ex.getMessage().contains("java.lang.String"));
    }
}
