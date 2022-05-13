package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.InstantGen;
import jsonvalues.JsInstant;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsInstantGen implements Gen<JsInstant> {
    public static final Gen<JsInstant> biased = JsInstantGen.of(InstantGen.biased);
    public static final Gen<JsInstant> arbitrary = JsInstantGen.of(InstantGen.arbitrary);
    private final Gen<Instant> gen;

    private JsInstantGen(Gen<Instant> gen) {
        this.gen = gen;
    }

    public static Gen<JsInstant> arbitrary(long min,
                                           long max) {
        return JsInstantGen.of(InstantGen.arbitrary(min,
                                                    max));
    }

    public static Gen<JsInstant> arbitrary(ZonedDateTime min,
                                           ZonedDateTime max) {
        return JsInstantGen.of(InstantGen.arbitrary(min,
                                                    max));
    }

    public static Gen<JsInstant> biased(long min,
                                        long max) {
        return JsInstantGen.of(InstantGen.biased(min,
                                                 max));
    }

    public static Gen<JsInstant> of(final Gen<Instant> gen) {
        return new JsInstantGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsInstant> apply(Random seed) {
        return gen.map(JsInstant::of)
                  .apply(seed);
    }
}
