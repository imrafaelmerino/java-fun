package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.SplitGen;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public final class JsTupleGen implements Gen<JsArray> {
    private final List<Gen<? extends JsValue>> gens = new ArrayList<>();


    @SafeVarargs
    public static Gen<JsArray> of(final Gen<? extends JsValue> gen,
                                  final Gen<? extends JsValue>... others
    ) {
        return new JsTupleGen(gen, others);
    }


    @SafeVarargs
    @SuppressWarnings("varargs")
    private JsTupleGen(final Gen<? extends JsValue> gen,
                       final Gen<? extends JsValue>... others
    ) {
        gens.add(requireNonNull(gen));
        gens.addAll(Arrays.asList(requireNonNull(others)));
    }

    @Override
    public Supplier<JsArray> apply(final RandomGenerator random) {
        requireNonNull(random);
        var suppliers = gens.stream().map(it -> it.apply(SplitGen.DEFAULT.apply(random))).toList();
        return () ->
        {
            JsArray array = JsArray.empty();
            for (var supplier : suppliers) array = array.append(supplier.get());
            return array;
        };
    }


}
