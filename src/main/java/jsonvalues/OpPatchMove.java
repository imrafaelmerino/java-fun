package jsonvalues;

class OpPatchMove<T extends Json<?>> implements OpPatch<T>
{


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
