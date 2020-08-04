package jsonvalues.gen;
import jsonvalues.JsValue;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 provides different methods to test properties described as predicates. By default,
 if not specified the times parameter, every property is tested against 1000 generated values.
 The seed of the generation is generated if not provided. I recommend you generate the seed
 before the test and printing it out in case of failure.
 */
public class TestProperty {

   private static final int DEFAULT_TIMES = 1000;

    /**
     @param gen       generator to produce randomized input data
     @param condition the property to be tested
     @param errorConsumer consumer that takes the value that made the test fail
     @param <O> the type of the generated and tested value
     */
    public static <O extends JsValue> void test(JsGen<O> gen,
                                                Predicate<O> condition,
                                                Consumer<O> errorConsumer
                                               ) {
        test(gen,
             condition,
             DEFAULT_TIMES,
             errorConsumer,
             new Random());
    }
    /**
     @param gen       generator to produce randomized input data
     @param condition the property to be tested
     @param times     number of times an input is produced and tested on the property
     @param errorConsumer consumer that takes the value that made the test fail
     @param <O> the type of the generated and tested value
     */
    public static <O extends JsValue> void test(JsGen<O> gen,
                                                Predicate<O> condition,
                                                int times,
                                                Consumer<O> errorConsumer
                                               ) {
        test(gen,
             condition,
             times,
             errorConsumer,
             new Random());
    }

    /**
     @param gen       generator to produce randomized input data
     @param condition the property to be tested
     @param times     number of times an input is produced and tested on the property
     @param errorConsumer consumer that takes the value that made the test fail
     @param random the initial seed
     @param <O> the type of the generated and tested value
     */
    public static <O extends JsValue> void test(JsGen<O> gen,
                                                Predicate<O> condition,
                                                int times,
                                                Consumer<O> errorConsumer,
                                                Random random
                                               ) {
        for (int i = 0; i < times; i++) {

            final O value = gen.apply(random)
                               .get();
            boolean test = condition.test(value);
            if (!test) errorConsumer.accept(value);

        }
    }

    /**
     @param gen       generator to produce randomized input data
     @param condition the property to be tested
     @param errorConsumer consumer that takes the value that made the test fail
     @param random the initial seed
     @param <O> the type of the generated and tested value
     */
    public static <O extends JsValue> void test(JsGen<O> gen,
                                                Predicate<O> condition,
                                                Consumer<O> errorConsumer,
                                                Random random
                                               ) {
      test(gen,condition,errorConsumer,random);
    }
}
