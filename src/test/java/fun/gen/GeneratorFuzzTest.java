package fun.gen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

@Tag("fuzz")
class GeneratorFuzzTest {

    @Test
    void shouldRespectCombinationInvariantsWhenFuzzingDistinctAndDuplicateInputs() {
        for (long seed = 0; seed < 250; seed++) {
            Random random = new Random(seed);
            int size = 1 + random.nextInt(80);
            List<Integer> input = IntStream.range(0, size)
                                           .map(i -> random.nextInt((size / 2) + 1))
                                           .boxed()
                                           .toList();
            Set<Integer> distinct = new LinkedHashSet<>(input);
            int k = random.nextInt(distinct.size() + 1);

            Set<Integer> sample = Combinators.combinations(k, input).sample(seed).get();

            Assertions.assertEquals(k,
                                    sample.size(),
                                    "seed=" + seed + " produced invalid combination cardinality");
            Assertions.assertTrue(distinct.containsAll(sample),
                                  "seed=" + seed + " produced values outside input domain");
        }
    }

    @Test
    void shouldPreserveElementMultiplicityWhenFuzzingShuffleAcrossSeeds() {
        for (long seed = 0; seed < 300; seed++) {
            Random random = new Random(seed);
            int size = random.nextInt(200);
            List<Integer> input = IntStream.range(0, size)
                                           .map(i -> random.nextInt(30))
                                           .boxed()
                                           .toList();

            List<Integer> shuffled = Combinators.shuffle(input).sample(seed + 1_000).get();

            Assertions.assertEquals(input.size(),
                                    shuffled.size(),
                                    "seed=" + seed + " changed list size");
            Assertions.assertEquals(histogram(input),
                                    histogram(shuffled),
                                    "seed=" + seed + " changed element multiplicity");
        }
    }

    @Test
    void shouldHonorRecordSchemaInvariantsWhenFuzzingOptionalAndNullableConfigurations() {
        for (long seed = 0; seed < 120; seed++) {
            Random random = new Random(seed);
            long currentSeed = seed;
            int fieldCount = 20 + random.nextInt(30);

            MyRecordGen.Builder builder = MyRecordGen.builder();
            List<String> keys = new ArrayList<>();
            List<String> optionalKeys = new ArrayList<>();
            List<String> nullableKeys = new ArrayList<>();

            for (int i = 0; i < fieldCount; i++) {
                String key = "k" + i;
                keys.add(key);
                builder.field(key, IntGen.arbitrary(-1_000, 1_000));
                if (random.nextBoolean()) optionalKeys.add(key);
                if (random.nextBoolean()) nullableKeys.add(key);
            }

            MyRecordGen gen = builder.build()
                                     .withOptKeys(optionalKeys)
                                     .withNullValues(nullableKeys);

            Set<String> keyUniverse = new HashSet<>(keys);
            Set<String> nullableUniverse = new HashSet<>(nullableKeys);

            List<MyRecord> samples = gen.sample(120, seed).toList();
            for (MyRecord record : samples) {
                Map<String, ?> values = record.asMap();
                Assertions.assertTrue(keyUniverse.containsAll(values.keySet()),
                                      "seed=" + currentSeed + " produced unknown keys");

                values.forEach((key, value) -> {
                    if (!nullableUniverse.contains(key)) {
                        Assertions.assertNotNull(value,
                                                 "seed=" + currentSeed + " produced null for non-nullable key " + key);
                    }
                });
            }
        }
    }

    private static Map<Integer, Long> histogram(List<Integer> input) {
        Map<Integer, Long> counts = new HashMap<>();
        for (Integer value : input) {
            counts.compute(value, (k, old) -> old == null ? 1L : old + 1L);
        }
        return counts;
    }
}
