package jsonvalues;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class OpPatchAdd<T extends Json<T>> implements OpPatch<T>
{

    private final JsElem value;
    private final JsPath path;

    OpPatchAdd(final JsPath path,
               final JsElem elem
              )
    {
        this.value = requireNonNull(elem);
        this.path = requireNonNull(path);
    }

    OpPatchAdd(final JsObj op) throws PatchMalformed
    {
        this.value = validateValue(requireNonNull(op));
        this.path = validatePath(op);
    }

    //todo en arrays definir la operacion de insertar moviendo
    @Override
    public TryPatch<T> apply(final T json)
    {
        Objects.requireNonNull(json);
        if (path.isEmpty()) return new TryPatch<>(json);
        try
        {
            return new TryPatch<>(json.add(path,
                                            e -> value));
        }
        catch (UserError error)
        {
            return new TryPatch<>(PatchOpError.of(error));
        }
    }

    @Override
    public String toString()
    {
        return "OpPatchAdd{" + "value=" + value + ", path=" + path + '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchAdd<?> that = (OpPatchAdd<?>) o;
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
