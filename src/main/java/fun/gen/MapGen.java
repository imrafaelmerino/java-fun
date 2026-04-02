package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator for maps with keys of type {@code K} and values of type {@code V}. This class provides methods to generate maps with arbitrary or biased key-value pairs.
 *
 * @param <K> The type of keys in the generated map.
 * @param <V> The type of values in the generated map.
 */
public final class MapGen<K, V> implements Gen<Map<K, V>> {
    private final Gen<K> keyGen;
    private final Gen<V> valueGen;
    private final int size;
    private final int maxTries;

    private final SplitGen splitGen;

    private MapGen(final Gen<K> keyGen,
                   final Gen<V> valueGen,
                   final int size,
                   final int maxTries) {
        if (size < 0) throw new IllegalArgumentException("size < 0");
        if (maxTries < size) throw new IllegalArgumentException("maxTries < size");
        this.keyGen = requireNonNull(keyGen);
        this.valueGen = requireNonNull(valueGen);
        this.size = size;
        this.maxTries = maxTries;
        this.splitGen = SplitGen.DEFAULT;
    }

    /**
     * Creates a MapGen instance with the specified key and value generators and target map size.
     *
     * @param <K>      The type of keys in the generated map.
     * @param <V>      The type of values in the generated map.
     * @param keyGen   A generator for map keys.
     * @param valueGen A generator for map values.
     * @param size     The desired size of the generated map.
     * @return A MapGen instance with the specified generators and size.
     */
    public static <K, V> MapGen<K, V> of(final Gen<K> keyGen,
                                         final Gen<V> valueGen,
                                         final int size) {
        return new MapGen<>(keyGen,
                            valueGen,
                            size,
                            defaultMaxTries(size));
    }

    /**
     * Creates a MapGen instance that attempts to generate exactly {@code size} entries.
     * This is an alias of {@link #of(Gen, Gen, int)} for consistency with other generators.
     *
     * @param <K>      The type of keys in the generated map.
     * @param <V>      The type of values in the generated map.
     * @param keyGen   A generator for map keys.
     * @param valueGen A generator for map values.
     * @param size     The desired size of the generated map.
     * @return A MapGen instance with the specified generators and size.
     */
    public static <K, V> MapGen<K, V> ofN(final Gen<K> keyGen,
                                          final Gen<V> valueGen,
                                          final int size) {
        return of(keyGen,
                  valueGen,
                  size);
    }

    /**
     * Returns a generator of maps whose size is selected uniformly from [{@code minSize}, {@code maxSize}] (inclusive).
     * Each generated map contains unique keys.
     *
     * @param <K>      the key type
     * @param <V>      the value type
     * @param keyGen   generator for keys
     * @param valueGen generator for values
     * @param minSize  minimum map size (inclusive)
     * @param maxSize  maximum map size (inclusive)
     * @return generator for maps with variable size in the given range
     */
    public static <K, V> Gen<Map<K, V>> arbitrary(final Gen<K> keyGen,
                                                  final Gen<V> valueGen,
                                                  final int minSize,
                                                  final int maxSize) {
        if (minSize < 0) throw new IllegalArgumentException("minSize < 0");
        if (maxSize < minSize) throw new IllegalArgumentException("maxSize < minSize");
        requireNonNull(keyGen);
        requireNonNull(valueGen);

        return seed -> {
            Supplier<Integer> sizeSupplier = IntGen.arbitrary(minSize,
                                                              maxSize)
                                                  .apply(SplitGen.DEFAULT.apply(seed));
            Supplier<K> keys = keyGen.apply(SplitGen.DEFAULT.apply(seed));
            Supplier<V> values = valueGen.apply(SplitGen.DEFAULT.apply(seed));
            return () -> {
                int size = sizeSupplier.get();
                return generateMap(keys,
                                   values,
                                   size,
                                   defaultMaxTries(size));
            };
        };
    }

    /**
     * Returns a biased generator of maps with sizes in [{@code minSize}, {@code maxSize}] (inclusive).
     * It increases the chance of seeing boundary sizes ({@code minSize}, {@code maxSize}) while still sampling interior sizes.
     *
     * @param <K>      the key type
     * @param <V>      the value type
     * @param keyGen   generator for keys
     * @param valueGen generator for values
     * @param minSize  minimum map size (inclusive)
     * @param maxSize  maximum map size (inclusive)
     * @return biased generator for maps in the given size range
     */
    public static <K, V> Gen<Map<K, V>> biased(final Gen<K> keyGen,
                                               final Gen<V> valueGen,
                                               final int minSize,
                                               final int maxSize) {
        if (minSize < 0) throw new IllegalArgumentException("minSize < 0");
        if (maxSize < minSize) throw new IllegalArgumentException("maxSize < minSize");
        requireNonNull(keyGen);
        requireNonNull(valueGen);

        List<Pair<Integer, Gen<? extends Map<K, V>>>> gens = new ArrayList<>();
        gens.add(Pair.of(1,
                         MapGen.of(keyGen,
                                   valueGen,
                                   minSize)));
        if (maxSize != minSize) {
            gens.add(Pair.of(1,
                             MapGen.of(keyGen,
                                       valueGen,
                                       maxSize)));
        }
        gens.add(Pair.of(gens.size(),
                         MapGen.arbitrary(keyGen,
                                          valueGen,
                                          minSize,
                                          maxSize)));
        return Combinators.freqList(gens);
    }

    /**
     * Sets the maximum number of attempts to generate the map of the specified size. This method allows you to control the maximum number of iterations or try to generate a map that meets the desired size criterion.
     *
     * @param tries The maximum number of attempts to generate the map.
     * @return A new instance of MapGen with the specified maximum tries.
     * @throws IllegalArgumentException If {@code tries} is less than {@code size}.
     */
    public MapGen<K, V> withMaxTries(final int tries) {
        return new MapGen<>(keyGen,
                            valueGen,
                            size,
                            tries);
    }

    @Override
    public Supplier<Map<K, V>> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        final Supplier<K> keys = keyGen.apply(splitGen.apply(requireNonNull(gen)));
        final Supplier<V> values = valueGen.apply(splitGen.apply(gen));
        return () -> generateMap(keys,
                                 values,
                                 size,
                                 maxTries);
    }

    private static int defaultMaxTries(final int size) {
        long tries = (long) size * 100L;
        return tries > Integer.MAX_VALUE
                ? Integer.MAX_VALUE
                : (int) tries;
    }

    private static <K, V> Map<K, V> generateMap(final Supplier<K> keys,
                                                final Supplier<V> values,
                                                final int size,
                                                final int maxTries) {
        HashMap<K, V> map = new HashMap<>();
        if (size == 0) {
            return map;
        }
        int tries = 0;
        do {
            map.put(keys.get(),
                    values.get());
            tries++;
        } while (tries != maxTries && map.size() < size);
        if (map.size() < size) {
            throw new GenerationExhaustedException(String.format(
                    "Couldn't generate map of %s elements after %s tries",
                    size,
                    maxTries
            ));
        }
        return map;
    }
}
