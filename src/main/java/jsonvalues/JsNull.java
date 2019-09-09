package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 Represents null, which a valid json value. It's a singleton, only the instance JsNull.NULL exists.
 */
public final class JsNull implements JsElem
{
    /**
     * The singleton null value.
     */
    public static final JsNull NULL = new JsNull();


    private JsNull()
    {
    }


    /**
     Returns true if that is the singleton {@link JsNull#NULL}.
     @param that the reference object with which to compare.
     @return true if that is {@link JsNull#NULL}
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        return that == this;
    }

    /**
     Returns the hashcode of this json null
     @return 1
     */
    @Override
    public int hashCode()
    {
        return 1;
    }


    /**
     @return "null"
     */
    @Override
    public String toString()
    {
        return "null";
    }

    @Override
    public boolean isObj()
    {
        return false;
    }

    public boolean isArray()
    {
        return false;
    }

    @Override
    public boolean isNothing()
    {
        return false;
    }


    @Override
    public boolean isNull()
    {
        return true;
    }

    @Override
    public boolean isNumber()
    {
        return false;
    }

    @Override
    public boolean isBool()
    {
        return false;
    }

    @Override
    public boolean isTrue()
    {
        return false;
    }

    @Override
    public boolean isFalse()
    {
        return false;
    }

    @Override
    public boolean isInt()
    {
        return false;
    }

    @Override
    public boolean isDouble()
    {
        return false;
    }

    @Override
    public boolean isBigDec()
    {
        return false;
    }

    @Override
    public boolean isLong()
    {
        return false;
    }


    @Override
    public boolean isStr()
    {
        return false;
    }

    @Override
    public boolean isBigInt()
    {
        return false;
    }

}

