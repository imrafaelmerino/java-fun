package jsonvalues.gen;

<<<<<<< HEAD
import fun.gen.*;
import fun.tuple.Pair;
import jsonvalues.JsBigInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import fun.gen.BigIntGen;
import fun.gen.Gen;
import jsonvalues.JsBigInt;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsBigIntGen implements Gen<JsBigInt> {
    private final Gen<BigInteger> gen;

<<<<<<< HEAD
    public static Gen<JsBigInt> arbitrary(int min, int max) {
        return JsBigIntGen.of(BigIntGen.arbitrary(min, max));
    }

    public static Gen<JsBigInt> biased(int maxBits) {
        return JsBigIntGen.of(BigIntGen.biased(maxBits));
    }

    private JsBigIntGen(Gen<BigInteger> gen) {
        this.gen = gen;
=======
    private JsBigIntGen(Gen<BigInteger> gen) {
        this.gen = gen;
    }

    public static Gen<JsBigInt> arbitrary(int min,
                                          int max) {
        return JsBigIntGen.of(BigIntGen.arbitrary(min,
                                                  max));
    }

    public static Gen<JsBigInt> biased(int maxBits) {
        return JsBigIntGen.of(BigIntGen.biased(maxBits));
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsBigInt> of(final Gen<BigInteger> gen) {
        return new JsBigIntGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsBigInt> apply(RandomGenerator seed) {
=======
    public Supplier<JsBigInt> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsBigInt::of).apply(seed);
    }

}
