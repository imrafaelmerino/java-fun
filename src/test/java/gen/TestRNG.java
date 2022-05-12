package gen;

import fun.gen.*;
import fun.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestRNG {
    private static <I> void assertGeneratedValuesHaveSameProbability(Map<I, Long> counts,
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
        return times -> Math.abs(times - expected) < margin * expected;
    }

    private static <I> Map<I, Long> generate(int times,
                                             Gen<I> generator) {
        var results = new HashMap<I, Long>();

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

    @Test
    public void testBigInt() {
        int bits = 5;

        var counts = generate(100000,
                 BigIntGen.arbitrary(bits,bits+1));

        var values = IntStream.range(0,
                1 << bits).mapToObj(BigInteger::valueOf).toList();


        assertGeneratedValuesHaveSameProbability(counts,
                values,
                0.05);


    }

    @Test
    public void freqCombinator() {

        var gen = Combinators.freq(new Pair<>(1,
                        Gen.cons(1)),
                new Pair<>(1,
                        Gen.cons(2)),
                new Pair<>(1,
                        Gen.cons(3)),
                new Pair<>(1,
                        Gen.cons(4)),
                new Pair<>(1,
                        Gen.cons(5)));

        var counts = generate(100000,
                gen);

        assertGeneratedValuesHaveSameProbability(counts,
                List.of(1,
                        2,
                        3,
                        4,
                        5),
                0.05);
    }


    @Test
    public void biasedInt() {

        var counts = generate(100000,
                IntGen.biased);

        var problematic = List.of(Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                (int) Short.MAX_VALUE,
                (int) Short.MIN_VALUE,
                (int) Byte.MAX_VALUE,
                (int) Byte.MIN_VALUE,
                0);

        assertGeneratedValuesHaveSameProbability(counts,
                problematic,
                0.05);


    }

    @Test
    public void biasedLongInterval() {

        var counts = generate(100000,
                LongGen.biased(-100000000000000000L,
                        100000000000000000L + 1L));

        var problematic = List.of(
                100000000000000000L,
                -100000000000000000L,
                (long) Integer.MAX_VALUE,
                (long) Short.MAX_VALUE,
                (long) Byte.MAX_VALUE,
                (long) Integer.MIN_VALUE,
                (long) Short.MIN_VALUE,
                (long) Byte.MIN_VALUE,
                0L);

        assertGeneratedValuesHaveSameProbability(counts,
                problematic,
                0.05);


    }

    @Test
    public void biasedInstant() {

        var counts = generate(100000, InstantGen.biased);

        var problematic = Stream.of(0L,
                Instant.MAX.getEpochSecond(),
                Instant.MIN.getEpochSecond(),
                (long) Integer.MAX_VALUE,
                (long) Integer.MIN_VALUE,
                0L).map(Instant::ofEpochMilli).toList();

        assertGeneratedValuesHaveSameProbability(counts, problematic, 0.05);


    }

    @Test
    public void biasedInstantInterval() {

        var counts = generate(100000, InstantGen.biased( Integer.MIN_VALUE-2L,Integer.MAX_VALUE+2L));

        var problematic = Stream.of(0L,
                Integer.MIN_VALUE-2L,
                Integer.MAX_VALUE+1L,
                (long) Integer.MAX_VALUE,
                (long) Integer.MIN_VALUE,
                0L).map(Instant::ofEpochMilli).toList();

        assertGeneratedValuesHaveSameProbability(counts,
                problematic,
                0.05);


    }

    @Test
    public void biasedIntInterval() {

        var counts = generate(100000,
                IntGen.biased(-1000000000,
                        1000000001));

        var problematic = List.of(1000000000,
                -1000000000,
                (int) Short.MAX_VALUE,
                (int) Byte.MAX_VALUE,
                (int) Short.MIN_VALUE,
                (int) Byte.MIN_VALUE,
                0);

        assertGeneratedValuesHaveSameProbability(counts,
                problematic,
                0.05);
    }


    @Test
    public void biasedLong() {

        var counts = generate(100000,
                LongGen.biased);

        var problematic = List.of(Long.MAX_VALUE,
                Long.MAX_VALUE,
                (long) Integer.MAX_VALUE,
                (long) Integer.MIN_VALUE,
                (long) Short.MAX_VALUE,
                (long) Short.MIN_VALUE,
                (long) Byte.MAX_VALUE,
                (long) Byte.MIN_VALUE,
                0L);

        assertGeneratedValuesHaveSameProbability(counts,
                problematic,
                0.05);


    }


    @Test
    public void testBooleanGen() {

        var counts = generate(100000,
                BoolGen.arbitrary);

        var values = List.of(true,
                false);

        assertGeneratedValuesHaveSameProbability(counts,
                values,
                0.05);

    }


    @Test
    public void testCharGen() {

        var countsLetters = generate(100000,
                CharGen.letter);

        var countsDigits = generate(100000,
                CharGen.digit);

        var countAlpha = generate(1000000,
                CharGen.alpha);

        assertGeneratedValuesHaveSameProbability(countsLetters,
                countsLetters.keySet(),
                0.05);
        assertGeneratedValuesHaveSameProbability(countsDigits,
                countsDigits.keySet(),
                0.05);

        assertGeneratedValuesHaveSameProbability(countAlpha,
                countAlpha.keySet(),
                0.05);

    }


    @Test
    public void testStringGen() {

        var countsLetter = generate(100000,
                StrGen.letter);

        var countsDigit = generate(100000,
                StrGen.digit);

        var countsDigits = generate(1000000,
                StrGen.digits(2));

        var countAlpha = generate(10000000,
                StrGen.alpha(2));

        var countsLetters = generate(1000000,
                StrGen.letters(2));


        assertGeneratedValuesHaveSameProbability(countsLetter,
                countsLetter.keySet(),
                0.1);
        assertGeneratedValuesHaveSameProbability(countsDigit,
                countsDigit.keySet(),
                0.1);
        assertGeneratedValuesHaveSameProbability(countAlpha,
                countAlpha.keySet(),
                0.1);
        assertGeneratedValuesHaveSameProbability(countsDigits,
                countsDigits.keySet(),
                0.1);

        assertGeneratedValuesHaveSameProbability(countsLetters,
                countsLetters.keySet(),
                0.1);


    }


    @Test
    public void testRecordGen() {

        var gen = RecordGen.of("a",
                IntGen.arbitrary(0,
                        11),
                "b",
                StrGen.letter,
                "c",
                BoolGen.arbitrary);


        var counts = generate(1000000,
                gen);


        assertGeneratedValuesHaveSameProbability(counts,
                counts.keySet(),
                0.1);

    }

    @Test
    public void testRecordGenWithOptionals() {

        var gen = RecordGen.of("a",
                IntGen.arbitrary(0,
                        100000),
                "b",
                StrGen.letter,
                "c",
                StrGen.digits(10));

        var map = generate(1000000,
                gen.setOptionals("a",
                        "b"));
        var allKeys =
                TestRNG.countKeys(map,
                        key -> key.containsKey("a") && key.containsKey("b"));

        var someKeyRemoved =
                TestRNG.countKeys(map,
                        key -> !key.containsKey("a") || !key.containsKey("b"));

        //50%
        Predicate<Long> inMargin = isInMargin(map.size() / 2,
                0.05);

        Assertions.assertTrue(inMargin.test(allKeys));
        Assertions.assertTrue(inMargin.test(someKeyRemoved));


        var aRemoved =
                TestRNG.countKeys(map,
                        key -> !key.containsKey("a") && key.containsKey("b"));

        var bRemoved = TestRNG.countKeys(map,
                key -> !key.containsKey("b") && key.containsKey("a"));


        var allRemoved = TestRNG.countKeys(map,
                key -> !key.containsKey("b") && key.containsKey("a"));

        Predicate<Long> x = isInMargin(someKeyRemoved / 3,
                0.05);
        Assertions.assertTrue(x.test(aRemoved));
        Assertions.assertTrue(x.test(bRemoved));
        Assertions.assertTrue(x.test(allRemoved));


    }

    @Test
    public void testRecordGenWithNullable() {

        var gen = RecordGen.of("a",
                IntGen.arbitrary(0,
                        100000),
                "b",
                StrGen.letter,
                "c",
                StrGen.digits(10));

        var map = generate(1000000,
                gen.setNullables("a",
                        "b"));
        var noneNull =
                TestRNG.countKeys(map,
                        key -> key.get("a") != null && key.get("b") != null);

        var someNull =
                TestRNG.countKeys(map,
                        key -> key.get("a") == null || key.get("b") == null);

        //50%
        Predicate<Long> inMargin = isInMargin(map.size() / 2,
                0.05);

        Assertions.assertTrue(inMargin.test(noneNull));
        Assertions.assertTrue(inMargin.test(someNull));


        var aNull =
                TestRNG.countKeys(map,
                        key -> key.containsKey("a") && key.get("a") == null && key.get("b") != null);

        var bNull = TestRNG.countKeys(map,
                key -> key.get("a") != null && key.containsKey("b") && key.get("b") == null);


        var allNull = TestRNG.countKeys(map,
                key -> key.containsKey("a")
                        && key.get("a") == null
                        && key.containsKey("b")
                        && key.get("b") == null);

        Predicate<Long> x = isInMargin(someNull / 3,
                0.05);
        Assertions.assertTrue(x.test(aNull));
        Assertions.assertTrue(x.test(bNull));
        Assertions.assertTrue(x.test(allNull));


    }

    private static <I> long countKeys(Map<I, Long> map,
                                      Predicate<I> predicate) {
        return map.keySet()
                .stream()
                .filter(predicate)
                .count();
    }
}
