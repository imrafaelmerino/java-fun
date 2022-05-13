package jsonvalues.gen;


import fun.gen.BoolGen;
import fun.gen.Gen;
import jsonvalues.JsBool;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsBoolGen implements Gen<JsBool> {

    public static final Gen<JsBool> arbitrary = JsBoolGen.of(BoolGen.arbitrary);

    private final Gen<Boolean> gen;

    private JsBoolGen(Gen<Boolean> gen) {
        this.gen = gen;
    }

    public static Gen<JsBool> of(final Gen<Boolean> gen) {
        return new JsBoolGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBool> apply(Random seed) {
        return gen.map(JsBool::of).apply(seed);
    }
}

