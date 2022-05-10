package jsonvalues.gen;


import fun.gen.*;
import fun.tuple.Pair;
import jsonvalues.JsBinary;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsBinaryGen implements Gen<JsBinary> {
    private final Gen<byte[]> gen;

    public static Gen<JsBinary> arbitrary(int min, int max) {
        return JsBinaryGen.of(BytesGen.arbitrary(min, max));
    }

    public static Gen<JsBinary> biased(int min, int max) {
        return JsBinaryGen.of(BytesGen.biased(min, max));
    }

    private JsBinaryGen(Gen<byte[]> gen) {
        this.gen = gen;
    }

    public static Gen<JsBinary> of(final Gen<byte[]> gen) {
        return new JsBinaryGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBinary> apply(RandomGenerator seed) {
        return gen.map(JsBinary::of).apply(seed);
    }
}
