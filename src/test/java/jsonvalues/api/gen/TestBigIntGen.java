package jsonvalues.api.gen;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.IntStream;
import jsonvalues.JsBigInt;
import jsonvalues.gen.JsBigIntGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBigIntGen {


  @Test
  public void arbitraryBigInt() {
    Map<JsBigInt, Long> counts = TestFun.generate(100000,
                                                  JsBigIntGen.arbitrary(3));

    Assertions.assertTrue(IntStream.range(0,
                                          7)
                                   .allMatch(it -> counts.containsKey(JsBigInt.of(BigInteger.valueOf(it)))));
  }


}
