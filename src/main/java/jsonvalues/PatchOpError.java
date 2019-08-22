package jsonvalues;

public final class PatchOpError extends Exception
{
    private static final long serialVersionUID = 1L;

    private PatchOpError(final String message)
    {
        super(message);
    }

    static PatchOpError parentIsNotAJson(final JsPath parent,
                                         final Json<?> json,
                                         final JsPath path,
                                         final String op
                                        )
    {
        return new PatchOpError(String.format("Element located at '%s' is not a Json. %s operation can not be applied in %s at %s.",
                                              parent,
                                              op,
                                              json,
                                              path
                                             )
        );
    }

    static PatchOpError addingKeyIntoArray(final String key,
                                           final Json<?> json,
                                           final JsPath path,
                                           final String op
                                          )
    {
        return new PatchOpError(String.format("Trying to add the key '%s' in an array. %s operation can not be applied in %s at %s",
                                              key,
                                              op,
                                              json,
                                              path
                                             )
        );
    }

    static PatchOpError addingIndexIntoObject(final int index,
                                              final Json<?> json,
                                              final JsPath path,
                                              final String op
                                             )
    {
        return new PatchOpError(String.format("Trying to add at the index '%s' in an object. %s operation can not be applied in %s at %s",
                                              index,
                                              op,
                                              json,
                                              path
                                             )
        );
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
