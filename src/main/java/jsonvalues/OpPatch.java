package jsonvalues;

interface OpPatch<T extends Json<T>>
{
    TryPatch<T> apply(final T tryPath);
}
