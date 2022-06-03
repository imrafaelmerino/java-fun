package fun.gen;

import fun.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class BigIntGen implements Gen<BigInteger> {

    private final int nBits;

    private BigIntGen(final int nBits) {
        if (nBits < 0) throw new IllegalArgumentException("nBits < 0");
        this.nBits = nBits;
    }

    public static Gen<BigInteger> arbitrary(int bits) {
        return new BigIntGen(bits);
    }

    public static Gen<BigInteger> arbitrary(int minBits,
                                            int maxBits) {
        if (maxBits < minBits) throw new IllegalArgumentException("maxBits < minBits");
        return seed -> {

            Supplier<Integer> bitsSupplier = IntGen.arbitrary(minBits,
                                                              maxBits)
                                                   .apply(SplitGen.DEFAULT.apply(requireNonNull(seed)));

            Random bitsSeed = SplitGen.DEFAULT.apply(seed);
            @SuppressWarnings("serial") Random rnd = new Random() {
                @Override
                protected int next(int bits) {
                    return bitsSeed.nextInt(bits);
                }
            };

            return () -> new BigInteger(bitsSupplier.get(),
                                        rnd);

        };

    }

    public static Gen<BigInteger> biased(int minBits,
                                         int maxBits) {
        if (maxBits < minBits) throw new IllegalArgumentException("maxBits < minBits");
        BigInteger min = BigInteger.valueOf(2).pow(minBits).min(BigInteger.ONE);
        BigInteger max = BigInteger.valueOf(2).pow(maxBits).min(BigInteger.ONE);

        List<Pair<Integer, Gen<? extends BigInteger>>> gens = new ArrayList<>();
        if (max.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) >= 0 && min.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Long.MAX_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) >= 0 && min.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) <= 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Long.MIN_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 && min.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Integer.MAX_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) > 0 && min.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Integer.MIN_VALUE))));


        if (max.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) > 0 && min.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Short.MAX_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Short.MIN_VALUE)) > 0 && min.compareTo(BigInteger.valueOf(Short.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Short.MIN_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) > 0 && min.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Byte.MAX_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Byte.MIN_VALUE)) > 0 && min.compareTo(BigInteger.valueOf(Byte.MIN_VALUE)) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.valueOf(Byte.MIN_VALUE))));

        if (max.compareTo(BigInteger.ZERO) > 0 && min.compareTo(BigInteger.ZERO) < 0)
            gens.add(Pair.of(1,
                                Gen.cons(BigInteger.ZERO)));

        gens.add(Pair.of(1,
                            Gen.cons(min)));

        gens.add(Pair.of(1,
                            Gen.cons(max)));

        gens.add(Pair.of(gens.size(),
                            arbitrary(minBits,
                                      maxBits)));

        return Combinators.freqList(gens);

    }


    @Override
    @SuppressWarnings("java:S2142")
    public Supplier<BigInteger> apply(final Random gen) {
        requireNonNull(gen);
        @SuppressWarnings("serial") Random rnd = new Random() {

            @Override
            protected int next(int bits) {
                return gen.nextInt(bits);
            }
        };

        return () -> new BigInteger(nBits,
                                    rnd);
    }


}
