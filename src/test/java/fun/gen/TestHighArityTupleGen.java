package fun.gen;

import fun.tuple.Quadruple;
import fun.tuple.Quintuple;
import fun.tuple.Sextuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.IntPredicate;

public class TestHighArityTupleGen {

    private static final IntPredicate IS_BINARY = n -> n == 0 || n == 1;

    @Test
    public void shouldGenerateQuadruples() {
        Gen<Quadruple<Integer, Integer, Integer, Integer>> gen =
                QuadrupleGen.of(IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1));

        Assertions.assertTrue(gen.sample(1000)
                                 .allMatch(t -> IS_BINARY.test(t.first())
                                         && IS_BINARY.test(t.second())
                                         && IS_BINARY.test(t.third())
                                         && IS_BINARY.test(t.fourth())));
    }

    @Test
    public void shouldGenerateQuintuples() {
        Gen<Quintuple<Integer, Integer, Integer, Integer, Integer>> gen =
                QuintupleGen.of(IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1),
                                IntGen.arbitrary(0,
                                                 1));

        Assertions.assertTrue(gen.sample(1000)
                                 .allMatch(t -> IS_BINARY.test(t.first())
                                         && IS_BINARY.test(t.second())
                                         && IS_BINARY.test(t.third())
                                         && IS_BINARY.test(t.fourth())
                                         && IS_BINARY.test(t.fifth())));
    }

    @Test
    public void shouldGenerateSextuples() {
        Gen<Sextuple<Integer, Integer, Integer, Integer, Integer, Integer>> gen =
                SextupleGen.of(IntGen.arbitrary(0,
                                                1),
                               IntGen.arbitrary(0,
                                                1),
                               IntGen.arbitrary(0,
                                                1),
                               IntGen.arbitrary(0,
                                                1),
                               IntGen.arbitrary(0,
                                                1),
                               IntGen.arbitrary(0,
                                                1));

        Assertions.assertTrue(gen.sample(1000)
                                 .allMatch(t -> IS_BINARY.test(t.first())
                                         && IS_BINARY.test(t.second())
                                         && IS_BINARY.test(t.third())
                                         && IS_BINARY.test(t.fourth())
                                         && IS_BINARY.test(t.fifth())
                                         && IS_BINARY.test(t.sixth())));
    }

    @Test
    public void shouldValidateNullGenerators() {
        Assertions.assertThrows(NullPointerException.class,
                                () -> QuadrupleGen.of(null,
                                                      Gen.constant(1),
                                                      Gen.constant(1),
                                                      Gen.constant(1)));

        Assertions.assertThrows(NullPointerException.class,
                                () -> QuintupleGen.of(Gen.constant(1),
                                                      null,
                                                      Gen.constant(1),
                                                      Gen.constant(1),
                                                      Gen.constant(1)));

        Assertions.assertThrows(NullPointerException.class,
                                () -> SextupleGen.of(Gen.constant(1),
                                                     Gen.constant(1),
                                                     null,
                                                     Gen.constant(1),
                                                     Gen.constant(1),
                                                     Gen.constant(1)));
    }
}
