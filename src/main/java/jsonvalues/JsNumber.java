package jsonvalues;

/**
 * Represents a sealed abstract class for JSON number values. It serves as a common base class for various
 * numeric JSON types, such as integers and floating-point numbers. Instances of this class are immutable.
 *
 * This class is part of a sealed hierarchy and permits specific subclasses for different numeric JSON types.
 *
 * @see JsBigDec
 * @see JsBigInt
 * @see JsDouble
 * @see JsInt
 * @see JsLong
 */
public abstract sealed class JsNumber extends JsPrimitive permits JsBigDec, JsBigInt, JsDouble, JsInt, JsLong {

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

}
