package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator of lists.
 */
public final class ListGen<T> implements Gen<List<T>> {

    private final Gen<T> gen;
    private final int size;

    private ListGen(final Gen<T> gen,
                    final int size) {
        if (size < 0) throw new IllegalArgumentException("size < 0");
        this.gen = requireNonNull(gen);
        this.size = size;
    }

    public static <T> Function<Gen<T>, Gen<List<T>>> arbitrary(int length) {
        return gen -> new ListGen<>(gen,
                                    length);
    }

    public static <T> Function<Gen<T>, Gen<List<T>>> biased(int min,
                                                            int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return gen -> {
            requireNonNull(gen);
            var gens = new ArrayList<Pair<Integer, Gen<? extends List<T>>>>();
            if (min == 0)
                gens.add(new Pair<>(1,
                                    Gen.cons(ArrayList::new)));
            else
                gens.add(new Pair<>(1,
                                    new ListGen<>(gen,
                                                  min)));
            gens.add(new Pair<>(1,
                                new ListGen<>(gen,
                                              max - 1)));
            gens.add(new Pair<>(gens.size(),
                                ListGen.<T>arbitrary(min,
                                                     max).apply(gen)));
            return Combinators.freqList(gens);
        };

    }

    public static <T> Function<Gen<T>, Gen<List<T>>> arbitrary(int min,
                                                               int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= min) throw new IllegalArgumentException("max <= min");
        return gen -> {
            requireNonNull(gen);
            return seed -> {
                var sizeSupplier =
                        IntGen.arbitrary(min,
                                         max)
                              .apply(SplitGen.DEFAULT.apply(seed));

                var elemSupplier = gen.apply(SplitGen.DEFAULT.apply(seed));
                return listSupplier(sizeSupplier,
                                    elemSupplier);
            };
        };
    }

    private static <T> Supplier<List<T>> listSupplier(Supplier<Integer> sizeSupplier,
                                                      Supplier<T> elemSupplier) {
        return () -> IntStream.range(0,
                                     sizeSupplier.get())
                              .mapToObj(i -> elemSupplier.get())
                              .collect(Collectors.toList());
    }

    @Override
    public Supplier<List<T>> apply(RandomGenerator seed) {
        Objects.requireNonNull(seed);
        return listSupplier(() -> size,
                            gen.apply(requireNonNull(seed)));
    }
}
