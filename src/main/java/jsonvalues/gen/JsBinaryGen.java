package jsonvalues.gen;


import fun.gen.BytesGen;
import fun.gen.Gen;
import jsonvalues.JsBinary;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsBinaryGen implements Gen<JsBinary> {
    private final Gen<byte[]> gen;

    private JsBinaryGen(Gen<byte[]> gen) {
        this.gen = gen;
    }

    public static Gen<JsBinary> arbitrary(int min,
                                          int max) {
        return JsBinaryGen.of(BytesGen.arbitrary(min,
                                                 max));
    }

    public static Gen<JsBinary> biased(int min,
                                       int max) {
        return JsBinaryGen.of(BytesGen.biased(min,
                                              max));
    }

    public static Gen<JsBinary> of(final Gen<byte[]> gen) {
        return new JsBinaryGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBinary> apply(Random seed) {
        return gen.map(JsBinary::of).apply(seed);
    }
}
