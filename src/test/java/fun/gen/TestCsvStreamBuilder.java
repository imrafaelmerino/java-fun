package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TestCsvStreamBuilder {

    @Test
    void shouldParseHeadersUsingConfiguredSeparator() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-separator",
                                         ".csv");
        Files.writeString(file,
                          "a;b\n1;2\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ";")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a"));
            Assertions.assertEquals(2,
                                    records.get(0).getInt("b"));
        }
    }

    @Test
    void shouldConvertQuotedFalseToBoolean() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-quoted-bool",
                                         ".csv");
        Files.writeString(file,
                          "flag\n\"false\"\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertFalse(records.get(0).getBool("flag"));
        }
    }

    @Test
    void shouldSupportRegexMetaCharacterSeparator() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-pipe-separator",
                                         ".csv");
        Files.writeString(file,
                          "a|b\n1|2\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              "|")
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals(1,
                                    records.get(0).getInt("a"));
            Assertions.assertEquals(2,
                                    records.get(0).getInt("b"));
        }
    }

    @Test
    void shouldUnquoteEmptyStringValues() throws Exception {
        Path file = Files.createTempFile("java-fun-csv-empty-quoted",
                                         ".csv");
        Files.writeString(file,
                          "name\n\"\"\n");

        try (var stream = CsvStreamBuilder.of(file.toFile(),
                                              ",")
                                          .withoutTypeConversion()
                                          .get()) {
            List<MyRecord> records = stream.toList();
            Assertions.assertEquals(1,
                                    records.size());
            Assertions.assertEquals("",
                                    records.get(0).getStr("name"));
        }
    }
}
