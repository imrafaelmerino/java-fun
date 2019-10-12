package jsonvalues;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class OpPatchTest<T extends Json<T>> implements OpPatch<T>
{
    private final JsElem value;
    private final JsPath path;

    OpPatchTest(final JsObj op) throws PatchMalformed
    {
        this.value = requireNonNull(op).get(JsPath.fromKey("value"));
        Optional<String> opPath = op.getStr(JsPath.fromKey("path"));
        if (!opPath.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path = JsPath.path(opPath.get());
    }

    @Override
    public TryPatch<T> apply(final T json
                            )
    {
        final JsElem elem = json.get(path);
        if (elem.isNothing()) return new TryPatch<>(PatchOpError.testingNonExistingValue(path,
                                                                                         json
                                                                                        ));
        if (elem.equals(value)) return new TryPatch<>(json);
        return new TryPatch<>(PatchOpError.testingDifferentElements(path,
                                                                    value,
                                                                    elem
                                                                   ));
    }


    @Override
    public String toString()
    {
        return "OpPatchTest{" +
        "value=" + value +
        ", path=" + path +
        '}';
    }
}
