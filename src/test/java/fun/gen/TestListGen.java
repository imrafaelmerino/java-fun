package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestListGen {


    @Test
    public void arbitrary() {


        Gen<List<String>> gen = ListGen.<String>arbitrary(0,
                                                          3)
                                       .apply(StrGen.letters(0,
                                                             10));

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

        Gen<List<String>> gen = ListGen.<String>biased(0,
                                                       3)
                                       .apply(StrGen.letters(0,
                                                             10));

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.size() < 4));

        Map<Integer, Long> count = TestFun.generate(100000,
                                                    gen.map(List::size));

        TestFun.isInMargin(100000 / 3,
                           0.1).test(count.get(0));
        TestFun.isInMargin(100000 / 3,
                           0.1).test(count.get(3));
        TestFun.isInMargin(100000 / 3,
                           0.1).test(count.get(1) + count.get(2));


    }


}
