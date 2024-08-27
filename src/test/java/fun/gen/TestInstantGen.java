package fun.gen;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestInstantGen {

    public static void main(String[] args) {
        Gen<Set<String>> permutations = Combinators.subsets(Arrays.asList("a",
                                                                          "b",
                                                                          "c"));

        Set<Set<String>> r = new HashSet<>();

        permutations.sample(10000).forEach(r::add);

        System.out.println(r);

    }

    @Test
    public void biasedInstant() {

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
    public void biasedInstantInterval() {

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
