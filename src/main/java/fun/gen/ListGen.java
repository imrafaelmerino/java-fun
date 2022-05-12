package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
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


    public static <T> Function<Gen<T>, Gen<List<T>>> biased(final int minLength,
                                                            final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return gen -> {
            requireNonNull(gen);
            List<Pair<Integer, Gen<? extends List<T>>>> gens = new ArrayList<>();

            gens.add(new Pair<>(1,
                                new ListGen<>(gen,
                                              minLength)));
            gens.add(new Pair<>(1,
                                new ListGen<>(gen,
                                              maxLength)));
            gens.add(new Pair<>(gens.size(),
                                ListGen.<T>arbitrary(minLength,
                                                     maxLength)
                                       .apply(gen)));
            return Combinators.freqList(gens);
        };

    }

    public static <T> Function<Gen<T>, Gen<List<T>>> arbitrary(final int minLength,
                                                               final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        return gen -> {
            requireNonNull(gen);
            return seed -> {
                Supplier<Integer> sizeSupplier =
                        IntGen.arbitrary(minLength,
                                         maxLength)
                              .apply(SplitGen.DEFAULT.apply(seed));

                Supplier<T> elemSupplier = gen.apply(SplitGen.DEFAULT.apply(seed));
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
    public Supplier<List<T>> apply(Random seed) {
        requireNonNull(seed);
        return listSupplier(() -> size,
                            gen.apply(requireNonNull(seed)));
    }
}
