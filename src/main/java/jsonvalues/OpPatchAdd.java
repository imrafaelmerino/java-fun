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
        final JsPath init = path.init();
        final Position last = path.last();
        final JsElem parent = json.get(init);
        if (!parent.isJson())
            return new TryPatch<>(PatchOpError.parentIsNotAJson(init,
                                                                json,
                                                                path,
                                                                Patch.OP.ADD.name()
                                                                ));
        if (parent.isObj() && last.isIndex())
            return new TryPatch<>(PatchOpError.addingIndexIntoObject(last.asIndex().n,
                                                                     json,
                                                                     path,
                                                                     Patch.OP.ADD.name()
                                                                     ));

        if (parent.isArray() && last.isKey())
            return new TryPatch<>(PatchOpError.addingKeyIntoArray(last.asKey().name,
                                                                  json,
                                                                  path,
                                                                  Patch.OP.ADD.name()
                                                                  ));

        return new TryPatch<>(json.put(path,
                                       value
                                      ));

    }

    @Override
    public String toString()
    {
        return "OpPatchAdd{" +
        "value=" + value +
        ", path=" + path +
        '}';
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
