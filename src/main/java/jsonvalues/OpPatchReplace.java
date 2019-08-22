package jsonvalues;


import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class OpPatchReplace<T extends Json<T>> implements OpPatch<T>
{
    private final JsElem value;
    private final JsPath path;

    OpPatchReplace(final JsPath path,
                   final JsElem value

                  )
    {
        this.value = Objects.requireNonNull(value);
        this.path = Objects.requireNonNull(path);
    }

    OpPatchReplace(final JsObj op) throws PatchMalformed
    {
        this.value = validateValue(requireNonNull(op));
        this.path = validatePath(op);
    }

    @Override
    public TryPatch<T> apply(final T json
                            )
    {
        return new OpPatchRemove<T>(path).apply(requireNonNull(json)
                                               )
                                         .flatMap(it -> new OpPatchAdd<T>(path,
                                                                          value
                                         ).apply(it
                                                ));


    }

    @Override
    public String toString()
    {
        return "OpPatchReplace{" +
        "value=" + value +
        ", path=" + path +
        '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchReplace<?> that = (OpPatchReplace<?>) o;
        return value.equals(that.value) &&
        path.equals(that.path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value,
                            path
                           );
    }


}
