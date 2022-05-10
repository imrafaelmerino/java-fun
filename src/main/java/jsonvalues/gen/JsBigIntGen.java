package jsonvalues.gen;

import fun.gen.*;
import fun.tuple.Pair;
import jsonvalues.JsBigInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsBigIntGen implements Gen<JsBigInt> {
    private final Gen<BigInteger> gen;

    public static Gen<JsBigInt> arbitrary(int min, int max) {
        return JsBigIntGen.of(BigIntGen.arbitrary(min, max));
    }

    public static Gen<JsBigInt> biased(int maxBits) {
        return JsBigIntGen.of(BigIntGen.biased(maxBits));
    }

    private JsBigIntGen(Gen<BigInteger> gen) {
        this.gen = gen;
    }

    public static Gen<JsBigInt> of(final Gen<BigInteger> gen) {
        return new JsBigIntGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBigInt> apply(RandomGenerator seed) {
        return gen.map(JsBigInt::of).apply(seed);
    }

}
