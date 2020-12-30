package jsonvalues;

import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type integer.
 */
public final class JsInt extends JsNumber implements Comparable<JsInt> {
    /**
     prism between the sum type JsValue and JsInt
     */
    public static final Prism<JsValue, Integer> prism =
            new Prism<>(s ->
                        {
                            if (s.isInt())
                                return Optional.of(s.toJsInt().value);
                            if (s.isLong())
                                return s.toJsLong()
                                        .intValueExact();
                            if (s.isBigInt())
                                return s.toJsBigInt()
                                        .intValueExact();
                            return Optional.empty();
                        },
                        JsInt::of
            );
    public static final int TYPE_ID = 9;
    /**
     The integer value.
     */
    public final int value;

    private JsInt(final int value) {
        this.value = value;
    }

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public boolean isInt() {
        return true;
    }

    /**
     Compares two {@code JsInt} objects numerically.

     @see Integer#compareTo(Integer)
     */
    @Override
    public int compareTo(final JsInt o) {
        return Integer.compare(value,
                               requireNonNull(o).value
                              );
    }

    /**
     Returns the hashcode of this json integer.

     @return hashcode of this JsInt
     */
    @Override
    public int hashCode() {
        return value;
    }

    /**
     Indicates whether some other object is "equal to" this json integer. Numbers of different types
     are equals if the have the same value.

     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this JsInt.
     */
    @Override
    public boolean equals(final Object that) {

        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;

        if (number.isInt()) return value == number.toJsInt().value;
        if (number.isLong()) return equals(number.toJsLong());
        if (number.isBigInt()) return equals(number.toJsBigInt());
        if (number.isBigDec()) return equals(number.toJsBigDec());
        if (number.isDouble()) return equals(number.toJsDouble());

        return false;
    }

    /**
     @return a string representation of the integer value.
     @see Integer#toString() Integer.toString
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /**
     returns true if this integer and the specified long represent the same number

     @param jsLong the specified JsLong
     @return true if both JsValue are the same value
     */
    private boolean equals(JsLong jsLong) {
        return requireNonNull(jsLong).equals(this);
    }

    /**
     returns true if this integer and the specified biginteger represent the same number

     @param jsBigInt the specified JsBigInt
     @return true if both JsValue are the same value
     */
    private boolean equals(JsBigInt jsBigInt) {
        return requireNonNull(jsBigInt).equals(this);
    }

    /**
     returns true if this integer and the specified bigdecimal represent the same number

     @param jsBigDec the specified JsBigDec
     @return true if both JsValue are the same value
     */
    private boolean equals(JsBigDec jsBigDec) {
        return requireNonNull(jsBigDec).equals(this);
    }

    /**
     returns true if this integer and the specified double represent the same number

     @param jsDouble the specified JsDouble
     @return true if both JsValue are the same value
     */
    boolean equals(JsDouble jsDouble) {
        return (double) value == requireNonNull(jsDouble).value;
    }

    /**
     Maps this json integer into another one.

     @param fn the mapping function
     @return a new JsInt
     */
    public JsInt map(IntUnaryOperator fn) {
        return JsInt.of(requireNonNull(fn).applyAsInt(value));
    }

    /**
     Static factory method to create a JsInt from an integer primitive type.

     @param n the integer primitive type
     @return a new JsInt
     */
    public static JsInt of(int n) {
        return new JsInt(n);
    }

    /**
     /**
     Tests the value of this json integer on a predicate.

     @param predicate the predicate
     @return true if this integer satisfies the predicate
     */
    public boolean test(IntPredicate predicate) {
        return requireNonNull(predicate).test(value);
    }

}
