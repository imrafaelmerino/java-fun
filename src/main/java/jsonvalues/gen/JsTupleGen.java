package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.SplitGen;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
<<<<<<< HEAD
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsTupleGen implements Gen<JsArray> {
    private final List<Gen<? extends JsValue>> gens = new ArrayList<>();


    @SafeVarargs
<<<<<<< HEAD
    public static Gen<JsArray> of(final Gen<? extends JsValue> gen,
                                  final Gen<? extends JsValue>... others
    ) {
        return new JsTupleGen(gen, others);
    }


    @SafeVarargs
=======
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
    @SuppressWarnings("varargs")
    private JsTupleGen(final Gen<? extends JsValue> gen,
                       final Gen<? extends JsValue>... others
    ) {
        gens.add(requireNonNull(gen));
        gens.addAll(Arrays.asList(requireNonNull(others)));
    }

<<<<<<< HEAD
    @Override
    public Supplier<JsArray> apply(final RandomGenerator random) {
        requireNonNull(random);
        var suppliers = gens.stream().map(it -> it.apply(SplitGen.DEFAULT.apply(random))).toList();
        return () ->
        {
            JsArray array = JsArray.empty();
            for (var supplier : suppliers) array = array.append(supplier.get());
=======
    @SafeVarargs
    public static Gen<JsArray> of(final Gen<? extends JsValue> gen,
                                  final Gen<? extends JsValue>... others
    ) {
        return new JsTupleGen(gen,
                              others);
    }

    @Override
    public Supplier<JsArray> apply(final Random random) {
        requireNonNull(random);
        List<Supplier<? extends JsValue>> suppliers =
                gens.stream()
                    .map(it -> it.apply(SplitGen.DEFAULT.apply(random)))
                    .collect(Collectors.toList());
        return () ->
        {
            JsArray array = JsArray.empty();
            for (Supplier<? extends JsValue> supplier : suppliers) array = array.append(supplier.get());
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
            return array;
        };
    }


}
