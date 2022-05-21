package jsonvalues.gen;

import fun.gen.Gen;
import jsonvalues.JsArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestArrayGen {


    @Test
    public void arbitrary() {

        Gen<JsArray> gen = JsArrayGen.arbitrary(0,
                                                3)
                                     .apply(JsStrGen.letter());

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.size() < 4));

        Map<Integer, Long> count = TestFun.generate(100000,
                                                    gen.map(JsArray::size));


        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list(0,
                                                                      1,
                                                                      2,
                                                                      3),
                                                         0.1);
    }

    @Test
    public void biased() {

        Gen<JsArray> gen = JsArrayGen.biased(0,
                                             3)
                                     .apply(JsStrGen.letters(0,
                                                             10));

        int TIMES = 1000000;
        Assertions.assertTrue(gen.sample(TIMES)
                                 .allMatch(it -> it.size() < 4));

        Map<Integer, Long> count = TestFun.generate(TIMES,
                                                    gen.map(JsArray::size));

        System.out.println(count);

        Assertions.assertTrue(TestFun.isInMargin(count.get(0),
                                                 0.1).test(count.get(3)));
        Assertions.assertTrue(TestFun.isInMargin(count.get(1),
                                                 0.1).test(count.get(2)));



    }


}
