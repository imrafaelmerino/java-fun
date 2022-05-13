package jsonvalues.gen;

import fun.gen.Gen;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsConsGen<O extends JsValue> implements Gen<O> {

    public static final Gen<JsValue> NULL = new JsConsGen<>(JsNull.NULL);
    public static final Gen<JsBool> TRUE = new JsConsGen<>(JsBool.TRUE);
    public static final Gen<JsBool> FALSE = new JsConsGen<>(JsBool.FALSE);

    private final O value;

    private JsConsGen(O value) {
        this.value = value;
    }

    public static <O extends JsValue> Gen<O> cons(O value) {
        return new JsConsGen<>(requireNonNull(value));
    }

    @Override
    public Supplier<O> apply(Random generator) {
        return () -> value;
    }
}
