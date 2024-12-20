package fun.gen;

import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestFun {
    static <I> void assertGeneratedValuesHaveSameProbability(Map<I, Long> counts,
                                                             Collection<I> values,
                                                             double errorMargin) {
        if (errorMargin < 0.0) throw new IllegalArgumentException("errorMargin < 0");
        if (errorMargin > 1.0) throw new IllegalArgumentException("errorMargin > 1");

        System.out.println("error of margin specified: " + errorMargin);
        System.out.println(values);
        List<Long> valueCounts = values.stream().map(key -> {
            if (!counts.containsKey(key))
                throw new RuntimeException(key + " was not generated");
            return counts.get(key);
        }).collect(Collectors.toList());
        long expected = avg(valueCounts);
        System.out.println("expected number of times: " + expected);

        final Predicate<Long> isOk = isInMargin(expected,
                                                errorMargin);
        values.forEach(val -> {
            System.out.println("generated value " + val);
            System.out.println("real number of times generated: " + counts.get(val));
            Assertions.assertTrue(isOk.test(counts.get(val)));
        });
    }

    /**
     * @param expected the expected number of generations
     * @param margin   the tolerable margin of error in % [0,1]
     * @return a predicate that is true if the number of times a value was generated is ok
     */
    static Predicate<Long> isInMargin(long expected,
                                      double margin) {
        return times -> {
            System.out.printf("times - expected = %s - %s = %s\n",
                              times,
                              expected,
                              times - expected);
            System.out.printf("times - expected < margin * expected = %s * %s = %s is %s\n",
                              margin,
                              expected,
                              margin * expected,
                              (times - expected) < margin * expected);
            return Math.abs(times - expected) < margin * expected;
        };
    }

    static <I> Map<I, Long> generate(int times,
                                     Gen<I> generator) {
        HashMap<I, Long> results = new HashMap<>();

        generator.sample(times)
                 .forEach(countOccurrences(results));

        return results;
    }


    private static <I> Consumer<I> countOccurrences(Map<I, Long> results) {
        return value -> results.compute(value,
                                        (k, old) -> old == null ?
                                                    1L :
                                                    old + 1L);
    }

    private static long avg(List<Long> xs) {
        return xs.stream()
                 .reduce(Long::sum)
                 .get() / xs.size();
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <T> List<T> list(T... elems) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result,
                           elems);
        return result;
    }
}
