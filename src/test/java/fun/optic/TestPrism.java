package fun.optic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestPrism {

    final Prism<Exception, IllegalArgumentException> prism =
            new Prism<>(e -> e instanceof IllegalArgumentException ?
                             Optional.of(((IllegalArgumentException) e)) :
                             Optional.empty(),
                        e -> e);

    @Test
    public void testEmpty() {


        Assertions.assertTrue(prism.isEmpty.test(new ArithmeticException()));
        Assertions.assertTrue(prism.nonEmpty.test(new IllegalArgumentException()));


    }

    @Test
    public void testModify() {
        Function<Exception, Exception> fn =
                prism.modify.apply(it -> new IllegalArgumentException("modified " + it.getMessage()));
        Assertions.assertTrue(fn.apply(new ArithmeticException()) instanceof ArithmeticException);
        Exception newExcp = fn.apply(new IllegalArgumentException("hi"));
        Assertions.assertTrue(newExcp instanceof IllegalArgumentException
                                      && newExcp.getMessage().equals("modified hi"));

    }

    @Test
    public void testModifyOpt() {
        Function<Exception, Optional<Exception>> fn =
                prism.modifyOpt.apply(it -> new IllegalArgumentException("modified " + it.getMessage()));
        Assertions.assertFalse(fn.apply(new ArithmeticException()).isPresent());
        Optional<Exception> opt = fn.apply(new IllegalArgumentException("hi"));
        Assertions.assertTrue(opt.isPresent() && opt.get() instanceof IllegalArgumentException
                                      && opt.get().getMessage().equals("modified hi"));

    }


    @Test
    public void testAll() {

        Predicate<Exception> predicate =
                prism.all.apply(exc -> exc.getMessage().startsWith("hi"));

        Assertions.assertTrue(predicate.test(new ArrayIndexOutOfBoundsException()));

        Assertions.assertFalse(predicate.test(new IllegalArgumentException("bye")));

        Assertions.assertTrue(predicate.test(new IllegalArgumentException("hi all!")));

    }

    @Test
    public void testExists() {
        Predicate<Exception> predicate =
                prism.exists.apply(exc -> exc.getMessage().startsWith("hi"));

        Assertions.assertFalse(predicate.test(new ArrayIndexOutOfBoundsException()));

        Assertions.assertFalse(predicate.test(new IllegalArgumentException("bye")));

        Assertions.assertTrue(predicate.test(new IllegalArgumentException("hi all!")));

    }

    @Test
    public void testFind() {
        Function<Exception, Optional<IllegalArgumentException>> fn =
                prism.find.apply(exc -> exc.getMessage().startsWith("hi"));

        Assertions.assertFalse(fn.apply(new ArrayIndexOutOfBoundsException()).isPresent());

        Assertions.assertFalse(fn.apply(new IllegalArgumentException("bye")).isPresent());

        Assertions.assertTrue(fn.apply(new IllegalArgumentException("hi all!")).isPresent());

    }


}
