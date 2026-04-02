package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RecordTypeNotExpectedTest {

    @Test
    void shouldKeepExplicitMessageWhenConstructedWithMessage() {
        RecordTypeNotExpected ex = new RecordTypeNotExpected("boom");

        Assertions.assertEquals("boom",
                                ex.getMessage());
    }

    @Test
    void shouldBuildMessageFromExpectedAndRealTypeWhenUsingTypeConstructor() {
        RecordTypeNotExpected ex = new RecordTypeNotExpected("Integer",
                                                             String.class,
                                                             "age");

        Assertions.assertTrue(ex.getMessage().contains("age"));
        Assertions.assertTrue(ex.getMessage().contains("Integer"));
        Assertions.assertTrue(ex.getMessage().contains("java.lang.String"));
    }
}
