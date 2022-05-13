package jsonvalues.gen;


<<<<<<< HEAD
import fun.gen.InstantGen;
import fun.gen.Gen;
import jsonvalues.JsInstant;
=======
import fun.gen.Gen;
import fun.gen.InstantGen;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
import jsonvalues.JsInstant;

import java.time.Instant;
import java.time.ZonedDateTime;
<<<<<<< HEAD
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsInstantGen implements Gen<JsInstant> {
<<<<<<< HEAD
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
=======
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
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsInstant> of(final Gen<Instant> gen) {
        return new JsInstantGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsInstant> apply(RandomGenerator seed) {
=======
    public Supplier<JsInstant> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsInstant::of)
                  .apply(seed);
    }
}
