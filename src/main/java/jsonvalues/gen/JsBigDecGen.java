package jsonvalues.gen;

import fun.gen.*;
import jsonvalues.JsBigDec;
import jsonvalues.JsDouble;
import jsonvalues.JsStr;

import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsBigDecGen implements Gen<JsBigDec> {

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
    }

    public static Gen<JsBigDec> of(final Gen<BigDecimal> gen) {
        return new JsBigDecGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBigDec> apply(RandomGenerator seed) {
        return gen.map(JsBigDec::of).apply(seed);
    }

}
