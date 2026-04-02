package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RecursiveGenTest {

    @Test

    void shouldGenerateRecursiveRecordsWhenSchemaHasOptionalParent() {


        Gen<MyRecord> recordGen = NamedGen.of("person",
                                              MyRecordGen.of("age",
                                                             IntGen.arbitrary(16,
                                                                          100),
                                                             "name",
                                                             StrGen.alphabetic(10,
                                                                           50),
                                                             "father",
                                                             NamedGen.of("person")
                                                     )
                                                         .withNullValues("father"));
        recordGen
                .sample(1)
                .forEach(record -> {
                    Assertions.assertTrue(record.getOptionalInt("age")
                                                .isPresent());
                    Assertions.assertTrue(record.getOptionalString("name")
                                                .isPresent());
                });


    }

    @Test
    void shouldGenerateRecursiveRecordListsWhenUsingBoundedSizes() {


        Gen<MyRecord> recordGen =
                NamedGen.of("person-1",
                            MyRecordGen.of("age",
                                           IntGen.arbitrary(16,
                                                          100),
                                           "name",
                                           StrGen.alphabetic(10,
                                                           50),
                                           "father",
                                           NamedGen.of("person-1")
                                     )
                                       .withOptKeys("father"));

        Gen<List<MyRecord>> listGen = ListGen.arbitrary(recordGen,
                                                        1,
                                                        10);
        listGen
                .sample(100)
                .forEach(records -> {
                    Assertions.assertTrue(records.size() >= 1 && records.size() <= 10);
                    Assertions.assertTrue(records.stream()
                                                 .allMatch(record -> record.getOptionalInt("age")
                                                                           .isPresent()
                                                         && record.getOptionalString("name")
                                                                  .isPresent()));
                });


    }


}
