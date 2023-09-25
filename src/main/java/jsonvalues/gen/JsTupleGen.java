package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.SplitGen;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Represents a tuple generator that is modeled with a JsArray generator.
 * Each generator of the tuple is created from a new seed that is calculated
 * by passing the original one to the {@link SplitGen#DEFAULT split generator}.
 */
public final class JsTupleGen implements Gen<JsArray> {
    private final List<Gen<? extends JsValue>> gens = new ArrayList<>();

    @SafeVarargs
    @SuppressWarnings("varargs")
    private JsTupleGen(final Gen<? extends JsValue> gen,
                       final Gen<? extends JsValue>... others
    ) {
        gens.add(requireNonNull(gen));
        gens.addAll(Arrays.asList(requireNonNull(others)));
    }

    /**
     * Returns a tuple generator. The tuple is modeled with a JsArray. Each element generator
     * is independent of each other, being created from a different seed.
     *
     * @param gen    The head element generator.
     * @param others The rest of the generators.
     * @return A JsArray generator.
     */

    @SafeVarargs
    public static Gen<JsArray> of(final Gen<? extends JsValue> gen,
                                  final Gen<? extends JsValue>... others
    ) {
        return new JsTupleGen(gen,
                              others);
    }


    @Override
    public Supplier<JsArray> apply(final Random seed) {
        requireNonNull(seed);
        List<Supplier<? extends JsValue>> suppliers =
                gens.stream()
                    .map(it -> it.apply(SplitGen.DEFAULT.apply(seed)))
                    .collect(Collectors.toList());
        return () ->
        {
            JsArray array = JsArray.empty();
            for (Supplier<? extends JsValue> supplier : suppliers) array = array.append(supplier.get());
            return array;
        };
    }


}
