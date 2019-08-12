package jsonvalues;

/**
 Represents an immutable json number. It's a marker interface for the types {@link JsInt}, {@link JsLong}, {@link JsDouble}, {@link JsBigInt} and {@link JsBigDec}
 */
public interface JsNumber extends JsElem
{
    @Override
    default boolean isObj()
    {
        return false;
    }

    default boolean isArray()
    {
        return false;
    }

    @Override
    default boolean isNothing()
    {
        return false;
    }


    @Override
    default boolean isNull()
    {
        return false;
    }

    @Override
    default boolean isNumber()
    {
        return true;
    }

    @Override
    default boolean isBool()
    {
        return false;
    }

    @Override
    default boolean isStr()
    {
        return false;
    }

    @Override
    default boolean isTrue()
    {
        return false;
    }

    @Override
    default boolean isFalse()
    {
        return false;
    }
}
