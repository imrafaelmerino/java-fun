package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

class StrGenTest {

    private static final int SMALL_SAMPLE_SIZE = 1_000;
    private static final int CONTENT_SAMPLE_SIZE = 100_000;
    private static final int LARGE_STATS_SAMPLE_SIZE = 10_000_000;
    private static final int MEDIUM_STATS_SAMPLE_SIZE = 1_000_000;

    @Nested
    class LengthAndDistributionTests {

        @Test
        void shouldGenerateStringsWithinLengthRangeWhenUsingArbitrary() {
            TestFun.assumeStatsEnabled();

            Map<Integer, Long> count = TestFun.generate(LARGE_STATS_SAMPLE_SIZE,
                                                        StrGen.arbitrary(0, 3)
                                                              .map(String::length));
            TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                             count.keySet(),
                                                             0.15);
        }

        @Test
        void shouldBiasTowardBoundaryLengthsWhenUsingBiased() {
            Assertions.assertTrue(StrGen.biased(0, 0)
                                       .sample(SMALL_SAMPLE_SIZE)
                                       .allMatch(String::isEmpty));
            Assertions.assertTrue(StrGen.biased(0, 2)
                                       .sample(SMALL_SAMPLE_SIZE)
                                       .allMatch(it -> it.length() < 3));

            TestFun.assumeStatsEnabled();
            Map<Integer, Long> count = TestFun.generate(LARGE_STATS_SAMPLE_SIZE,
                                                        StrGen.biased(0, 3)
                                                              .map(String::length));
            Assertions.assertTrue(count.get(0) > count.get(1));
            Assertions.assertTrue(count.get(0) > count.get(2));
            Assertions.assertTrue(count.get(3) > count.get(1));
            Assertions.assertTrue(count.get(3) > count.get(2));
        }

        @Test
        void shouldGenerateUniformSingleCharacterOutputsWhenLengthIsOneAcrossFactories() {
            TestFun.assumeStatsEnabled();

            Map<String, Long> countsLetter = TestFun.generate(MEDIUM_STATS_SAMPLE_SIZE,
                                                              StrGen.letters(1, 1));
            Map<String, Long> countsDigit = TestFun.generate(MEDIUM_STATS_SAMPLE_SIZE,
                                                             StrGen.digits(1, 1));
            Map<String, Long> countsAscii = TestFun.generate(MEDIUM_STATS_SAMPLE_SIZE,
                                                             StrGen.ascii(1, 1));
            Map<String, Long> countsAlphabetic = TestFun.generate(LARGE_STATS_SAMPLE_SIZE,
                                                                  StrGen.alphabetic(1, 1));

            TestFun.assertGeneratedValuesHaveSameProbability(countsAscii,
                                                             countsAscii.keySet(),
                                                             0.1);
            TestFun.assertGeneratedValuesHaveSameProbability(countsLetter,
                                                             countsLetter.keySet(),
                                                             0.1);
            TestFun.assertGeneratedValuesHaveSameProbability(countsDigit,
                                                             countsDigit.keySet(),
                                                             0.1);
            TestFun.assertGeneratedValuesHaveSameProbability(countsAlphabetic,
                                                             countsAlphabetic.keySet(),
                                                             0.1);
        }
    }

    @Nested
    class CharacterSetTests {

        @Test
        void shouldGenerateDigitOnlyStringsWhenUsingDigits() {
            Assertions.assertTrue(StrGen.digits(0, 2)
                                       .sample(CONTENT_SAMPLE_SIZE)
                                       .allMatch(it -> it.isEmpty()
                                               || (it.length() < 3 && Integer.parseInt(it) < 100)));
        }

        @Test
        void shouldGenerateAlphanumericStringsWhenUsingAlphanumeric() {
            Assertions.assertTrue(StrGen.alphanumeric(0, 2)
                                       .sample(CONTENT_SAMPLE_SIZE)
                                       .allMatch(str -> str.isEmpty()
                                               || (str.length() < 3
                                               && str.chars().allMatch(c -> Character.isDigit(c)
                                                       || Character.isAlphabetic(c)))));
        }

        @Test
        void shouldGenerateAlphabeticStringsWhenUsingAlphabetic() {
            Assertions.assertTrue(StrGen.alphabetic(0, 2)
                                       .sample(CONTENT_SAMPLE_SIZE)
                                       .allMatch(str -> str.isEmpty()
                                               || (str.length() < 3
                                               && str.chars().allMatch(Character::isAlphabetic))));
        }

        @Test
        void shouldGenerateLetterStringsWhenUsingLetters() {
            Assertions.assertTrue(StrGen.letters(0, 2)
                                       .sample(CONTENT_SAMPLE_SIZE)
                                       .allMatch(it -> it.isEmpty()
                                               || (it.length() < 3 && it.chars().allMatch(Character::isLetter))));
        }

        @Test
        void shouldGenerateAsciiStringsWhenUsingAscii() {
            Assertions.assertTrue(StrGen.ascii(0, 2)
                                       .sample(CONTENT_SAMPLE_SIZE)
                                       .allMatch(it -> it.isEmpty()
                                               || (it.length() < 3
                                               && it.chars().allMatch(ch -> ch <= 0x7f && ch >= 0x00))));
        }
    }
}
