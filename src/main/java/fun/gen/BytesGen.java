package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * * Represents a generator of bytes.
 */
public final class BytesGen implements Gen<byte[]> {

    private final int length;

    private BytesGen(int length) {
        this.length = length;
    }


    public static Gen<byte[]> arbitrary(int min,
                                        int max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return seed -> genBytes(seed,
                                IntGen.arbitrary(min,
                                                 max).apply(seed));
    }


    public static Gen<byte[]> biased(int min,
                                     int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");

        var gens = new ArrayList<Pair<Integer, Gen<? extends byte[]>>>();
        gens.add(new Pair<>(1,
                            new BytesGen(min)));
        gens.add(new Pair<>(1,
                            new BytesGen(max - 1)));
        if (min == 0)
            gens.add(new Pair<>(1,
                                Gen.cons(() -> new byte[0])));
        gens.add(new Pair<>(gens.size(),
                            arbitrary(min,
                                      max)));

        return Combinators.freqList(gens);
    }

    private static Supplier<byte[]> genBytes(RandomGenerator gen,
                                             Supplier<Integer> size) {
        return () -> {
            byte[] bytes = new byte[size.get()];
            gen.nextBytes(bytes);
            return bytes;
        };
    }

    @Override
    public Supplier<byte[]> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);
        return genBytes(gen,
                        () -> length);
    }

}
