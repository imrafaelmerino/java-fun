package jsonvalues;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class OpPatchRemove<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath path;

    OpPatchRemove(final JsObj op) throws PatchMalformed
    {
        this.path = validatePath(requireNonNull(op));
    }

    OpPatchRemove(final JsPath path)
    {
        this.path = requireNonNull(path);
    }

    @Override
    public TryPatch<T> apply(final T json
                            )
    {
        if (requireNonNull(json).get(path)
                                .isNothing())
            return new TryPatch<>(PatchOpError.removingNonExistingValue(path,
                                                                        json
                                                                       ));
        return new TryPatch<>(json.remove(path));
    }


    @Override
    public String toString()
    {
        return "OpPatchRemove{" +
        "path=" + path +
        '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchRemove<?> that = (OpPatchRemove<?>) o;
        return path.equals(that.path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path);
    }


}
