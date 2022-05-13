package jsonvalues.gen;

<<<<<<< HEAD
import fun.gen.*;
import jsonvalues.JsBigDec;
import jsonvalues.JsDouble;
import jsonvalues.JsStr;

import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import fun.gen.BigDecGen;
import fun.gen.Gen;
import jsonvalues.JsBigDec;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsBigDecGen implements Gen<JsBigDec> {

<<<<<<< HEAD
    public static Gen<JsBigDec> biased = JsBigDecGen.of(BigDecGen.biased);
    public static Gen<JsBigDec> arbitrary = JsBigDecGen.of(BigDecGen.arbitrary);

    public static Gen<JsBigDec> arbitrary(final BigDecimal min, final BigDecimal max) {
        return JsBigDecGen.of(BigDecGen.arbitrary(min, max));
    }

    public static Gen<JsBigDec> biased(final BigDecimal min, final BigDecimal max) {
        return JsBigDecGen.of(BigDecGen.biased(min, max));
    }

    private final Gen<BigDecimal> gen;

    private JsBigDecGen(Gen<BigDecimal> gen) {
        this.gen = gen;
=======
    public static final Gen<JsBigDec> biased = JsBigDecGen.of(BigDecGen.biased);
    public static final Gen<JsBigDec> arbitrary = JsBigDecGen.of(BigDecGen.arbitrary);
    private final Gen<BigDecimal> gen;

    private JsBigDecGen(Gen<BigDecimal> gen) {
        this.gen = gen;
    }

    public static Gen<JsBigDec> arbitrary(final BigDecimal min,
                                          final BigDecimal max) {
        return JsBigDecGen.of(BigDecGen.arbitrary(min,
                                                  max));
    }

    public static Gen<JsBigDec> biased(final BigDecimal min,
                                       final BigDecimal max) {
        return JsBigDecGen.of(BigDecGen.biased(min,
                                               max));
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    }

    public static Gen<JsBigDec> of(final Gen<BigDecimal> gen) {
        return new JsBigDecGen(requireNonNull(gen));
    }

    @Override
<<<<<<< HEAD
    public Supplier<JsBigDec> apply(RandomGenerator seed) {
=======
    public Supplier<JsBigDec> apply(Random seed) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return gen.map(JsBigDec::of).apply(seed);
    }

}
