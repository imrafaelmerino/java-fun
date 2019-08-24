package jsonvalues;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class OpPatchTest<T extends Json<T>> implements OpPatch<T>
{
    private final JsElem value;
    private final JsPath path;


    OpPatchTest(final JsPath path,
                final JsElem value

               )
    {
        this.value = requireNonNull(value);
        this.path = requireNonNull(path);
    }

    OpPatchTest(final JsObj op) throws PatchMalformed
    {
        this.value = requireNonNull(op).get(JsPath.fromKey("value"));
        this.path = validatePath(op);
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
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchTest<?> that = (OpPatchTest<?>) o;
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

    @Override
    public String toString()
    {
        return "OpPatchTest{" +
        "value=" + value +
        ", path=" + path +
        '}';
    }
}
