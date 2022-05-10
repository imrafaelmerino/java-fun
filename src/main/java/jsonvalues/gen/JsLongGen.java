package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.LongGen;
import jsonvalues.JsLong;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsLongGen implements Gen<JsLong> {
    public static Gen<JsLong> biased = JsLongGen.of(LongGen.biased);
    public static Gen<JsLong> arbitrary = JsLongGen.of(LongGen.arbitrary);
    private final Gen<Long> gen;

    public static Gen<JsLong> arbitrary(long min, long max) {
        return JsLongGen.of(LongGen.arbitrary(min, max));
    }

    public static Gen<JsLong> biased(long min, long max) {
        return JsLongGen.of(LongGen.biased(min, max));
    }

    private JsLongGen(Gen<Long> gen) {
        this.gen = gen;
    }

    public static Gen<JsLong> of(final Gen<Long> gen) {
        return new JsLongGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsLong> apply(RandomGenerator seed) {
        return gen.map(JsLong::of).apply(seed);
    }
}
