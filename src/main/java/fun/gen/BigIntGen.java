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


    /**
     * Constructs a randomly generated BigInteger, uniformly distributed over the range 0 to (2numBits - 1), inclusive.
     * Note that this generator always generates  non-negative big integers
     * @param bits  maximum bitLength of the new BigInteger
     * @return a big integer generator
     */
    public static Gen<BigInteger> arbitrary(int bits) {
        return new BigIntGen(bits);

    }

    public static Gen<BigInteger> biased(int bits) {

        BigInteger max = BigInteger.valueOf(2)
                                   .pow(bits);

        List<Pair<Integer, Gen<? extends BigInteger>>> gens = new ArrayList<>();
        if (max.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigInteger.valueOf(Long.MAX_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigInteger.valueOf(Integer.MAX_VALUE))));

        if (max.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) > 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigInteger.valueOf(Short.MAX_VALUE))));


        if (max.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) > 0)
            gens.add(Pair.of(1,
                             Gen.cons(BigInteger.valueOf(Byte.MAX_VALUE))));


        gens.add(Pair.of(1,
                         Gen.cons(BigInteger.ZERO)));


        gens.add(Pair.of(1,
                         Gen.cons(max)));


        gens.add(Pair.of(gens.size(),
                         arbitrary(bits)));

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
