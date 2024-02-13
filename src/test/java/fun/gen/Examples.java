package fun.gen;

import fun.tuple.Pair;

public class Examples {


    public static void main(String[] args) {
        //Suppose you need a generator which generates a tuple that
        // contains two random integer values, one of them being at
        // least twice as big as the other.

        IntGen.arbitrary(10,
                         20)
              .then(n -> IntGen.arbitrary(n * 2,
                                          500));


        //The following generator generates a vowel:

        Combinators.oneOf("A",
                          "E",
                          "I",
                          "O",
                          "U");


        Combinators.freq(
                Pair.of(3,
                        Gen.cons('A')),
                Pair.of(4,
                        Gen.cons('E')),
                Pair.of(2,
                        Gen.cons('I')),
                Pair.of(3,
                        Gen.cons('O')),
                Pair.of(1,
                        Gen.cons('U')));
    }
}
