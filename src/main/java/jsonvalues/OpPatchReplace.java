package jsonvalues;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class OpPatchReplace<T extends Json<T>> implements OpPatch<T>
{
    private final JsElem value;
    private final JsPath path;

    OpPatchReplace(final JsObj op) throws PatchMalformed
    {
        JsElem valueOp = requireNonNull(op).get(JsPath.fromKey("value"));
        if (valueOp.isNothing()) throw PatchMalformed.valueRequired(op);
        this.value = valueOp;
        Optional<String> opPath = op.getStr(JsPath.fromKey("path"));
        if (!opPath.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path = JsPath.path(opPath.get());
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


}
