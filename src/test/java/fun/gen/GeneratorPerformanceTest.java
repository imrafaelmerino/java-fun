package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Tag("perf")
class GeneratorPerformanceTest {

    @Test
    void shouldGenerateNearEdgeCombinationsWithoutTimeoutWhenInputIsLarge() {
        List<Integer> input = IntStream.range(0, 4_000)
                                       .boxed()
                                       .toList();
        Set<Integer> universe = new HashSet<>(input);
        Gen<Set<Integer>> gen = Combinators.combinations(3_999, input);

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < 30; i++) {
                Set<Integer> sample = gen.sample(42L + i).get();
                Assertions.assertEquals(3_999, sample.size());
                Assertions.assertTrue(universe.containsAll(sample));
            }
        });
    }

    @Test
    void shouldGenerateManyOptionalNullableRecordsWithoutCombinatorialExplosionWhenSchemaIsWide() {
        MyRecordGen.Builder builder = MyRecordGen.builder();
        int fieldCount = 400;
        for (int i = 0; i < fieldCount; i++) {
            builder.field("k" + i, IntGen.arbitrary(0, 10_000));
        }

        MyRecordGen gen = builder.build()
                                 .withAllOptKeys()
                                 .withAllNullValues();

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            List<MyRecord> samples = gen.sample(2_000, 7L).toList();
            Assertions.assertEquals(2_000, samples.size());
            samples.forEach(record -> Assertions.assertTrue(record.size() <= fieldCount));
        });
    }

    @Test
    void shouldShuffleLargeListsWithinTimeBudgetWhenRepeatedManyTimes() {
        List<Integer> base = IntStream.range(0, 20_000)
                                      .boxed()
                                      .toList();
        Gen<List<Integer>> shuffler = Combinators.shuffle(base);

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            List<Integer> first = shuffler.sample(11L).get();
            List<Integer> second = shuffler.sample(12L).get();
            Assertions.assertEquals(base.size(), first.size());
            Assertions.assertEquals(base.size(), second.size());
            Assertions.assertNotEquals(new ArrayList<>(base), first);
            Assertions.assertNotEquals(first, second);
        });
    }
}
