package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * * Represents a generator of bytes.
 */
public final class BytesGen implements Gen<byte[]> {

    private final int length;

    private BytesGen(int length) {
        this.length = length;
    }


    public static Gen<byte[]> arbitrary(int minLength,
                                        int maxLength) {
        if (maxLength < minLength) throw new IllegalArgumentException("max < min");
        return seed -> genBytes(seed,
                                IntGen.arbitrary(minLength,
                                                 maxLength)
                                      .apply(seed));
    }


    public static Gen<byte[]> biased(int minLength,
                                     int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("min < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("max < min");

        List<Pair<Integer, Gen<? extends byte[]>>> gens = new ArrayList<>();
        gens.add(new Pair<>(1,
                            new BytesGen(minLength)));
        gens.add(new Pair<>(1,
                            new BytesGen(maxLength)));

        gens.add(new Pair<>(gens.size(),
                            arbitrary(minLength,
                                      maxLength)));

        return Combinators.freqList(gens);
    }

    private static Supplier<byte[]> genBytes(Random gen,
                                             Supplier<Integer> size) {
        return () -> {
            byte[] bytes = new byte[size.get()];
            gen.nextBytes(bytes);
            return bytes;
        };
    }

    @Override
    public Supplier<byte[]> apply(final Random gen) {
        requireNonNull(gen);
        return genBytes(gen,
                        () -> length);
    }

}
