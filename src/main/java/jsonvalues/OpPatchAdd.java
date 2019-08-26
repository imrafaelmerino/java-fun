package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Patch.PATH_FIELD;
import static jsonvalues.Patch.VALUE_FIELD;

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
        JsElem value = requireNonNull(op).get(JsPath.fromKey(VALUE_FIELD));
        if (value.isNothing()) throw PatchMalformed.valueRequired(op);
        this.value = value;
        Optional<String> path = op.getStr(JsPath.fromKey(PATH_FIELD));
        if (!path.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.of(path.get());
    }

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
    public boolean equals(final @Nullable Object o)
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
