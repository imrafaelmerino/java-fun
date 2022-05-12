package fun.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.Random;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of maps.
 */
public final class MapGen<K, V> implements Gen<Map<K, V>> {
    private final Gen<K> keyGen;
    private final Gen<V> valueGen;
    private final int size;
    private final int maxTries;

    private final SplitGen splitGen;

    public MapGen(final Gen<K> keyGen,
                  final Gen<V> valueGen,
                  final int size,
                  final int maxTries) {
        if (size < 0) throw new IllegalArgumentException("size < 0");
        if (maxTries < 0) throw new IllegalArgumentException("maxTries < 0");
        this.keyGen = requireNonNull(keyGen);
        this.valueGen = requireNonNull(valueGen);
        this.size = size;
        this.maxTries = maxTries;
        this.splitGen = SplitGen.DEFAULT;
    }

    public MapGen(final Gen<K> keyGen,
                  final Gen<V> valueGen,
                  final int size) {
        this(keyGen,
             valueGen,
             size,
             size * 100);
    }

    @Override
    public Supplier<Map<K, V>> apply(final Random gen) {
        requireNonNull(gen);
        final Supplier<K> keys = keyGen.apply(splitGen.apply(requireNonNull(gen)));
        final Supplier<V> values = valueGen.apply(splitGen.apply(gen));
        return () -> {
            HashMap<K, V> map = new HashMap<K, V>();
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
