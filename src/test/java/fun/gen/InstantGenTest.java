package fun.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tag("stats")
class InstantGenTest {

    @Test
    void shouldBiasTowardInterestingEpochSecondsWhenUsingBiasedDefault() {

        Map<Instant, Long> counts = TestFun.generate(100000,
                                                     InstantGen.biased());

        List<Instant> problematic = Stream.of(0L,
                                              Instant.MAX.getEpochSecond(),
                                              Instant.MIN.getEpochSecond(),
                                              (long) Integer.MAX_VALUE,
                                              (long) Integer.MIN_VALUE,
                                              0L)
                                          .map(Instant::ofEpochSecond)
                                          .collect(Collectors.toList());

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

    @Test
    void shouldBiasTowardInterestingEpochSecondsWhenUsingBiasedInterval() {

        Map<Instant, Long> counts = TestFun.generate(100000,
                                                     InstantGen.biased(Integer.MIN_VALUE - 2L,
                                                                       Integer.MAX_VALUE + 2L));

        List<Instant> problematic = Stream.of(0L,
                                              Integer.MIN_VALUE - 2L,
                                              Integer.MAX_VALUE + 2L,
                                              (long) Integer.MAX_VALUE,
                                              (long) Integer.MIN_VALUE,
                                              0L)
                                          .map(Instant::ofEpochSecond)
                                          .collect(Collectors.toList());

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }
}
