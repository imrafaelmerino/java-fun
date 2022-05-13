package jsonvalues.gen;


import fun.gen.DoubleGen;
import fun.gen.Gen;
<<<<<<< HEAD
import fun.gen.IntGen;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import jsonvalues.JsDouble;

import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsDoubleGen implements Gen<JsDouble> {
<<<<<<< HEAD
    public static Gen<JsDouble> biased = JsDoubleGen.of(DoubleGen.biased);
    public static Gen<JsDouble> arbitrary = JsDoubleGen.of(DoubleGen.arbitrary);

    public static Gen<JsDouble> uniform(double min, double max) {
        return JsDoubleGen.of(DoubleGen.arbitrary(min, max));
    }

    public static Gen<JsDouble> biased(double min, double max) {
        return JsDoubleGen.of(DoubleGen.biased(min, max));
    }


    private final Gen<Double> gen;

    private JsDoubleGen(Gen<Double> gen) {
        this.gen = gen;
=======
    public static final Gen<JsDouble> biased = JsDoubleGen.of(DoubleGen.biased);
    public static final Gen<JsDouble> arbitrary = JsDoubleGen.of(DoubleGen.arbitrary);
    private final Gen<Double> gen;

    private JsDoubleGen(Gen<Double> gen) {
        this.gen = gen;
    }

    public static Gen<JsDouble> uniform(double min,
                                        double max) {
        return JsDoubleGen.of(DoubleGen.arbitrary(min,
                                                  max));
    }

    public static Gen<JsDouble> biased(double min,
                                       double max) {
        return JsDoubleGen.of(DoubleGen.biased(min,
                                               max));
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsDouble> of(final Gen<Double> gen) {
        return new JsDoubleGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsDouble> apply(RandomGenerator seed) {
=======
    public Supplier<JsDouble> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsDouble::of).apply(seed);
    }

}
