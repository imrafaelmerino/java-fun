package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a generator for lists of elements of type {@code T}. This class provides methods to generate arbitrary and biased lists of elements.
 * Use this class to create random list generators with specified sizes and element generators.
 *
 * @param <T> The type of elements in the generated lists.
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
    /**
     * Creates a new {@code ListGen} with the specified element generator and size.
     *
     * @param <T>  The type of elements in the list.
     * @param gen  The generator for individual elements in the list.
     * @param size The size of the generated lists.
     * @return A new {@code ListGen} instance.
     */
    public static <T> ListGen<T> ofN(final Gen<T> gen,
                                     final int size) {
        return new ListGen<>(gen,
                             size);
    }

    /**
     * Returns a biased generator for lists of elements. This generator is biased towards generating lists with lengths
     * within the specified range [{@code minLength}, {@code maxLength}] (inclusive).
     *
     * @param <T>        The type of elements in the list.
     * @param gen        The generator for individual elements in the list.
     * @param minLength  The minimum length of the generated lists.
     * @param maxLength  The maximum length of the generated lists.
     * @return A biased generator for lists of elements with lengths in the specified range.
     * @throws IllegalArgumentException If {@code minLength} is less than 0 or {@code maxLength} is less than {@code minLength}.
     */
    public static <T> Gen<List<T>> biased(final Gen<T> gen,
                                          final int minLength,
                                          final int maxLength
    ) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        requireNonNull(gen);
        List<Pair<Integer, Gen<? extends List<T>>>> gens = new ArrayList<>();

        gens.add(Pair.of(1,
                         new ListGen<>(gen,
                                       minLength)));
        gens.add(Pair.of(1,
                         new ListGen<>(gen,
                                       maxLength)));
        gens.add(Pair.of(gens.size(),
                         ListGen.arbitrary(gen,
                                           minLength,
                                           maxLength)));
        return Combinators.freqList(gens);


    }

    public static <T> Gen<List<T>> arbitrary(final Gen<T> gen,
                                             final int minLength,
                                             final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
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
