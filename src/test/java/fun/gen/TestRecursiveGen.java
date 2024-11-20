package fun.gen;

import org.junit.jupiter.api.Test;

import java.util.List;

public class TestRecursiveGen {

    @Test

    public void testRecordGen() {


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
                .forEach(System.out::println);


    }

    @Test
    public void testListGen() {


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
                .forEach(System.out::println
                );


    }


}
