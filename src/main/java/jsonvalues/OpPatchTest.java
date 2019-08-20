package jsonvalues;

class OpPatchTest<T extends Json<?>> implements OpPatch<T>
{


    @Override
    public TryPatch<T> apply(final T t)
    {
        return null;
    }

    @Override
    public TryPatch<T> map(final TryPatch<T> tryPath)
    {
        return null;
    }
}
