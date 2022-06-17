package fun.gen;

import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestCharGen {


    @Test
    public void testCharGen() {

        Map<Character, Long> countsLetters = TestFun.generate(100000,
                                                              CharGen.letter());

        Map<Character, Long> countsDigits = TestFun.generate(100000,
                                                             CharGen.digit());

        Map<Character, Long> countAlpha = TestFun.generate(1000000,
                                                           CharGen.alphabetic());

        TestFun.assertGeneratedValuesHaveSameProbability(countsLetters,
                                                         countsLetters.keySet(),
                                                         0.1);
        TestFun.assertGeneratedValuesHaveSameProbability(countsDigits,
                                                         countsDigits.keySet(),
                                                         0.1);

        TestFun.assertGeneratedValuesHaveSameProbability(countAlpha,
                                                         countAlpha.keySet(),
                                                         0.1);

    }
}
