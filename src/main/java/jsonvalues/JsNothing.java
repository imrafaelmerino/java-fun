package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 It's a special json element that represents 'nothing'. Inserting nothing in a json leaves the json
 unchanged. Some functions that return a JsElem, like {@link Json#get(JsPath)} and {@link Json#get(String)},
 return nothing when no element is found, what makes them total on the input path.
 */
public final class JsNothing implements JsElem
{

    private JsNothing()
    {
    }

    /**
     The singleton nothing value.
     */
    public static final JsNothing NOTHING = new JsNothing();

    /**
     * @return "NOTHING"
     */
    @Override
    public String toString()
    {
        return "NOTHING";
    }

    /**
     Returns true if that is the singleton {@link JsNothing#NOTHING}.
     @param that the reference object with which to compare.
     @return true if that is {@link JsNothing#NOTHING}
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        return this == that;
    }

    /**
     Returns the hashcode of this JsNothing.
     @return 1
     */
    @Override
    public int hashCode()
    {
        return 1;
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
        return true;
    }


    @Override
    public boolean isNull()
    {
        return false;
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
