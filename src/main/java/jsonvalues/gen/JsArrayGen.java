package jsonvalues.gen;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.SplitGen;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * represent a generator of Json arrays.
 */
public final class JsArrayGen implements Gen<JsArray> {

    private final int size;
    private final Gen<? extends JsValue> gen;

    private JsArrayGen(final int size,
                       final Gen<? extends JsValue> gen
    ) {
        this.size = size;
        this.gen = gen;
    }

    public static Gen<JsArray> of(final Gen<Iterable<? extends JsValue>> gen) {
        return gen.map(JsArray::ofIterable);
    }


    public static Function<Gen<? extends JsValue>, Gen<JsArray>> arbitrary(final int size) {
        return gen -> new JsArrayGen(size,
                                     gen);
    }

    public static Function<Gen<? extends JsValue>, Gen<JsArray>> arbitrary(final int min,
                                                                           final int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return gen -> {
            requireNonNull(gen);
            return seed -> {
                Supplier<Integer> sizeSupplier =
                        IntGen.arbitrary(min,
                                         max)
                              .apply(SplitGen.DEFAULT.apply(seed));

                Supplier<? extends JsValue> elemSupplier =
                        gen.apply(SplitGen.DEFAULT.apply(seed));
                return arraySupplier(elemSupplier,
                                     sizeSupplier);
            };
        };
    }


    public static Function<Gen<? extends JsValue>, Gen<JsArray>> biased(final int min,
                                                                        final int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return gen -> {
            requireNonNull(gen);
            if (min == 0)
                return Combinators.freq(new Pair<>(1,
                                                   Gen.cons(JsArray.empty())),
                                        new Pair<>(1,
                                                   new JsArrayGen(max - 1,
                                                                  gen)),
                                        new Pair<>(2,
                                                   JsArrayGen.arbitrary(min,
                                                                        max)
                                                             .apply(gen)));

            else
                return Combinators.freq(new Pair<>(1,
                                                   new JsArrayGen(min,
                                                                  gen)),
                                        new Pair<>(1,
                                                   new JsArrayGen(max - 1,
                                                                  gen)),
                                        new Pair<>(2,
                                                   JsArrayGen.arbitrary(min,
                                                                        max)
                                                             .apply(gen)));

        };

    }


    private static Supplier<JsArray> arraySupplier(Supplier<? extends JsValue> elemSupplier,
                                                   Supplier<Integer> sizeSupplier) {
        return () ->
        {
            JsArray array = JsArray.empty();
            for (int i = 0; i < sizeSupplier.get(); i++) array = array.append(elemSupplier.get());
            return array;
        };
    }

    @Override
    public Supplier<JsArray> apply(final Random random) {
        Objects.requireNonNull(random);
        return arraySupplier(gen.apply(requireNonNull(random)),
                             () -> size);
    }


}
