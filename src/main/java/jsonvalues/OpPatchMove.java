package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Patch.FROM_FIELD;
import static jsonvalues.Patch.PATH_FIELD;

final class OpPatchMove<T extends Json<T>> implements OpPatch<T>
{
    private final JsPath from;
    private final JsPath path;

    OpPatchMove(final JsObj op) throws PatchMalformed
    {
        Optional<String> from = requireNonNull(op).getStr(JsPath.fromKey(FROM_FIELD));
        if (!from.isPresent()) throw PatchMalformed.fromRequired(op);
        this.from = JsPath.of(from.get());
        Optional<String> path = op.getStr(JsPath.fromKey(PATH_FIELD));
        if (!path.isPresent()) throw PatchMalformed.pathRequired(op);
        this.path =  JsPath.of(path.get());
    }

    @Override
    public TryPatch<T> apply(final T json)
    {
        return new OpPatchRemove<T>(from).apply(requireNonNull(json)
                                               )
                                         .flatMap(it -> new OpPatchAdd<T>(path,
                                                                          json.get(from)
                                         ).apply(it
                                                ));

    }

    @Override
    public String toString()
    {
        return "OpPatchMove{" +
        "from=" + from +
        ", path=" + path +
        '}';
    }

    @Override
    public boolean equals(final @Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OpPatchMove<?> that = (OpPatchMove<?>) o;
        return from.equals(that.from) &&
        path.equals(that.path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(from,
                            path
                           );
    }




}
