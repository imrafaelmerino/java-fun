package fun.gen;

import java.util.HashMap;
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
                            size * 100);
    }

    /**
     * Sets the maximum number of attempts to generate the map of the specified size. This method allows you to control the maximum number of iterations or try to generate a map that meets the desired size criterion.
     *
     * @param tries The maximum number of attempts to generate the map.
     * @return A new instance of MapGen with the specified maximum tries.
     * @throws IllegalArgumentException If the provided number of tries is less than the current size of the map to be generated, or if the number of tries is negative.
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
        return () -> {
            HashMap<K, V> map = new HashMap<>();
            int tries = 0;
            do {
                map.put(keys.get(),
                        values.get());
                tries++;
            } while (tries != maxTries && map.size() < size);
            if (map.size() < size)
                throw new RuntimeException(String.format("Couldn't generate map of %s elements after %s tries",
                                                         size,
                                                         maxTries));
            return map;
        };
    }
}
