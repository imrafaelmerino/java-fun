package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class StrGenTest {

    @Test
    void shouldGenerateStringsWithinLengthRangeWhenUsingArbitrary() {


        Map<Integer, Long> count = TestFun.generate(10000000,
                                                    StrGen.arbitrary(0,
                                                                     3)
                                                          .map(String::length)
        );
        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         count.keySet(),
                                                         0.15);
    }

    @Test
    void shouldBiasTowardBoundaryLengthsWhenUsingBiased() {

        Assertions.assertTrue(StrGen.biased(0,
                                            0)
                                    .sample(1000)
                                    .allMatch(String::isEmpty));

        Assertions.assertTrue(StrGen.biased(0,
                                            2).sample(1000)
                                    .allMatch(it -> it.length() < 3));

        Map<Integer, Long> count = TestFun.generate(10000000,
                                                    StrGen.biased(0,
                                                                  3).map(String::length));


        Assertions.assertTrue(count.get(0) > count.get(1));
        Assertions.assertTrue(count.get(0) > count.get(2));
        Assertions.assertTrue(count.get(3) > count.get(1));
        Assertions.assertTrue(count.get(3) > count.get(2));


    }


    @Test
    void shouldGenerateDigitOnlyStringsWhenUsingDigits() {

        Assertions.assertTrue(StrGen.digits(0,
                                            2)
                                    .sample(100000)
                                    .allMatch(it -> it.isEmpty() ||
                                            (it.length() < 3 && Integer.parseInt(it) < 100)));
    }

    @Test
    void shouldGenerateAlphanumericStringsWhenUsingAlphanumeric() {
        Assertions.assertTrue(
                StrGen.alphanumeric(0,
                                    2)
                      .sample(100000)
                      .allMatch(str ->
                                        str.isEmpty() ||
                                                (str.length() < 3 &&
                                                        str.chars().allMatch(c -> Character.isDigit(c) || Character.isAlphabetic(c)))));
    }

    @Test
    void shouldGenerateAlphabeticStringsWhenUsingAlphabetic() {
        Assertions.assertTrue(
                StrGen.alphabetic(0,
                                  2)
                      .sample(100000)
                      .allMatch(str ->
                                        str.isEmpty() ||
                                                (str.length() < 3 && str.chars().allMatch(Character::isAlphabetic))));
    }


    @Test
    void shouldGenerateLetterStringsWhenUsingLetters() {
        Assertions.assertTrue(StrGen.letters(0,
                                             2)
                                    .sample(100000)
                                    .allMatch(it -> it.isEmpty() || (it.length() < 3 && it.chars().allMatch(Character::isLetter))));
    }

    @Test
    void shouldGenerateAsciiStringsWhenUsingAscii() {
        Assertions.assertTrue(StrGen.ascii(0,
                                           2)
                                    .sample(100000)
                                    .allMatch(it -> it.isEmpty() ||
                                            (it.length() < 3 &&
                                                    it.chars()
                                                      .allMatch(ch ->
                                                                        ch <= ((int) '\u007f') &&
                                                                                ch >= ((int) '\u0000')))));
    }

    @Test
    void shouldGenerateUniformSingleCharacterOutputsWhenLengthIsOneAcrossFactories() {

        Map<String, Long> countsLetter = TestFun.generate(1000000,
                                                          StrGen.letters(1,
                                                                         1));

        Map<String, Long> countsDigit = TestFun.generate(1000000,
                                                         StrGen.digits(1,
                                                                       1));

        Map<String, Long> countsAscii = TestFun.generate(1000000,
                                                         StrGen.ascii(1,
                                                                      1));


        Map<String, Long> countAlpha = TestFun.generate(10000000,
                                                        StrGen.alphabetic(1,
                                                                          1));

        TestFun.assertGeneratedValuesHaveSameProbability(countsAscii,
                                                         countsAscii.keySet(),
                                                         0.1);

        TestFun.assertGeneratedValuesHaveSameProbability(countsLetter,
                                                         countsLetter.keySet(),
                                                         0.1);
        TestFun.assertGeneratedValuesHaveSameProbability(countsDigit,
                                                         countsDigit.keySet(),
                                                         0.1);
        TestFun.assertGeneratedValuesHaveSameProbability(countAlpha,
                                                         countAlpha.keySet(),
                                                         0.1);


    }
}
