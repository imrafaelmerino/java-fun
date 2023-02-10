package jsonvalues;

/**
 * Represents a JsValue that is not a container (JsObj or JsArray)
 */
public abstract sealed class JsPrimitive implements JsValue permits JsBinary, JsBool, JsInstant, JsNull, JsNumber, JsStr {

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isJson() {
        return false;
    }
}
