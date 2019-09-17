package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

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
        JsElem valueOp = requireNonNull(op).get(JsPath.fromKey("value"));
        if (valueOp.isNothing()) throw PatchMalformed.valueRequired(op);
        this.value = valueOp;
        Optional<String> opPath = op.getStr(JsPath.fromKey("path"));
        if (!opPath.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.path(opPath.get());
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



}
