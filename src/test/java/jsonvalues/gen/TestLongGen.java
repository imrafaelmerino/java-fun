package jsonvalues.gen;

import fun.gen.DoubleGen;
import fun.gen.LongGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLongGen {


    @Test
    public void test_interval() {

        long min = 0;
        long max = Integer.MAX_VALUE;
        Assertions.assertTrue(
                LongGen.arbitrary(min,
                                  max).then(n ->
                                                    JsLongGen.arbitrary(min,
                                                                        n))
                       .sample(100000).allMatch(it -> it.isLong(d -> d >= min && d <= max))
        );


        Assertions.assertTrue(
                LongGen.arbitrary(min,
                                  max).then(n ->
                                                    JsLongGen.biased(min,
                                                                        n))
                       .sample(100000).allMatch(it -> it.isLong(d -> d >= min && d <= max))
        );
    }
}