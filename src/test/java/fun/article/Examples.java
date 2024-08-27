package fun.article;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.PairGen;
import fun.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Examples {


    public static <I> Map<I, String> toPer(Map<I, Long> counters,
                                           double gen) {

        return counters.entrySet()
                       .stream()
                       .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(),
                                                               (e.getValue() / gen) * 100 + " %"))
                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                 Map.Entry::getValue));


    }


    @Test
    public void test_1() {

        Map<Integer, Long> collect = IntGen.arbitrary(-5,
                                                      5)
                                           .collect(100_000_000);
        Map<Integer, String> map = toPer(collect,
                                         100_000_000);

        System.out.println(map);

        Gen<Integer> gen = IntGen.arbitrary();
        Supplier<Integer> supplier = gen.apply(new Random());

        var a = supplier.get();
        var b = supplier.get();
        var c = supplier.get();

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

    }

    @Test
    public void test_2() {

        Map<Integer, Long> collect = IntGen.biased(-5,
                                                   5)
                                           .collect(100_000_000);
        Map<Integer, String> map = toPer(collect,
                                         100_000_000);

        System.out.println(map);

    }

    @Test
    public void test_3() {

        //        Suppose you need a generator which generates a tuple that contains two random integer values,
        //        one of them being at least twice as big as the other

        var gen = PairGen.of(IntGen.arbitrary(0,
                                              20),
                             IntGen.arbitrary(0,
                                              20))
                         .suchThat(pair -> pair.second() > 2 * pair.first());


        System.out.println(gen.collect(100_000));

        var gen1 = IntGen.arbitrary(0,
                                    20)
                         .then(a -> IntGen.arbitrary(2 * a + 1)
                                          .map(b -> Pair.of(a,
                                                            b))
                         );

        System.out.println(gen1.collect(100_000));
    }

    @Test
    public void test_4() {
        //You can create generators that pick one value out of a selection of values. The oneOf method creates a
        // generator that randomly picks one of its parameters each time it generates a value. Notice that plain
        // values are implicitly converted to generators (which always generate that value) if needed.

        var uniVowelGen = Combinators.oneOf('A',
                                            'E',
                                            'I',
                                            'O',
                                            'U');
        System.out.println(uniVowelGen.collect(100_000));

        var vowelGen = Combinators.freq(Pair.of(3,
                                                Gen.cons('A')),
                                        Pair.of(4,
                                                Gen.cons('E')),
                                        Pair.of(2,
                                                Gen.cons('I')),
                                        Pair.of(3,
                                                Gen.cons('O')),
                                        Pair.of(1,
                                                Gen.cons('U')));

        System.out.println(toPer(vowelGen.collect(100_000),
                                 100_000)
        );

    }

}
