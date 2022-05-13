package jsonvalues.gen;


import fun.gen.BoolGen;
import fun.gen.Gen;
<<<<<<< HEAD
import fun.gen.IntGen;
import jsonvalues.JsBool;
import jsonvalues.JsInt;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import jsonvalues.JsBool;

import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsBoolGen implements Gen<JsBool> {

<<<<<<< HEAD
    public static Gen<JsBool> arbitrary = JsBoolGen.of(BoolGen.arbitrary);
=======
    public static final Gen<JsBool> arbitrary = JsBoolGen.of(BoolGen.arbitrary);
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

    private final Gen<Boolean> gen;

    private JsBoolGen(Gen<Boolean> gen) {
        this.gen = gen;
    }

    public static Gen<JsBool> of(final Gen<Boolean> gen) {
        return new JsBoolGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsBool> apply(RandomGenerator seed) {
=======
    public Supplier<JsBool> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsBool::of).apply(seed);
    }
}

