package fun.gen;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSetGen {

    @Test
    public void testNotAbleToGenerateSetOfSize() {
        SetGen<String> gen =
                SetGen.ofN(StrGen.letters(1,
                                          1),
                           5000)
                      .withMaxTries(5001);

        Assertions.assertThrows(RuntimeException.class,
                                () -> gen.sample(10).peek(System.out::println).count());
    }

}
