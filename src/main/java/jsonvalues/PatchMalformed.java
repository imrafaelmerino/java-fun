package jsonvalues;

public final class PatchMalformed extends Exception
{
    PatchMalformed(final String message)
    {
        super(message);
    }

    static PatchMalformed fromRequired(final JsObj objOp)
    {
        return new PatchMalformed(String.format("from field is missing in %s",
                                                objOp
                                               ));
    }

    static PatchMalformed operationNotSupported(final JsObj objOp)
    {
        return new PatchMalformed(String.format("op specified in %s not supported",
                                                objOp
                                               ));
    }

    static PatchMalformed operationRequired(final JsObj obj)
    {
        return new PatchMalformed(String.format("op field is missing in %s",
                                                obj
                                               ));
    }

    static PatchMalformed pathRequired(final JsObj obj)
    {
        return new PatchMalformed(String.format("path field is missing in %s",
                                                obj
                                               ));
    }

    static PatchMalformed valueRequired(final JsObj obj)
    {
        return new PatchMalformed(String.format("value field is missing in %s",
                                                obj
                                               ));
    }

    static PatchMalformed patchIsNotAnArray(final String str)
    {
        return new PatchMalformed(String.format("patch is not an array of operations: %s",
                                                str
                                               ));
    }

    static PatchMalformed operationIsNotAnObj(final JsElem elem)
    {
        return new PatchMalformed(String.format("the operation %s is not an object.",
                                                elem
                                               ));
    }


}
