package jsonvalues.gen;


import fun.gen.InstantGen;
import fun.gen.Gen;
import jsonvalues.JsInstant;
import jsonvalues.JsInstant;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsInstantGen implements Gen<JsInstant> {
    private final Gen<Instant> gen;

    public static Gen<JsInstant> biased = JsInstantGen.of(InstantGen.biased);
    public static Gen<JsInstant> arbitrary = JsInstantGen.of(InstantGen.arbitrary);
    public static Gen<JsInstant> arbitrary(long min, long max) {
        return JsInstantGen.of(InstantGen.arbitrary(min, max));
    }

    public static Gen<JsInstant> arbitrary(ZonedDateTime min, ZonedDateTime max) {
        return JsInstantGen.of(InstantGen.arbitrary(min, max));
    }


    public static Gen<JsInstant> biased(long min, long max) {
        return JsInstantGen.of(InstantGen.biased(min, max));
    }



    private JsInstantGen(Gen<Instant> gen) {
        this.gen = gen;
    }

    public static Gen<JsInstant> of(final Gen<Instant> gen) {
        return new JsInstantGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsInstant> apply(RandomGenerator seed) {
        return gen.map(JsInstant::of)
                  .apply(seed);
    }
}
