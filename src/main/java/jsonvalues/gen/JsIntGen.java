package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
import jsonvalues.JsInt;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsIntGen implements Gen<JsInt> {
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
    }

    public static Gen<JsInt> of(final Gen<Integer> gen) {
        return new JsIntGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsInt> apply(Random seed) {
        return gen.map(JsInt::of).apply(seed);
    }
}
