package jsonvalues.gen;

import fun.gen.Gen;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

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

    public static JsArrayGen of(final Gen<? extends JsValue> gen,
                                final int size
    ) {
        return new JsArrayGen(size,
                              gen
        );
    }


    @Override
    public Supplier<JsArray> apply(final RandomGenerator random) {
        Objects.requireNonNull(random);
        final Supplier<? extends JsValue> supplier =
                gen.apply(requireNonNull(random));
        return () ->
        {
            JsArray array = JsArray.empty();
            for (int i = 0; i < size; i++) array = array.append(supplier.get());
            return array;
        };
    }


}
