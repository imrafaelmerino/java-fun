package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 Represents an immutable json boolean. Only two instances are created: {@link #FALSE} and {@link #TRUE}
 */
public final class JsBool implements JsValue
{
    public static final int ID = 0;


    @Override
    public int id()
    {
        return ID;
    }
    /**
     The singleton false value.
     */
    public static final JsBool FALSE = new JsBool(false);
    /**
     The singleton true value.
     */
    public static final JsBool TRUE = new JsBool(true);


    /**
     the boolean value.
     */
    public final boolean value;

    private JsBool(final boolean value)
    {
        this.value = value;
    }


    /**
     Indicates whether some other object is "equal to" this json boolean.
     @param that the reference object with which to compare.
     @return true if <code>that</code> is a JsBool with the same value as <code>this</code> JsBool
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final JsBool thatBool = (JsBool) that;
        return value == thatBool.value;
    }

    /**
     Returns the hashcode of this json boolean.
     @return 1 if true, 0 if false
     */
    @Override
    public int hashCode()
    {
        return value ? 1 : 0;
    }

    /**
     Negates this json boolean (implementation of ! operator).
     @return the inverse value of this
     */
    public JsBool negate()
    {
        return JsBool.of(!value);
    }

    /**
     * Static factory method to create a JsBool from a boolean primitive type.
     * @param b the boolean value
     * @return either {@link JsBool#TRUE} or {@link JsBool#FALSE}
     */
    public static JsBool of(boolean b)
    {
        return b ? TRUE : FALSE;
    }

    /**
     *
     * @return "true" or "false"
     */
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

    @Override
    public boolean isBool()
    {
        return true;
    }

    @Override
    public boolean isTrue()
    {
        return value;
    }

    @Override
    public boolean isFalse()
    {
        return !value;
    }


}
