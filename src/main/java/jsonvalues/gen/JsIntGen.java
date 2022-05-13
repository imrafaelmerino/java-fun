package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
<<<<<<< HEAD
import fun.gen.LongGen;
import jsonvalues.JsInt;
import jsonvalues.JsLong;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import jsonvalues.JsInt;

import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsIntGen implements Gen<JsInt> {
<<<<<<< HEAD
    public static Gen<JsInt> biased = JsIntGen.of(IntGen.biased);
    public static Gen<JsInt> arbitrary = JsIntGen.of(IntGen.arbitrary);

    public static Gen<JsInt> arbitrary(int min, int max) {
        return JsIntGen.of(IntGen.arbitrary(min, max));
    }

    public static Gen<JsInt> biased(int min, int max) {
        return JsIntGen.of(IntGen.biased(min, max));
    }

    private final Gen<Integer> gen;

    private JsIntGen(Gen<Integer> gen) {
        this.gen = gen;
=======
    public static final Gen<JsInt> biased = JsIntGen.of(IntGen.biased);
    public static final Gen<JsInt> arbitrary = JsIntGen.of(IntGen.arbitrary);
    private final Gen<Integer> gen;

    private JsIntGen(Gen<Integer> gen) {
        this.gen = gen;
    }

    public static Gen<JsInt> arbitrary(int min,
                                       int max) {
        return JsIntGen.of(IntGen.arbitrary(min,
                                            max));
    }

    public static Gen<JsInt> biased(int min,
                                    int max) {
        return JsIntGen.of(IntGen.biased(min,
                                         max));
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsInt> of(final Gen<Integer> gen) {
        return new JsIntGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsInt> apply(RandomGenerator seed) {
=======
    public Supplier<JsInt> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsInt::of).apply(seed);
    }
}
