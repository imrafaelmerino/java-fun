package jsonvalues.gen;


<<<<<<< HEAD
import fun.gen.*;
import fun.tuple.Pair;
import jsonvalues.JsBinary;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import fun.gen.BytesGen;
import fun.gen.Gen;
import jsonvalues.JsBinary;

import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsBinaryGen implements Gen<JsBinary> {
    private final Gen<byte[]> gen;

<<<<<<< HEAD
    public static Gen<JsBinary> arbitrary(int min, int max) {
        return JsBinaryGen.of(BytesGen.arbitrary(min, max));
    }

    public static Gen<JsBinary> biased(int min, int max) {
        return JsBinaryGen.of(BytesGen.biased(min, max));
    }

    private JsBinaryGen(Gen<byte[]> gen) {
        this.gen = gen;
=======
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
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsBinary> of(final Gen<byte[]> gen) {
        return new JsBinaryGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsBinary> apply(RandomGenerator seed) {
=======
    public Supplier<JsBinary> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsBinary::of).apply(seed);
    }
}
