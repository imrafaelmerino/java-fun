package fun.gen;

import fun.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class BigIntGen implements Gen<BigInteger> {

    private final int nBits;

    private BigIntGen(int nBits) {
        if (nBits < 0) throw new IllegalArgumentException("nBits < 0");
        this.nBits = nBits;
    }

    public static Gen<BigInteger> arbitrary(int min,
                                            int max) {
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return IntGen.arbitrary(min,
                                max).then(BigIntGen::new);
    }

    public static Gen<BigInteger> biased(int maxBits) {
        if (maxBits <= 65) throw new IllegalArgumentException("maxBits <= 65");
        var gens = new ArrayList<Pair<Integer, Gen<? extends BigInteger>>>();
        gens.add(new Pair<>(1,
                            LongGen.biased.map(BigInteger::valueOf)));
        gens.add(new Pair<>(1,
                            arbitrary(65,
                                      maxBits)));
        return Combinators.freqList(gens);
    }

    @Override
    @SuppressWarnings("java:S2142")
    public Supplier<BigInteger> apply(final RandomGenerator gen) {
        requireNonNull(gen);
        final Random rnd = new Random() {
            @Override
            protected int next(int bits) {
                return gen.nextInt(bits);
            }
        };

        return () -> new BigInteger(nBits,
                                    rnd);
    }


}
