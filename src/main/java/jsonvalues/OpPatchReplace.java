package jsonvalues;

class OpPatchReplace<T extends Json<?>> implements OpPatch<T>
{


    @Override
    public TryPatch<T> apply(final T json)
    {
        return null;
    }

    @Override
    public TryPatch<T> map(final TryPatch<T> tryPath)
    {
        return null;
    }
}
