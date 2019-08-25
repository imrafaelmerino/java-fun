package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Patch.PATH_FIELD;

final class OpPatchRemove<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath path;

    OpPatchRemove(final JsObj op) throws PatchMalformed
    {
        Optional<String> path = requireNonNull(op).getStr(JsPath.fromKey(PATH_FIELD));
        if (!path.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.of(path.get());
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
    public boolean equals(final @Nullable  Object o)
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
