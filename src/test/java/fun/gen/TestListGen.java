package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestListGen {


    @Test
    public void arbitrary() {


        Gen<List<String>> gen = ListGen.arbitrary(StrGen.letters(0,
                                                                 10),
                                                  0,
                                                  3);

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.size() < 4));

        Map<Integer, Long> count = TestFun.generate(100000,
                                                    gen.map(List::size));

        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list(0,
                                                                      1,
                                                                      2,
                                                                      3),
                                                         0.05);
    }

    @Test
    public void biased() {

        Gen<List<String>> gen = ListGen.biased(StrGen.letters(0,
                                                              10),
                                               0,
                                               3);

        int TIMES = 10000;
        Assertions.assertTrue(gen.sample(TIMES)
                                 .allMatch(it -> it.size() < 4));

        Map<Integer, Long> count = TestFun.generate(TIMES,
                                                    gen.map(List::size));

        Assertions.assertTrue(TestFun.isInMargin(count.get(3),
                                                 0.1).test(count.get(0)));
        Assertions.assertTrue(TestFun.isInMargin(count.get(1),
                                                 0.1).test(count.get(2)));


    }


}
