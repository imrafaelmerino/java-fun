package jsonvalues;

/**
 Represents an error applying a patch.
 */
public final class PatchOpError extends Exception
{
    private static final long serialVersionUID = 1L;

    private PatchOpError(final String message)
    {
        super(message);
    }

    private PatchOpError(final UserError error)
    {
        super(error.getMessage());
    }

    static PatchOpError of(final UserError error)
    {
        return new PatchOpError(error);
    }

    static PatchOpError removingNonExistingValue(final JsPath path,
                                                 final Json<?> json
                                                )
    {
        return new PatchOpError(String.format("Trying to remove a non-existing element. REMOVE operation can not be applied in %s at %s",
                                              json,
                                              path
                                             ));
    }

    static PatchOpError copyingNonExistingValue(final JsPath from,
                                                final JsPath path,
                                                final Json<?> json
                                               )
    {
        return new PatchOpError(String.format("Trying to copy a non-existing element. COPY operation can not be applied in %s from %s to %s",
                                              json,
                                              from,
                                              path
                                             ));
    }

    static PatchOpError testingNonExistingValue(final JsPath path,
                                                final Json<?> json
                                               )
    {
        return new PatchOpError(String.format("Trying to test a non-existing element. TEST operation can not be applied in %s at %s",
                                              json,
                                              path
                                             ));
    }

    static PatchOpError testingDifferentElements(final JsPath path,
                                                 final JsElem expected,
                                                 final JsElem found
                                                )
    {
        return new PatchOpError(String.format("Element expected at %s: %s. Element found: %s",
                                              path,
                                              expected,
                                              found
                                             ));
    }

}
