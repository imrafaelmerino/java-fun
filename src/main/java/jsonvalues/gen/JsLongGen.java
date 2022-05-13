package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.LongGen;
import jsonvalues.JsLong;

<<<<<<< HEAD
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsLongGen implements Gen<JsLong> {
<<<<<<< HEAD
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
=======
    public static final Gen<JsLong> biased = JsLongGen.of(LongGen.biased);
    public static final Gen<JsLong> arbitrary = JsLongGen.of(LongGen.arbitrary);
    private final Gen<Long> gen;

    private JsLongGen(Gen<Long> gen) {
        this.gen = gen;
    }

    public static Gen<JsLong> arbitrary(long min,
                                        long max) {
        return JsLongGen.of(LongGen.arbitrary(min,
                                              max));
    }

    public static Gen<JsLong> biased(long min,
                                     long max) {
        return JsLongGen.of(LongGen.biased(min,
                                           max));
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsLong> of(final Gen<Long> gen) {
        return new JsLongGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsLong> apply(RandomGenerator seed) {
=======
    public Supplier<JsLong> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsLong::of).apply(seed);
    }
}
