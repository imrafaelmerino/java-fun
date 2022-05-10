package jsonvalues.gen;


import fun.gen.BoolGen;
import fun.gen.Gen;
import fun.gen.IntGen;
import jsonvalues.JsBool;
import jsonvalues.JsInt;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsBoolGen implements Gen<JsBool> {

    public static Gen<JsBool> arbitrary = JsBoolGen.of(BoolGen.arbitrary);

    private final Gen<Boolean> gen;

    private JsBoolGen(Gen<Boolean> gen) {
        this.gen = gen;
    }

    public static Gen<JsBool> of(final Gen<Boolean> gen) {
        return new JsBoolGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBool> apply(RandomGenerator seed) {
        return gen.map(JsBool::of).apply(seed);
    }
}

