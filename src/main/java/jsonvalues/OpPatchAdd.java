package jsonvalues;

import java.util.Optional;

class OpPatchAdd<T extends Json<?>> implements OpPatch<T>
{

    private JsElem value;
    private JsPath path;

    OpPatchAdd(final JsObj op) throws PatchMalformed
    {

    }

    @Override
    public TryPatch apply(final T json)
    {
        return null;
    }

    @Override
    public TryPatch<T> map(final TryPatch<T> tryPath)
    {
        return null;
    }
}
