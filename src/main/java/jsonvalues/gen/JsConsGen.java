package jsonvalues.gen;

import fun.gen.Gen;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

<<<<<<< HEAD
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
=======
import java.util.Random;
import java.util.function.Supplier;
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

import static java.util.Objects.requireNonNull;

public final class JsConsGen<O extends JsValue> implements Gen<O> {

<<<<<<< HEAD
    public static Gen<JsValue> NULL = new JsConsGen<>(JsNull.NULL);
    public static Gen<JsBool> TRUE = new JsConsGen<>(JsBool.TRUE);
    public static Gen<JsBool> FALSE = new JsConsGen<>(JsBool.FALSE);
=======
    public static final Gen<JsValue> NULL = new JsConsGen<>(JsNull.NULL);
    public static final Gen<JsBool> TRUE = new JsConsGen<>(JsBool.TRUE);
    public static final Gen<JsBool> FALSE = new JsConsGen<>(JsBool.FALSE);
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723

    private final O value;

    private JsConsGen(O value) {
        this.value = value;
    }

    public static <O extends JsValue> Gen<O> cons(O value) {
        return new JsConsGen<>(requireNonNull(value));
    }

    @Override
<<<<<<< HEAD
    public Supplier<O> apply(RandomGenerator generator) {
=======
    public Supplier<O> apply(Random generator) {
>>>>>>> d43bc88ce46e08079b32242491e8d64ef7d72723
        return () -> value;
    }
}
