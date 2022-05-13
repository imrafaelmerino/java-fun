package jsonvalues.gen;


import fun.gen.DoubleGen;
import fun.gen.Gen;
import jsonvalues.JsDouble;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsDoubleGen implements Gen<JsDouble> {
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
    }

    public static Gen<JsDouble> of(final Gen<Double> gen) {
        return new JsDoubleGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsDouble> apply(Random seed) {
        return gen.map(JsDouble::of).apply(seed);
    }

}
