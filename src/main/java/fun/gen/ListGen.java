package fun.gen;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
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
     * @param <T>       The type of elements in the list.
     * @param gen       The generator for individual elements in the list.
     * @param minLength The minimum length of the generated lists.
     * @param maxLength The maximum length of the generated lists.
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

    /**
     * Generates a list of arbitrary elements using the provided element generator within a specified length range. This method allows you to create lists of elements with varying lengths, where each element is generated independently based on the given generator.
     *
     * <p>The {@code gen} parameter specifies the generator for individual elements of the list, while {@code minLength} and {@code maxLength} define the allowed length range for the generated list. You can control the size and content of the list by specifying these minimum and maximum lengths.</p>
     *
     * @param <T>       the type of elements to generate and include in the list
     * @param gen       the generator for individual elements of the list
     * @param minLength the minimum allowed length of the generated list (must be non-negative)
     * @param maxLength the maximum allowed length of the generated list (must be greater than or equal to {@code minLength})
     * @return a generator for lists of arbitrary elements within the specified length range
     * @throws IllegalArgumentException if {@code minLength} is negative or if {@code maxLength} is less than {@code minLength}
     */
    public static <T> Gen<List<T>> arbitrary(final Gen<T> gen,
                                             final int minLength,
                                             final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("minLength < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("maxLength < minLength");
        requireNonNull(gen);
        return seed -> {
            var sizeSupplier =
                    IntGen.arbitrary(minLength,
                                     maxLength)
                          .apply(SplitGen.DEFAULT.apply(seed));

            var elemSupplier = gen.apply(SplitGen.DEFAULT.apply(seed));
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
    public Supplier<List<T>> apply(RandomGenerator seed) {
        requireNonNull(seed);
        return listSupplier(() -> size,
                            gen.apply(requireNonNull(seed)));
    }
}
